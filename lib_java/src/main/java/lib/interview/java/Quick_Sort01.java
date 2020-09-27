package lib.interview.java;

import java.util.Arrays;

/**
 * @author:sjy
 * @date: 2020/9/21
 * Description
 */
public class Quick_Sort01 {
    public static void main(String[] args) {
        int a[] = {6, 3, 7, 10, 12, 9, 13, 5, 2, 8, 4, 1, 11};
        quickSort_02(a, 0, a.length - 1, "main");
        System.out.println("排序=" + Arrays.toString(a));
    }

    //快速排序方法
    public static int[] quickSort_02(int[] arr, int low, int high, String tag) {
        int i, j, temp, t;
        if (low > high) {
            return null;
        }
        i = low;//数组位置
        j = high;//数组位置
        // temp就是基准位
        temp = arr[low];
        System.out.println(tag + ":数组范围[" + low + "，" + j + "],基准值=" + temp + "--执行前数组值=" + Arrays.toString(arr));

        //循环，将值比temp小的，交换到左边，比temp大的，交换到右边
        while (i < j) {
            System.out.println(tag + "：循环交换=" + Arrays.toString(arr));
            // 先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            // 再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            // 如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        // 最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];//交换基准值
        arr[i] = temp;//原基准值赋值给排序位置
        // 递归调用左半数组
        quickSort_02(arr, low, j - 1, "left");
        // 递归调用右半数组
        quickSort_02(arr, j + 1, high, "right");
        return arr;
    }
}
