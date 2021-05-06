package lib.interview.java.threadDemo;

/**
 * 实现3个线程同步打印，从小到大
 * <p>
 * 关键点：num%3，用余数控制线程
 */
public class ThreadDemo2 {

    public static final Object monitor = new Object();
    public static volatile int num = 0;

    public static void main(String[] args) {
        new MyThread(0).start();
        new MyThread(1).start();
        new MyThread(2).start();

    }

    static class MyThread extends Thread {
        int id = 0;

        public MyThread(int i) {
            id = i;
        }

        @Override
        public void run() {
            super.run();
            while (num <= 100) {
                synchronized (monitor) {
                    System.out.println("Thread：" + id);
                    if ((num % 3) == id) {
                        System.out.println("Thread " + id + "--打印=" + num++);
                        monitor.notifyAll();
                    } else {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
