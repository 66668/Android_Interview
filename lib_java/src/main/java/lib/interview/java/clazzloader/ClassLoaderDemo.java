package lib.interview.java.clazzloader;

/**
 * 自定义ClassLoader的loadclass，绕过双亲委派机制
 */
public class ClassLoaderDemo {

    //测试
    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            Class<?> loadClazz = myClassLoader.loadClass("lib.interview.java.clazzloader.TestClazz");
            System.out.println("自定义ClassLoader的loadclass，绕过双亲委派机制=" + loadClazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
