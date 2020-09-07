package aleetcode.design;

import java.util.Arrays;

public class Trie {

    public Trie [] tries;
    public boolean isEnd;
    public Trie(){
        tries=new Trie[26];
        isEnd=false;
    }

    public void insert(String s){

        Trie trie=this;

        char[]chars=s.toCharArray();
        for (int i=chars.length-1;i>=0;i--){

            int pos=chars[i]-'a';
            if(trie.tries[pos]==null){
                trie.tries[pos]=new Trie();
            }
            trie =trie.tries[pos];

        }
        isEnd=true;

    }

    //dp[i] 代表前i个字符最少的未识别的字符数量
    public int respace(String[] dictionary, String sentence) {

        Trie root=new Trie();
        for(String word:dictionary){
            root.insert(word);
        }
        int n=sentence.length();
        int[] dp =new int[n+1];
        Arrays.fill(dp,Integer.MAX_VALUE);
        dp[0]=0;
        for(int i=1;i<=n;i++){
            dp[i]=dp[i-1]+1;
            Trie cus=root;
            for(int j=i-1;j>=0;j--){
                int t=sentence.charAt(j)-'a';
                if(cus.tries[t]==null){
                    break;
                }else if(cus.tries[t].isEnd){
                    dp[i]=Math.min(dp[i],dp[j]);//如果也是一个单词，那更新dp
                }
                if(dp[i]==0){
                    break;
                }
                cus=cus.tries[t];
            }

        }
        return dp[n];

    }

    public static void main(String[] args) {
        String[] dictionary={"looked","just","like","her","brother"};
        Trie root = new Trie();
        for (String word: dictionary) {
            root.insert(word);
        }

        System.out.println(1);

    }

}
