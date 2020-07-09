package aleetcode;

/**
 * 字典树
 * 快速判断当前子串是否存在于词典中
 * Trie 是一种最大程度利用多个字符串前缀信息的数据结构，
 * 它可以在 O(w) 的时间复杂度内判断一个字符串是否是一个字符串集合中某个字符串的前缀，其中 w 代表字符串的长度。
 *
 */
public class Trie {
    public Trie[] next;
    public boolean isEnd;

    public Trie() {
        next = new Trie[26];
        isEnd = false;
    }

    public void insert(String s) {
        Trie curPos = this;

        for (int i = s.length() - 1; i >= 0; --i) {
            int t = s.charAt(i) - 'a';
            if (curPos.next[t] == null) {
                curPos.next[t] = new Trie();
            }
            curPos = curPos.next[t];
        }
        curPos.isEnd = true;
    }

    public static void main(String[] args) {
        String[] dictionary={"looked","just","like","her","brother"};
        Trie root = new Trie();
        for (String word: dictionary) {
            root.insert(word);
        }


    }

}
