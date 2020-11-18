package lib.interview.java;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * 字符串截取
 */
public class StringSub {
    static String str = "D2061842146D60FHS0RAC120SMB1805160001adaababababababab";
    static String str1 = "http://39.105.69.83:10026/Api/doorInfo";
    static String str2 = "http://39.105.69.83:10026/";
    static String str3 = "/Api/doorInfo";
    StringBuffer buffer;


    public static void main(String[] args) {
        String sub = str.substring(15, 37);
        System.out.println(sub + "--size:" + sub.length());

        System.out.println("长度:" + str1.substring(str2.length(), str1.length()));
        try {
            URL url = new URL(str1);
            System.out.println("url:" + url.getPath() + "--" + url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //
        String[] str4 = str3.split("/");
        System.out.println("分割:" + str4.length + "--" + Arrays.toString(str4));
        String newUrl = new String();
        for (int i = 0; i < str4.length; i++) {
            if (str4[i].isEmpty()) {
                continue;
            } else if (i == str4.length - 1) {
                newUrl += str4[i];
            } else {
                newUrl += str4[i] + "//";
            }
        }
        System.out.println("分割:" + newUrl );

    }
}
