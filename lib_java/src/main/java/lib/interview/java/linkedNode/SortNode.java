package lib.interview.java.linkedNode;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description:链表排序：归并排序，快速排序
 */
public class SortNode {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        // 1 3 5 2 4
        node1.next = node3;
        node3.next = node5;
        node5.next = node2;
        node2.next = node4;

        ListNode ListNode = sortNode(node1);
        while (ListNode != null) {
            System.out.print(" " + ListNode.val);
            ListNode = ListNode.next;
        }
    }
    //归并排序
    public static ListNode sortNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        //中间值
        ListNode slow = head;
        ListNode fast = head.next;//或者fast = head，都一样
        while (fast != null && fast.next != null) {
            slow = slow.next;//slow为中间值
            fast = fast.next.next;
        }
        //链表一分为2，head为前半部分，last为后半部
        ListNode last = slow.next;//关键点
        slow.next = null;//关键点

        ListNode n1 = sortNode(head);//
        ListNode n2 = sortNode(last);
        //归并
        return mergeNode(n1, n2);//归并排序
    }

    public static ListNode mergeNode(ListNode n1, ListNode n2) {
        if (n1 == null)
            return n2;
        if (n2 == null)
            return n1;

        ListNode head = null;
        if (n1.val > n2.val) {
            //把head较小的结点给头结点
            head = n2;
            //继续递归head2
            head.next = mergeNode(n1, n2.next);
        } else {
            head = n1;
            head.next = mergeNode(n1.next, n2);
        }
        return head;
    }

}
