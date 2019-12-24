package aleetcode;

import java.util.LinkedList;
import java.util.Queue;

import util.TreeNode;

public class Contest150 {

	public static void main(String[] args) {
		String[] words = { "cat", "bt", "hat", "tree" };
		String chars = "atach";
		Contest150 contest = new Contest150();
		int count = contest.countCharacters(words, chars);
		System.out.println(count);
		// boolean flag=chars.contains("cat");
		// System.out.println(flag);
		
		
		
	}


	void dfs(TreeNode root) {

		
		if (root == null)
			return;
		
		if (root.left != null) {
			dfs(root.left);
			
		}
		if (root.right != null) {
			dfs(root.right);
			
		}

	}

	public int maxLevelSum(TreeNode root) {
		
		int level = 1, max = Integer.MIN_VALUE, maxLevel = 1;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);//向队列尾部插入元素
//      q.add(root);
        while (!q.isEmpty()) {
            int sum = 0; 
            for (int sz = q.size(); sz > 0; --sz) {
                TreeNode n = q.poll();
                sum += n.val;
                if (n.left != null) { q.offer(n.left); }
                if (n.right != null) { q.offer(n.right); }
            }
            if (max < sum) {
                max = sum;
                maxLevel = level;           
            }
            ++level;
        }
        return maxLevel;
    }
	
	public int countCharacters(String[] words, String chars) {

		int[] charsMap = new int[26];

		for (char c : chars.toCharArray()) {
			charsMap[(int) c - 97]++;
		}

		int res = 0;
		for (String word : words) {
			if (compareWordToChars(word, charsMap)) {
				res += word.length();
			}
		}
		return res;
	}

	private boolean compareWordToChars(String word, int[] charsMap) {
		int[] wordMap = new int[26];

		for (char c : word.toCharArray()) {
			int i = (int) c - 97;
			wordMap[i]++;
			if (wordMap[i] > charsMap[i]) {
				return false;
			}
		}
		return true;
	}

	public int countCharacters2(String[] words, String chars) {

		int[] model = new int[26];
		int count = 0;
		for (int i = 0; i < chars.length(); i++) {
			model[chars.charAt(i) - 'a']++;
		}
		for (String word : words) {
			int newCount = 0;
			int[] model2 = new int[26];
			for (int i = 0; i < word.length(); i++) {
				model2[word.charAt(i) - 'a']++;
				if (model[word.charAt(i) - 'a'] >= model2[word.charAt(i) - 'a'])
					newCount++;
				else
					break;
			}
			if (newCount == word.length())
				count += word.length();
		}

		return count;

	}

}
