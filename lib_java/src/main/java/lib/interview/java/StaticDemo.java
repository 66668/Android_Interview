package lib.interview.java;

public class StaticDemo {
    public static void main(String[] args) {
        Foo foo = new Foo();
    }
}
class Foo{
    static{
        System.out.println("执行顺序1：static块");
    }
    {
        System.out.println("执行顺序2：代码块");
    }

    public Foo(){
        System.out.println("执行顺序3：构造器");
    }
}

