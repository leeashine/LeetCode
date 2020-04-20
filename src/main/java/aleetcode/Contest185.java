package aleetcode;

public class Contest185 {
    public static void main(String[] args) {

        String s="covid2019";
        reformat(s);

    }
//    s = "a0b1c2"
//    格式化字符串，数字和字母相邻
//    思路：统计数字和字母出现次数 要考虑数字比字母多一个或少一个的情况
    public static String reformat(String s) {
        if(s==null||s.length()==0)
            return "";
        String res="";
        String a="";
        String b="";
        for(char c:s.toCharArray()){
            if(c>='0'&&c<='9')
                a+=c;
            else
                b+=c;
        }
        if(Math.abs(a.length()-b.length())>1)
            res="";
        else{
            char []ca=a.toCharArray();
            char []cb=b.toCharArray();
            int length=ca.length>cb.length?cb.length:ca.length;
            int i=0;
            while(i<length){
                if(ca.length>=cb.length){
                    res+=ca[i];
                    res+=cb[i];
                }else {
                    res+=cb[i];
                    res+=ca[i];
                }
                i++;
            }
            if(ca.length>cb.length){
                res+=ca[i];
            }else if(ca.length<cb.length){
                res+=cb[i];
            }

        }

        return res;

    }
}
