package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MooreMajorityVote {

	public static void main(String[] args) {
		
		int [] nums={1,1,2,1,2,1,2,2,1,1,1};
		majorityElement(nums);

		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(8);
		list.add(12);
		int [] nums2={0,1,3,5,6,9};
//		int low=lowestIndexSearch(nums2,2);
//		int high=highestIndexSearch(nums2, 3);
//		System.out.println("lowIndex="+low+"  ;  highIndex="+high);
		
		
		
	}
	
	public int moer(int left,int right){
		int[] newNums=Arrays.copyOfRange(nums, left, right+1);
		
	    int majority = -1;

	    int count = 0;

	    for (int num : newNums) {
	        if (count == 0) {
	            majority = num;
	            count++;
	        } else {
	            if (majority == num) {
	                count++;
	            } else {
	                count--;
	            }
	        }
	    }
//	    return majority;

	    int counter = 0;
	    if (count <= 0) {
	        return -1;
	    } else {
	        for (int num : newNums) {
	            if (num == majority) counter ++;
	        }
	    }

	    if (counter > newNums.length / 2) {
	        return majority;
	    }

	    return -1;
		
	}
	
	//�ҳ���������ִ�������һ���Ԫ��
	//������ �������г���һ������(n/2)��Ԫ�ء�
	public static int majorityElement(int[] nums) {

	    int majority = -1;

	    int count = 0;

	    for (int num : nums) {
	        if (count == 0) {
	            majority = num;
	            count++;
	        } else {
	            if (majority == num) {
	                count++;
	            } else {
	                count--;
	            }
	        }
	    }
//	    return majority;

	    int counter = 0;
	    if (count <= 0) {
	        return -1;
	    } else {
	        for (int num : nums) {
	            if (num == majority) counter ++;
	        }
	    }

	    if (counter > nums.length / 2) {
	        return majority;
	    }

	    return -1;
	}

	public static int binSearch(int arr[], int target){
		
		int l = 0, r = arr.length - 1; 
        while(l <= r){ 
            int mid = l + (r - l)/2; //��ֹ�������
            if(target == arr[mid]){
                return mid;
            }
            if (target > arr[mid]) {
                l = mid + 1; 
            }else{ 
                r = mid - 1; 
            }
        }
        return -1; 
		
	}
	
	
	
	public static int lowestIndexSearch(List<Integer> list, int t){
        int i=0, j=list.size();
        while(i<j){
            int mid = (i+j)/2;
            if(list.get(mid)<t){
                 i=mid+1;
            }else{
                j=mid;
            }
        }
       
       return i;
   }
   
	public static int highestIndexSearch(List<Integer> list, int t){
       int i=0,j=list.size();
       while(i<j){
           int mid = (i+j)/2;
           if(list.get(mid)<=t){
               i=mid+1;
           }else{
               j=mid;
           }
       }
       
       return i-1;
   }
	
	 Map<Integer, List<Integer>> map = new HashMap<>();
     int[] nums;
     
	 public int query(int left, int right, int threshold) {
         
		 int num=moer(left,right);
         
		 if(num==-1) return -1;
		 
         int low_index = lowestIndexSearch(map.get(num), left);
         int high_index = highestIndexSearch(map.get(num), right);
         
         if(high_index-low_index+1>=threshold){
             //System.out.println(high_index + " " + low_index);
             return num;
         }
         
         return -1;
   }
}
