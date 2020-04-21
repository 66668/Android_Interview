package lib.interview.java;

public class HuaWeiOD2 {
    public static void main(String[] args) {
        PointP p1 = new PointP(0, 0);
        PointP p2 = new PointP(0, 0);
        transP(p1, p2);
        System.out.println("p1值：" + p1.x + "--y=" + p1.y);//输出：0 0
        System.out.println("p2值：" + p2.x + "--y=" + p2.y);//输出 5 5
    }
    static void transP(PointP p1, PointP p2) {
        //地址交换，赋值有效，new值无效
        PointP nP = p1;
        p1 = p2;
        p2 = nP;

        p1.set(5, 5);//交换地址的赋值，会影响实参,实际是赋值给p2
        p2.set(1,1);
        p2 = new PointP(5, 5);//new不影响实参
    }
    static class PointP {
        public int x, y;
        public PointP(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void set(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
