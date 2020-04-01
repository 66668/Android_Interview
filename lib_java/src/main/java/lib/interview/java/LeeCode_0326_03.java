package lib.interview.java;

import java.util.Arrays;

/**
 * 在一个无序数组中，寻找一个第K大的元素（快排，标记位的选择）
 */
public class LeeCode_0326_03 {

    public static void main(String[] args) {
        int[] a = new int[]{10, 15, 9, 7, 8, 2, 22, 12, 5, 0};
        findKthLargest(a, 3);
    }

    /**
     * @param nums
     */
    public static void findKthLargest(int[] nums, int k) {
        int[] b = QuickSort(nums, 0, nums.length - 1);
        //查找第K大的值
        int val = b[b.length - k];
        System.out.println("打印值=" + val + "--" + Arrays.toString(nums));
    }

    public static int[] QuickSort(int[] array, int start, int end) {
        System.out.println("QuickSort递归=" + Arrays.toString(array));
        if (array.length < 1 || start < 0 || end >= array.length || start > end) return null;
        int smallIndex = partition(array, start, end);
        System.out.println("最终基准点pivot=" + smallIndex + "--对应值=" + array[smallIndex]);
        if (smallIndex > start) {
            System.out.println("smallIndex > start执行[" + start + "," + (smallIndex - 1) + "]的分治快排");
            QuickSort(array, start, smallIndex - 1);
        } else if (smallIndex < end) {
            System.out.println("smallIndex < end执行[" + (smallIndex + 1) + "," + end + "]的分治快排");
            QuickSort(array, smallIndex + 1, end);
        }

        return array;
    }

    /**
     * 快速排序算法——partition
     * 查找基准位置
     *
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int partition(int[] array, int start, int end) {
        //生成随机基准点
        int pivot = (int) (start + Math.random() * (end - start + 1));
        System.out.println("随机基准点pivot的pos=" + pivot + "--对应的值=" + array[pivot] + "--数组范围[" + start + "," + end + "]");
        int smallIndex = start - 1;
        System.out.println("1基准点和end交换=" + "--pivot=" + array[pivot] + "--end=" + array[end] + "--" + Arrays.toString(array));
        swap(array, pivot, end);
        System.out.println("2基准点和end交换=" + "--pivot=" + array[pivot] + "--end=" + array[end] + "--" + Arrays.toString(array));
        for (int i = start; i <= end; i++)
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex)//比基准点位置大的值，交换
                    swap(array, i, smallIndex);
            }
        return smallIndex;
    }

    /**
     * 交换数组内两个元素
     *
     * @param array
     * @param i
     * @param j
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
