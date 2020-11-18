package lib.interview.java.syncDemo;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * list线程安全的遍历
 * 说明：多线程下，使用Vector,可以实现线程安全的add,remove等操作，但是在遍历下，如果有add,remove操作，仍然是非线程安全的
 */
public class SyncListDemo {

    public static void main(String[] args) {
        //
        copyOnWriteArrayList();
        //
        listForEach();
    }

    /**
     * 方案一：copyOnWriteArrayList
     */
    public static void copyOnWriteArrayList() {
        // 初始化一个list，放入5个元素
        final List<Integer> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        // 线程一：通过Iterator遍历List
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int item : list) {
                    System.out.println("遍历元素：" + item);
                    // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 线程二：remove一个元素
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                list.remove(4);
                System.out.println("list.remove(4)");
            }
        }).start();
    }

    /**
     * Vector的listForeach遍历才能线程安全
     */
    public static void listForEach() {
// 初始化一个list，放入5个元素
        final List<Integer> list = new Vector<>();
        for(int i = 0; i < 5; i++) {
            list.add(i);
        }

        // 线程一：通过Iterator遍历List
        new Thread(new Runnable() {
            @Override
            public void run() {
                list.forEach(item -> {
                    System.out.println("遍历元素：" + item);
                    // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();

        // 线程二：remove一个元素
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 由于程序跑的太快，这里sleep了1秒来调慢程序的运行速度
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                list.remove(4);
                System.out.println("list.remove(4)");
            }
        }).start();
    }
}
