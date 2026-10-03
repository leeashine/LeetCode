package crdt;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DeltaORSetTest {
    @Test
    public void pendingPeeksUntilAckAndExposesOnlyReadOnlyCollections() {
        DeltaORSet<String> empty = DeltaORSet.empty("A");
        DeltaORSet<String> a = empty.add("x").add("y");
        DeltaORSet.Delta<String> pending = a.pending();
        assertEquals(pending, a.pending());
        assertEquals(new HashSet<String>(Arrays.asList("x", "y")), a.elements());
        assertEquals(2, a.size());
        assertTrue(empty.pending().isEmpty());
        assertFalse(pending.isEmpty());
        try {
            pending.added().clear();
            fail("delta map must be read-only");
        } catch (UnsupportedOperationException expected) {
            // The map must not expose mutable state.
        }
        try {
            pending.added().get("x").clear();
            fail("delta tag sets must be read-only");
        } catch (UnsupportedOperationException expected) {
            // The tag sets must not expose mutable state.
        }
        try {
            a.elements().clear();
            fail("visible elements must be read-only");
        } catch (UnsupportedOperationException expected) {
            // The returned view must not expose mutable state.
        }
        assertEquals("A", a.replicaId());
        assertTrue(a.ack().pending().isEmpty());
        assertEquals(pending, a.pending());
        assertEquals(a, a.ack());
        assertEquals(a.hashCode(), a.ack().hashCode());
    }

    @Test
    public void repeatedAndReorderedDeliveryConverge() {
        DeltaORSet<String> receiver = DeltaORSet.empty("receiver");
        DeltaORSet.Delta<String> d1 = DeltaORSet.<String>empty("A").add("x").pending();
        DeltaORSet.Delta<String> d2 = DeltaORSet.<String>empty("B").add("y").pending();
        DeltaORSet.Delta<String> d3 = DeltaORSet.<String>empty("C").add("z").pending();
        DeltaORSet<String> once = receiver.apply(d1);
        DeltaORSet<String> repeated = receiver;
        for (int i = 0; i < 100; i++) {
            repeated = repeated.apply(d1);
        }
        assertEquals(once, repeated);
        assertEquals(receiver.apply(d1).apply(d2), receiver.apply(d2).apply(d1));
        assertEquals(receiver.apply(d1.merge(d2).merge(d3)),
                receiver.apply(d1.merge(d2.merge(d3))));
        assertEquals(receiver.apply(d1).apply(d2).apply(d3),
                receiver.apply(d3).apply(d2).apply(d1));
        assertTrue(receiver.apply(DeltaORSet.Delta.<String>empty()).pending().isEmpty());
    }

    @Test
    public void concurrentUnobservedAddWinsOverRemoveViaDeltaChannel() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x");
        DeltaORSet<String> b = DeltaORSet.<String>empty("B").add("x");
        a = a.remove("x"); // B's concurrent tag has not been observed.
        DeltaORSet.Delta<String> fromA = a.pending();
        DeltaORSet.Delta<String> fromB = b.pending();
        a = a.ack().apply(fromB);
        b = b.ack().apply(fromA);
        assertTrue(a.contains("x"));
        assertTrue(b.contains("x"));
        assertEquals(a, b);
        assertTrue(a.pending().isEmpty());
        assertTrue(b.pending().isEmpty());
    }

    @Test
    public void observedAddIsRemovedViaDeltaChannel() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x");
        DeltaORSet<String> b = DeltaORSet.empty("B");
        b = b.apply(a.pending());
        a = a.ack();
        assertTrue(b.contains("x"));
        b = b.remove("x");
        a = a.apply(b.pending());
        b = b.ack();
        assertFalse(a.contains("x"));
        assertFalse(b.contains("x"));
        assertEquals(a, b);
    }

    @Test
    public void localAddThenRemoveSendsOnlyTombstone() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x");
        ORSet.Tag tag = a.pending().added().get("x").iterator().next();
        DeltaORSet<String> removed = a.remove("x");
        DeltaORSet.Delta<String> delta = removed.pending();
        assertTrue(delta.added().isEmpty());
        assertEquals(new HashSet<ORSet.Tag>(Arrays.asList(tag)), delta.removed());
        assertFalse(DeltaORSet.<String>empty("B").apply(delta).contains("x"));
        assertFalse(DeltaORSet.<String>empty("B").apply(delta).apply(a.pending()).contains("x"));
        assertEquals(removed, removed.remove("absent"));
        assertTrue(a.contains("x"));
    }

    @Test
    public void ackLeavesReplicatedStateAndFutureOperationsIntact() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x").remove("x").add("y");
        DeltaORSet.Delta<String> delta = a.pending();
        DeltaORSet<String> acked = a.ack();
        assertTrue(acked.pending().isEmpty());
        assertEquals(a, acked);
        assertTrue(acked.contains("y"));
        assertFalse(acked.contains("x"));
        assertNotEquals(a, acked.add("y"));
        assertFalse(delta.isEmpty());
    }

    @Test
    public void applyAndFullMergePreserveLocalUnackedBuffers() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x");
        DeltaORSet<String> b = DeltaORSet.<String>empty("B").add("y");
        DeltaORSet.Delta<String> local = a.pending();
        assertEquals(local, a.apply(b.pending()).pending());
        DeltaORSet<String> merged = a.merge(b);
        assertEquals(local, merged.pending());
        assertEquals("A", merged.replicaId());
        assertEquals(a.apply(b.pending()), merged);
        assertEquals(merged.hashCode(), a.apply(b.pending()).hashCode());
        assertFalse(merged.toString().isEmpty());
        assertFalse(local.toString().isEmpty());
    }

    @Test
    public void incomingTombstonePrunesLocalPendingAddWithoutMutatingBuffer() {
        DeltaORSet<String> a = DeltaORSet.<String>empty("A").add("x");
        DeltaORSet<String> b = DeltaORSet.<String>empty("B").apply(a.pending()).remove("x");
        DeltaORSet<String> pruned = a.apply(b.pending());
        assertFalse(pruned.contains("x"));
        assertTrue(pruned.pending().isEmpty());
        assertFalse(a.pending().isEmpty());
    }

    @Test
    public void deltaMergeIsIdempotentCommutativeAndAssociative() {
        DeltaORSet.Delta<String> d1 = DeltaORSet.<String>empty("A").add("x").pending();
        DeltaORSet.Delta<String> d2 = DeltaORSet.<String>empty("B").add("y").pending();
        DeltaORSet.Delta<String> d3 = DeltaORSet.<String>empty("C").add("z").remove("z").pending();
        assertEquals(d1, d1.merge(d1));
        assertEquals(d1.merge(d2), d2.merge(d1));
        assertEquals(d1.merge(d2).merge(d3), d1.merge(d2.merge(d3)));
        assertEquals(d1, d1.merge(DeltaORSet.Delta.<String>empty()));
        assertEquals(d1, DeltaORSet.Delta.<String>empty().merge(d1));
        assertEquals(d1.hashCode(), d1.merge(d1).hashCode());
        try {
            d3.removed().clear();
            fail("delta tombstones must be read-only");
        } catch (UnsupportedOperationException expected) {
            // The returned set must not expose mutable state.
        }
    }

    @Test
    public void receivedOwnTagNeverReusesSequence() {
        DeltaORSet<String> source = DeltaORSet.<String>empty("A").add("x");
        DeltaORSet<String> relayed = DeltaORSet.<String>empty("B").apply(source.pending());
        DeltaORSet<String> resumed = DeltaORSet.<String>empty("A").merge(relayed);
        assertNotEquals(resumed, resumed.add("x"));
        DeltaORSet<String> removed = relayed.remove("x");
        DeltaORSet<String> resumedAfterDeletion = DeltaORSet.<String>empty("A").apply(removed.pending());
        assertTrue(resumedAfterDeletion.add("x").contains("x"));
    }

    @Test
    public void replicaIdMustBeNonEmpty() {
        for (String invalid : new String[] {null, ""}) {
            try {
                DeltaORSet.empty(invalid);
                fail("expected invalid replica id");
            } catch (IllegalArgumentException expected) {
                // Identity must be valid for unique tags.
            }
        }
    }
}
