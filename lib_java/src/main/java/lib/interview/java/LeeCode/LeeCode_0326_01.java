package lib.interview.java.LeeCode;

import java.util.Arrays;

/**
 * 数组只有无序的0，1，2三种值的元素，排序
 */
public class LeeCode_0326_01 {

    public static void main(String[] args) {
        int[] a = new int[]{0, 0, 1, 2, 0, 1, 1, 2};
        sort(a);
    }

    /**
     * 三路快排
     *
     * @param nums
     */
    public static void sort(int[] nums) {
        int zero = -1;//nums[0,zero]
        int two = nums.length;//nums[two,size-1]
        //一次遍历
        for (int i = 0; i < two; ) {
            if (nums[i] == 1) {
                i++;//遍历下一个点
            } else if (nums[i] == 0) {
                zero++;//zero标记为前移
                //交换
                int temp = nums[zero];
                nums[zero] = nums[i];
                nums[i] = temp;
                i++;//遍历下一个点
            } else if (nums[i] == 2) {
                two--;
                int temp = nums[two];
                nums[two] = nums[i];
                nums[i] = temp;
                //该处的点还需要用，直到该处的点不是2，才i++
            }
        }
        System.out.println("打印值=" + Arrays.toString(nums));
    }
}
