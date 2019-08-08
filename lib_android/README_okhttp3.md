# okhttp3 总结

## 1. 请求流程分析

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_01.png)

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_02.png)

（1）okhttpClient的创建是通过Builder对象创建一个Call
（2）okhttp会通过Dispatcher对我们所有的RealCall（Call的具体实现类）进行统一管理，并通过execute()及enqueue()方法对同步或异步请求进行处理，
（3）RealCall中实现了异步（enqueue）和同步（execute）的方法，最终这些方法都会执行getResponseWithInterceptorChain方法，
该方法是处理5+1中拦截器的核心方法，通过chain.proceed(),将chain中的拦截器顺序的执行自己的intercept方法处理，
（4）拦截器链中，依次通过RetryAndFollowUpInterceptor（重定向拦截器）、BridgeInterceptor（桥接拦截器）、CacheInterceptor（缓存拦截器）
、ConnectInterceptor（连接拦截器）、CallServerInterceptor（网络拦截器）对请求依次处理，与服务的建立连接后，获取返回数据，
再经过上述拦截器依次处理后，最后将结果返回给getResponseWithInterceptorChain处理，然后流程结束。

## 2. 拦截器

1. RetryAndFollowUpInterceptor： 失败重试和重定向拦截器
2. BridgeInterceptor 桥拦截器，比较简单，添加或者移除一些header
负责将原始Requset转换给发送给服务端的Request以及将Response转化成对调用方友好的Response。
具体就是对request添加Content-Type、Content-Length、cookie、Connection、Host、Accept-Encoding等请求头以及对返回结果进行解压、保持cookie等。
3. CacheInterceptor 缓存拦截器,根据缓存策略，判断是否返回缓存数据，响应的数据是否要缓存起来
4. ConnectInterceptor 连接池
使用StreamAllocation.newStream来和服务端建立连接，并返回输入输出流（HttpCodec），实际上是通过StreamAllocation中的findConnection寻找
一个可用的Connection，然后调用Connection的connect方法，使用socket与服务端建立连接。

5. CallServerInterceptor 发送和接收数据,数据由okio处理

   主要的工作就是把请求的Request写入到服务端，然后从服务端读取Response。
（1）、写入请求头
（2）、写入请求体
（3）、读取响应头
（4）、读取响应体

## 3 连接池原理

由于HTTP是基于TCP，TCP连接时需要经过三次握手，为了加快网络访问速度，我们可以Reuqst的header中将Connection设置为keepalive来复用连接。
Okhttp支持5个并发KeepAlive，默认链路生命为5分钟(链路空闲后，保持存活的时间)，连接池有ConectionPool实现，对连接进行回收和管理。

## 4 同步异步的实现原理
1. 同步：Response response = client.newCall(request).execute();

先将RealCall加入Dispatcher的runningSyncCalls队列，然后调用getResponseWithInterceptorChain获取Response，
最后调用Dispatcher的finished方法，将自身从runningSyncCalls移除，然后进行轮询readyAsyncCalls队列，
取出ready的异步任务在满足条件的情况下进行执行。

2. 异步：client.newCall(request).enqueue(callback)
如果当前正在执行的RealCall的数量小于最大并发数maxRequest(64)，并且该call对应的Host上的call小于同一host上的最大并发数maxRequestsPerHos(5)，
则将该call加入runningAsyncCalls，并将这个call放到线程池中进行执行，否则加入readyAsyncCall排队等待。

注意：

同步请求和异步请求执行完成之后，都会调用dispatcher的finished方法，将自身从对应的队列中移除，然后进行轮询readyAsyncCalls队列，
取出ready的异步任务在满足条件下放到线程池中执行。



## 5 okhttp和volley比较
### okhttp

是专注于提升网络连接效率的http客户端。

优点：
（1）内置连接池，支持连接复用
（2）支持gzip压缩响应体
（3）通过缓存避免重复的请求
（4）支持http2，对一台机器的所有请求共享同一个socket，实现同一ip和端口的请求重用一个socket（大降低网络连接的时间，和每次请求都建立socket，再断开socket的方式相比，降低了服务器服务器的压力）
（5）可扩展，支持添加一个自定义拦截器对请求和返回结果进行处理。
缺点：

okhttp请求网络切换回来是在线程里面的，不是在主线程，不能直接刷新UI，需要我们手动处理。封装比较麻烦。

### Volley

Volley是google在2013 io大会上推出的网络通信框架，特别适合处理数据量小，通信频繁的网络操作

优点：
内部封装了异步线程，可直接在主线程请求网络，并处理返回的结果。同时可以取消请求，容易扩展。

缺点是：面对大数据量的请求，比如下载表现糟糕，不支持https。

Volley的底层在针对android2.3以下系统使用httpclicent，在android2.3以上采用HttpUrlConnection请求网络。

## 6 OkHttp中的设计模式

责任链模式：拦截器链
单例模式：线程池
观察者模式：各种回调监听
策略模式：缓存策略
Builder模式：OkHttpClient的构建过程
外观模式：OkHttpClient封装了很对类对象
工厂模式：Socket的生产

## 7. okio的原理

[android okio的原理 跳转](https://github.com/66668/Android_Interview/blob/master/lib_android/README_okio.md);


 