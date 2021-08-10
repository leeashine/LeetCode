package java8;

import aleetcode.Practise;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by LIZIXUAN560 on 2020/12/3.
 *
 * @author LIZIXUAN560
 */
public class Test {

    public static void main(String[] args) {

//        String s="abcd e ";
//        checkPageQuery(123L,s,10,20);
//        System.out.println(s+"!");
//        System.out.println(s.trim()+"!");
//        char[] s={'h','e','l','l','o'};
//        reverseString2(s);
//        ListNode listNode1=new ListNode(1);
//        ListNode listNode2=new ListNode(2);
//        ListNode listNode3=new ListNode(3);
//        ListNode listNode4=new ListNode(4);
//        listNode1.next=listNode2;
//        listNode2.next=listNode3;
//        listNode3.next=listNode4;
//        ListNode listNode = swapPairs(listNode1);
//        System.out.println(listNode);


    }

    private static boolean checkPageQuery(long docTreeId,String article, int pageNo, int pageSize){
        if (docTreeId < 0 || pageNo <= 0 || pageSize <= 0) {
//            logger.warn("[getHelpCenterDocList] param error | docTreeId:{} | "
//                    + "docTitle:{} | pageNo:{} | pageSize:{}", docTreeId, article, pageNo, pageSize);
//            DubboExtProperty.setErrorCode(JkApiCode.QUERY_FAILURE);
            return false;
        }
        if (StringUtils.isBlank(article)) {
            article = null;
        } else {
            article = article.trim();
        }
        return true;
    }

    public static void reverseString(char[] s) {
        helper(0,s);
    }
    private static void helper(int index, char [] str) {
        if (str == null || index >= str.length) {
            return;
        }
        helper(index + 1, str);
        System.out.print(str[index]);
    }

    public static void reverseString2(char[] s) {
        int n=s.length;
        for (int left = 0, right = n - 1; left < right; ++left, --right) {
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
        }
    }

    /**
     * 带返回值的递归
     * 链表 两两翻转
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead=head.next;
        head.next= swapPairs(newHead.next);
        newHead.next=head;
        return newHead;

    }

    //1->2->3->4
    public ListNode swap(ListNode head){

        //2
        ListNode newHead=head.next;
        head.next=swapPairs(newHead.next);
        //1
        newHead.next=head;
        return newHead;

    }


}
