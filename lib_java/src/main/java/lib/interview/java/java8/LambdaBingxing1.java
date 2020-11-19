package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda 并行/并发操作
 */
public class LambdaBingxing1 {
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

    /**
     * 串行化：计算list中ArtDemo中个数
     */
    private static void demo1() {
        int sum = arts.stream()
                .flatMap(item -> item.artList.stream())//合并多个list
                .mapToInt(item -> 1)//一个ArtDemo算一个
                .sum();

        System.out.println("串行计算list中ArtDemo中个数1=" + sum);
    }

    /**
     * 并行化：计算list中ArtDemo中个数
     * Stream——》parallelStream
     */
    private static void demo2() {
        int sum = arts.parallelStream()
                .flatMap(item -> item.artList.stream())//合并多个list
                .mapToInt(item -> item.artName.length())//一个ArtDemo的artName.length计算
                .sum();

        System.out.println("串行计算list中ArtDemo中个数2 =" + sum);
    }

}
