package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda 高级集合
 */
public class LambdaDemo2 {
    static Art art;
    static String name = "默认";
    static List<Art.ArtDemo> list = new ArrayList<>();

    public static void main(String[] args) {
        Art.ArtDemo demo = new Art.ArtDemo("艺术家作品1", 100, true);
        list.add(demo);
        art = new Art("压压", list);


        demo1();
        demo3();
    }

    /**
     * 实例的方法引用
     * 方法是非静态的
     */
    private static void demo1() {
        //
        Supplier<String> supplier = () -> art.getName();
        System.out.println("Lambda表达式 执行结果1=" + supplier.get());
        System.out.println("Lambda表达式 执行结果2=" + art.getName());

        //
        Supplier<String> supplier2 = art::getName;
        System.out.println("实例的方法引用 执行结果1=" + supplier2.get());

    }

    /**
     * 对象的方法引用
     * 调用的方法最好是静态方法，直接使用
     */
    private static void demo2() {
        Supplier<String> name = Art::getName2;
    }

    /**
     * 构造方法的方法引用
     */
    private static void demo3() {
        Supplier<Art> artSupplier = Art::new;
        System.out.println("构造的方法引用 执行结果1=" + artSupplier.get().getName());

    }


}
