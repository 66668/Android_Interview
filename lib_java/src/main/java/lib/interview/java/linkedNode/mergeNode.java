package lib.interview.java.linkedNode;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description:合并2个有序链表
 */
public class mergeNode {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node3;
        node3.next = node5;

        node2.next = node4;
        //n1: 1-3-5  n2:2-4  合并为1-2-3-4-5
        ListNode ListNode = mergeNode2(node1, node2);
        while (ListNode != null) {
            System.out.print(" " + ListNode.val);
            ListNode = ListNode.next;
        }
    }

    //递归
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

    //非递归
    public static ListNode mergeNode2(ListNode n1, ListNode n2) {
        if (n1 == null)
            return n2;
        if (n2 == null)
            return n1;

        ListNode head = null;//最终结果
        //临时变量
        ListNode cur1 = null;
        ListNode cur2 = null;
        ListNode temp = null;

        //变量赋值
        if (n1.val < n2.val) {
            head = n1;
            cur1 = n1.next;
            cur2 = n2;
        }else{
            head =n2;
            cur1 = n1;
            cur2 = n2.next;
        }
        temp = head;
        while (cur1 != null && cur2 != null) {
            if (cur1.val < cur2.val) {//拼接2链表
                temp.next = cur1;
                temp = cur1;
                cur1 = cur1.next;//更新cur1，用于下次遍历
            } else {
                temp.next = cur2;
                temp = cur2;
                cur2 = cur2.next;//更新cur2,用于下次遍历
            }
        }
        //当一个走null,则拼接另一个非null的ListNode，整个流程完成
        temp.next = cur1 == null ? cur2 : cur1;
        return head;
    }
}
