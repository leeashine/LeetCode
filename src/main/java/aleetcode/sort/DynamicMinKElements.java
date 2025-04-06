package aleetcode.sort;

import java.util.PriorityQueue;

/**
 * 利用堆动态求topK
 */
public class DynamicMinKElements {

    static class Station {
        int level;
        String name;

        Double price;

        public Station(Double price) {
            this.price = price;
        }
    }

    private PriorityQueue<Station> minHeap;
    private int k;

    public DynamicMinKElements(int k) {
        this.k = k;
        minHeap = new PriorityQueue<>((a, b) -> b.price.compareTo(a.price)); // 创建大小为k的最大堆
    }

    public void add(Station station) {
        // 如果堆的大小小于k，直接添加元素
        if (minHeap.size() < k) {
            minHeap.offer(station);
        } else {
            // 如果堆已满，并且新元素小于堆顶元素，替换堆顶元素
            if (station.price.compareTo(minHeap.peek().price) < 0) {
                minHeap.poll(); // 移除堆顶元素
                minHeap.offer(station); // 添加新元素
            }
        }
    }

    public Station[] getMinimumK() {
        Station[] result = new Station[k];
        int index = 0;
        for (Station num : minHeap) {
            result[index++] = num;
        }
        return result;
    }

    public static void main(String[] args) {
        DynamicMinKElements solution = new DynamicMinKElements(3); // 求最小的3个值

        solution.add(new Station(4.0));
        solution.add(new Station(2.0));
        solution.add(new Station(7.0));
        solution.add(new Station(1.0));
        solution.add(new Station(9.0));
        solution.add(new Station(3.0));
        solution.add(new Station(6.0));

        System.out.println("最小的3个值为:");
        Station[] result = solution.getMinimumK();
        for (Station num : result) {
            System.out.print(num.price + " ");
        }
    }
}