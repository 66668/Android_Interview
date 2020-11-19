package lib.interview.java.LeeCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * x^n计算
 */
public class LeeCode_0325_01 {
    Vector v;
    public static void main(String[] args) {

        System.out.println("打印值pow（4，2）=" + pow(3, 0));
    }

    public static double pow(int x, int n) {
        assert (n >= 0);
        if (n == 0) {
            return 1;
        }
        double t = pow(x, n / 2);
        if (n % 2 == 1) {
            return x * t * t;
        } else {
            return t * t;
        }
    }
}
