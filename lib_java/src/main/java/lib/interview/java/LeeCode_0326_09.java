package lib.interview.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。力扣49
 */
public class LeeCode_0326_09 {
    static List<List<String>> ans = new ArrayList<>();
    static HashMap<String, List<String>> map = new HashMap<>();
    PriorityQueue<Integer> queue = new PriorityQueue<>();

    public static void main(String[] args) {
        String[] a = new String[]{"ate", "tee", "eat", "ete", "aet", "abc"};
        List<List<String>> b = groupAnagrams(a);
        System.out.println("找到值=" + Arrays.toString(b.toArray()));
        System.out.println("位运算>>=" + (10 >> 3));
    }

    public static List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return ans;
        for (String s : strs) {
            String temp = getString(s);//字符串排序，ate aet等，保证一个key:aet
            if (!map.containsKey(temp)) {
                map.put(temp, new ArrayList<>());//val为一个list表
            }
            map.get(temp).add(s);//逐个原始字符串
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            ans.add(entry.getValue());
        }
        return ans;
    }

    public static String getString(String s) {
        char[] ss = s.toCharArray();
        Arrays.sort(ss);
        return String.valueOf(ss);
    }


}
