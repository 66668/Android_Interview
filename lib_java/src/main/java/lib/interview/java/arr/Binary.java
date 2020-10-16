package lib.interview.java.arr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 二分法
 */
public class Binary {
    List<Integer> list = new ArrayList<Integer>();
    static int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 17, 19};

    public static void main(String[] args) {

        int pos = binarySearch(arr, 5, 0, arr.length - 1);
        System.out.println("递归-打印位置=" + pos + "--值=" + arr[pos]);
        int pos2 = binarySearch2(arr, 7);
        System.out.println("非递归-打印位置=" + pos2 + "--值=" + arr[pos2]);
    }

    //递归
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

    //非递归
    public static int binarySearch2(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int middle = (low + high) / 2;//初始中间位置
            if (arr[middle] > key) {
                high = middle - 1;
            } else if (arr[middle] < key) {
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    /**
     * 两数之和
     */
    public static int[] sum(int[] arr, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                return new int[]{map.get(arr[i]), i};
            }
            map.put(target - arr[i], i);
        }
        return null;
    }

    //滑动窗口：一个字符串，返回一个没有重复的最长子串
    public static int lengthOfLongestSubstring(String s) {
        int ans = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        for (int end = 0, start = 0; end < s.length(); end++) {
            char nextKey = s.charAt(end);//截end处的char
            if (map.containsKey(nextKey)) {//如果map保存中包含该char,将重复的char的位置，作为起点
                start = Math.max(map.get(nextKey), start);
            }
            ans = Math.max(ans, end - start + 1);//将最大长度保存
            map.put(nextKey, end + 1);
        }
        System.out.println("最大子串长度=" + ans);
        return ans;
    }
}
