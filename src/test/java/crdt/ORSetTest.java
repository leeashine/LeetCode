package crdt;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ORSetTest {
    @Test
    public void localAddsAndRemovesAllObservedTags() {
        ORSet<String> empty = ORSet.empty("A");
        ORSet<String> added = empty.add("x").add("x").add("y");
        assertFalse(empty.contains("x"));
        assertEquals(2, added.size());
        assertEquals(new HashSet<String>(Arrays.asList("x", "y")), added.elements());

        ORSet<String> removed = added.remove("x");
        assertFalse(removed.contains("x"));
        assertTrue(removed.contains("y"));
        assertEquals(1, removed.size());
        assertEquals(removed, removed.remove("absent"));
        assertTrue(added.contains("x"));
        try {
            added.elements().remove("x");
            fail("visible elements must not be mutable");
        } catch (UnsupportedOperationException expected) {
            // The returned view must not expose the state for mutation.
        }
    }

    @Test
    public void concurrentAddSurvivesUnobservedRemove() {
        ORSet<String> a = ORSet.<String>empty("A").add("x");
        ORSet<String> b = ORSet.<String>empty("B").add("x");
        ORSet<String> aRemoved = a.remove("x"); // B's concurrent tag was not observed.
        ORSet<String> aSynced = aRemoved.merge(b);
        ORSet<String> bSynced = b.merge(aRemoved);

        assertTrue(aSynced.contains("x"));
        assertTrue(bSynced.contains("x"));
        assertEquals(aSynced, bSynced);
        assertFalse(aRemoved.contains("x"));
        assertTrue(b.contains("x"));
    }

    @Test
    public void observedAddCanBeRemovedAcrossReplicas() {
        ORSet<String> a = ORSet.<String>empty("A").add("x");
        ORSet<String> b = ORSet.<String>empty("B").merge(a).remove("x");
        ORSet<String> aSynced = a.merge(b);
        ORSet<String> bSynced = b.merge(aSynced);

        assertFalse(aSynced.contains("x"));
        assertFalse(bSynced.contains("x"));
        assertEquals(aSynced, bSynced);
        assertTrue(a.contains("x"));
    }

    @Test
    public void threeReplicasConvergeAfterSeededOperationsAndMerges() {
        @SuppressWarnings("unchecked")
        ORSet<Integer>[] replicas = new ORSet[] {
                ORSet.<Integer>empty("A"), ORSet.<Integer>empty("B"), ORSet.<Integer>empty("C")
        };
        Random random = new Random(473829L);
        for (int i = 0; i < 240; i++) {
            int index = random.nextInt(3);
            int element = random.nextInt(8);
            replicas[index] = random.nextBoolean()
                    ? replicas[index].add(element) : replicas[index].remove(element);
            if (i % 7 == 0) {
                int receiver = (index + 1) % 3;
                replicas[receiver] = replicas[receiver].merge(replicas[index]);
            }
        }
        ORSet<Integer> joined = replicas[0].merge(replicas[1]).merge(replicas[2]);
        for (int i = 0; i < replicas.length; i++) {
            replicas[i] = replicas[i].merge(joined);
        }
        assertEquals(replicas[0], replicas[1]);
        assertEquals(replicas[1], replicas[2]);
    }

    @Test
    public void mergeIsJoinWithEmptyIdentity() {
        ORSet<String> empty = ORSet.empty("identity");
        ORSet<String> a = ORSet.<String>empty("A").add("x").add("y").remove("x");
        ORSet<String> b = ORSet.<String>empty("B").add("x").add("z");
        ORSet<String> c = ORSet.<String>empty("C").add("y").remove("y").add("z");

        assertEquals(a, empty.merge(a));
        assertEquals(a, a.merge(a));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals(a.merge(b).merge(c), a.merge(b.merge(c)));
        assertEquals(a.merge(b).hashCode(), b.merge(a).hashCode());
        assertNotEquals(a, b);
    }

    @Test
    public void identityAndCounterDoNotChangeReplicatedEquality() {
        ORSet<String> a = ORSet.<String>empty("A").add("x").remove("x");
        ORSet<String> b = ORSet.<String>empty("B").merge(a);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(ORSet.empty("A"), ORSet.empty("B"));
        ORSet<String> later = a.add("x");
        assertTrue(later.contains("x"));
        assertEquals(later, later.merge(b));
    }

    @Test
    public void forwardedOwnTagDoesNotReuseCounter() {
        ORSet<String> original = ORSet.<String>empty("A").add("x");
        ORSet<String> forwarded = ORSet.<String>empty("B").merge(original);
        ORSet<String> resumed = ORSet.<String>empty("A").merge(forwarded);
        assertNotEquals(resumed, resumed.add("x"));

        ORSet<String> removed = original.remove("x");
        ORSet<String> forwardedDeletion = forwarded.merge(removed);
        ORSet<String> resumedAfterDeletion = ORSet.<String>empty("A").merge(forwardedDeletion);
        assertTrue(resumedAfterDeletion.add("x").contains("x"));
    }

    @Test
    public void replicaIdMustBeNonEmpty() {
        for (String invalid : new String[] {null, ""}) {
            try {
                ORSet.empty(invalid);
                fail("expected invalid replica id");
            } catch (IllegalArgumentException expected) {
                // Invalid identities cannot generate unique tags.
            }
        }
    }

    @Test
    public void gcWithEmptyFrontierRemovesNothing() {
        ORSet<String> removed = ORSet.<String>empty("A").add("x").add("y").remove("x");
        ORSet<String> collected = removed.gc(VersionVector.empty());
        assertEquals(1, collected.tombstoneCount());
        assertEquals(removed.tombstoneCount(), collected.tombstoneCount());
        assertEquals(removed.elements(), collected.elements());
        assertEquals(removed, collected);
        assertEquals(removed.version(), collected.version());
    }

    @Test
    public void gcRemovesStableTombstonesOnly() {
        ORSet<String> a = ORSet.<String>empty("A").add("early").remove("early")
                .add("late").remove("late").add("live");
        assertEquals(5L, a.version().get("A"));
        assertEquals(2, a.tombstoneCount());
        VersionVector partial = VersionVector.of(Collections.singletonMap("A", 2L));
        ORSet<String> partlyCollected = a.gc(partial);
        assertEquals(1, partlyCollected.tombstoneCount());
        assertEquals(a.elements(), partlyCollected.elements());
        assertEquals(a.version(), partlyCollected.version());

        ORSet<String> fullyCollected = partlyCollected.gc(a.version());
        assertEquals(0, fullyCollected.tombstoneCount());
        assertEquals(a.elements(), fullyCollected.elements());
        assertEquals(2, a.tombstoneCount());
        assertNotEquals(a, fullyCollected);
    }

    @Test
    public void tombstoneStampsStayInSyncWithTombstones() {
        ORSet<String> base = ORSet.<String>empty("A").add("x").add("y");
        ORSet<String> a = base.remove("x");
        ORSet<String> b = ORSet.<String>empty("B").merge(base).remove("x").remove("y");
        ORSet<String> joined = a.merge(b);
        assertEquals(2, joined.tombstoneCount());
        // Both replicas tombstoned x: its minimum stamp is B#1, while y was deleted at B#2.
        VersionVector partial = VersionVector.of(Collections.singletonMap("B", 1L));
        ORSet<String> collected = joined.gc(partial);
        assertEquals(1, collected.tombstoneCount());
        assertEquals(joined.elements(), collected.elements());
        assertEquals(0, collected.gc(joined.version()).tombstoneCount());
        assertEquals(0, joined.gc(joined.version()).tombstoneCount());
        ORSet<String> reintroduced = collected.merge(joined);
        assertEquals(2, reintroduced.tombstoneCount());
        assertEquals(0, reintroduced.gc(joined.version()).tombstoneCount());

        try {
            joined.gc(null);
            fail("null frontier must be rejected");
        } catch (IllegalArgumentException expected) {
            // A frontier must be supplied before any tombstone can be reclaimed.
        }
    }

    @Test
    public void versionVectorConvergesAfterMerge() {
        ORSet<String> a = ORSet.<String>empty("A").add("x").remove("x");
        ORSet<String> b = ORSet.<String>empty("B").add("y").add("z").remove("z");
        VersionVector joinedVersion = a.version().merge(b.version());
        ORSet<String> aSynced = a.merge(b);
        ORSet<String> bSynced = b.merge(a);
        assertEquals(joinedVersion, aSynced.version());
        assertEquals(joinedVersion, bSynced.version());
        assertEquals(aSynced.version(), bSynced.version());
        assertEquals(aSynced, bSynced);
    }

    @Test
    public void removeWithoutTagsDoesNotBumpVersion() {
        ORSet<String> a = ORSet.<String>empty("A").add("x").remove("x");
        ORSet<String> noOp = a.remove("missing").remove("x");
        assertEquals(a.version(), noOp.version());
        assertEquals(a.tombstoneCount(), noOp.tombstoneCount());
        assertEquals(a, noOp);
    }
}
