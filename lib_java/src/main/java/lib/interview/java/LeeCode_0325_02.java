package lib.interview.java;

import java.util.Arrays;
import java.util.Vector;

/**
 * [1,0,3,15,0],只移动0到末尾，其余值保持相对位置
 * 相关问题：
 * 有序数组去重 [0, 0, 1, 2, 3, 15, 15, 17],并返回去重后的元素个数，和新的数组值
 */
public class LeeCode_0325_02 {

    public static void main(String[] args) {
        int[] a = new int[]{0,0, 1,0, 3, 15, 0};
//        moveZeros01(a);
        moveZeros02(a);
    }

    /**
     * 时间复杂度 O(n)
     * 空间复杂度O(1)
     *
     * @param arr
     */
    public static void moveZeros01(int[] arr) {
        int k = 0;
        //填充值
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[k++] = arr[i];
            }
        }
        //填充0
        for (int i = k; i < arr.length; i++) {
            arr[i] = 0;
        }
        System.out.println("打印值=" + Arrays.toString(arr));
    }

    /**
     * 交换位置
     * 时间复杂度 O(n)
     * 空间复杂度O(1)
     *
     * @param arr
     */
    public static void moveZeros02(int[] arr) {
        int k = 0;//用k表示新的位置，用于交换值使用
        //交换值
        for (int i = 0; i < arr.length; i++) {//
            if (arr[i] != 0) {
                if (i != k) {//i标记出非0的位置,用k表示新的位置
                    System.out.println("打印值i=" + i + "--k=" + k);
                    //执行交换
                    int temp = arr[k];
                    arr[k] = arr[i];
                    arr[i] = temp;
                    k++;
                }
            }
        }
        System.out.println("打印值2=" + Arrays.toString(arr));
    }
}
