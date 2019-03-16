# handler机制详解

## handler引起的问题：
   
1. 子线程更新UI+Looper创建
2. OOM
3. Message优化
4. 空指针

## handler的功能

1. 处理延时任务
2. 线程间通信

## 支撑handler框架的5个类 ?

1. Handler
2. Message
3. 