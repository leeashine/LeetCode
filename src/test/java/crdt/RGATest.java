package crdt;

import java.util.Random;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RGATest {
    @Test
    public void localEditsAtBeginningMiddleAndEnd() {
        RGA empty = new RGA("A");
        RGA first = empty.insert(0, 'a');
        RGA second = first.insert(1, 'c').insert(1, 'b');
        RGA third = second.insert(0, '0').insert(4, 'z');
        assertEquals("", empty.text());
        assertEquals("a", first.text());
        assertEquals("abc", second.text());
        assertEquals("0abcz", third.text());
        assertEquals(5, third.size());
        assertEquals("0abcz", third.toString());
        RGA deleted = third.delete(2).delete(0);
        assertEquals("acz", deleted.text());
        assertEquals(3, deleted.size());
        assertEquals("0abcz", third.text());
        assertEquals("ba", empty.insert(0, 'a').insert(0, 'b').text());
    }

    @Test
    public void concurrentInsertsKeepBothCharactersAndTheirSubtrees() {
        RGA base = new RGA("A").insert(0, 'x').insert(1, 'y');
        RGA a = base.insert(1, 'a').insert(2, 'c');
        RGA b = new RGA("B").merge(base).insert(1, 'b');
        RGA atA = a.merge(b);
        RGA atB = b.merge(a);
        assertEquals("xbacy", atA.text());
        assertEquals(atA.text(), atB.text());
        assertEquals(atA, atB);
        assertEquals(atA.hashCode(), atB.hashCode());
        assertEquals("A", atA.replicaId());
        assertEquals("B", atB.replicaId());
        assertEquals("xacy", a.text());
        assertEquals("xby", b.text());
    }

    @Test
    public void duplicateAndOutOfOrderDeliveryIsIdempotentAndCommutative() {
        RGA a = new RGA("A").insert(0, 'a');
        RGA b = new RGA("B").insert(0, 'b');
        RGA once = a.merge(b);
        assertEquals(once, once.merge(b));
        assertEquals(once, a.merge(b).merge(b));
        assertEquals(once, b.merge(a));
        assertEquals(once, once.merge(once));
        assertEquals("ba", once.text());
        RGA later = once.insert(0, 'z');
        assertEquals("zba", later.text());
    }

    @Test
    public void concurrentDeleteAndInsertPreserveTombstonedDescendants() {
        RGA base = new RGA("A").insert(0, 'a').insert(1, 'b');
        RGA deletion = base.delete(0);
        RGA insertion = new RGA("B").merge(base).insert(2, 'c');
        assertEquals("bc", deletion.merge(insertion).text());
        assertEquals(deletion.merge(insertion), insertion.merge(deletion));
        assertEquals("ab", base.text());
        assertEquals("bxc", deletion.merge(insertion).insert(1, 'x').text());
    }

    @Test
    public void threeReplicasConvergeAfterRandomEditsAndShuffledSynchronization() {
        Random random = new Random(71043);
        RGA[] replicas = {new RGA("A"), new RGA("B"), new RGA("C")};
        for (int step = 0; step < 30; step++) {
            int writer = random.nextInt(3);
            RGA current = replicas[writer];
            if (current.size() == 0 || random.nextBoolean()) {
                replicas[writer] = current.insert(random.nextInt(current.size() + 1), (char) ('a' + step));
            } else {
                replicas[writer] = current.delete(random.nextInt(current.size()));
            }
            int from = random.nextInt(3);
            int to = (from + 1 + random.nextInt(2)) % 3;
            replicas[to] = replicas[to].merge(replicas[from]);
        }
        RGA joined = replicas[0].merge(replicas[1]).merge(replicas[2]);
        for (int i = 0; i < 3; i++) {
            replicas[i] = replicas[i].merge(joined);
        }
        assertEquals(replicas[0], replicas[1]);
        assertEquals(replicas[1], replicas[2]);
        assertEquals(replicas[0].text(), replicas[1].text());
        assertEquals(replicas[1].text(), replicas[2].text());
    }

    @Test
    public void invalidIndicesAndConflictingDuplicateIdsAreRejected() {
        RGA a = new RGA("A").insert(0, 'a');
        assertInvalidIndex(() -> a.insert(-1, 'x'));
        assertInvalidIndex(() -> a.insert(2, 'x'));
        assertInvalidIndex(() -> a.delete(-1));
        assertInvalidIndex(() -> a.delete(1));
        try {
            a.merge(null);
            fail("merge(null) must fail");
        } catch (IllegalArgumentException expected) {
            // An absent state is not a join operand.
        }
        try {
            a.merge(new RGA("A").insert(0, 'b'));
            fail("the same ID cannot represent two different writes");
        } catch (IllegalArgumentException expected) {
            // Reject conflicting payloads instead of silently dropping a write.
        }
        assertEquals("a", a.text());
    }

    @Test
    public void nodeIdentityOrdersByTimestampThenReplicaAndIncludesParentInEquality() {
        RGA.NodeId earlier = new RGA.NodeId(1, "Z");
        RGA.NodeId later = new RGA.NodeId(2, "A");
        RGA.NodeId sameTime = new RGA.NodeId(1, "a");
        assertTrue(earlier.compareTo(later) < 0);
        assertTrue(earlier.compareTo(sameTime) < 0);
        assertEquals(earlier, new RGA.NodeId(1, "Z"));
        assertEquals(earlier.hashCode(), new RGA.NodeId(1, "Z").hashCode());
        assertNotEquals(new RGA.Node(later, earlier, 'x'), new RGA.Node(later, null, 'x'));
    }

    @Test
    public void gcWithEmptyFrontierRemovesNothing() {
        RGA original = new RGA("A").insert(0, 'a').delete(0);
        RGA collected = original.gc(VersionVector.empty());
        assertEquals(1, collected.tombstoneCount());
        assertEquals(original.tombstoneCount(), collected.tombstoneCount());
        assertEquals(original.text(), collected.text());
        assertEquals(original, collected);
        assertEquals(original.version(), collected.version());
    }

    @Test
    public void gcKeepsTextUnchanged() {
        RGA rga = new RGA("A");
        for (char c : "abcde".toCharArray()) {
            rga = rga.insert(rga.size(), c);
        }
        rga = rga.delete(4).delete(2).delete(0);
        String before = rga.text();
        int sizeBefore = rga.size();
        RGA collected = rga.gc(rga.version());
        assertEquals("bd", before);
        assertEquals(before, collected.text());
        assertEquals(sizeBefore, collected.size());
        assertEquals(2, collected.tombstoneCount()); // 'a' and 'c' anchor live descendants.
        assertEquals(rga.version(), collected.version());
        assertEquals(before, rga.text());
    }

    @Test
    public void gcCollapsesDeadChain() {
        RGA rga = new RGA("A").insert(0, 'a').insert(1, 'b').insert(2, 'c');
        rga = rga.delete(0).delete(0).delete(0);
        assertEquals(3, rga.tombstoneCount());
        RGA collected = rga.gc(rga.version());
        assertEquals(0, collected.tombstoneCount());
        assertEquals("", collected.text());
        assertEquals(0, collected.size());
    }

    @Test
    public void gcDoesNotRemoveLiveNodes() {
        RGA rga = new RGA("A").insert(0, 'a').insert(1, 'b').insert(2, 'c');
        rga = rga.delete(1);
        RGA collected = rga.gc(rga.version());
        assertEquals("ac", collected.text());
        assertEquals(rga.text(), collected.text());
        assertEquals(rga.size(), collected.size());
        assertTrue(collected.tombstoneCount() <= rga.tombstoneCount());
        assertEquals(1, collected.tombstoneCount()); // 'b' anchors the live 'c'.
    }

    @Test
    public void partialFrontierRemovesOnlyCovered() {
        RGA rga = new RGA("A").insert(0, 'a').insert(1, 'b');
        rga = rga.delete(1).delete(0); // 'b': A#3, 'a': A#4.
        VersionVector frontier = VersionVector.of(java.util.Collections.singletonMap("A", 3L));
        RGA partial = rga.gc(frontier);
        assertEquals(1, partial.tombstoneCount());
        assertEquals("", partial.text());
        assertEquals(rga.version(), partial.version());
        assertEquals(0, partial.gc(rga.version()).tombstoneCount());
        assertEquals(2, rga.tombstoneCount());
    }

    @Test
    public void versionVectorConvergesAfterMerge() {
        RGA a = new RGA("A").insert(0, 'a').delete(0);
        RGA b = new RGA("B").insert(0, 'b').delete(0);
        assertEquals(2L, a.version().get("A"));
        assertEquals(2L, b.version().get("B"));
        RGA atA = a.merge(b);
        RGA atB = b.merge(a);
        assertEquals(atA.version(), atB.version());
        assertEquals(2L, atA.version().get("A"));
        assertEquals(2L, atA.version().get("B"));
        assertEquals("A", atA.replicaId());
        assertEquals("B", atB.replicaId());
        assertEquals(2L, a.version().get("A"));
    }

    @Test
    public void mergeAfterGcRestoresTombstoneAlongsideNode() {
        RGA original = new RGA("A").insert(0, 'a').delete(0);
        RGA collected = original.gc(original.version());
        RGA merged = collected.merge(original);
        assertEquals("", merged.text());
        assertEquals(1, merged.tombstoneCount());
        assertEquals(0, merged.gc(merged.version()).tombstoneCount());
    }

    @Test
    public void concurrentDeletesChooseEarliestStampInEitherMergeOrder() {
        RGA base = new RGA("A").insert(0, 'a');
        RGA a = base.delete(0); // A#2
        RGA b = new RGA("B").merge(base).delete(0); // B#1 is the smaller stamp.
        RGA atA = a.merge(b);
        RGA atB = b.merge(a);
        assertEquals(atA, atB);
        VersionVector frontier = VersionVector.of(java.util.Collections.singletonMap("B", 1L));
        assertEquals(0, atA.gc(frontier).tombstoneCount());
        assertEquals(0, atB.gc(frontier).tombstoneCount());
    }

    @Test
    public void gcRejectsNullFrontier() {
        try {
            new RGA("A").gc(null);
            fail("gc(null) must fail");
        } catch (IllegalArgumentException expected) {
            // A stability frontier is required even for an empty document.
        }
    }

    private static void assertInvalidIndex(Runnable action) {
        try {
            action.run();
            fail("expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // Validated against the visible text, not the number of tombstoned nodes.
        }
    }
}
