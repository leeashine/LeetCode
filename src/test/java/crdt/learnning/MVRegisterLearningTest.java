package crdt.learnning;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MVRegisterLearningTest {
    @Test
    public void receiveThenEditReplacesOldValue() {
        MVRegister<String> phone = MVRegister.<String>empty().assign("六点", "A");
        MVRegister<String> computer = MVRegister.<String>empty().merge(phone).assign("七点", "B");
        assertEquals(Collections.singleton("七点"), phone.merge(computer).values());
    }

    @Test
    public void offlineEditsRemainUntilSavedTogether() {
        MVRegister<String> phone = MVRegister.<String>empty().assign("六点", "A");
        MVRegister<String> computer = MVRegister.<String>empty().assign("七点", "B");
        MVRegister<String> both = phone.merge(computer);
        assertEquals(new HashSet<String>(Arrays.asList("六点", "七点")), both.values());
        MVRegister<String> chosen = both.assign("七点", "A");
        assertEquals(2L, chosen.version().get("A"));
        assertEquals(1L, chosen.version().get("B"));
        assertEquals(Collections.singleton("七点"), chosen.merge(phone).values());
    }

    @Test
    public void compareRecordsPerDevice() {
        VersionVector a = VersionVector.empty().increment("A");
        VersionVector b = VersionVector.empty().increment("B");
        VersionVector joined = a.merge(b);
        assertTrue(a.isConcurrent(b));
        assertTrue(joined.dominates(a));
        assertTrue(joined.dominates(b));
        assertFalse(joined.dominates(joined));
        assertEquals(0L, a.get("B"));
    }

    @Test
    public void repeatedAndReorderedMergesKeepTheSameState() {
        MVRegister<String> a = MVRegister.<String>empty().assign("六点", "A");
        MVRegister<String> b = MVRegister.<String>empty().assign("七点", "B");
        MVRegister<String> later = a.merge(b).assign("已确认", "A");
        assertEquals(a, a.merge(a));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals(a.merge(b).merge(later), a.merge(b.merge(later)));
    }
}
