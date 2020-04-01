package lib.interview.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现2线程的打印，从小到大
 */
public class ThreadDemo {
    private static volatile int count = 1;

    private static boolean isJishu = true;

    public static volatile boolean flag = true;
    public static final Object monitor1 = new Object();
    public static volatile int num = 0;

    public static void main(String[] args) {
        new ThreadA().start();
        new ThreadB().start();

    }

    static class ThreadA extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 1; i < 100; i++) {
                System.out.println("进入ThreadA");
                synchronized (monitor1) {
                    System.out.println("进入ThreadA-2");
                    if (flag) {
                        try {
                            System.out.println("1：wait();");
                            monitor1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("1：奇数： " + num++);
                    flag = true;
                    monitor1.notify();
                }
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            super.run();
            while (num <= 100) {
                System.out.println("进入ThreadB");
                synchronized (monitor1) {
                    System.out.println("进入ThreadB-2");
                    if (!flag) {
                        try {
                            System.out.println("2 wait();");
                            monitor1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("2 偶数： " + num++);
                    flag = false;
                    monitor1.notify();
                }
            }
        }
    }
}
