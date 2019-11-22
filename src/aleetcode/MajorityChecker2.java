package aleetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MajorityChecker2 {

	public static void main(String[] args) {

		int[] arr = { 2, 2, 1, 2, 1, 2, 2, 1, 1, 2 };
		MajorityChecker2 majorityChecker = new MajorityChecker2(arr);
		majorityChecker.query(2, 3, 2);
	}

	private interface Merger<E> {
	      E merge(E a, E b);
	  }

    private class SegmentTree<E> {

      private E[] tree;
      private E[] data;
      private Merger<E> merger;

      public SegmentTree(E[] arr, Merger<E> merger){

          this.merger = merger;

          data = (E[])new Object[arr.length];
          for(int i = 0 ; i < arr.length ; i ++)
              data[i] = arr[i];

          tree = (E[])new Object[4 * arr.length];
          buildSegmentTree(0, 0, arr.length - 1);
      }

      // 在treeIndex的位置创建表示区间[l...r]的线段树
      private void buildSegmentTree(int treeIndex, int l, int r){

          if(l == r){
              tree[treeIndex] = data[l];
              return;
          }

          int leftTreeIndex = leftChild(treeIndex);
          int rightTreeIndex = rightChild(treeIndex);

          // int mid = (l + r) / 2;
          int mid = l + (r - l) / 2;
          buildSegmentTree(leftTreeIndex, l, mid);
          buildSegmentTree(rightTreeIndex, mid + 1, r);

          tree[treeIndex] = merger.merge(tree[leftTreeIndex], tree[rightTreeIndex]);
      }

      public int getSize(){
          return data.length;
      }

      public E get(int index){
          if(index < 0 || index >= data.length)
              throw new IllegalArgumentException("Index is illegal.");
          return data[index];
      }

      // 返回完全二叉树的数组表示中，一个索引所表示的元素的左孩子节点的索引
      private int leftChild(int index){
          return 2*index + 1;
      }

      // 返回完全二叉树的数组表示中，一个索引所表示的元素的右孩子节点的索引
      private int rightChild(int index){
          return 2*index + 2;
      }

      // 返回区间[queryL, queryR]的值
      public E query(int queryL, int queryR){

          if(queryL < 0 || queryL >= data.length ||
                  queryR < 0 || queryR >= data.length || queryL > queryR)
              throw new IllegalArgumentException("Index is illegal.");

          return query(0, 0, data.length - 1, queryL, queryR);
      }

      // 在以treeIndex为根的线段树中[l...r]的范围里，搜索区间[queryL...queryR]的值
      private E query(int treeIndex, int l, int r, int queryL, int queryR){

          if(l == queryL && r == queryR)
              return tree[treeIndex];

          int mid = l + (r - l) / 2;
          // treeIndex的节点分为[l...mid]和[mid+1...r]两部分

          int leftTreeIndex = leftChild(treeIndex);
          int rightTreeIndex = rightChild(treeIndex);
          if(queryL >= mid + 1)
              return query(rightTreeIndex, mid + 1, r, queryL, queryR);
          else if(queryR <= mid)
              return query(leftTreeIndex, l, mid, queryL, queryR);

          E leftResult = query(leftTreeIndex, l, mid, queryL, mid);
          E rightResult = query(rightTreeIndex, mid + 1, r, mid + 1, queryR);
          return merger.merge(leftResult, rightResult);
      }

      @Override
      public String toString(){
          StringBuilder res = new StringBuilder();
          res.append('[');
          for(int i = 0 ; i < tree.length ; i ++){
              if(tree[i] != null)
                  res.append(tree[i]);
              else
                  res.append("null");

              if(i != tree.length - 1)
                  res.append(", ");
          }
          res.append(']');
          return res.toString();
      }
  }

	private SegmentTree<Integer> segmentTree;
	private Map<Integer, List<Integer>> idx;
	private Merger<Integer> merger;

	public MajorityChecker2(int[] arr) {
		
		idx = new HashMap<>();
		for (int i = 0; i < arr.length; i++) {
			idx.computeIfAbsent(arr[i], a -> new ArrayList<>()).add(i);
		}
		// int[] 转 Integer[]
        Integer[] arr2 = Arrays.stream(arr).boxed().toArray(Integer[]::new);
		segmentTree=new SegmentTree<Integer>(arr2, merger);
	}

	
	public int query(int left, int right, int threshold) {
		int[] M =new int[1], C = new int[]{-1};
	    segmentTree.query(left, right);
	    int freq = getFreq(M[0],left,right);
	    return freq>=threshold?M[0]:-1;
//	    mergeQuery(M,count,root.majority,root.count);
	}
	


	private void mergeQuery(int[] majority, int[] count, int curM, int curC) {
		if (count[0] == -1) {
			majority[0] = curM;
			count[0] = curC;
		} else if (majority[0] == curM)
			count[0] += curC;
		else {
			majority[0] = count[0] >= curC ? majority[0] : curM;
			count[0] = Math.abs(count[0] - curC);
		}
	}

	public int getFreq(int target, int left, int right) {
		List<Integer> index = idx.get(target);
		int rightIdx = findIdx(index, right), leftIdx = findIdx(index, left);
		if (rightIdx < 0)
			return 0;
		else if (leftIdx < 0)
			return rightIdx - leftIdx;
		else if (index.get(leftIdx) == left)
			return rightIdx - leftIdx + 1;
		else
			return rightIdx - leftIdx;
	}

	private int findIdx(List<Integer> idx, int pos) {
		int start = 0, end = idx.size() - 1, mid;
		while (start <= end) {
			mid = (start + end) >> 1;
			if (idx.get(mid) < pos)
				start = mid + 1;
			else if (idx.get(mid) > pos)
				end = mid - 1;
			else
				return mid;
		}
		return end;
	}

}
