package aleetcode.contest2019;

import java.util.Arrays;

public class RelativeSortArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int [] arr1=new int[] {2,3,1,3,2,4,6,7,9,2,19};
		
		int [] arr2=new int[] {2,1,4,3,9,6};
		relativeSortArray(arr1,arr2);
	}

//	Input: arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
//	Output: [2,2,2,1,4,3,3,9,6,7,19]
	
	public static int[] relativeSortArray(int[] arr1, int[] arr2) {

		int [] counter=new int [arr1.length];  
		int [] newArr=new int [arr1.length];  
		int [] carr2=new int [arr2.length];
		int index=0;
		for(int i=0;i<arr1.length;i++){
			for(int j=0;j<arr2.length;j++){

				if(arr1[i]==arr2[j]){

					counter[i]=1;
					
					newArr[index]=arr2[j];
				}
				//不在数组里的按升序排序
				newArr[index]=arr1[i];
				
			}
			index++;
		}
		
		return newArr;
		
		
		
	}
	
}
