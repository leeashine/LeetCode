package aleetcode;


import util.TreeNode;

public class Contest190 {
    public static void main(String[] args) {
//        int i=new Contest190().isPrefixOfWord("i use triple pillow","pill");
//        System.out.println(i);

        int i1 = new Contest190().maxVowels("wevallloeyou", 7);
        System.out.println(i1);

    }
//    因为节点值都是0-9所以可以用位运算
//    位操作 异或 与
    class Solution {
        int ans=0;
        public int pseudoPalindromicPaths (TreeNode root) {
            if(root==null) return 0;
            helper(root,0);
            return ans;
        }

        void helper(TreeNode node,int temp){
            temp^=(1<<node.val);//node节点的val为几就左移几位
            if(node.left==null&&node.right==null){//判断是否叶子节点
                if(temp==0||(temp&(temp-1))==0){//判断是否为伪回文 都是偶数个 或 一个奇数其他都是偶数
                    ans++;
                }
            }
            if(node.left!=null) helper(node.left,temp);
            if(node.right!=null) helper(node.right,temp);
//            return;
        }
    }


//    输入：s = "leetcode", k = 3
//    输出：2
//    解释："lee"、"eet" 和 "ode" 都包含 2 个元音字母。 a e i o u
//     slidewindow 滑动窗口问题
    public int maxVowels(String s, int k) {

            int cnt = 0, anw = 0;
            //移动右端点 i
            for(int i = 0; i < s.length(); i++) {
                cnt += check(s.charAt(i));
                //窗口超过 k 了，
                if(i >= k) {
                    //从窗口中移除s[j], j = i-k
                    cnt -= check(s.charAt(i-k));
                }
                // 更新下最大值
                anw = Math.max(anw, cnt);
            }
            return anw;

    }
    public int check(char c) {
        if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
            return 1;
        }
        return 0;
    }

//    i use triple pillow
//    4
    public int isPrefixOfWord(String sentence, String searchWord) {

        int idx=-1;

        String [] words=sentence.split(" ");
        for(int i=0;i<words.length;i++){

            String word=words[i];
            int length= searchWord.length();
            if(length>word.length())
                continue;
            String sword=word.substring(0,length);
            if(sword.equals(searchWord))
                return i+1;
        }
        return idx;

    }
}
