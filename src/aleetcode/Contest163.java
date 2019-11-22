package aleetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Contest163 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int [][] grid={{1},{2},{3},{4},{7},{6},{5}};
		int k=2;
		List<List<Integer>> gd=new ArrayList<List<Integer>>();
		gd=shiftGrid(grid, k);
		
//		System.out.println(gd);
	}

	
	public int [] rightLeft(int [] arr){
		
	}
	
	public static List<List<Integer>> shiftGrid(int[][] grid, int k) {

		
		
		int n=grid.length;
		int m=grid[0].length;
		k=k % (m*n);
		List<List<Integer>> gd=new ArrayList<List<Integer>>();
		
	
		if( k % (m*n)  ==0 ||k==0){
			 for(int i=0;i<grid.length;i++){
				 List<Integer> gd1=new ArrayList<Integer>();
				 for(int j=0;j<grid[0].length;j++){
					 gd1.add(grid[i][j]);
				 }
				 gd.add(gd1);
			 }
			 return gd;
		}
		int []arr=new int [n*m];
		int l=0;
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				arr[l]=grid[i][j];
				l++;
			}
		}
		
		int []larr=new int[k];
		int kk=k;
		int idx=kk-1;
		//数组后移k个位置
		for(int i=n*m-1;kk>0;--kk,i--){
			
			larr[idx]=arr[i];
			idx--;
		}
		//数组前移k个位置
		for(int i=n*m-1;i>k-1;--i){
			
			arr[i]=arr[i-k];
			
		}
		
		for(int i=0;i<k;i++){
			arr[i]=larr[i];
		}
		
		int index=0;
		for(int j=0;j<n;j++){
			
			List<Integer> gd1=new ArrayList<Integer>();
			for(int i=0;i<m;i++){
				
				gd1.add(arr[index]);
				index++;
			}
			gd.add(gd1);
		}
		
		
		
		return gd;
		
	}

}
