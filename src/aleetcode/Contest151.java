package aleetcode;


import java.util.*;

public class Contest151 {
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int x) {
			val = x;
		}
	}
	public static void main(String[] args) {

		String[] transactions = { "alice,20,800,mtv", "alice,50,100,beijing" };
		new Contest151().invalidTransactions(transactions);

		String []queries = {"bbb","cc"}, words = {"a","aa","aaa","aaaa"};
		new Contest151().numSmallerByFrequency(queries, words);

//		head = [1,2,3,-3,4]
		ListNode head=new ListNode(1);
		head.next=new ListNode(2);
		head.next.next=new ListNode(3);
		head.next.next.next=new ListNode(-3);
		head.next.next.next.next=new ListNode(4);

		new Contest151().removeZeroSumSublists(head);

		DinnerPlates D=new DinnerPlates(2);
		D.push(1);D.push(2);D.push(3);D.push(4);D.push(5);
		D.popAtStack(0);
		D.push(20);D.push(21);
		D.popAtStack(0);
		D.popAtStack(2);
		D.pop();           // Returns 5.
		D.pop();           // Returns 4.
		D.pop();           // Returns 3.
		D.pop();          // Returns 1.
		D.pop();         // Returns -1.

	}

	public ListNode removeZeroSumSublists(ListNode head) {

		ListNode dummy = new ListNode(0), cur = dummy;
		dummy.next = head;
		int prefix = 0;
		Map<Integer, ListNode> m = new HashMap<>();
		while (cur != null) {
			prefix += cur.val;
			if (m.containsKey(prefix)){

				cur=m.get(prefix).next;
				int p=prefix+cur.val;
				while (p!=prefix){
					m.remove(p);
					cur=cur.next;
					p+=cur.val;
				}

				m.get(prefix).next=cur.next;//连接

			}else{
				m.put(prefix,cur);
			}
			cur=cur.next;//遍历
		}
		return dummy.next;

	}

	public int[] numSmallerByFrequency(String[] queries, String[] words) {

		int [] res=new int [queries.length];
//		int index=0;
		//最小字符
//		for(String query:queries){
//			for(String word:words){
//				int a=f(query);
//				int b=f(word);
//				if(a<b){
//					res[index]++;
//				}
//			}
//			index++;
//		}
		//先排序再利用二分查找位置
		int [] wordsInt=new int [words.length];
		int [] queryInt=new int [queries.length];
		for(int i=0;i<words.length;i++){
			wordsInt[i]=f(words[i]);
		}
		for(int i=0;i<queries.length;i++){
			queryInt[i]=f(queries[i]);
		}
		Arrays.sort(wordsInt);
		for(int i=0;i<queries.length;i++){
			
			int l=0,r=wordsInt.length-1;
			while(l<=r){
				
				int m=l+(r-l)/2;
				if(wordsInt[m]<=queryInt[i]){
					l=m+1;
				}else{
					r=m-1;
				}
				
			}
			 res[i] = wordsInt.length - l;
			
		}
		
		return res;
	}
	
	public int f(String s){
		
		int minChar=Integer.MAX_VALUE;
		int cnt=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)-'a'<minChar){
				minChar=s.charAt(i)-'a';
			}
		}
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)-'a'==minChar){
				cnt++;
			}
		}
		
		return cnt;
	}

	public List<String> invalidTransactions(String[] transactions) {

		HashMap<String, ArrayList<String[]>> hmap = new HashMap<>();
		Set<String> res = new HashSet<String>();
		String name;
		String time;
		int amount;
		String city;

		for (String transaction : transactions) {
			String[] items = transaction.split(",");
			name = items[0];
			time = items[1];
			amount = Integer.parseInt(items[2]);
			city = items[3];

			if (amount > 1000)
				res.add(transaction);

			if (hmap.containsKey(name)) {
				ArrayList<String[]> t = hmap.get(name);
				for (String[] s : t) {

					if (Math.abs(Integer.parseInt(time)
							- Integer.parseInt(s[0])) <= 60
							&& !city.equals(s[2])) {
						res.add(transaction);
						res.add(name + "," + s[0] + "," + s[1] + "," + s[2]);
					}

				}
				t.add(new String[] { time, amount + "", city });
				hmap.put(name, t);

			} else {
				ArrayList<String[]> t = new ArrayList<String[]>();
				String[] s = { time, amount + "", city };
				t.add(s);
				hmap.put(name, t);
			}

		}
		List<String> aList = new ArrayList<String>(res);
		return aList;

	}

}
