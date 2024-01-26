package aleetcode.sort;

import java.util.PriorityQueue;

/**
 * 利用堆动态求topK
 */
public class DynamicMinKElements {
    private PriorityQueue<Integer> minHeap;
    private int k;

    public DynamicMinKElements(int k) {
        this.k = k;
        minHeap = new PriorityQueue<>((a, b) -> b - a); // 创建大小为k的最大堆
    }

    public void add(int num) {
        // 如果堆的大小小于k，直接添加元素
        if (minHeap.size() < k) {
            minHeap.offer(num);
        } else {
            // 如果堆已满，并且新元素小于堆顶元素，替换堆顶元素
            if (num < minHeap.peek()) {
                minHeap.poll(); // 移除堆顶元素
                minHeap.offer(num); // 添加新元素
            }
        }
    }

    public int[] getMinimumK() {
        int[] result = new int[k];
        int index = 0;
        for (int num : minHeap) {
            result[index++] = num;
        }
        return result;
    }

    public static void main(String[] args) {
        DynamicMinKElements solution = new DynamicMinKElements(3); // 求最小的3个值
        solution.add(4);
        solution.add(2);
        solution.add(7);
        solution.add(1);
        solution.add(9);
        solution.add(3);
        solution.add(6);

        System.out.println("最小的3个值为:");
        int[] result = solution.getMinimumK();
        for (int num : result) {
            System.out.print(num + " ");
        }
    }
}