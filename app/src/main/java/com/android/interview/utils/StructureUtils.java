package com.android.interview.utils;

import java.util.Arrays;

public class StructureUtils {

    //================================================================================
    //================================================================================
    //================================================================================

    /**
     * 01 插入排序
     *
     * @param array
     * @return
     */
    public static int[] insertSort_01(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        System.out.println("01插入排序：" + Arrays.toString(array));
        return array;
    }

    /**
     * 02 插入排序
     *
     * @param array
     */
    private static int[] insertSort_02(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int temp = array[i];
            int j = i - 1;
            for (; j >= 0 && array[j] > temp; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
        System.out.println("02插入排序：" + Arrays.toString(array));
        return array;
    }

    //03 插入排序
    public static int[] insertSort_03(int[] array) {
        int len = array.length;
        for (int i = 1; i < len; i++) {
            for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                int temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
        System.out.println("03插入排序：" + Arrays.toString(array));
        return array;
    }
    //================================================================================
    //================================================================================
    //================================================================================

    /**
     * 冒泡排序
     *
     * @param array
     * @return
     */
    public static int[] bubbleSort(int[] array) {
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array.length - 1 - i; j++)
                if (array[j + 1] < array[j]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
        System.out.println("01冒泡排序：" + Arrays.toString(array));
        return array;
    }
    
    //================================================================================
    //================================================================================
    //================================================================================

    /**
     * 选择排序
     *
     * @param array
     * @return
     */
    public static int[] selectionSort(int[] array) {
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex]) // 找到最小的数
                    minIndex = j; // 将最小数的索引保存
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        System.out.println("01选择排序：" + Arrays.toString(array));
        return array;
    }

}
