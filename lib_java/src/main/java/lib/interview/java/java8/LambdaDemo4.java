package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda 字符串拼接+组合收集器
 */
public class LambdaDemo4 {
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
        demo3();
    }

    /**
     * java7拼接：用StringBuilder
     */
    private static void demo1() {
        StringBuilder builder = new StringBuilder("[");
        for (Art.ArtDemo item : list) {
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(item.artName);
        }
        builder.append("]");
        System.out.println("字符串拼接1=" + builder.toString());
    }

    /**
     * java8拼接
     */
    private static void demo2() {
        String result = list.stream()
                .map(item -> item.artName)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("字符串拼接2=" + result.toString());
    }

    /**
     * 组合计算：每个作品的名字，每个作品的个数（2b玩意）
     */
    private static void demo3() {
        Map<String, Long> map = list.stream().collect(Collectors.groupingBy(item -> item.artName, Collectors.counting()));

        Set<String> set = map.keySet();
        set.forEach(e -> {
            System.out.println("key String =" + e + "--val size =" + map.get(e));
        });
    }


}
