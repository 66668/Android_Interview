package lib.interview.java.arr;

import java.util.Arrays;

/**
 * @author:sjy
 * @date: 2020/10/11
 * Description:插入排序：简单插入和希尔快速插入
 */
public class sort_xier {
    public static void main(String[] args) {
        int a[] = {6, 3, 7, 16, 10, 12, 9, 13, 5, 2, 15, 8, 4, 1, 14, 11};
        shellSort(a);
//        insertSort_03(a);
    }

    public static int[] insertSort_03(int[] array) {
        int len = array.length;
        for (int i = 1; i < len; i++) {
            for (int j = i; j > 0 && array[j - 1] > array[j]; j--) {//满足就交换
                int temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
            }
        }
        System.out.println("简单插入排序：" + Arrays.toString(array));
        return array;
    }

    public static int[] shellSort(int[] array) {
        int j = 0;
        int temp = 0;
        for (int increment = array.length / 2; increment > 0; increment /= 2) {//分组后，时间复杂度达到O(logn)级别
            System.out.println("希尔排序-增量分组=：" + increment + "组");
            for (int i = increment; i < array.length; i++) {
                temp = array[i];
                System.out.println("希尔排序-i=：" + increment + "key=" + temp);
                for (j = i; j >= increment; j -= increment) {
                    System.out.println("希尔排序-分组内排序 j=：" + j);
                    if (temp < array[j - increment]) {
                        array[j] = array[j - increment];
                        System.out.println("希尔排序-交换j=" + j + "--array[j]=：" + array[j - increment]);
                    } else {
                        break;
                    }
                }
                array[j] = temp;
                System.out.println("希尔排序-交换j=" + j + "--array[j]=" + temp);//俩位置交换，最后交换temp
            }
        }
        System.out.println("希尔排序：" + Arrays.toString(array));
        return array;
    }
}
