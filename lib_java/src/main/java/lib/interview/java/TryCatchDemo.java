package lib.interview.java;

import java.util.Vector;

/**
 * try catch finally
 */
public class TryCatchDemo {

    public static void main(String[] args) {
        System.out.println(TryCatchDemo.test());
    }

    public static String test() {
        String t = "";

        try {
            t = "try";
            Integer.parseInt(null);
            System.out.println(t);
            return t;
        } catch (Exception e) {
            t = "catch";
            System.out.println(t);
            return t;
        } finally {
            t = "finally";
            System.out.println(t);
            return t;
        }
    }

    public static int add (int a, int b) {
        a = 2;
        int result = a + b;
        return result;
    }
}
