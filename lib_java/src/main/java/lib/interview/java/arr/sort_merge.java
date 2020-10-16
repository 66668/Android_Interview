package lib.interview.java.arr;

import java.util.Arrays;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description：归并排序
 */
public class sort_merge {

    public static void main(String[] args) {
        //数组有序时，是从1开始：{1，2，3，4...}
        int a[] = {6, 16, 7, 3, 10, 12, 9, 13, 5, 2, 15, 8, 4, 1, 14, 11};
        //
        int[] sort = mergeSort(a);
        System.out.println("归并排序：" + Arrays.toString(sort));
    }

    /**
     * 归并排序
     *
     * @param array
     * @return：造子串，一个串最少2个元素
     */
    public static int[] mergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        //均分为2个子串
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        System.out.println("造子串：left=" + Arrays.toString(left) + "--right=" + Arrays.toString(right));
        return merge(mergeSort(left), mergeSort(right));
    }

    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     * i记录left下标，j记录right下标，赋值后执行++操作
     * @param left
     * @param right
     * @return
     */
    public static int[] merge(int[] left, int[] right) {
        System.out.println("\n》》合并子串：left=" + Arrays.toString(left) + "--right=" + Arrays.toString(right));
        //新建一个数组
        int[] result = new int[left.length + right.length];
        //
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length) {
                result[index] = right[j++];
                System.out.println("i >= left.length --i=" + i + "--j=" + j + "--result[" + index + "]=" + result[index]);
            } else if (j >= right.length) {
                result[index] = left[i++];
                System.out.println("j >= right.length --i=" + i + "--j=" + j + "--result[" + index + "]=" + result[index]);
            } else if (left[i] > right[j]) {//
                result[index] = right[j++];
                System.out.println("left[i] > right[j] --i=" + i + "--j=" + j + "--result[" + index + "]=" + result[index]);
            } else {
                result[index] = left[i++];
                System.out.println("left[i] < right[j] --i=" + i + "--j=" + j + "--result[" + index + "]=" + result[index]);
            }
        }
        System.out.println("返回排序子串=" + Arrays.toString(result));
        return result;
    }
}