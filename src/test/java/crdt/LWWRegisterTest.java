package crdt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LWWRegisterTest {

    @Test
    public void laterTimestampWinsRegardlessOfMergeDirection() {
        LWWRegister<String> first = LWWRegister.<String>empty().set("A", 7, "a");
        LWWRegister<String> second = LWWRegister.<String>empty().set("B", 8, "b");

        assertEquals("B", first.merge(second).value());
        assertEquals(second, first.merge(second));
        assertEquals(second, second.merge(first));
        assertEquals("A", first.value());
    }

    @Test
    public void replicaIdBreaksConcurrentTimestampTies() {
        LWWRegister<String> a = LWWRegister.<String>empty().set("A", 7, "alpha");
        LWWRegister<String> b = LWWRegister.<String>empty().set("B", 7, "beta");

        assertEquals(b, a.merge(b));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals("beta", a.merge(b).replicaId());
    }

    @Test
    public void observedWriteHasLargerLamportTimestamp() {
        LamportClock aClock = LamportClock.start().tick();
        LWWRegister<String> a = LWWRegister.<String>empty().set("A", aClock.time(), "a");
        LamportClock bClock = LamportClock.start().observe(aClock.time()).tick();
        LWWRegister<String> b = LWWRegister.<String>empty().set("B", bClock.time(), "b");

        assertEquals(0L, LamportClock.start().time());
        assertEquals(1L, aClock.time());
        assertEquals(2L, bClock.time());
        assertEquals(aClock, aClock.observe(0));
        assertEquals(b, a.merge(b));
        assertEquals(aClock, LamportClock.at(1));
        assertEquals(aClock.hashCode(), LamportClock.at(1).hashCode());
    }

    @Test
    public void bottomIsIdentityAndJoinLawsHold() {
        LWWRegister<String> bottom = LWWRegister.empty();
        LWWRegister<String> a = bottom.set("A", 1, "a");
        LWWRegister<String> b = bottom.set("B", 2, "b");
        LWWRegister<String> c = bottom.set("C", 2, "c");

        assertTrue(bottom.isEmpty());
        assertEquals(Long.MIN_VALUE, bottom.timestamp());
        assertEquals("", bottom.replicaId());
        assertNull(bottom.value());
        assertFalse(a.isEmpty());
        assertSame(a, a.merge(bottom));
        assertEquals(a, bottom.merge(a));
        assertEquals(a, a.merge(a));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals(a.merge(b).merge(c), a.merge(b.merge(c)));
        assertEquals("C", a.merge(b).merge(c).value());
    }

    @Test
    public void equalWriteIsIdempotentAndTupleDefinesEquality() {
        LWWRegister<String> a = LWWRegister.<String>empty().set("value", 3, "node");
        LWWRegister<String> copy = LWWRegister.<String>empty().set("value", 3, "node");

        assertSame(a, a.merge(copy));
        assertEquals(a, copy);
        assertEquals(a.hashCode(), copy.hashCode());
        assertNotEquals(a, a.set("other", 3, "node"));
        assertNotEquals(a, a.set("value", 4, "node"));
        assertNotEquals(a, a.set("value", 3, "other"));
        assertEquals("value", a.value());
    }

    @Test
    public void reservedTimestampAndMissingReplicaAreRejected() {
        LWWRegister<String> bottom = LWWRegister.empty();
        assertRejected(new Runnable() {
            @Override public void run() { bottom.set("x", Long.MIN_VALUE, "a"); }
        });
        assertRejected(new Runnable() {
            @Override public void run() { bottom.set("x", 1, ""); }
        });
        assertRejected(new Runnable() {
            @Override public void run() { bottom.set("x", 1, null); }
        });
        assertRejected(new Runnable() {
            @Override public void run() { LamportClock.at(-1); }
        });
    }

    private static void assertRejected(Runnable operation) {
        try {
            operation.run();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Invalid timestamp or replica identifier.
        }
    }
}
