package aleetcode.contest2019;

public class AlphabetBoardPath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] board = {"abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"};
		
		alphabetBoardPath("leet");
	}

	public static String alphabetBoardPath(String target) {
        if(target == null) return "";
        char[] chs = target.toCharArray();
        StringBuilder sb = new StringBuilder();
        int previ = 0, prevj = 0;
        for(int i = 0; i < chs.length; i++) {
            int curi = (chs[i] - 'a') / 5;
            int curj = (chs[i] - 'a') % 5;
            if(curi == previ && curj == prevj) {
                sb.append("!");
            } else {
                printPath(sb, previ, prevj, curi, curj); 
                sb.append("!");
                previ = curi; 
                prevj = curj;
            }
        }
        
        return sb.toString();
    }
    
    private  static void printPath(StringBuilder sb, int previ, int prevj, int curi, int curj) {       
        while(curi < previ) {
            sb.append("U"); curi++;
        } 
        while(curj > prevj) {
            sb.append("R"); curj--;
        } 
        while(curj < prevj) {
            sb.append("L"); curj++;
        }  
        while(curi > previ) {
            sb.append("D"); curi--;
        }                      
    }

}
