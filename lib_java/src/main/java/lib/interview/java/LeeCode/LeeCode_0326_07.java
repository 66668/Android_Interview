package lib.interview.java.LeeCode;

import java.util.*;

/**
 * 给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。力扣438
 * 使用滑动窗口+索引指针
 */
public class LeeCode_0326_07 {

    public static void main(String[] args) {
        List<Integer> result = findAnagrams("abcdefbca", "acb");
        System.out.println("起始索引=" + Arrays.toString(result.toArray()));
    }

    public static List<Integer> findAnagrams(String s, String p) {
        char[] arrS = s.toCharArray();//转成字符数组
        char[] arrP = p.toCharArray();

        // 存储起始坐标
        List<Integer> ans = new ArrayList<>();

        // 定义一个 needs 数组来看 arrP 中包含元素的个数
        int[] needs = new int[26];
        // 定义一个 window 数组来看滑动窗口中是否有 arrP 中的元素，并记录出现的个数
        int[] window = new int[26];

        // 先将 arrP 中的元素保存到 needs 数组中
        for (int i = 0; i < arrP.length; i++) {
            needs[arrP[i] - 'a'] += 1;
        }

        // 定义滑动窗口的两端
        int start = 0;
        int end = 0;

        // 右窗口开始不断向右移动
        while (end < arrS.length) {
            int curR = arrS[end] - 'a';
            end++;
            // 将右窗口当前访问到的元素 curR 个数加 1
            window[curR] += 1;

            // 当 window 数组中 curR 比 needs 数组中对应元素的个数要多的时候就该移动左窗口指针
            while (window[curR] > needs[curR]) {
                int curL = arrS[start] - 'a';
                start++;
                // 将左窗口当前访问到的元素 curL 个数减 1
                window[curL] -= 1;
            }

            // 这里将所有符合要求的左窗口索引放入到了接收结果的 List 中
            if (end - start == arrP.length) {
                ans.add(start);
            }
        }
        return ans;
    }

}
