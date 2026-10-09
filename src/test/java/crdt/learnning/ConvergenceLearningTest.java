package crdt.learnning;

import java.util.Collections;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** 第 07 章：固定传播顺序包含暂时丢包、重试和旧快照，核对状态与业务结果。 */
public class ConvergenceLearningTest {
    @Test
    public void counterConvergesAfterALostDeliveryIsRetried() {
        GCounter phone = new GCounter("A").increment(2);
        GCounter computer = new GCounter("B").increment(3);
        GCounter delayedPhone = phone;
        // 第一次 A -> B 丢失；B -> A 成功，随后双方继续记录操作。
        phone = phone.merge(computer).increment();
        computer = computer.increment();
        GCounter fromPhone = phone;
        phone = phone.merge(computer).merge(computer);
        computer = computer.merge(delayedPhone).merge(fromPhone).merge(phone);
        assertEquals(phone, computer);
        assertEquals(7L, phone.value());
        assertEquals(7L, computer.value());
    }

    @Test
    public void labelConvergenceAlsoKeepsTheUnobservedConcurrentAddition() {
        ORSet<String> shared = ORSet.<String>empty("A").add("旅行");
        ORSet<String> phone = shared.remove("旅行");
        ORSet<String> computer = ORSet.<String>empty("B").merge(shared).add("旅行");
        ORSet<String> fromPhone = phone;
        phone = phone.merge(computer).merge(shared);
        computer = computer.merge(shared).merge(fromPhone).merge(phone);
        assertEquals(phone, computer);
        assertEquals(Collections.singleton("旅行"), phone.elements());
        assertEquals(Collections.singleton("旅行"), computer.elements());
    }

    @Test
    public void textConvergesWithConcurrentEditsAndRepeatedOldSnapshots() {
        RGA shared = new RGA("A").insert(0, '笔').insert(1, '记');
        RGA phone = shared.delete(0);
        RGA computer = new RGA("B").merge(shared).insert(1, '新');
        RGA fromPhone = phone;
        phone = phone.merge(computer).merge(shared).merge(computer);
        computer = computer.merge(shared).merge(fromPhone).merge(phone);
        assertEquals(phone, computer);
        assertEquals("新记", phone.text());
        assertEquals("新记", computer.text());
    }
}
