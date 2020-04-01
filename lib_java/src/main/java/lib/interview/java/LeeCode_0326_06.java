package lib.interview.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个字符串，返回一个没有重复的最长子串
 * 使用滑动窗口解
 */
public class LeeCode_0326_06 {

    public static void main(String[] args) {
        lengthOfLongestSubstring("aabdcdfacgdcgadffdsa");
    }

    //滑动窗口
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int end = 0, start = 0; end < n; end++) {
            char nextKey = s.charAt(end);//截end处的char
            if (map.containsKey(nextKey)) {//如果map保存中包含该char,将重复的char的位置，作为起点
                start = Math.max(map.get(nextKey), start);
            }
            ans = Math.max(ans, end - start + 1);//将最大长度保存
            map.put(nextKey, end + 1);
        }
        System.out.println("最大子串长度="+ans);
        return ans;
    }
}
