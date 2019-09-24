# binder机制总结（序列化，反序列化）


每个Android的进程(app)，只能运行在自己进程所拥有的虚拟地址空间。虚拟地址空间又分为用户空间和内核空间，对于用户空间是不能共享的。
内核空间是可以共享的。Client端向Service端进程通信，利用的就是可共享内核内存空间来完成底层底层通信工作的。

### Android中的Binder实现机制

要进行Client-Server之间的通信，从面向对象的角度，在Server内部有一个Binder实体，在Client内部有一个Binder对象的引用，
其实就是Binder的一个代理，Client通过对Binder引用间接的操作Server内部的Binder实体，这样就实现了通信。

ServiceManager用于注册管理C/S的一一对应关系的。

流程总结：客户端通过bindService，通过Binder驱动查询ServiceManager是否已经注册该服务，
如果没有注册，Service进程会想Binder驱动发起服务注册请求，一旦注册，调用该服务的onBind返回一个Binder对象到Binder驱动，
已经注册则意味着Binder驱动内包含这个Binder对象，Binder驱动返回一个BinderProxy对象，并通过回调，
传递给客户端，客户端通过这个BinderProxy(在java层仍然是Binder对象)操作Binder驱动内的Binder对象（transact方法），
Binder驱动含有很多的Binder对象，它们是通过InterfaceToken区分不同服务的

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




## 序列化与反序列化

### 概念

序列化是指将Java对象转换为字节序列的过程

反序列化则是将字节序列转换为Java对象的过程。

Java对象序列化是将实现了Serializable接口的对象转换成一个字节序列，能够通过网络传输、文件存储等方式传输 ，传输过程中却不必担心数据在不同机器、不同环境下发生改变，也不必关心字节的顺序或其他任何细节，并能够在以后将这个字节序列完全恢复为原来的对象(恢复这一过程称之为反序列化)。

对象的序列化是非常有趣的，因为利用它可以实现轻量级持久性，“持久性”意味着一个对象的生存周期不单单取决于程序是否正在运行，它可以生存于程序的调用之间。通过将一个序列化对象写入磁盘，然后在重新调用程序时恢复该对象，从而达到实现对象的持久性的效果。

本质上讲，序列化就是把实体对象状态按照一定的格式写入到有序字节流，反序列化就是从有序字节流重建对象，恢复对象状态。

### 为什么需要使用序列化和反序列化(场景)

我们知道，不同进程/程序间进行远程通信时，可以相互发送各种类型的数据，包括文本、图片、音频、视频等，而这些数据都会以二进制序列的形式在网络上传送。

那么当两个Java进程进行通信时，能否实现进程间的对象传送呢？当然是可以的！如何做到呢？这就需要使用Java序列化与反序列化了。发送方需要把这个Java对象转换为字节序列，然后在网络上传输，接收方则需要将字节序列中恢复出Java对象。

我们清楚了为什么需要使用Java序列化和反序列化后，我们很自然地会想到Java序列化有哪些好处（场景）：

1. 实现了数据的持久化，通过序列化可以把数据永久地保存到硬盘上（如：存储在文件里），实现永久保存对象。
2. 利用序列化实现远程通信，即：能够在网络上传输对象。
3. 内存中数据调用（Intent，下面会多次用到）。

### Java中下Serializable

 在使用 ObjectInputStream和ObjectOutputStream读写对象时，该对象的类必须实现序列化的。
 
### Android下Serializable

果对类成员属性进行了修改，那么再次读取这个对象时，会因为UID不同而抛出异常。
所以我们在实现Serializable接口同时，在类中要声明UID。

### Parcelable类

序列化方法和步骤

这是我们实现Parcelable接口的一系列方法。主要可以分为四步：

1. 创建私有化构造方法（或者protected）
2. 重写describeContents方法。
3. 重写writeToParcel方法，这个方法是我们将对象序列化的方法。
4. 实现Creator类，并实现createFromParcel方法和newArray方法，newArray方法不是很重要，主要看createFromParcel方法，这个方法是我们反序列化得到对象的方法

序列化后的对象全都存储在了这个里面。其实Parcelable类只是一个外壳，而真正实现了序列化的，是Parcel这个类，它里面有大量的方法，对各种数据进行序列化

## Serializable和Parcelable总结

1. 两者都是实现序列化得接口，都可以用Intent传递数据。
2. Serializable使用时会产生大量的临时变量，进行IO操作频繁，消耗比较大，但是实现方式简单。
3. Parcelable是Android提供轻量级方法，效率高，但是实现复杂。
4. 一般在内存中序列画传递时选用Parcelable。在设备或网络中传递选用Serializable。
5. 无论是Serializable还是Parcelable，两种内属性只要有对象，那么对应对象的类一定也要实现序列化。











