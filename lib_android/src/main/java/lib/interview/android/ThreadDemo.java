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
            while (num <= 100) {
                synchronized (monitor1) {
                    System.out.println("进入ThreadA");
                    if (num % 2 == 0) {
                        try {
                            System.out.println("ThreadA：wait()");
                            monitor1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("1：奇数： " + num++);
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
                synchronized (monitor1) {
                    System.out.println("进入ThreadB");
                    if (num % 2 == 1) {
                        try {
                            System.out.println("ThreadB wait()");
                            monitor1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("2 偶数： " + num++);
                    monitor1.notify();
                }
            }
        }
    }
}
