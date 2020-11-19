package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda 并行数组操作
 */
public class LambdaBingxing2 {
    static Art art, art1, art2;
    static String name = "默认";
    static List<Art.ArtDemo> list = new ArrayList<>();
    static List<Art> arts = new ArrayList<>();
    //
    static Map<Boolean, List<Art.ArtDemo>> datas;

    public static void main(String[] args) {
        Art.ArtDemo demo1 = new Art.ArtDemo("艺术家作品1", 100, true);
        Art.ArtDemo demo2 = new Art.ArtDemo("艺术家作品2", 80, true);
        Art.ArtDemo demo3 = new Art.ArtDemo("艺术家作品3", 10, false);
        Art.ArtDemo demo4 = new Art.ArtDemo("艺术家作品4", 120, true);
        list.add(demo1);
        list.add(demo2);
        list.add(demo3);
        list.add(demo4);
        art = new Art("压压", list);
        art1 = new Art("哈哈", list);
        art2 = new Art("啦啦", list);
        arts.add(art);
        arts.add(art1);
        arts.add(art2);

        demo1();
        demo2();
    }


    private static void demo1() {
        /**
         * java7:数组初始化赋值操作
         */
        double[] values = new double[10];
        for (int i = 0; i < values.length; i++) {
            values[i] = i;
            System.out.println("java7 数组赋值=" + i);
        }

        /**
         * java8:数组初始化赋值操作
         * parallelSetAll
         */
        double[] values2 = new double[10];
        Arrays.parallelSetAll(values2, i -> i);
        for (int i = 0; i < values2.length; i++) {
            System.out.println("java8 数组赋值=" + values2[i]);
        }
        Arrays.parallelPrefix(values2, Double::sum);//累加计算，结果放到数组中
        System.out.println("java8 parallelPrefix=" + Arrays.toString(values2));
    }

    /**
     *
     */
    private static void demo2() {

    }

}
