package lib.interview.java;

import java.util.HashMap;

/**
 * @author:sjy
 * @date: 2020/10/12
 * Description
 */
public class LurCache {
    private class Node {
        Node prev;
        Node next;
        int key;
        int val;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
            prev = null;
            next = null;
        }
    }

    private int count;
    private HashMap<Integer, Node> cacheMap = new HashMap<>();
    private Node head = new Node(-1, -1);
    private Node tail = new Node(-1, -1);

    public LurCache(int count) {
        this.count = count;
        tail.prev = head;
        head.next = tail;
    }

    public int get(int key) {
        if (!cacheMap.containsKey(key)) {
            return -1;
        }
        //remove curr
        Node curr = cacheMap.get(key);
        curr.prev.next = curr.next;
        curr.next.prev = curr.prev;

        moveToTail(curr);
        return curr.val;
    }

    //set会直接把Node放到末尾
    public void set(int key, int val) {
        //update
        if (get(key) != -1&&get(key)==key) {
           cacheMap.get(key).val = val;
           return;
        }
        //超出限制
        if (cacheMap.size() == count) {
            //删除头部
            cacheMap.remove(head.next.key);
            head.next = head.next.next;
            head.next.prev = head;
        }
        //新建节点
        Node insert = new Node(key, val);
        cacheMap.put(key, insert);
        moveToTail(insert);
        count++;
    }

    private void moveToTail(Node curr){
        curr.prev = tail.prev;
        tail.prev  =curr;
        curr.prev.next = curr;
        curr.next = tail;
    }
}
