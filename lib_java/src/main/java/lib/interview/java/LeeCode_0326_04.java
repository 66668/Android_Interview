package lib.interview.java;

import java.util.Arrays;

/**
 * 在一个无序数组中，寻找一个第K大的元素（快排，标记位的选择）
 */
public class LeeCode_0326_04 {

    public static void main(String[] args) {
        int[] a = new int[]{8, 15, 9, 7, 10, 2, 22, 12, 5, 0};
        findKthLargest(a, 3);
    }

    /**
     * @param nums
     */
    public static void findKthLargest(int[] nums, int k) {
        int[] b = quickSort_02(nums, 0, nums.length - 1);
        //查找第K大的值
        int val = b[b.length - k];
        System.out.println("打印值=" + val + "--" + Arrays.toString(nums));
    }

    public static int[] quickSort_02(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return null;
        }
        i = low;
        j = high;
        // temp就是基准位
        temp = arr[low];
        System.out.println("数组范围[" + low + "，" + j + "],基准位=" + temp + "--执行前数组值=" + Arrays.toString(arr));

        //循环，将值比temp小的，交换到左边，比temp大的，交换到右边
        while (i < j) {
            // 先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            // 再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            System.out.println("-----i=" + i + "--j=" + j);
            // 如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        System.out.println("i=" + i + "--j=" + j);
        // 最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        // 递归调用左半数组
        System.out.println("左半数组递归[" + low + "，" + (j - 1) + "]");
        quickSort_02(arr, low, j - 1);
        // 递归调用右半数组
        System.out.println("右半数组递归[" + (j + 1) + "，" + high + "]");
        quickSort_02(arr, j + 1, high);
        return arr;
    }
}
