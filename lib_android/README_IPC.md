# andorid IPC 跨进程通信 总结

## 1 Android中进程和线程的关系?区别
1. 线程是CPU调度的最小单元，同时线程是一种有限的系统资源;而进程一般指一个执行单元，在 PC和移动设备上指一个程序或者一个应用。 
2. 一般来说，一个App程序至少有一个进程，一个进程至少有一个线程(包含与被包含的关系)，通 俗来讲就是，在App这个工厂里面有一个进程，线程就是里面的生产线，但主线程(即主生产线) 只有一条，而子线程(即副生产线)可以有多个。 
3. 进程有自己独立的地址空间，而进程中的线程共享此地址空间，都可以并发执行

## 如何开启多进程?应用是否可以开启N个进程?

在AndroidManifest中给四大组件指定属性android:process开启多进程模式，在内存允许的条件下可以开启N个进程。

## 为何需要IPC? 

所有运行在不同进程的四大组件(Activity、Service、Receiver、ContentProvider)共享数据都会失 败，这是由于Android为每个应用分配了独立的虚拟机，不同的虚拟机在内存分配上有不同的地址空 间，这会导致在不同的虚拟机中访问同一个类的对象会产生多份副本

## 多进程通信可能会出现的问题?

一般来说，使用多进程通信会造成如下几方面的问题:

1. 静态成员和单例模式完全失效:独立的虚拟机造成。 
2. 线程同步机制完全失效:独立的虚拟机造成。 
3. SharedPreferences的可靠性下降:这是因为Sp不支持两个进程并发进行读写，有一定几率导致数 据丢失。 
4. Application会多次创建:Android系统在创建新的进程时会分配独立的虚拟机，所以这个过程其实 就是启动一个应用的过程，自然也会创建新的Application。

## Android中IPC方式、各种方式优缺点?

1. AIDL
2. Messenger
3. ContentProvider，如访问系统相册
4. 广播， 如显示系统时间
5. Intent+Uri跳转系统应用
6. 文件共享
7. RemoteView
8. Socket

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_01.png)

##AIDL的实现

AIDL(Android Interface Definition Language，Android接口定义语言):如果在一个进程中要调用另一 个进程中对象的方法，可使用AIDL生成可序列化的参数，AIDL会生成一个服务端对象的代理类，通过 它客户端可以实现间接调用服务端对象的方法。

AIDL的本质是系统提供了一套可快速实现Binder的工具。关键类和方法:

1. AIDL接口:继承IInterface。
2. Stub类:Binder的实现类，服务端通过这个类来提供服务。 
3. Proxy类:服务端的本地代理，客户端通过这个类调用服务端的方法。 
4. asInterface():客户端调用，将服务端返回的Binder对象，转换成客户端所需要的AIDL接口类型 的对象。如果客户端和服务端位于同一进程，则直接返回Stub对象本身，否则返回系统封装后的 Stub.proxy对象。
5. asBinder():根据当前调用情况返回代理Proxy的Binder对象。 
6. onTransact():运行在服务端的Binder线程池中，当客户端发起跨进程请求时，远程请求会通过系 统底层封装后交由此方法来处理。 
7. transact():运行在客户端，当客户端发起远程请求的同时将当前线程挂起。之后调用服务端的 onTransact()直到远程请求返回，当前线程才继续执行。

##如何优化多模块都使用AIDL的情况

当有多个业务模块都需要AIDL来进行IPC，此时需要为每个模块创建特定的aidl文件，那么相应的 Service就会很多。必然会出现系统资源耗费严重、应用过度重量级的问题。

解决办法:

1. 建立Binder连接池，即将每个业务模块的Binder请求统一转发到一个远程Service中去执行，从而避免重复创建 Service。
2. 工作原理:每个业务模块创建自己的AIDL接口并实现此接口，然后向服务端提供自己的唯一标识和其对 应的Binder对象。服务端只需要一个Service并提供一个queryBinder接口，它会根据业务模块的特征来 返回相应的Binder对象，不同的业务模块拿到所需的Binder对象后就可以进行远程方法的调用了。

## 为什么选择Binder
为什么选用Binder，在讨论这个问题之前，我们知道Android也是基于Linux内核，Linux现有的进程通
信手段有以下几种:

1. 管道:在创建时分配一个page大小的内存，缓存区大小比较有限; 
2. 消息队列:信息复制两次，额外的CPU消耗;不合适频繁或信息量大的通信; 
3. 共享内存:无须复制，共享缓冲区直接附加到进程虚拟地址空间，速度快;但进程间的同步问题操 作系统无法实现，必须各进程利用同步工具解决; 
4. 套接字:作为更通用的接口，传输效率低，主要用于不同机器或跨网络的通信; 
5. 信号量:常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主 要作为进程间以及同一进程内不同线程之间的同步手段。 不适用于信息交换，更适用于进程中断 控制，比如非法内存访问，杀死某个进程等;

### 重新设计一套Binder机制,主要是出于以上三个方面的考量:

