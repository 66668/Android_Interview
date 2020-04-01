package lib.interview.java;

import java.util.ArrayList;
import java.util.List;

/**
 * 二分法
 */
public class Binary {
    List<Integer> list = new ArrayList<Integer>();
    static int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 17, 19};

    public static void main(String[] args) {

        int pos = binarySearch(arr, 5, 0, arr.length - 1);
        System.out.println("打印位置=" + pos + "--值=" + arr[pos]);
    }

    public static int binarySearch(int[] arr, int key, int low, int high) {

        if (low >= arr.length || high >= arr.length || key < arr[low] || key > arr[high] || low > high) {
            System.out.println("不符合返回");
            return -1;
        }
        int middle = (low + high) / 2;//初始中间位置
        System.out.println("计算middle=" + middle);
        if (arr[middle] > key) {
            //比关键字大则关键字在左区域
            System.out.println("左区域搜索high=" + (middle - 1));
            return binarySearch(arr, key, low, middle - 1);
        } else if (arr[middle] < key) {
            //比关键字小则关键字在右区域
            System.out.println("右区域搜索low=" + (middle + 1));
            return binarySearch(arr, key, middle + 1, high);
        } else {
            return middle;//是查找的坐标值
        }
    }
}
