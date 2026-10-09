package crdt.learnning;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** 第 02 章：手机 A 与电脑 B 离线记录笔记操作统计。 */
public class CounterLearningTest {
    @Test
    public void offlineOperationCountsAreBothKept() {
        GCounter phone = new GCounter("A").increment(2);
        GCounter computer = new GCounter("B").increment(3);
        GCounter synced = phone.merge(computer);
        assertEquals(5L, synced.value());
        assertEquals(2L, synced.countOf("A"));
        assertEquals(3L, synced.countOf("B"));
        assertEquals("A", synced.replicaId());
        assertEquals(synced, computer.merge(phone));
    }

    @Test
    public void retransmittingOldAndNewSnapshotsDoesNotCountTwice() {
        GCounter oldPhone = new GCounter("A").increment(2);
        GCounter newPhone = oldPhone.increment(3);
        GCounter computer = new GCounter("B").increment().merge(newPhone);
        assertEquals(6L, computer.merge(oldPhone).merge(newPhone).value());
        assertEquals(2L, oldPhone.value());
        assertEquals(5L, newPhone.value());
        assertEquals(0L, computer.countOf("unknown"));
    }

    @Test
    public void editsAndUndoUseIndependentPositiveAndNegativeCounts() {
        PNCounter phone = new PNCounter("A").increment(5).decrement(2);
        PNCounter computer = new PNCounter("B").increment().decrement(3);
        PNCounter synced = phone.merge(computer);
        assertEquals(1L, synced.value());
        assertEquals(synced, synced.merge(phone).merge(computer));
        assertEquals(synced, computer.merge(phone));
        assertEquals(3L, phone.value());
        assertEquals(-2L, computer.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDeltaCannotBeAnIncrement() {
        new GCounter("A").increment(-1);
    }
}