1. 效率:传输效率主要影响因素是内存拷贝的次数，拷贝次数越少，传输速率越高。

从Android 进程架构角度分析:对于消息队列、Socket和管道来说，数据先从发送方的缓存区拷贝到内核开 辟的缓存区中，再从内核缓存区拷贝到接收方的缓存区，一共两次拷贝，如图:
![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_02.png)

而对于Binder来说，数据从发送方的缓存区拷贝到内核的缓存区，而接收方的缓存区与内核的缓存区是 映射到同一块物理地址的，节省了一次数据拷贝的过程 如图

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_03.png)

共享内存不需要拷贝，Binder的性能仅次于共享内存((其底层使用了共享内存机制))

2. 稳定性:上面说到共享内存的性能优于Binder，那为什么不采用共享内存呢，因为共享内存需要处理并发同步问题，容易出现死锁和资源竞争，稳定性较差。Socket虽然是基于C/S架构的，但是它主要是用于网络间的通信且传输效率较低。Binder基于C/S架构 ，Server端与Client端相对独立，稳定性较好。

3. 安全性:传统Linux IPC的接收方无法获得对方进程可靠的UID/PID，从而无法鉴别对方身份; 而Binder机制为每个进程分配了UID/PID，且在Binder通信时会根据UID/PID进行有效性检测。

## Binder机制的作用和原理

Linux系统将一个进程分为用户空间和内核空间。对于进程之间来说，用户空间的数据不可共享，内核 空间的数据可共享，为了保证安全性和独立性，一个进程不能直接操作或者访问另一个进程，即 Android的进程是相互独立、隔离的，这就需要跨进程之间的数据通信方式。普通的跨进程通信方式一 般需要2次内存拷贝，

如下图所示:
![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_04.png)

一次完整的 Binder IPC 跨进程通信的核心原理:

1. 首先 Binder 驱动在内核空间创建一个数据接收缓存区。
2. 接着在内核空间开辟一块内核缓存区，建立内核缓存区和内核中数据接收缓存区之间的映射关系， 以及内核中数据接收缓存区和接收进程用户空间地址的映射关系。
3. 发送方进程通过系统调用 copyfromuser() 将数据 copy 到内核中的内核缓存区，由于内核缓存区 和接收进程的用户空间存在内存映射，因此也就相当于把数据发送到了接收进程的用户空间，这样 便完成了一次进程间的通信。

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_05.png)

### Binder框架中ServiceManager的作用?
Binder框架 是基于 C/S 架构的。由一系列的组件组成，包括 Client、Server、ServiceManager、 Binder驱动，其中 Client、Server、Service Manager 运行在用户空间，Binder 驱动运行在内核空 间。

1. Server&Client:服务器&客户端。在Binder驱动和Service Manager提供的基础设施上，进行 Client-Server之间的通信。 
2. ServiceManager(如同DNS域名服务器)服务的管理者，将Binder名字转换为Client中对该 Binder的引用，使得Client可以通过Binder名字获得Server中Binder实体的引用。 
3. Binder驱动(如同路由器):负责进程之间binder通信的建立，计数管理以及数据的传递交互等 底层支持。

如下图所示:
![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_06.png)

### 模型原理步骤说明:
![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/ipc_07.png)


### Binder 的完整定义
1. 从进程间通信的角度看，Binder 是一种进程间通信的机制;
2. 从 Server 进程的角度看，Binder 指的是 Server 中的 Binder 实体对象;
3. 从 Client 进程的角度看，Binder 指的是 Binder 代理对象，是 Binder 实体对象的一个远程代理;
4. 从传输过程的角度看，Binder 是一个可以跨进程传输的对象;Binder 驱动会对这个跨越进程边界 的对象对一点点特殊处理，自动完成代理对象和本地对象之间的转换。


写给 Android 应用工程师的 Binder 原理剖析:https://juejin.im/post/5acccf845188255c3201100f

Binder设计与实现: https://juejin.im/post/5acccf845188255c3201100f

Binder学习指南: http://weishu.me/2016/01/12/binder-index-for-newer/

Android进程间通信（IPC）机制Binder简要介绍和学习计划: https://blog.csdn.net/luoshengyang/article/details/6618363

## binder又是如何在Android中使用的呢？主要有两种方式：

1. 采用代理模式，每个Client在访问Service之前都会获取一个Service的代理，然后通过这个代理来调用Service端提供的功能。比方说我们在Activity或者Service中通过getSystemService()获取到的XXXManager接口就属于这种。
2. 采用广播发送/接收的形式，也就是我们今天要讲的Broadcast和BroadcastReceiver，这种方式本质上属于消息订阅/发布的事件驱动流形式。

那么这两种形式有什么区别呢？

代理模式一般用于点对点通信，也就是网络通信中的单播模式，优点是效率高，缺点是通信是即时发起的，同步调用。
广播方式的话，效率相对低一些，但是通信是随时发起，异步调用。

当需要App应用内通信时，优先选择观察者或者Eventbus，当需要进程间通信或者监听系统广播事件时，选择Broadcast

