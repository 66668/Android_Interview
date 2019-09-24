# java 面试题
1. 内部类与静态内部类与外部类的关系
[java 内部类与静态内部类与外部类的关系 跳转](https://github.com/66668/Android_Interview/blob/master/lib_java/README_inner_outer.md);

## HashMap Hashtable ConcurrentHashMap区别 

这个一定要去看源码！看源码！看源码！实在看不下去的可以上网看别人的分析。简单总结有几点：

1.HashMap支持null Key和null Value；Hashtable不允许。这是因为HashMap对null进行了特殊处理，将null的hashCode值定为了0，从而将其存放在哈希表的第0个bucket。
2.HashMap是非线程安全，HashMap实现线程安全方法为Map map = Collections.synchronziedMap(new HashMap())；Hashtable是线程安全
3.HashMap默认长度是16，扩容是原先的2倍；Hashtable默认长度是11，扩容是原先的2n+1
4.HashMap继承AbstractMap；Hashtable继承了Dictionary

HashTable

    底层数组+链表实现，无论key还是value都不能为null，线程安全，实现线程安全的方式是在修改数据时锁住整个HashTable，效率低，ConcurrentHashMap做了相关优化
    初始size为11，扩容：newsize = olesize*2+1
    计算index的方法：index = (hash & 0x7FFFFFFF) % tab.length

HashMap

    底层数组+链表实现，可以存储null键和null值，线程不安全
    初始size为16，扩容：newsize = oldsize*2，size一定为2的n次幂
    扩容针对整个Map，每次扩容时，原来数组中的元素依次重新计算存放位置，并重新插入
    插入元素后才判断该不该扩容，有可能无效扩容（插入后如果扩容，如果没有再次插入，就会产生无效扩容）
    当Map中元素总数超过Entry数组的75%，触发扩容操作，为了减少链表长度，元素分配更均匀
    计算index方法：index = hash & (tab.length – 1)

ConcurrentHashMap

    底层采用分段的数组+链表实现，线程安全
    通过把整个Map分为N个Segment段，可以提供相同的线程安全，但是效率提升N倍，默认提升16倍。(读操作不加锁，由于HashEntry的value变量是 volatile的，也能保证读取到最新的值。)
    Hashtable的synchronized是针对整张Hash表的，即每次锁住整张表让线程独占，ConcurrentHashMap允许多个修改操作并发进行，其关键在于使用了锁分离技术
    有些方法需要跨段，比如size()和containsValue()，它们可能需要锁定整个表而不仅仅是某个段，这需要按顺序锁定所有段，操作完毕后，又按顺序释放所有段的锁
    扩容：段内扩容（段内元素超过该段对应Entry数组长度的75%触发扩容，不会对整个Map进行扩容），插入前检测需不需要扩容，有效避免无效扩容


Hashtable和HashMap都实现了Map接口，但是Hashtable的实现是基于Dictionary抽象类的。Java5提供了ConcurrentHashMap，它是HashTable的替代，比HashTable的扩展性更好。


## HashMap，SparseArray和ArrayMap比较
### （1）HashMap
1. 才有数组+链表的key value形式，默认是一个容量为16的数组

HashMap中默认的存储大小就是一个容量为16的数组。当hashMap存储元素达到当前元素的75%时,hashMap 的存储空间会扩大，而且扩大的新空间一定时原来的2倍。

当hashMap 达到扩容条件时,HashMap会以2倍的速度扩容，当我们有几十万、几百万数据时，hashMap 将造成内存空间的消耗和浪费。

### （2）SparseArray 稀疏矩阵
1. 存储 整型类型的 key

2. 比HashMap 更省内存,某些条件下 性能更好,主要是因为它避免了对key的自动装箱。

3. 内部使用两个数组来存储key 和 value。并且在存储和查找数据时 都使用二分查找法,因此SparseArray内部的key都是有序的。

### （3）ArrayMap

1. ArrayMap 是一个 <key,value>映射的数据结构。它设计上更多考虑内存的优化。内部是使用两个数组进行数据存储，一个数组记录key的hash值，另外一个数组记录Value值。

2. 它和SparseArray一样，也会对key使用二分法进行从小到大排序。在添加、删除、查找数据的时候都是先使用二分查找法得到相应的index，然后通过index来进行添加、查找、删除等操作。

3. ArrayMap 与 SparseArray最大的一点不同就是 ArrayMap的key可以为任意的类型。而SparseAraay的key只能是整型。


## ArrayList对比Vector区别
1. 同步性：

Vector是线程安全的，也就是说是它的方法之间是线程同步的，
而ArrayList是线程序不安全的，它的方法之间是线程不同步的。

如果只有一个线程会访问到集合，那最好是使用ArrayList，因为它不考虑线程安全，效率会高些；如果有多个线程会访问到集合，那最好是使用Vector，因为不需要我们自己再去考虑和编写线程安全的代码。

(备注：对于Vector&ArrayList、Hashtable&HashMap，要记住线程安全问题，记住Vector与Hashtable是旧的，是java一诞生就提供的，它们是线程安全的，ArrayList与HashMap是java2时才提供的，它们是线程不安全的。所以，我们讲课时先讲老的。)

2. 数据增长：Vector增长原来的一倍，ArrayList增加原来的0.5倍。

## LinkedList和ArrayList区别
 

1. ArrayList和LinkedList可想从名字分析，它们一个是Array(动态数组)的数据结构，一个是Link(链表)的数据结构，此外，它们两个都是对List接口的实现。

前者是数组队列，相当于动态数组；后者为双向链表结构，也可当作堆栈、队列、双端队列

2. ArrayList的get和set操作）效率更高，因为LinkedList是线性的数据存储方式，所以需要移动指针从前往后依次查找。

3. LinkedList的add和remove操作效率更高，因为ArrayList是数组，所以在其中进行增删操作时，会对操作点之后所有数据的下标索引造成影响，需要进行数据的移动。

4. ArrayList主要控件开销在于需要在lList列表预留一定空间；而LinkList主要控件开销在于需要存储结点信息以及结点指针信息。

## int、Integer有什么区别

int 是我们常说的整形数字，是 Java 的 8 个原始数据类型（Primitive Types，boolean、byte 、short、char、int、float、double、long）之一。
Java 语言虽然号称一切都是对象，但原始数据类型是例外。

Integer 是 int 对应的包装类，它有一个 int 类型的字段存储数据，并且提供了基本操作，比如数学运算、int 和字符串之间转换等。
在 Java 5 中，引入了自动装箱和自动拆箱功能（boxing/unboxing），Java 可以根据上下文，自动进行转换，极大地简化了相关编程。

关于 Integer 的值缓存，这涉及 Java 5 中另一个改进。构建 Integer 对象的传统方式是直接调用构造器，直接 new 一个对象。但是根据实践，
我们发现大部分数据操作都是集中在有限的、较小的数值范围，因而，在 Java 5 中新增了静态工厂方法 valueOf，在调用它的时候会利用一个缓存机制，
带来了明显的性能改进。按照 Javadoc，这个值默认缓存是 -128 到 127 之间。

## String a = "a"+"b"+"c"+ "d" + "e"; 会创建几个String对象
一个，
String s = "a" + "b" + "c" + "d" + "e"; 
在虚拟机中，被解释为：String s=(new StringBuffer().append("a").append("b").append("c")...).toString();
因此，只产生了一个对象 

## String a="a"; String b="b"; a=a+b; 这里共创建了几个对象？
三个，string a="a" string b="b" 在字符串池中创建了两个对象一个是a 一个是b 而a=a+b则是直接在对内重新new了一个对象 位"ab"; 
你要知道，直接string定义一个新对象是 例如string a ="a" 它的过程是 先在字符串池中找有没有相同的对象 找不到就创建一个，
如果（我说如果）你在string a 之后又string b="a" 那么不会创建一个新的对象 而只是将b指向a而已；而a=a+b的话，你是改变的a的内容不管你有没有一样的，
他就直接在堆内new一个string a=new string ("ab")

## String StringBuffer，StringBuilder的区别
1. String String的值是不可变的，这就导致每次对String的操作都会生成新的String对象,StringBuffer，StringBuilder是可变的
2. StringBuffer是线程安全的（可用于并发环境），StringBuilder不是线性安全的（不能用于并发环境，如果使用，可能会出现问题）