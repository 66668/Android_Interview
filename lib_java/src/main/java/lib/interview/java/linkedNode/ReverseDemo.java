package lib.interview.java.linkedNode;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description:链表反转
 */
public class ReverseDemo {
    public static void main(String[] args) {

    }
    //
    public static ListNode linkedReverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode nextTemp = head.next;//记录下一个节点
            head.next = prev;
            prev = head;
            head = nextTemp;
        }
        return prev;
    }
}