## 手写实现简化版AMS(AIDL实现)

与Binder相关的几个类的职责:

            
            1. IBinder:跨进程通信的Base接口，它声明了跨进程通信需要实现的一系列抽象方法，实现了这个 接口就说明可以进行跨进程通信，Client和Server都要实现此接口。 
            2. IInterface:这也是一个Base接口，用来表示Server提供了哪些能力，是Client和Server通信的协 议。 
            3. Binder:提供Binder服务的本地对象的基类，它实现了IBinder接口，所有本地对象都要继承这个 类。 
            4. BinderProxy:在Binder.java这个文件中还定义了一个BinderProxy类，这个类表示Binder代理对 象它同样实现了IBinder接口，不过它的很多实现都交由native层处理。Client中拿到的实际上是这个代理对象。 
            5. Stub:这个类在编译aidl文件后自动生成，它继承自Binder，表示它是一个Binder本地对象;它是 一个抽象类，实现了IInterface接口，表明它的子类需要实现Server将要提供的具体能力(即aidl 文件中声明的方法)。 
            6. Proxy:它实现了IInterface接口，说明它是Binder通信过程的一部分;它实现了aidl中声明的方 法，但最终还是交由其中的mRemote成员来处理，说明它是一个代理对象，mRemote成员实际 上就是BinderProxy。

aidl文件只是用来定义C/S交互的接口，Android在编译时会自动生成相应的Java类，生成的类中包含了 Stub和Proxy静态内部类，用来封装数据转换的过程，实际使用时只关心具体的Java接口类即可。为什 么Stub和Proxy是静态内部类呢?这其实只是为了将三个类放在一个文件中，提高代码的聚合性。通过 上面的分析，我们其实完全可以不通过aidl，手动编码来实现Binder的通信，下面我们通过编码来实现 ActivityManagerService:

1. 首先定义IActivityManager接口:

 
      public interface IActivityManager extends IInterface { //binder描述符
      String DESCRIPTOR = "android.app.IActivityManager";
      //方法编号
      int TRANSACTION_startActivity = IBinder.FIRST_CALL_TRANSACTION + 0; //声明一个启动activity的方法，为了简化，这里只传入intent参数
      int startActivity(Intent intent) throws RemoteException;
      }
      
2. 然后，实现ActivityManagerService侧的本地Binder对象基类

        
        // 名称随意，不一定叫Stub
        public abstract class ActivityManagerNative extends Binder implements IActivityManager {
            public static IActivityManager asInterface(IBinder obj) {
                if (obj == null) {
                    return null;
                }
                IActivityManager in = (IActivityManager)
        obj.queryLocalInterface(IActivityManager.DESCRIPTOR);
                if (in != null) {
                    return in;
        }
        //代理对象，见下面的代码
        return new ActivityManagerProxy(obj);
        }
            @Override
            public IBinder asBinder() {
                return this;
            }
        @Override
            protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        throws RemoteException {
        switch (code) {
        // 获取binder描述符
        case INTERFACE_TRANSACTION:
                        reply.writeString(IActivityManager.DESCRIPTOR);
                        return true;
        // 启动activity，从data中反序列化出intent参数后，直接调用子类startActivity 方法启动activity。
                    case IActivityManager.TRANSACTION_startActivity:
                        data.enforceInterface(IActivityManager.DESCRIPTOR);
                        Intent intent = Intent.CREATOR.createFromParcel(data);
                        int result = this.startActivity(intent);
                        reply.writeNoException();
                        reply.writeInt(result);
                        return true;
        }
                return super.onTransact(code, data, reply, flags);
            }
        }
3. 接着，实现Client侧的代理对象:

       
       public class ActivityManagerProxy implements IActivityManager {
           private IBinder mRemote;
           public ActivityManagerProxy(IBinder remote) {
               mRemote = remote;
       }
           @Override
           public IBinder asBinder() {
               return mRemote;
           }
           @Override
           public int startActivity(Intent intent) throws RemoteException {
               Parcel data = Parcel.obtain();
               Parcel reply = Parcel.obtain();
               int result;
               try {
       reply, 0);
       // 将intent参数序列化，写入data中
       intent.writeToParcel(data, 0);
       // 调用BinderProxy对象的transact方法，交由Binder驱动处理。 mRemote.transact(IActivityManager.TRANSACTION_startActivity, data,
       reply.readException();
       // 等待server执行结束后，读取执行结果 result = reply.readInt();
               } finally {
                   data.recycle();
                   reply.recycle();
               }
               return result;
           }
       }
       
4. 最后，实现Binder本地对象(IActivityManager接口):

        
            public class ActivityManagerService extends ActivityManagerNative {
                @Override
            public int startActivity(Intent intent) throws RemoteException { // 启动activity
            return 0; }
            }
            
简化版的ActivityManagerService到这里就已经实现了，剩下就是Client只需要获取到AMS的代理对象 IActivityManager就可以通信了。







