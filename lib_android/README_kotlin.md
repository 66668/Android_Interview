# kotlin系列 总结

## kotlin下的5种单例模式

1. 饿汉式


    //java实现
    public class SingletonDemo {
    private SingletonDemo () {

    }
    // 在类加载时就完成了初始化，使得类加载较慢
    private static SingletonDemo instance = new SingletonDemo ();

    public static SingletonDemo getInstance() {
        return instance ;
    }
    }
    
    //kotlin实现
    
    object SingletonDemo {
    
    }


2. 懒汉式 - 非线程安全


    //java实现
    public class SingletonDemo {
    
        private SingletonDemo () {
    
        }
       
        private static SingletonDemo instance ;
    
        public static SingletonDemo getInstance() {
            if (instance == null) {
                instance = new SingletonDemo ();
            }
            return instance ;
        }
    }
  
    //Kotlin实现
    class SingletonDemo private constructor() {
       
        companion object {
            private var instance: SingletonDemo? = null
                get() {
                    if (field == null) {
                        field = SingletonDemo()
                    }
                    return field
                }
                
            fun get(): SingletonDemo{
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
             return instance!!
            }
        }
    }

3. 懒汉式 - 线程安全


    //java实现
    public class SingletonDemo {
        private static SingletonDemo instance;
        private SingletonDemo(){}
        public static synchronized SingletonDemo getInstance(){
            if(instance== null){
                instance = new SingletonDemo();
            }
            return instance;
        }
    }
    
    //
    class SingletonDemo {
        private constructor() {}
        
        companion object {
            private var instance: SingletonDemo? = null
                get() {
                    if (field == null) {
                        field = SingletonDemo()
                    }
                    return field
                }
            
            @Synchronized
            fun get(): SingletonDemo{
                return instance!!
            }
        }
    }
    
    
    
 4. 双重校验锁式（Double Check)
 
 说明：
 
 Lazy是接受一个 lambda 并返回一个 Lazy 实例的函数，返回的实例可以作为实现延迟属性的委托：
  第一次调用 get() 会执行已传递给 lazy() 的 lambda 表达式并记录结果， 后续调用 get() 只是返回记录的结果。
 
    //Java实现
    public class SingletonDemo {
        private volatile static SingletonDemo instance;
        private SingletonDemo(){} 
        public static SingletonDemo getInstance(){
            if(instance==null){
                synchronized (SingletonDemo.class){
                    if(instance==null){
                        instance=new SingletonDemo();
                    }
                }
            }
            return instance;
        }
    }
    
    //kotlin实现
    class SingletonDemo private constructor() {
        companion object {
            val instance: SingletonDemo by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonDemo() }
        }
    }

  
 5. 静态内部类式       
 
    
    //Java实现
    public class SingletonDemo {
        private static class SingletonHolder{
            private static SingletonDemo instance=new SingletonDemo();
        }
        private SingletonDemo(){
            System.out.println("Singleton has loaded");
        }
        public static SingletonDemo getInstance(){
            return SingletonHolder.instance;
        }
    }
    
    //kotlin实现
    class SingletonDemo private constructor() {
        companion object {
            val instance = SingletonHolder.holder
        }
    
        private object SingletonHolder {
            val holder= SingletonDemo()
        }
    
    }