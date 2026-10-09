package crdt.learnning;

import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** 第 08 章：增量传输保留待确认内容，重复和乱序仍满足笔记语义。 */
public class DeltaLearningTest {
    @Test
    public void counterDeltaCarriesAbsoluteAccumulatedCount() {
        DeltaGCounter phone = new DeltaGCounter("A").increment(2);
        DeltaGCounter.Delta older = phone.pending();
        phone = phone.ack().increment(3);
        DeltaGCounter.Delta newer = phone.pending();
        assertEquals(5L, newer.counts().get("A").longValue());
        DeltaGCounter computer = new DeltaGCounter("B").apply(newer).apply(older);
        assertEquals(5L, computer.value());
        assertEquals(newer, older.merge(newer));
        assertEquals(newer, newer.merge(older));
    }

    @Test
    public void lostAcknowledgementCanResendWithoutMultiplyingOperations() {
        DeltaGCounter phone = new DeltaGCounter("A").increment(4);
        DeltaGCounter.Delta sent = phone.pending();
        DeltaGCounter computer = new DeltaGCounter("B").apply(sent).apply(sent).apply(sent);
        assertEquals(4L, computer.value());
        assertEquals(sent, phone.pending());
        assertFalse(phone.pending().isEmpty());
        assertTrue(computer.pending().isEmpty());
        assertEquals(sent, sent.merge(sent));
        assertEquals(sent, sent.merge(DeltaGCounter.Delta.empty()));
    }

    @Test
    public void acknowledgementKeepsStateAndTheNextWriteBecomesPending() {
        DeltaGCounter original = new DeltaGCounter("A").increment(2);
        DeltaGCounter acked = original.ack();
        assertTrue(acked.pending().isEmpty());
        assertEquals(2L, acked.value());
        assertEquals(original, acked);
        DeltaGCounter edited = acked.increment();
        assertEquals(3L, edited.pending().counts().get("A").longValue());
        assertEquals(3L, edited.value());
        assertEquals(2L, original.pending().counts().get("A").longValue());
    }

    @Test
    public void tagRemovalArrivingBeforeAdditionStillSuppressesThatAddition() {
        DeltaORSet<String> phone = DeltaORSet.<String>empty("A").add("已完成");
        DeltaORSet.Delta<String> addition = phone.pending();
        DeltaORSet<String> removed = phone.ack().remove("已完成");
        DeltaORSet.Delta<String> removal = removed.pending();
        assertTrue(removal.added().isEmpty());
        assertEquals(1, removal.removed().size());
        DeltaORSet<String> computer = DeltaORSet.<String>empty("B")
                .apply(removal).apply(addition).apply(removal);
        assertFalse(computer.contains("已完成"));
        assertEquals(removed, computer);
        assertTrue(computer.pending().isEmpty());
    }

    @Test
    public void acknowledgedTagRemovalDoesNotBlockANewAddition() {
        DeltaORSet<String> removed = DeltaORSet.<String>empty("A")
                .add("待办").remove("待办");
        DeltaORSet.Delta<String> deletion = removed.pending();
        DeltaORSet<String> acked = removed.ack();
        assertTrue(acked.pending().isEmpty());
        assertEquals(removed, acked);
        DeltaORSet<String> phone = acked.add("待办");
        assertFalse(phone.pending().isEmpty());
        DeltaORSet<String> computer = DeltaORSet.<String>empty("B")
                .apply(phone.pending()).apply(deletion).apply(phone.pending());
        assertEquals(Collections.singleton("待办"), computer.elements());
        assertEquals(phone, computer);
        assertEquals(deletion, removed.pending());
    }
}
