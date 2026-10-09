package crdt.learnning;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** 第 09 章：前沿来自所有存活设备；旧消息退役属于同步协议的前置条件。 */
public class GcLearningTest {
    @Test
    public void frontierIsTheMinimumConfirmedByEveryDevice() {
        Map<String, Long> phoneCounts = new LinkedHashMap<String, Long>();
        phoneCounts.put("A", 3L);
        phoneCounts.put("B", 2L);
        Map<String, Long> computerCounts = new LinkedHashMap<String, Long>();
        computerCounts.put("A", 2L);
        VersionVector frontier = CausalStability.frontier(Arrays.asList(
                VersionVector.of(phoneCounts), VersionVector.of(computerCounts)));
        assertEquals(2L, frontier.get("A"));
        assertEquals(0L, frontier.get("B"));
        assertTrue(CausalStability.covers(frontier, new CausalStamp("A", 2L)));
        assertFalse(CausalStability.covers(frontier, new CausalStamp("A", 3L)));
        assertFalse(CausalStability.covers(frontier, new CausalStamp("B", 1L)));
        assertEquals(VersionVector.empty(),
                CausalStability.frontier(Collections.<VersionVector>emptyList()));
    }

    @Test
    public void deviceThatHasNotObservedRemovalPreventsTagGc() {
        ORSet<String> beforeRemoval = ORSet.<String>empty("A").add("已完成");
        ORSet<String> phone = beforeRemoval.remove("已完成");
        ORSet<String> computer = ORSet.<String>empty("B").merge(beforeRemoval);
        VersionVector frontier = CausalStability.frontier(Arrays.asList(
                phone.version(), computer.version()));
        ORSet<String> retained = phone.gc(frontier);
        assertEquals(1, retained.tombstoneCount());
        assertFalse(retained.contains("已完成"));
        assertFalse(retained.merge(computer).contains("已完成"));
    }

    @Test
    public void stableTagRemovalCanBeCollectedWithoutChangingVisibleTags() {
        ORSet<String> phone = ORSet.<String>empty("A").add("已完成").remove("已完成")
                .add("旅行");
        ORSet<String> computer = ORSet.<String>empty("B").merge(phone);
        VersionVector frontier = CausalStability.frontier(Arrays.asList(
                phone.version(), computer.version()));
        ORSet<String> collected = phone.gc(frontier);
        assertEquals(0, collected.tombstoneCount());
        assertEquals(Collections.singleton("旅行"), collected.elements());
        assertEquals(1, phone.tombstoneCount());
        // 电脑已观察删除但尚未 GC；此断言不覆盖任意删除前的旧快照。
        ORSet<String> synced = collected.merge(computer);
        assertEquals(collected.elements(), synced.elements());
        assertEquals(1, synced.tombstoneCount());
    }

    @Test
    public void stableDeadTextTailCanBeCollectedInOnePass() {
        RGA phone = new RGA("A").insert(0, '笔').insert(1, '记')
                .insert(2, '草').insert(3, '稿').delete(3).delete(2);
        RGA offlineComputer = new RGA("B");
        assertEquals(2, phone.gc(CausalStability.frontier(Arrays.asList(
                phone.version(), offlineComputer.version()))).tombstoneCount());
        RGA computer = offlineComputer.merge(phone);
        RGA collected = phone.gc(CausalStability.frontier(Arrays.asList(
                phone.version(), computer.version())));
        assertEquals("笔记", collected.text());
        assertEquals(2, collected.size());
        assertEquals(0, collected.tombstoneCount());
        assertEquals("笔记", collected.merge(computer).text());
        // 本练习不承诺 GC 后重用旧字符身份或重新建立同身份副本仍然安全。
    }

    @Test
    public void stableDeletedParentMustRemainAsALiveCharactersAnchor() {
        RGA phone = new RGA("A").insert(0, '笔').insert(1, '记').insert(2, '本')
                .delete(1);
        RGA computer = new RGA("B").merge(phone);
        RGA collected = phone.gc(CausalStability.frontier(Arrays.asList(
                phone.version(), computer.version())));
        assertEquals("笔本", collected.text());
        assertEquals(2, collected.size());
        assertEquals(1, collected.tombstoneCount());
        assertEquals("笔本", collected.merge(computer).text());
    }
}
