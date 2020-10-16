package lib.interview.java.arr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * KMP算法
 */
public class KMP_0702 {
    public static void main(String[] args) {
        String S = "BBC ABCDAB ABCDABCDABDE";
        String T = "ABCDABD";
        //String str2 = "BBC";
        int[] next = kmpNext(T); //[0, 1, 2, 0]
        System.out.println("next=" + Arrays.toString(next));
        int index = kmpSearch(S, T, next);
        System.out.println("index=" + index); // 15 了


    }


    /**
     * @param str1 源字符串
     * @param str2 子串
     * @param next 部分匹配表, 是子串对应的部分匹配表
     * @return 如果是-1 就是没有匹配到，否则返回第一个匹配的位置
     */
    public static int kmpSearch(String str1, String str2, int[] next) {
        /**
         * 遍历
         * j记录next的最大子串长度 next的最大长度为子串T的长度
         */
        for (int i = 0, j = 0; i < str1.length(); i++) {
            //需要处理 str1.charAt(i) != str2.charAt(j), 去调整 j 的大小
            //KMP 算法核心点, 可以验证...
            while (j > 0 && str1.charAt(i) != str2.charAt(j)) {
                j = next[j - 1];
            }

            if (str1.charAt(i) == str2.charAt(j)) {
                j++;
            }
            if (j == str2.length()) {//找到了 // j = 3 i
                return i - j + 1;
            }
        }
        return -1;
    }


    //获取到一个字符串(子串) 的部分匹配值表
    public static int[] kmpNext(String dest) {
        //创建一个 next 数组保存部分匹配值
        int[] next = new int[dest.length()];
        next[0] = 0; //如果字符串是长度为 1 部分匹配值就是 0
        for (int i = 1, j = 0; i < dest.length(); i++) {
            //当 dest.charAt(i) != dest.charAt(j) ，我们需要从 next[j-1]获取新的 j
            //直到我们发现 有 dest.charAt(i) == dest.charAt(j)成立才退出
            //这时 kmp 算法的核心点
            while (j > 0 && dest.charAt(i) != dest.charAt(j)) {
                j = next[j - 1];
            }

            //当 dest.charAt(i) == dest.charAt(j) 满足时，部分匹配值就是+1
            if(dest.charAt(i) == dest.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }
}
