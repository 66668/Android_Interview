package lib.interview.java;

import android.graphics.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HuaWeiOD {
    public static void main(String[] args) {
        String a = "a";
        String b = "b";
        transStr(a, b);
        System.out.println(a + "--" + b);//输出：a b
    }

    static void transStr(String a, String b) {
        String nP = a;
        a = b;
        b = nP;
        a = "c";//赋值是new操作，不影响实参数
        b = new String("d");//赋值是new操作，不影响实参数
    }

}
