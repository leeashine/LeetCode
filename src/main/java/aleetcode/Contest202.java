package aleetcode;

public class Contest202 {
    public static void main(String[] args) {

    }
    //存在连续三个奇数的数组
    //arr = [2,6,4,1]
    public boolean threeConsecutiveOdds(int[] arr) {

        for(int i=0;i<arr.length-2;i++){
            int j=i+1;
            int k=j+1;
            if(arr[i]%2!=0&&arr[j]%2!=0&&arr[k]%2!=0)
                return true;
        }
        return  false;


    }
}
