package lib.interview.java;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapDemo {
    ConcurrentMap concurrentMap;
    ConcurrentHashMap concurrentHashMap;
    Thread t;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static void main(String[] args) {
        System.out.print("数据=" + tableSizeFor(13) + "--" + (1 << 4));
    }

    private static final int tableSizeFor(int c) {//返回一个大于输入参数且最小的为2的n次幂的数。
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
