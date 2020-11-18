package lib.interview.java;

import java.util.Vector;

/**
 * 位运算
 */
public class 位运算 {
    private static final int MODE_SHIFT = 30;
    private static final int MODE_DATA = 0x3;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;
    private static final int MODE_ON_WIDTH = 1 << MODE_SHIFT;
    private static final int MODE_DEVICE_SIZE = 2 << MODE_SHIFT;

    public static void main(String[] args) {
        System.out.println("MODE_DATA=" + MODE_DATA);
        System.out.println("MODE_MASK=" + MODE_MASK);
        System.out.println("MODE_ON_WIDTH=" + MODE_ON_WIDTH);
        System.out.println("MODE_DEVICE_SIZE=" + MODE_DEVICE_SIZE);
    }

}
