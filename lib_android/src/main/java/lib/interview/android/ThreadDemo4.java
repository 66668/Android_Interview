package lib.interview.android;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现3个线程按照顺序交替打印
 * <p>
 * ReentrantLock+condition实现
 */
public class ThreadDemo4 {

    private int count = 1;//

    //1 2 3
    private static volatile int flag = 1;

    //重入锁,ReentrantLock
    static Lock lock = new ReentrantLock();
    static Condition condition1 = lock.newCondition();
    static Condition condition2 = lock.newCondition();
    static Condition condition3 = lock.newCondition();


    public static void main(String[] args) {

        new Thread(new Runnable1()).start();
        new Thread(new Runnable2()).start();
        new Thread(new Runnable3()).start();

    }

    //线程1
    public static class Runnable1 implements Runnable {
        @Override
        public void run() {
            while (flag <= 100) {
                lock.lock();
                try {
                    while (flag % 3 != 1) {
                        condition1.await();
                    }
                    System.out.println(Thread.currentThread().getName() + "--" + flag);
                    flag++;//更换标记位
                    condition2.signal();//唤醒下一个线程
                } catch (Exception e) {
                    lock.unlock();
                }
            }
        }
    }


    public static class Runnable2 implements Runnable {
        @Override
        public void run() {
            while (flag <= 100) {
                lock.lock();
                try {
                    while (flag % 3 != 2) {
                        condition2.await();
                    }
                    System.out.println(Thread.currentThread().getName() + "--" + flag);
                    flag++;//更换标记位
                    condition3.signal();//唤醒下一个线程
                } catch (Exception e) {
                    lock.unlock();
                }
            }
        }
    }

    //线程1
    public static class Runnable3 implements Runnable {
        @Override
        public void run() {
            while (flag <= 100) {
                lock.lock();
                try {
                    while (flag % 3 != 0) {
                        condition3.await();
                    }
                    System.out.println(Thread.currentThread().getName() + "--" + flag);
                    flag++;//更换标记位
                    condition1.signal();//唤醒下一个线程
                } catch (Exception e) {
                    lock.unlock();
                }
            }
        }
    }

}
