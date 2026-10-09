package crdt.learnning;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** 第 03 章：笔记标题采用 LWW，逻辑时间与真实时间分开。 */
public class RegisterLearningTest {
    @Test
    public void receivingRemoteTimeMakesTheNextLocalWriteLater() {
        LamportClock phone = LamportClock.start().tick();
        LamportClock computer = LamportClock.start().observe(phone.time()).tick();
        assertEquals(1L, phone.time());
        assertEquals(2L, computer.time());
        assertEquals(8L, computer.observe(7L).tick().time());
        assertEquals(2L, computer.observe(1L).time());
    }

    @Test
    public void equalTimeOfflineTitlesUseTheSameReplicaTieBreak() {
        LWWRegister<String> phone = LWWRegister.<String>empty().set("周末旅行", 1L, "A");
        LWWRegister<String> computer = LWWRegister.<String>empty().set("周末徒步", 1L, "B");
        assertEquals("周末徒步", phone.merge(computer).value());
        assertEquals(phone.merge(computer), computer.merge(phone));
        assertEquals("B", phone.merge(computer).replicaId());
        assertEquals("周末旅行", phone.value());
    }

    @Test
    public void editAfterReceivingTitleWinsAndOldSnapshotsStayHarmless() {
        LWWRegister<String> phone = LWWRegister.<String>empty().set("草稿", 1L, "A");
        LamportClock computerClock = LamportClock.start().observe(phone.timestamp()).tick();
        LWWRegister<String> computer = LWWRegister.<String>empty().merge(phone)
                .set("旅行清单", computerClock.time(), "B");
        assertEquals("旅行清单", computer.merge(phone).merge(phone).value());
        assertEquals(2L, computer.timestamp());
    }

    @Test
    public void emptyRegisterIsTheStartingStateAndMergeIdentity() {
        LWWRegister<String> empty = LWWRegister.empty();
        LWWRegister<String> title = empty.set("离线笔记", 4L, "A");
        assertTrue(empty.isEmpty());
        assertEquals(title, empty.merge(title));
        assertEquals(title, title.merge(empty));
        assertEquals(title, title.merge(title));
    }
}
