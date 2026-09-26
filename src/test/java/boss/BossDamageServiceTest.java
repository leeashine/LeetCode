package boss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class BossDamageServiceTest {

    private BossDamageService service;

    @Before
    public void setUp() {
        service = new BossDamageService(4, 32_768, 1, 4_096);
    }

    @After
    public void tearDown() {
        service.close();
    }

    @Test
    public void damageNeverOverdrawsAndKillsOnlyOnce() {
        service.registerBoss("boss-1", 100).join();

        DamageResult first = service.damage("boss-1", 70).join();
        DamageResult killing = service.damage("boss-1", 50).join();
        DamageResult afterDeath = service.damage("boss-1", 10).join();

        assertEquals(70, first.getAppliedDamage());
        assertEquals(30, first.getRemainingHealth());
        assertFalse(first.isKilled());

        assertEquals(30, killing.getAppliedDamage());
        assertEquals(0, killing.getRemainingHealth());
        assertTrue(killing.isKilled());

        assertEquals(0, afterDeath.getAppliedDamage());
        assertEquals(0, afterDeath.getRemainingHealth());
        assertFalse(afterDeath.isKilled());
    }

    @Test
    public void concurrentDamagePreservesHealthAndProducesOneKill() throws Exception {
        int producerCount = 16;
        int attacksPerProducer = 1_000;
        long initialHealth = 10_000;
        service.registerBoss("world-boss", initialHealth).join();

        ExecutorService producers = Executors.newFixedThreadPool(producerCount);
        CountDownLatch ready = new CountDownLatch(producerCount);
        CountDownLatch start = new CountDownLatch(1);
        List<CompletableFuture<DamageResult>> results =
                Collections.synchronizedList(new ArrayList<CompletableFuture<DamageResult>>());
        List<Future<?>> producerTasks = new ArrayList<Future<?>>();

        try {
            for (int producer = 0; producer < producerCount; producer++) {
                producerTasks.add(producers.submit(new Runnable() {
                    @Override
                    public void run() {
                        ready.countDown();
                        await(start);
                        for (int attack = 0; attack < attacksPerProducer; attack++) {
                            results.add(service.damage("world-boss", 1));
                        }
                    }
                }));
            }

            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();
            for (Future<?> producerTask : producerTasks) {
                producerTask.get(10, TimeUnit.SECONDS);
            }
        } finally {
            producers.shutdownNow();
        }

        long totalAppliedDamage = 0;
        int killCount = 0;
        for (CompletableFuture<DamageResult> resultFuture : results) {
            DamageResult result = resultFuture.get(10, TimeUnit.SECONDS);
            totalAppliedDamage += result.getAppliedDamage();
            if (result.isKilled()) {
                killCount++;
            }
        }

        BossSnapshot snapshot = service.snapshot("world-boss").join();
        assertEquals(producerCount * attacksPerProducer, results.size());
        assertEquals(initialHealth, totalAppliedDamage);
        assertEquals(1, killCount);
        assertEquals(0, snapshot.getCurrentHealth());
        assertTrue(snapshot.isDead());
    }

    @Test
    public void differentBossesKeepIndependentState() {
        service.registerBoss("boss-a", 100).join();
        service.registerBoss("boss-b", 200).join();

        CompletableFuture<DamageResult> damageA = service.damage("boss-a", 40);
        CompletableFuture<DamageResult> damageB = service.damage("boss-b", 70);

        assertEquals(60, damageA.join().getRemainingHealth());
        assertEquals(130, damageB.join().getRemainingHealth());
        assertEquals(60, service.snapshot("boss-a").join().getCurrentHealth());
        assertEquals(130, service.snapshot("boss-b").join().getCurrentHealth());
    }

    @Test
    public void boundedMailboxRejectsCommandsThatCannotWaitForNextTick() {
        BossDamageService slowService = new BossDamageService(1, 1, 60_000, 1);
        CompletableFuture<Void> registration = slowService.registerBoss("slow-boss", 100);

        CompletableFuture<BossSnapshot> rejected = slowService.snapshot("slow-boss");

        assertFalse(registration.isDone());
        try {
            rejected.join();
            fail("expected mailbox rejection");
        } catch (CompletionException ex) {
            assertTrue(ex.getCause() instanceof RejectedExecutionException);
        } finally {
            slowService.close();
        }
        assertTrue(registration.isDone());
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("interrupted while waiting to start", ex);
        }
    }
}
