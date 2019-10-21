# LruCache 总结（结合Bitmap/Glide看）

## LruCache原理：

LruCache中Lru算法的实现就是通过LinkedHashMap来实现的。LinkedHashMap继承于HashMap，它使用了一个双向链表来存储Map中的Entry顺序关系，对于get、put、remove等操作，LinkedHashMap除了要做HashMap做的事情，还做些调整Entry顺序链表的工作。

LruCache的原理就是利用LinkedHashMap持有对象的强引用，按照Lru算法进行对象淘汰。具体说来 假设我们从表尾访问数据，在表头删除数据，当访问的数据项在链表中存在时，则将该数据项移动到表 尾，否则在表尾新建一个数据项。当链表容量超过一定阈值，则移除表头的数据。


## DisLruCache原理

DiskLruCache与LruCache原理相似，只是多了一个journal文件来做磁盘文件的管理

这里的缓存目录是应用的缓存目录/data/data/pckagename/cache
