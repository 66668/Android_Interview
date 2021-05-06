package lib.interview.java.threadDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现2线程的打印，从小到大
 * ReentrantLock实现
 */
public class ThreadDemo3 {

    private int start = 1;

    //对 flag 的写入虽然加锁保证了线程安全，但读取的时候由于 不是 volatile 所以可能会读取到旧值
    private volatile boolean flag = false;

    //重入锁,ReentrantLock
    private final static Lock lock = new ReentrantLock();

    public static void main(String[] args){
        ThreadDemo3 threadDemo3 = new ThreadDemo3();

        Thread t1 = new Thread(new Runnable1(threadDemo3));
        t1.setName("t1");

        Thread t2 = new Thread(new Runnable2(threadDemo3));
        t2.setName("t2");

        t1.start();
        t2.start();
    }

    //奇数线程
    public static class Runnable1 implements Runnable{

        private ThreadDemo3 dmeo;

        public Runnable1(ThreadDemo3 number){
            this.dmeo = number;
        }

        @Override
        public void run() {
            while(dmeo.start <= 100){
                if(!dmeo.flag){
                    try{
                        lock.lock();
                        System.out.println(Thread.currentThread().getName() + "+-+" + dmeo.start);
                        dmeo.start ++ ;
                        dmeo.flag = true;
                    }finally{
                        lock.unlock();
                    }
                }
            }
        }

    }

    //偶数线程
    public static class Runnable2 implements Runnable{

        private ThreadDemo3 dmeo2;

        public Runnable2(ThreadDemo3 number){
            this.dmeo2 = number;
        }

        @Override
        public void run() {
            while(dmeo2.start <= 100){
                if(dmeo2.flag){
                    try{
                        lock.lock();
                        System.out.println(Thread.currentThread().getName() + "+-+" + dmeo2.start);
                        dmeo2.start ++ ;
                        dmeo2.flag = false;
                    }finally{
                        lock.unlock();
                    }
                }
            }
        }
    }
}
