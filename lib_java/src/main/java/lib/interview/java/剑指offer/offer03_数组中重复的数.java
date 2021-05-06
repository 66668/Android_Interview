package lib.interview.java.剑指offer;

import java.util.HashMap;
import java.util.Map;

public class offer03_数组中重复的数 {
    public static void main(String[] args) {
        int[] a = new int[]{2, 3, 1, 0, 2, 5, 3};
        findRepeatNumber(a);
    }

    public static int findRepeatNumber(int[] nums) {
        int val = -1;
        Map<Integer, Integer> map = new HashMap<>();
        int i = 0;
        while (i < nums.length - 1) {
            if (map.get(i) != null) {
                val = nums[i];
                break;
            } else {
                map.put(i, nums[i]);
            }
            i++;
        }
        System.out.println(val);
        return val;
    }
}
