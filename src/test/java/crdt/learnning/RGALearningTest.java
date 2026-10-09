package crdt.learnning;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** 第 06 章：手机与电脑离线编辑正文，字符身份不随显示位置改变。 */
public class RGALearningTest {
    @Test
    public void localInsertAndDeleteUseVisiblePositions() {
        RGA original = new RGA("A").insert(0, '笔').insert(1, '记');
        RGA edited = original.insert(1, '新').delete(0);
        assertEquals("新记", edited.text());
        assertEquals(2, edited.size());
        assertEquals("笔记", original.text());
    }

    @Test
    public void concurrentInsertionsAtTheSamePositionHaveOneStableOrder() {
        RGA shared = new RGA("A").insert(0, '记');
        RGA phone = shared.insert(1, '甲');
        RGA computer = new RGA("B").merge(shared).insert(1, '乙');
        assertEquals("记乙甲", phone.merge(computer).text());
        assertEquals(phone.merge(computer), computer.merge(phone));
        assertEquals("B", computer.replicaId());
        assertEquals(3, phone.merge(computer).size());
    }

    @Test
    public void deletingAnAnchorDoesNotDeleteItsConcurrentChild() {
        RGA shared = new RGA("A").insert(0, '笔').insert(1, '记');
        RGA phone = shared.delete(0);
        RGA computer = new RGA("B").merge(shared).insert(1, '新');
        assertEquals("新记", phone.merge(computer).text());
        assertEquals(phone.merge(computer).text(), computer.merge(phone).text());
        assertEquals("记", phone.text());
    }

    @Test
    public void repeatedDeliveryBeforeGcDoesNotDuplicateOrReviveCharacters() {
        RGA oldPhone = new RGA("A").insert(0, '草').insert(1, '稿');
        RGA removed = new RGA("B").merge(oldPhone).delete(0);
        RGA computer = removed.merge(oldPhone).merge(removed).merge(oldPhone);
        assertEquals("稿", computer.text());
        assertEquals(removed, computer);
        assertEquals(1, computer.size());
    }
}
