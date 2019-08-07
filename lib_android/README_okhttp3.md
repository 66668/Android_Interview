# okhttp3 总结

## 1. 请求流程分析

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_01.png)

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_02.png)

（1）okhttpClient的创建是通过Builder对象创建并初始化的
（2）发送请求方式是通过okhttpClient.newCall(),进入到RealCall的实例中，
（3）RealCall中实现了异步（enqueue）和同步（execute）的方法，最终这些方法都会执行getResponseWithInterceptorChain方法，
该方法是处理5+1中拦截器的核心方法，通过chain.proceed(),将chain中的拦截器顺序的执行自己的intercept方法处理，
（4）最后调用的拦截器CallServerInterceptor，用okio请求网络返回了Response，然后Response往上一层的拦截器回传，
上一个拦截器通过 response = chain.proceed(request);就获取到response，继续处理并回传上层
（5）处理完拦截器，okhttp也就执行完成。

## 2. 拦截器

1. RetryAndFollowUpInterceptor： 失败重试和重定向拦截器
2. BridgeInterceptor 桥拦截器，比较简单，添加或者移除一些header
3. CacheInterceptor 缓存拦截器,根据缓存策略，判断是否返回缓存数据，响应的数据是否要缓存起来
4. ConnectInterceptor 连接池
5. CallServerInterceptor 发送和接收数据,数据由okio处理

## 3. 同步异步的实现原理
1. 同步：Response response = client.newCall(request).execute();
2. 异步：client.newCall(request).enqueue(callback)
最终调用RealCall方法实现



## okhttp和volley比较
okhttp是专注于提升网络连接效率的http客户端。

优点：

1. 它能实现同一ip和端口的请求重用一个socket，这种方式能大大降低网络连接的时间，和每次请求都建立socket，再断开socket的方式相比，降低了服务器服务器的压力。
2. okhttp 对http和https都有良好的支持。
3. okhttp 不用担心android版本变换的困扰。
4. 成熟的网络请求解决方案，比HttpURLConnection更好用。

缺点：

okhttp请求网络切换回来是在线程里面的，不是在主线程，不能直接刷新UI，需要我们手动处理。封装比较麻烦。

四、Volley

Volley是google在2013 io大会上推出的网络通信框架，特别适合处理数据量小，通信频繁的网络操作

优点：
内部封装了异步线程，可直接在主线程请求网络，并处理返回的结果。同时可以取消请求，容易扩展。

缺点是：面对大数据量的请求，比如下载表现糟糕，不支持https。

Volley的底层在针对android2.3以下系统使用httpclicent，在android2.3以上采用HttpUrlConnection请求网络。

## 4. okio的原理

[android okio的原理 跳转](https://github.com/66668/Android_Interview/blob/master/lib_android/README_okio.md);


 