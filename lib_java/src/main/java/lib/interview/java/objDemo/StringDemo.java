package lib.interview.java.objDemo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * 字符串截取
 */
public class StringDemo {
    public static String str = "sjy";


    public static void main(String[] args) {

        String sub = str.substring(15, 37);
        System.out.println(sub + "--size:" + sub.length());

    }

    public static void test(String name) {

    }
}
