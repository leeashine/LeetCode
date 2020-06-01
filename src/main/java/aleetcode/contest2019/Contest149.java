package aleetcode.contest2019;

public class Contest149 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String date = "1900-03-25";

//		System.out.println(dayOfYear(date));
		
//		numRollsToTarget(2,6,7);
		
		
		maxRepOpt1("ababa");
	}

	 public static int maxRepOpt1(String s) {
	        int len = s.length();
	        int[] count = new int[26];
	        
	        int[] newCount = new int[26];
	        for(char c:s.toCharArray()) 
	        	newCount[c-'a']++;
	        
	        int start = 0, maxCount = 0, maxLength = 0;
	        for (int end = 0; end < len; end++) {
	            maxCount = Math.max(maxCount, ++count[s.charAt(end) - 'a']);
	            while (end - start + 1 - maxCount > 1 || end - start + 1>newCount[s.charAt(start)-'a']) {
	                count[s.charAt(start) - 'a']--;
	                start++;
	                // maxCount = Math.max(count[s.charAt(end)-'a'],count[s.charAt(start)-'a']);
	                maxCount=0;
	                for(int i = 0; i < 26; i++){
	                    if(maxCount < count[i]){
	                        maxCount = count[i];
	                    }
	                }
	            }
	            maxLength = Math.max(maxLength, end - start + 1);
	        }
	        return maxLength;
	    
	    }
	
	public static int numRollsToTarget(int d, int f, int target) {
        int MOD = 1000000007;
        int[][] dp = new int[d + 1][target + 1]; 
        dp[0][0] = 1;
		//how many possibility can i dices sum up to j;
        for(int i = 1; i <= d; i++) {
            for(int j = 1; j<= target; j++) {
                if(j > i * f) {
                   continue;         //If j is larger than largest possible sum of i dices, there is no possible ways.        
                } else {                      //watch out below condition, or NPE
                    for(int k = 1; k <= f && k <= j ; k++) {
                        dp[i][j] = (dp[i][j] + dp[i - 1][j - k]) % MOD; 
                    }
                }
            }
        }
        return dp[d][target];
    }
	
	public static int dayOfYear(String date) {

		String [] dates=date.split("-");
		int year=Integer.parseInt(dates[0]);
		int month=Integer.parseInt(dates[1]);
		int day=Integer.parseInt(dates[2]);
		
		int res=0;
		int month2=0;
		if(year%4==0 && year%100!=0)
			month2=29;
		else month2=28;
		switch (month) {

		case 12:
			res+=30;
		case 11:
			res+=31;
		case 10:
			res+=30;
		case 9:
			res+=31;
		case 8:
			res+=31;
		case 7:
			res+=30;
		case 6:
			res+=31;
		case 5:
			res+=30;
		case 4:
			res+=31;
		case 3:
			res+=month2;
		case 2:
			res+=31;
		case 1:
			res+=0;
		default:
			break;
		}
		
		return res+=day;
		
	}
	
}
