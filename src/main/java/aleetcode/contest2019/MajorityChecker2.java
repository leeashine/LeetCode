package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

      // ��treeIndex��λ�ô�����ʾ����[l...r]���߶���
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

      // ������ȫ�������������ʾ�У�һ����������ʾ��Ԫ�ص����ӽڵ������
      private int leftChild(int index){
          return 2*index + 1;
      }

      // ������ȫ�������������ʾ�У�һ����������ʾ��Ԫ�ص��Һ��ӽڵ������
      private int rightChild(int index){
          return 2*index + 2;
      }

      // ��������[queryL, queryR]��ֵ
      public E query(int queryL, int queryR){

          if(queryL < 0 || queryL >= data.length ||
                  queryR < 0 || queryR >= data.length || queryL > queryR)
              throw new IllegalArgumentException("Index is illegal.");

          return query(0, 0, data.length - 1, queryL, queryR);
      }

      // ����treeIndexΪ�����߶�����[l...r]�ķ�Χ���������[queryL...queryR]��ֵ
      private E query(int treeIndex, int l, int r, int queryL, int queryR){

          if(l == queryL && r == queryR)
              return tree[treeIndex];

          int mid = l + (r - l) / 2;
          // treeIndex�Ľڵ��Ϊ[l...mid]��[mid+1...r]������

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
		// int[] ת Integer[]
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
