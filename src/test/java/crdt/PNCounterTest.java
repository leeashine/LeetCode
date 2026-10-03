package crdt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class PNCounterTest {
    @Test
    public void localIncrementsAndDecrementsCancelWithoutMutation() {
        PNCounter initial = new PNCounter("a");
        PNCounter increased = initial.increment().increment(2);
        PNCounter canceled = increased.decrement().decrement(2);
        assertEquals(0L, initial.value());
        assertEquals(3L, increased.value());
        assertEquals(0L, canceled.value());
        assertNotSame(initial, canceled);
    }

    @Test
    public void concurrentPositiveAndNegativeUpdatesConverge() {
        PNCounter positive = new PNCounter("a").increment(3);
        PNCounter negative = new PNCounter("b").decrement(2);
        PNCounter left = positive.merge(negative);
        PNCounter right = negative.merge(positive);
        assertEquals(1L, left.value());
        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertEquals(left, left.merge(left));
        assertEquals(left, left.merge(positive).merge(negative));
        PNCounter third = new PNCounter("c").increment(4).decrement();
        assertEquals(left.merge(third), positive.merge(negative.merge(third)));
        assertEquals(3L, third.value());
        assertEquals(3L, positive.value());
        assertEquals(-2L, negative.value());
    }

    @Test
    public void decrementGrowsSeparateNegativeComponentInsteadOfReducingPositiveComponent() {
        PNCounter prior = new PNCounter("a").increment(3);
        PNCounter later = prior.decrement(2);
        assertEquals(1L, later.value());
        assertEquals(later, later.merge(prior));
        assertEquals(1L, prior.merge(later).value());
        assertEquals(3L, prior.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeIncrementIsRejected() {
        new PNCounter("a").increment(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeDecrementIsRejected() {
        new PNCounter("a").decrement(-1);
    }
}
