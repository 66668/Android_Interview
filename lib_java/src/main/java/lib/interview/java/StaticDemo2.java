package lib.interview.java;

public class StaticDemo2 extends Base{
    static{
        System.out.println("执行顺序2：子类静态块");
    }
    {
        System.out.println("执行顺序5：子类块");
    }

    public StaticDemo2(){
        System.out.println("执行顺序6：子类构造");
    }

    //入口：
    public static void main(String[] args) {
        new StaticDemo2();
    }

}
class Base{
    static{
        System.out.println("执行顺序1：父类静态块");
    }
    {
        System.out.println("执行顺序3：父类块");
    }

    public Base(){
        System.out.println("执行顺序4：父类构造");
    }
}
