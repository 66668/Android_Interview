package lib.interview.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 链表反转。力扣206
 *
 * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。力扣92
 * 说明:
 * 1 ≤ m ≤ n ≤ 链表长度。
 */
public class LeeCode_032701 {
//    ListNode list = new ListNode();

    public static void main(String[] args) {
        System.out.println("找到值=");
    }


//    /**
//     * 链表反转。力扣206
//     * @param head
//     * @return
//     */
//    public static ListNode linkedReverse(ListNode head) {
//        ListNode prev = null;
//        ListNode curr = head;
//        while (curr != null) {
//            ListNode nextTemp = curr.next;
//            curr.next = prev;
//            prev = curr;
//            curr = nextTemp;
//        }
//        return prev;
//    }


    /**
     * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。力扣92
     *  * 说明:
     *  * 1 ≤ m ≤ n ≤ 链表长度。
     */
//    private boolean stop;
//    private ListNode left;

//    public ListNode reverseBetween(ListNode head, int m, int n) {
//        this.left = head;
//        this.stop = false;
//        this.recurseAndReverse(head, m, n);
//        return head;
//    }
//
//    public void recurseAndReverse(ListNode right, int m, int n) {
//        if (n == 1) {
//            return;
//        }
//        right = right.next;
//
//        if (m > 1) {
//            this.left = this.left.next;
//        }
//
//        //继续递归回溯,直到
//        this.recurseAndReverse(right, m - 1, n - 1);
//
//        //
//        if (this.left == right || right.next == this.left) {
//            this.stop = true;
//        }
//
//       //交换值
//        if (!this.stop) {
//            int t = this.left.val;
//            this.left.val = right.val;
//            right.val = t;
//            this.left = this.left.next;
//        }
//    }



//
//    private class ListNode {
//        ListNode item;
//        ListNode next;
//        ListNode prev;
//
//        ListNode() {
//        }
//        ListNode(ListNode prev, ListNode element, ListNode next) {
//
//            this.item = element;
//            this.next = next;
//            this.prev = prev;
//        }
//    }

}
