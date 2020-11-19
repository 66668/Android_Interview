package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda 数据分块/分组
 */
public class LambdaDemo3 {
    static Art art;
    static String name = "默认";
    static List<Art.ArtDemo> list = new ArrayList<>();
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

        demo1();
        demo2();
    }

    /**
     * 数据分组partitioningBy
     */
    private static void demo1() {
        datas = list.stream().collect(Collectors.partitioningBy(item -> item.isSingle == true));
        System.out.println("Collectors.partitioningBy 分组 执行结果=" + datas.size());
        List<Art.ArtDemo> l1 = datas.get(true);
        for (Art.ArtDemo item : l1) {
            System.out.println("item=" + item.isSingle);
        }

        List<Art.ArtDemo> l2 = datas.get(false);
        for (Art.ArtDemo item : l2) {
            System.out.println("item=" + item.isSingle);
        }
    }

    /**
     * 数据分组partitioningBy
     */
    private static void demo2() {
        datas = list.stream().collect(Collectors.groupingBy(item -> item.isSingle==false));
        System.out.println("Collectors.groupingBy 分组 执行结果=" + datas.size());
        List<Art.ArtDemo> l1 = datas.get(true);
        for (Art.ArtDemo item : l1) {
            System.out.println("item=" + item.isSingle);
        }

        List<Art.ArtDemo> l2 = datas.get(false);
        for (Art.ArtDemo item : l2) {
            System.out.println("item=" + item.isSingle);
        }
    }


}
