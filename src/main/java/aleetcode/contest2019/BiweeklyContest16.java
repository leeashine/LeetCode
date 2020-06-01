package aleetcode.contest2019;

public class BiweeklyContest16 {

    public static void main(String[] args) {

        int [] arr={17,18,5,4,6,1};
        new BiweeklyContest16().replaceElements(arr);
    }
//    Input: arr = [17,18,5,4,6,1]
//    Output: [18,6,6,6,1,-1]
    public int[] replaceElements(int[] arr) {
    //        int [] res=new int [arr.length];
        int max=-1;
        for (int i = arr.length-1; i >=0; i--) {
            int val=arr[i];
            arr[i]=max;
            max=Math.max(max,val);
        }

        System.out.println();
        return arr;

    }

}
