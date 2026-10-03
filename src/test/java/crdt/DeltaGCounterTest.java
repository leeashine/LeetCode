package crdt;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeltaGCounterTest {
    @Test
    public void pendingPeeksAtAbsoluteCountUntilAcknowledged() {
        DeltaGCounter original = new DeltaGCounter("a");
        assertTrue(original.pending().isEmpty());
        DeltaGCounter updated = original.increment(2);
        DeltaGCounter.Delta first = updated.pending();
        assertEquals(2L, first.counts().get("a").longValue());
        assertEquals(first, updated.pending());
        assertEquals(2L, updated.value());
        assertTrue(original.pending().isEmpty());

        DeltaGCounter acknowledged = updated.ack();
        assertTrue(acknowledged.pending().isEmpty());
        assertEquals(updated, acknowledged);
        assertEquals(updated.hashCode(), acknowledged.hashCode());
        assertEquals(first, updated.pending());
        assertEquals(3L, acknowledged.increment().pending().counts().get("a").longValue());
    }

    @Test
    public void repeatedDeliveryDoesNotMultiplyTheCount() {
        DeltaGCounter.Delta delta = new DeltaGCounter("a").increment(7).pending();
        DeltaGCounter receiver = new DeltaGCounter("b");
        DeltaGCounter once = receiver.apply(delta);
        for (int i = 0; i < 100; i++) {
            receiver = receiver.apply(delta);
        }
        assertEquals(once, receiver);
        assertEquals(7L, receiver.value());
        assertTrue(receiver.pending().isEmpty());
    }

    @Test
    public void deliveryOrderDoesNotAffectTheResult() {
        DeltaGCounter x = new DeltaGCounter("c").increment(3);
        DeltaGCounter.Delta d1 = new DeltaGCounter("a").increment(4).pending();
        DeltaGCounter.Delta d2 = new DeltaGCounter("b").increment(5).pending();
        assertEquals(x.apply(d1).apply(d2), x.apply(d2).apply(d1));
        assertEquals(12L, x.apply(d1).apply(d2).value());
    }

    @Test
    public void threeDeltasAssociateWhenAppliedOrJoined() {
        DeltaGCounter.Delta d1 = new DeltaGCounter("a").increment(2).pending();
        DeltaGCounter.Delta d2 = new DeltaGCounter("b").increment(3).pending();
        DeltaGCounter.Delta d3 = new DeltaGCounter("a").increment(5).pending();
        DeltaGCounter x = new DeltaGCounter("c");
        assertEquals(x.apply(d1).apply(d2).apply(d3), x.apply(d1.merge(d2.merge(d3))));
        assertEquals(x.apply(d1.merge(d2)).apply(d3), x.apply(d1.merge(d2.merge(d3))));
        assertEquals(8L, x.apply(d1.merge(d2.merge(d3))).value());
    }

    @Test
    public void twoReplicasConvergeThroughDeltaExchange() {
        DeltaGCounter a = new DeltaGCounter("a").increment(3);
        DeltaGCounter b = new DeltaGCounter("b").increment(4);
        DeltaGCounter receivedByA = a.apply(b.pending());
        DeltaGCounter receivedByB = b.apply(a.pending());
        assertEquals(receivedByA, receivedByB);
        assertEquals(7L, receivedByA.value());
        assertEquals(7L, receivedByB.value());
        assertEquals(receivedByA, a.merge(b));
        assertEquals(3L, receivedByA.pending().counts().get("a").longValue());
        assertEquals(4L, receivedByB.pending().counts().get("b").longValue());
        assertEquals(0L, receivedByA.countOf("unknown"));
        assertEquals("a", receivedByA.replicaId());
        assertEquals("b", receivedByB.replicaId());
    }

    @Test
    public void deltaJoinIsIdempotentCommutativeAndAssociative() {
        DeltaGCounter.Delta d1 = new DeltaGCounter("a").increment(2).pending();
        DeltaGCounter.Delta d2 = new DeltaGCounter("b").increment(3).pending();
        DeltaGCounter.Delta d3 = new DeltaGCounter("a").increment(5).pending();
        assertEquals(d1, d1.merge(d1));
        assertEquals(d1.merge(d2), d2.merge(d1));
        assertEquals(d1.merge(d2).merge(d3), d1.merge(d2.merge(d3)));
        assertEquals(d3, d1.merge(d3));
        assertEquals(d1, d1.merge(DeltaGCounter.Delta.empty()));
        assertTrue(DeltaGCounter.Delta.empty().isEmpty());
        assertEquals(d1.hashCode(), d1.merge(d1).hashCode());
    }

    @Test
    public void sortedStateAndDeltaAreIndependentReadOnlyCopies() {
        DeltaGCounter counter = new DeltaGCounter("z").increment(2)
                .apply(new DeltaGCounter("a").increment(3).pending());
        Map<String, Long> state = counter.state();
        Map<String, Long> delta = counter.pending().merge(new DeltaGCounter("a").increment(3).pending()).counts();
        assertEquals("a", state.keySet().iterator().next());
        assertEquals("a", delta.keySet().iterator().next());
        assertNotSame(state, counter.state());
        assertNotSame(delta, counter.pending().merge(new DeltaGCounter("a").increment(3).pending()).counts());
        assertFalse(new DeltaGCounter("a").increment(0).state().containsKey("a"));
        try {
            state.put("a", 99L);
            fail("state must be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            assertEquals(5L, counter.value());
        }
        try {
            delta.clear();
            fail("delta counts must be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            assertEquals(2L, counter.pending().counts().get("z").longValue());
        }
    }

    @Test
    public void equalityIgnoresSenderAndAcknowledgementWatermark() {
        DeltaGCounter a = new DeltaGCounter("a");
        DeltaGCounter b = new DeltaGCounter("b");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.increment(2), b.apply(a.increment(2).pending()));
        assertEquals(a.increment(2).ack(), a.increment(2));
        assertEquals(a.increment(2).ack().hashCode(), a.increment(2).hashCode());
    }

    @Test
    public void mergingFullStateKeepsLocalAcknowledgementWatermark() {
        DeltaGCounter a = new DeltaGCounter("a").increment(2).ack().increment();
        DeltaGCounter b = new DeltaGCounter("b").increment(4);
        DeltaGCounter merged = a.merge(b);
        assertEquals(3L, merged.pending().counts().get("a").longValue());
        assertEquals(7L, merged.value());
        assertEquals(3L, a.value());
        assertEquals(7L, merged.merge(b).value());
        assertEquals(7L, b.merge(a).value());
    }

    @Test
    public void receivingNewerLocalComponentDoesNotAdvanceAcknowledgementWatermark() {
        DeltaGCounter acknowledged = new DeltaGCounter("a").increment(2).ack();
        DeltaGCounter newer = new DeltaGCounter("a").increment(5);
        assertEquals(5L, acknowledged.apply(newer.pending()).pending().counts().get("a").longValue());
        assertEquals(5L, acknowledged.merge(newer).pending().counts().get("a").longValue());
        assertTrue(acknowledged.pending().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIncrementIsRejected() {
        new DeltaGCounter("a").increment(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyReplicaIdentityIsRejected() {
        new DeltaGCounter("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullReplicaIdentityIsRejected() {
        new DeltaGCounter(null);
    }
}
