package lib.interview.java.threadDemo;

/**
 * 同步锁
 */
public class SynchronizedDemo {
    public static final Object monitor = new Object();
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (monitor){
                    System.out.println("同步锁-Thread-run");
                }
            }
        }.start();
        synchronized (monitor){
            System.out.println("同步锁-主线程");
        }
    }
}
