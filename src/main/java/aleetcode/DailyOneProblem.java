package aleetcode;

import aleetcode.util.TreeNode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.*;

public class DailyOneProblem {

    public static void main(String[] args) {

//        String[] words = {"abcw", "baz", "foo", "bar", "xtfn", "abcdef"};
//
//        int i = new DailyOneProblem().maxProduct(words);
//        System.out.println(i);

        int i = new DailyOneProblem().integerReplacement(8);
        System.out.println(i);

        //线程池
        ExecutorService service = new ThreadPoolExecutor(10, 20, 1000 * 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("");
                return thread;
            }
        });
        service.submit(() -> { });

        //jdk动态代理
        Class clazz = DailyOneProblem.class;
        Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });

    }

    int sum = 0;

    public int integerReplacement(int n) {
        if (n == 1) {
            return 0;
        }
        if (n % 2 == 0) {
            return 1 + integerReplacement(n / 2);
        }
        //变成偶数
        return 2 + Math.min(integerReplacement(n / 2), integerReplacement(n / 2 + 1));

    }

//    int sum = 0;

    public int findTilt(TreeNode root) {
        dfs(root);
        return sum;
    }

    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int sumleft = dfs(root.left);
        int sumright = dfs(root.right);
        //root节点时
        sum += Math.abs(sumleft - sumright);
        //子节点时
        return sumright + sumleft + root.val;
    }

    public int maxProduct(String[] words) {
        int max = 0;
        for (int i = 0; i < words.length - 1; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (!contain(words[i], words[j])) {
                    max = Math.max(max, words[i].length() * words[j].length());
                }
            }
        }
        return max;

    }

    //26[] 去重
    public boolean contain(String s1, String s2) {
        int[] arr1 = new int[27];
        int[] arr2 = new int[27];
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        for (char c1 : chars1) {
            if (arr1[c1 - 'a'] == 0) {
                arr1[c1 - 'a'] = 1;
            }
        }

        for (char c1 : chars2) {
            if (arr2[c1 - 'a'] == 0) {
                arr2[c1 - 'a'] = 1;
            }
        }

        for (int i = 0; i < 27; i++) {
            if (arr1[i] * arr2[i] > 0) {
                return true;
            }
        }
        return false;
    }


}
