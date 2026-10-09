package crdt.learnning;

import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** 第 05 章：同步笔记标签，区分观察到的添加与离线并发添加。 */
public class ORSetLearningTest {
    @Test
    public void observedTagIsRemovedOnBothDevices() {
        ORSet<String> phone = ORSet.<String>empty("A").add("旅行");
        ORSet<String> computer = ORSet.<String>empty("B").merge(phone).remove("旅行");
        assertFalse(computer.contains("旅行"));
        assertFalse(phone.merge(computer).contains("旅行"));
        assertTrue(phone.contains("旅行"));
    }

    @Test
    public void unseenConcurrentAdditionSurvivesRemoval() {
        ORSet<String> phone = ORSet.<String>empty("A").add("旅行").remove("旅行");
        ORSet<String> computer = ORSet.<String>empty("B").add("旅行");
        ORSet<String> synced = phone.merge(computer);
        assertEquals(Collections.singleton("旅行"), synced.elements());
        assertEquals(synced, computer.merge(phone));
        assertEquals(1, synced.size());
    }

    @Test
    public void addingAgainAfterRemovalCreatesANewVisibleRecord() {
        ORSet<String> removed = ORSet.<String>empty("A").add("工作").remove("工作");
        ORSet<String> addedAgain = removed.add("工作");
        assertFalse(removed.contains("工作"));
        assertTrue(addedAgain.contains("工作"));
        assertTrue(addedAgain.merge(removed).contains("工作"));
    }

    @Test
    public void beforeGcDelayedAdditionSnapshotCannotReviveRemovedTag() {
        ORSet<String> added = ORSet.<String>empty("A").add("工作");
        ORSet<String> removed = ORSet.<String>empty("B").merge(added).remove("工作");
        ORSet<String> retried = removed.merge(added).merge(added);
        assertFalse(retried.contains("工作"));
        assertEquals(removed, retried);
        assertEquals(removed.hashCode(), retried.hashCode());
    }
}
