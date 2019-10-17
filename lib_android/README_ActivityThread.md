# ActivityThread 理解

ActivityThread其实不是一个Thread，而是一个final类型的Java类，并且拥有main(String[] args) 方法。
Android原生以Java语言为基础，Java的JVM启动的入口就是main(String[] args)。

主要的工作就是：

1. 设置消息处理Looper初始化
2. thread.attach(false, startSeq)把ActivityThread和主线程进行绑定
