package lib.interview.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LeeCode_1228 {
    public static void main(String[] args) {
        int[] a = fun2(new int[]{1, 2, 3});
        System.out.println(Arrays.toString(a));
        int ab = maxFreq("aababcaab", 2, 3, 4);
        System.out.println(ab);
    }

    //扣力 分式化简
    // https://leetcode-cn.com/problems/deep-dark-fraction/
    private static int[] fun1(int[] input) {
        if (input.length <= 0) {

            return new int[]{0, 1};
        } else if (input.length == 1) {

            return new int[]{input[0], 1};
        } else if (input.length == 2) {

            int a0 = input[0];
            int b0 = input[1];
            int a1 = a0 * b0;
            int b1 = b0;
            return new int[]{a1, b1};
        } else {
            return fun2(input);
        }
    }

    //倒着计算
    private static int[] fun2(int[] input) {

        int a0 = input[input.length - 2];//单独数字
        int b0 = input[input.length - 1];//分母
        int reslut0 = a0;
        int resut1 = b0;
        for (int i = input.length - 3; i >= 0; i--) {
            //
            int a1 = a0 * b0 + 1;//分子
            int b1 = b0;//分母
            //分式计算
            int a2 = input[i];//单独数字

            reslut0 = (a2 * a1 + b1);//分子
            resut1 = a1;//分母
            //切换
            a0 = reslut0;
            b0 = resut1;
        }

        return new int[]{reslut0, resut1};
    }

    //扣力 字串出现的最大次数
    public static int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        char[] chars = s.toCharArray();
        Map<String, Integer> map = new HashMap<>();
        // 固定窗口大小的滑动窗口，枚举每个左边界的起始位置，那么右边界位置等于 i + minSize - 1
        for (int i = 0; i <= chars.length - minSize; i++) {
            if (counter(chars, i, i + minSize - 1) <= maxLetters) {
                String key = String.valueOf(chars, i, minSize);
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
        }
        int max = 0;
        for (Integer value : map.values()) {
            max = Math.max(max, value);
        }
        return max;
    }

    // 用来统计不同元素的个数
    private static int counter(char[] chars, int start, int end) {
        Set<Character> set = new HashSet<>();
        for (int i = start; i <= end; i++) {
            set.add(chars[i]);
        }
        return set.size();
    }

}
