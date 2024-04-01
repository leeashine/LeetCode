package juc;

import java.lang.management.ManagementFactory;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.management.GarbageCollectorMXBean;

/**
 * jdk1.6的LBQ会存在gc问题，优化代码只有一行。nb!
 */
public class TestLBQ
{
    static GarbageCollectorMXBean fullgcx;
    static
    {
        for (GarbageCollectorMXBean gcx : ManagementFactory.getGarbageCollectorMXBeans().toArray(new GarbageCollectorMXBean[2]))
        {
            if (gcx == null) continue; // G1 MXBeans aren't defined yet it seems
            if ("PS MarkSweep".equals(gcx.getName()) || "MarkSweepCompact".equals(gcx.getName()))
            {
                fullgcx = gcx;
            }
            else if ("ConcurrentMarkSweep".equals(gcx.getName()))
            {
                throw new AssertionError("Test to be executed with PS MarkSweep (standard old collector)");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        long fullgc_count_ref=0, fullgc_count=0, start, end;

        // first test, create the queue after GC, head node will be in young gen
        System.gc();
        LinkedBlockingQueue q = new LinkedBlockingQueue();
        if (fullgcx != null) fullgc_count_ref = fullgcx.getCollectionCount();
        Object DUMMY = new Object();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++)
        {
            q.offer(DUMMY);
            q.take();
        }
        System.out.println("Test 1 took: " + (System.currentTimeMillis()-start) + "ms");
        if (fullgcx != null) fullgc_count = fullgcx.getCollectionCount();
        if (fullgc_count > fullgc_count_ref)
            throw new AssertionError("Full GC has occured during the test: " + (fullgc_count-fullgc_count_ref) + " times"); // not thrown

        // SAME test, but create the queue before GC, head node will be in old gen
        q = new LinkedBlockingQueue();
        System.gc();
        if (fullgcx != null) fullgc_count_ref = fullgcx.getCollectionCount();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++)
        {
            q.offer(DUMMY);
            q.take();
        }
        System.out.println("Test 2 took: " + (System.currentTimeMillis()-start) + "ms");
        if (fullgcx != null) fullgc_count = fullgcx.getCollectionCount();
        if (fullgc_count > fullgc_count_ref)
            throw new AssertionError("Full GC has occured during the test: " + (fullgc_count-fullgc_count_ref) + " times"); // ERROR
    }
}