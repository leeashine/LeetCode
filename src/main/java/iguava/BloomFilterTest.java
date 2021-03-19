package iguava;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

/**
 * 千亿万亿级数据如何去重？--------布隆过滤器
 *
 * <p>使用一个大的位数组，值都为0。
 * 使用k个独立的哈希函数，将元素映射到位数组里，并置为1。
 * 这样当下一个元素来了，看经过哈希函数过后映射的那几位的值。如果都为1，则表示可能被访问过，如果有一个为0，则表示一定不存在这个集合里。
 *
 * Bloom Filter是一种空间效率很高的随机数据结构，它利用位数组很简洁地表示一个集合，并能判断一个元素是否属于这个集合。
 * <p>Bloom Filter通过极少的错误换取了存储空间的极大节省。
 * <p>注意布隆过滤器不能删除元素，原因也很简单，你删除的这个位置上也有可能别的元素通过哈希映射到这个位置上。
 * <p>Guava的布隆过滤器BloomFilter，有个参数需要注意，fpp(期望误判率)，值越小所要的空间则越大。
 * <p>经统计期望数据100W,误判率0.1% 大小1.7139435 M。
 * <p>1000W,误判率0.1% 约为17M。
 * <p>一亿,误判率0.001% 约为285M。
 * debug-bloomFilter.bitSize()/8/1024/1024
 *
 * @{link https://blog.csdn.net/jiaomeng/article/details/1495500}
 *
 */
public class BloomFilterTest {
    private static int size = 10000000;//预计要插入多少数据

    private static double fpp = 0.001;//期望的误判率

    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);

    public static void main(String[] args) {
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        for (int i = 0; i < size; i++) {
            //肯定不在
            if (!bloomFilter.mightContain(i)) {
                System.out.println("有坏人逃脱了");
            }
        }

        List<Integer> list = new ArrayList<Integer>(1000);
        for (int i = size + 10000; i < size + 20000; i++) {
            //有可能重复的
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("有误伤的数量：" + list.size());

    }

}
