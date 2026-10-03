package crdt;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class GCounterTest {
    @Test
    public void threeReplicasConvergeRegardlessOfMergeOrder() {
        GCounter a = new GCounter("a").increment().increment(2);
        GCounter b = new GCounter("b").increment(4);
        GCounter c = new GCounter("c").increment(5);

        assertEquals(12L, a.merge(b).merge(c).value());
        assertEquals(a.merge(b).merge(c), b.merge(c).merge(a));
        assertEquals(a.merge(b).merge(c), c.merge(a).merge(b));
        assertEquals(a.merge(b).merge(c), a.merge(b.merge(c)));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals(a, a.merge(a));
        assertEquals(a.merge(b), a.merge(b).merge(b));
    }

    @Test
    public void mergeTakesMaximumPerReplicaRatherThanAddingRepeatedUpdates() {
        GCounter earlier = new GCounter("a").increment(2);
        GCounter later = earlier.increment(3);
        GCounter remote = new GCounter("b").increment(4);

        GCounter joined = earlier.merge(later).merge(remote);
        assertEquals(9L, joined.value());
        assertEquals(5L, joined.countOf("a"));
        assertEquals(4L, joined.countOf("b"));
        assertEquals(0L, joined.countOf("unknown"));
        assertEquals(2L, earlier.value());
    }

    @Test
    public void stateIsCanonicalSortedAndCannotBeChangedByCaller() {
        GCounter a = new GCounter("z");
        GCounter b = new GCounter("a").increment(2);
        GCounter joined = a.merge(b).increment(3);
        Map<String, Long> copy = joined.state();
        assertEquals("a", copy.keySet().iterator().next());
        assertEquals(2, copy.size());
        assertFalse(a.state().containsKey("z"));
        assertFalse(a.increment(0).state().containsKey("z"));
        assertNotSame(copy, joined.state());
        try {
            copy.put("a", 99L);
            org.junit.Assert.fail("state must be unmodifiable");
        } catch (UnsupportedOperationException expected) {
            assertEquals(5L, joined.value());
        }
    }

    @Test
    public void equalityDependsOnReplicatedStateNotLocalReplicaIdentity() {
        GCounter a = new GCounter("a");
        GCounter b = new GCounter("b");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("b", b.merge(a).replicaId()); // empty states have no components
        GCounter joined = a.increment(2).merge(b.increment(3));
        assertEquals(joined, b.increment(3).merge(a.increment(2)));
        assertEquals(joined.hashCode(), b.increment(3).merge(a.increment(2)).hashCode());
        assertEquals("a", joined.replicaId());
        assertEquals(2L, joined.countOf("a"));
        assertEquals(3L, joined.countOf("b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIncrementIsRejected() {
        new GCounter("a").increment(-1);
    }
}
