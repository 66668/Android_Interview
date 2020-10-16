package lib.interview.java.arr;

import java.util.Arrays;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description：选择排序：简单选择排序，堆排序（利用二叉树）
 */
public class sort_select {
    int len = 0;

    public static void main(String[] args) {
        int a[] = {6, 3, 7, 16, 10, 12, 9, 13, 5, 2, 15, 8, 4, 1, 14, 11};
        //
        sort_select sort = new sort_select();
        int[] sorts = sort.HeapSort(a);
        System.out.println("堆排序：" + Arrays.toString(sorts));
    }

    //堆排序
    public int[] HeapSort(int[] array) {
        len = array.length;
        if (len < 1) return array;
        //1.构建一个最大堆
        buildMaxHeap(array);
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while (len > 0) {
            swap(array, 0, len - 1);
            len--;
            adjustHeap(array, 0);
        }
        return array;
    }

    /**
     * 建立最大堆
     *
     * @param array
     */
    public void buildMaxHeap(int[] array) {
        //从最后一个非叶子节点开始向上构造最大堆
        for (int i = (len / 2 - 1); i >= 0; i--) { //
            adjustHeap(array, i);
        }
    }

    /**
     * 调整使之成为最大堆
     *
     * @param array
     * @param i
     */
    public void adjustHeap(int[] array, int i) {
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if (i * 2 < len && array[i * 2] > array[maxIndex])
            maxIndex = i * 2;
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex])
            maxIndex = i * 2 + 1;
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            //递归
            adjustHeap(array, maxIndex);
        }
    }

    //交换
    public void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }
}

