package aleetcode;

import java.util.*;

/**
 * 统计一段时间的topk,流动的
 * 插入元素：
 * 将新元素的出现次数在 count 中增加。
 * 更新 freqMap，将元素添加到相应的频率集合中。
 * 如果该元素的新频率足以使其成为 top-k，则将其添加到 topK 中。
 * 删除元素：
 * 当元素从窗口滑出时，更新 count 减少该元素的出现次数。
 * 更新 freqMap，将元素从旧频率集合中移除。
 * 如果该元素在 topK 中，检查是否需要从 topK 中移除。
 * 获取 top-k：
 * 直接从 topK 中读取 top-k 元素。
 */
public class SlidingWindowTopK {
    private Map<Integer, Integer> count; // Element to frequency mapping
    private TreeMap<Integer, Set<Integer>> freqMap; // Frequency to element mapping
    private TreeMap<Integer, Integer> topK; // Top k frequencies and elements
    private int k;
    private int[] nums;
    private int w;

    public SlidingWindowTopK(int[] nums, int k, int w) {
        this.count = new HashMap<>();
        this.freqMap = new TreeMap<>(Collections.reverseOrder());
        this.topK = new TreeMap<>(Collections.reverseOrder());
        this.nums = nums;
        this.k = k;
        this.w = w;
    }

    private void add(int num) {
        int oldCount = count.getOrDefault(num, 0);
        int newCount = oldCount + 1;
        count.put(num, newCount);

        // Update freqMap
        freqMap.computeIfAbsent(oldCount, x -> new HashSet<>()).remove(num);
        if (freqMap.get(oldCount).isEmpty()) {
            freqMap.remove(oldCount);
        }
        freqMap.computeIfAbsent(newCount, x -> new HashSet<>()).add(num);

        // Update topK
        if (topK.size() < k || newCount >= topK.firstKey()) {
            topK.put(newCount, num);
            if (topK.size() > k) {
                topK.pollLastEntry();
            }
        }
    }

    private void remove(int num) {
        int oldCount = count.get(num);
        int newCount = oldCount - 1;
        count.put(num, newCount);

        // Update freqMap
        freqMap.computeIfAbsent(oldCount, x -> new HashSet<>()).remove(num);
        if (freqMap.get(oldCount).isEmpty()) {
            freqMap.remove(oldCount);
        }
        if (newCount > 0) {
            freqMap.computeIfAbsent(newCount, x -> new HashSet<>()).add(num);
        }

        // Update topK
        if (topK.containsKey(oldCount) && topK.get(oldCount) == num) {
            topK.remove(oldCount);
            if (!topK.containsKey(newCount) || newCount > topK.firstKey()) {
                topK.put(newCount, num);
            }
            if (topK.size() > k) {
                topK.pollLastEntry();
            }
        }
    }

    public List<List<Integer>> getTopKElements() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            add(nums[i]);
            if (i >= w - 1) {
                result.add(new ArrayList<>(topK.values()));
                remove(nums[i - w + 1]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 2, 1, 3, 4, 4, 4, 4, 1, 1, 1, 1, 1};
        int k = 2;
        int w = 4;
        SlidingWindowTopK solution = new SlidingWindowTopK(nums, k, w);
        List<List<Integer>> results = solution.getTopKElements();
        for (List<Integer> res : results) {
            System.out.println(res.toString());
        }
    }
}