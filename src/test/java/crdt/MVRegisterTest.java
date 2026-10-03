package crdt;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MVRegisterTest {
    @Test
    public void concurrentWritesSurviveInEitherSynchronizationDirection() {
        MVRegister<String> a = MVRegister.<String>empty().assign("url-a", "A");
        MVRegister<String> b = MVRegister.<String>empty().assign("url-b", "B");

        MVRegister<String> onA = a.merge(b);
        MVRegister<String> onB = b.merge(a);
        assertEquals(new HashSet<String>(Arrays.asList("url-a", "url-b")), onA.values());
        assertEquals(onA.values(), onB.values());
        assertEquals(onA, onB);
        assertEquals(2, onA.size());
        assertTrue(onA.version().equals(a.version().merge(b.version())));
        assertEquals(Collections.singleton("url-a"), a.values());
        assertEquals(Collections.singleton("url-b"), b.values());
    }

    @Test
    public void causallyLaterWriteReplacesEarlierValue() {
        MVRegister<String> a = MVRegister.<String>empty().assign("old", "A");
        MVRegister<String> b = MVRegister.<String>empty().merge(a).assign("new", "B");

        assertEquals(Collections.singleton("new"), a.merge(b).values());
        assertEquals(Collections.singleton("new"), b.merge(a).values());
        assertEquals(1, a.merge(b).size());
        assertEquals(b, a.merge(b));
    }

    @Test
    public void thirdReplicaOverwritesBothConcurrentWrites() {
        MVRegister<String> a = MVRegister.<String>empty().assign("A value", "A");
        MVRegister<String> b = MVRegister.<String>empty().assign("B value", "B");
        MVRegister<String> c = MVRegister.<String>empty().merge(a).merge(b);
        assertEquals(2, c.size());

        MVRegister<String> updated = c.assign("C value", "C");
        MVRegister<String> onA = a.merge(updated);
        MVRegister<String> onB = b.merge(updated);
        assertEquals(Collections.singleton("C value"), onA.values());
        assertEquals(1, onA.size());
        assertEquals(updated, onA);
        assertEquals(updated, onB);
        assertEquals(updated, c.merge(updated));
    }

    @Test
    public void mergeLawsHoldAcrossConcurrentAndCausallyRelatedStates() {
        MVRegister<String> empty = MVRegister.empty();
        MVRegister<String> a = empty.assign("a", "A");
        MVRegister<String> b = empty.assign("b", "B");
        MVRegister<String> later = a.merge(b).assign("later", "C");

        assertEquals(a, a.merge(empty));
        assertEquals(a, empty.merge(a));
        assertEquals(a, a.merge(a));
        assertEquals(a.merge(b), b.merge(a));
        assertEquals(a.merge(b).merge(later), a.merge(b.merge(later)));
        assertEquals(later, a.merge(b).merge(later));
        assertEquals(empty, empty.merge(empty));
        assertEquals(a.hashCode(), a.merge(a).hashCode());
    }

    @Test
    public void mergePrunesDominatedEntriesEvenWhenTheyArriveLater() {
        MVRegister<String> oldA = MVRegister.<String>empty().assign("a", "A");
        MVRegister<String> oldB = MVRegister.<String>empty().assign("b", "B");
        MVRegister<String> laterA = oldA.assign("later a", "A");
        MVRegister<String> combined = oldA.merge(oldB).merge(laterA).merge(oldA).merge(oldB);

        assertEquals(new HashSet<String>(Arrays.asList("later a", "b")), combined.values());
        assertEquals(laterA.merge(oldB), combined);
        assertEquals(2, combined.size());
        assertEquals(combined, combined.merge(combined));
    }

    @Test
    public void equalVectorsWithDifferentValuesAreNotDeduplicated() {
        MVRegister<String> a = MVRegister.<String>empty().assign("fork one", "A");
        MVRegister<String> fork = MVRegister.<String>empty().assign("fork two", "A");
        MVRegister<String> merged = a.merge(fork);

        assertEquals(new HashSet<String>(Arrays.asList("fork one", "fork two")), merged.values());
        assertEquals(2, merged.size());
        assertEquals(merged, fork.merge(a));
        assertEquals(merged, merged.merge(fork));
        assertEquals(Collections.singleton("resolved"), merged.assign("resolved", "A").values());
    }

    @Test
    public void versionVectorTracksPartialOrderAndSortedImmutableState() {
        VersionVector empty = VersionVector.empty();
        VersionVector a = empty.increment("z").increment("z");
        VersionVector b = empty.increment("a");
        VersionVector joined = a.merge(b);

        assertEquals(0L, empty.get("missing"));
        assertTrue(empty.asMap().isEmpty());
        assertEquals(Arrays.asList("a", "z"), Arrays.asList(joined.asMap().keySet().toArray()));
        assertEquals(Long.valueOf(1L), joined.asMap().get("a"));
        assertEquals(Long.valueOf(2L), joined.asMap().get("z"));
        assertTrue(a.isConcurrent(b));
        assertTrue(b.isConcurrent(a));
        assertFalse(a.greaterOrEqual(b));
        assertFalse(b.greaterOrEqual(a));
        assertTrue(joined.dominates(a));
        assertTrue(joined.dominates(b));
        assertTrue(joined.greaterOrEqual(joined));
        assertFalse(joined.dominates(joined));
        assertFalse(joined.isConcurrent(joined));
        assertEquals(joined, b.merge(a));
        assertEquals(joined, joined.merge(joined));
        assertEquals(a.merge(b).merge(joined), a.merge(b.merge(joined)));
        assertEquals(joined.hashCode(), b.merge(a).hashCode());
        assertEquals(a, a.merge(empty));
        assertNotEquals(a, b);
        try {
            Map<String, Long> readOnly = joined.asMap();
            readOnly.put("a", 999L);
            fail("asMap must be immutable");
        } catch (UnsupportedOperationException expected) {
            assertEquals(1L, joined.get("a"));
        }
        assertEquals(0L, a.get("a"));
    }

    @Test
    public void rejectsMissingReplicaIdsAndExposesImmutableValues() {
        for (String replicaId : new String[]{null, ""}) {
            try {
                VersionVector.empty().increment(replicaId);
                fail("invalid replica id accepted");
            } catch (IllegalArgumentException expected) {
                // Invalid IDs must not become counters.
            }
            try {
                MVRegister.<String>empty().assign("value", replicaId);
                fail("invalid replica id accepted");
            } catch (IllegalArgumentException expected) {
                // Local writes use the same validation.
            }
        }
        MVRegister<String> register = MVRegister.<String>empty().assign("value", "A");
        try {
            register.values().add("injected");
            fail("values must be immutable");
        } catch (UnsupportedOperationException expected) {
            assertEquals(Collections.singleton("value"), register.values());
        }
        assertTrue(MVRegister.<String>empty().isEmpty());
        assertFalse(register.isEmpty());
    }
}
