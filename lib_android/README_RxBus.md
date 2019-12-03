# Rxbus/EventBus 总结

## EventBus
参考：https://jsonchao.github.io/2019/01/28/Android主流三方库源码分析（九、深入理解EventBus源码）/

EventBus 的源码在Android主流三方库源码分析系列中可以说是除了ButterKnife之外，算是比较简单的了。但是，它其中的一些思想和设计是值得借鉴的。比如它使用 FindState 复用池来复用 FindState 对象，在各处使用了 synchronized 关键字进行代码块同步的一些优化操作。其中上面分析了这个多，**EventBus最核心的逻辑就是利用了 subscriptionsByEventType 这个重要的列表，将订阅对象，即接收事件的方法存储在这个列表，发布事件的时候在列表中查询出相对应的方法并执行**

## RxBus

 


