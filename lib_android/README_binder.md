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













