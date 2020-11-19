package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description: java8 Lambda
 */
public class LambdaDemo {


    public static void main(String[] args) {
        runnable.run();
        new Thread(runnable).start();
        callback.onBack("测试");
        System.out.println("BinaryOperator执行add方法=" + add.apply(10L, 20L));
        System.out.println("BinaryOperator执行add2方法=" + add2.apply(10L, 20L));
        //
        String result = "test1";
        result = "test2";
        callback.onBack(result);
        //
        createData();
        listStreamDemo();

        //使用Stream多次调用操作

    }

    static Runnable runnable = () -> {
        System.out.println("Runnable执行run方法，执行打印功能" + Thread.currentThread().getName());

    };

    /**
     * java8:函数接口
     */
    static DemoCallback callback = event -> {
        System.out.println("DemoCallback执行onBack方法=" + event);
    };

    static BinaryOperator<Long> add = (x, y) -> x + y;
    static BinaryOperator<Long> add2 = (Long x, Long y) -> x + y;

    //
    static List<Integer> list = new ArrayList<Integer>();

    static void createData() {
        list.add(1);
        list.add(1);
        list.add(12);
        list.add(13);
        list.add(4);
        list.add(1);
        list.add(6);
    }

    /**
     * 流操作 demo
     */
    static void listStreamDemo() {
        //过滤所有值为1的个数
        //stream-filter
        Long count = list.stream()
                .filter(item -> item.equals(1))
                .count();
        System.out.println("内部迭代计算 个数=" + count);

        //stream-filter
        list.stream()
                .filter(item -> {
                    System.out.println("内部迭代filter方法=" + item.intValue());
                    return list.equals(1);
                })
                .count();
        //stream-filter
        List<String> strs = Stream.of("a", "b", "c")
                .filter(item -> true)
                .collect(toList());

        //stream-collect，collect将of的items转换成list
        List<String> strList = Stream.of("a", "b", "c").collect(toList());
        strList.stream()
                .filter(item -> {
                    System.out.println("Stream-collect方法是否生成list item=" + item);
                    return list.equals(1);
                })
                .count();

        //stream-map:类型转换
        List<String> lowerList = Stream.of("a", "baadfDff", "ccccc")
                .map(item -> item.toUpperCase())
                .collect(toList());
        for (String item : lowerList) {
            System.out.println("Stream-map item=" + item);
        }

        //stream-flatMap:多个list合并成一个list
        List<Integer> flatList = Stream.of(Arrays.asList(1, 2), Arrays.asList(4, 3), Arrays.asList(7, 8))
                .flatMap(items -> items.stream())
                .collect(toList());
        for (Integer item : flatList) {
            System.out.println("Stream-flatMap item=" + item);
        }

        //stream-max/min:查找最大最小item
        String maxOrmin1 = lowerList.stream()
                .max(Comparator.comparing(item -> item.length()))//长度比较
                .get();
        System.out.println("Stream-max(最大长度的) item=" + maxOrmin1);

        //reduce 合并流数据并产生单个值
        //累加计算
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int sum = Arrays.stream(numbers).reduce(0, (a, b) -> a + b);
        int sum2 = Arrays.stream(numbers).reduce(0, Integer::sum);
        System.out.println("Stream-reduce 累加和1=" + sum);
        System.out.println("Stream-reduce 累加和2=" + sum2);

    }

}
