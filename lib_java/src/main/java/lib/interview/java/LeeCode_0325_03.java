package lib.interview.java;

import java.util.Arrays;

/**
 *
 */
public class LeeCode_0325_03 {

    public static void main(String[] args) {
        int[] a = new int[]{0, 0, 1, 2, 3, 15, 15, 17};
        moveCover(a);

    }

    /**
     * @param arr
     */
    public static void moveCover(int[] arr) {
        int size = 0;//去重复元素个数
        int k = 0;
        //填充值
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[k++] = arr[i];
            }
        }
        System.out.println("打印值=" + Arrays.toString(arr));

    }
}
