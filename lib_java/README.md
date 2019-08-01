# java 面试题
1. 内部类与静态内部类与外部类的关系
[java 内部类与静态内部类与外部类的关系 跳转](https://github.com/66668/Android_Interview/blob/master/lib_java/README_inner_outer.md);

一、HashMap和Hashtable区别？
这个一定要去看源码！看源码！看源码！实在看不下去的可以上网看别人的分析。简单总结有几点：
1.HashMap支持null Key和null Value；Hashtable不允许。这是因为HashMap对null进行了特殊处理，将null的hashCode值定为了0，从而将其存放在哈希表的第0个bucket。
2.HashMap是非线程安全，HashMap实现线程安全方法为Map map = Collections.synchronziedMap(new HashMap())；Hashtable是线程安全
3.HashMap默认长度是16，扩容是原先的2倍；Hashtable默认长度是11，扩容是原先的2n+1
4.HashMap继承AbstractMap；Hashtable继承了Dictionary
扩展，HashMap 对比 ConcurrentHashMap ，HashMap 对比 SparseArray，LinkedArray对比ArrayList，ArrayList对比Vector

int、Integer有什么区别