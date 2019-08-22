# binder机制总结

## 进程相关
https://www.jianshu.com/p/cd0d8594abc7


## 进程间通讯的几种方式：

1. AIDL [aidl总结 跳转](https://github.com/66668/Android_Interview/blob/master/README_aidl.md);
2. Messager
3. ContentProvider
4. 广播
但是Android并没有采用Linux的几种IPC方案，而是使用了binder来完成进程之间的IPC。这主要是基于以下两点考虑：

效率，其底层使用了共享内存机制，提高了数据读写的效率
安全，从底层着手控制每个进程之间的访问权限，相比Linux在上层来控制访问权限要更加安全

那么binder又是如何在Android中使用的呢？主要有两种方式：

1. 采用代理模式，每个Client在访问Service之前都会获取一个Service的代理，然后通过这个代理来调用Service端提供的功能。比方说我们在Activity或者Service中通过getSystemService()获取到的XXXManager接口就属于这种。
2. 采用广播发送/接收的形式，也就是我们今天要讲的Broadcast和BroadcastReceiver，这种方式本质上属于消息订阅/发布的事件驱动流形式。

那么这两种形式有什么区别呢？

代理模式一般用于点对点通信，也就是网络通信中的单播模式，优点是效率高，缺点是通信是即时发起的，同步调用。
广播方式的话，效率相对低一些，但是通信是随时发起，异步调用。

当需要App应用内通信时，优先选择观察者或者Eventbus，当需要进程间通信或者监听系统广播事件时，选择Broadcast


