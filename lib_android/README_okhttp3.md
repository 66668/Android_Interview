# okhttp3 总结

## 库用途：
网络底层库，它是基于http协议封装的一套请求客户端，虽然它也可以开线程，但根本上它更偏向真正 的请求，跟HttpClient, HttpUrlConnection的职责是一样的。其中封装了网络请求get、post等底层操 作的实现。

## 优点：
1. 提供了对最新的 HTTP 协议版本 HTTP/2 和 SPDY 的支持，这使得对同一个主机发出的所 有请求都可以共享相同的套接字连接。

  换一种说法： 支持http2，对一台机器的所有请求共享同一个socket，实现同一ip和端口的请求重用一个socket（大降低网络连接的时间，和每次请求都建立socket，再断开socket的方式相比，降低了服务器服务器的压力）

2. 如果 HTTP/2 和 SPDY 不可用，OkHttp 会使用**连接池**来复用连接以提高效率。
3. 支持GZIP压缩来降低传输内容的大小。
4. OkHttp 也提供了对 HTTP 响应的缓存机制，可以避免不必要的网络请求。 当网络出现问题时，OkHttp 会自动重试一个主机的多个 IP 地址。
5. 可扩展，支持添加一个自定义拦截器对请求和返回结果进行处理。

## 缺点：

1. okhttp请求网络切换回来是在线程里面的，不是在主线程，不能直接刷新UI，需要我们手动处理。
2. 需要手动封装。

## 支持用法

get、post请求、上传文件、上传表单等等。

## 这个库的核心实现原理是什么?如果让你实现这个库的某些核心功能，你会考虑怎么去实现?
掌握这三点即可：

1. 背出使用流程
2. 缓存
3. 连接

## Okhttp详解

### 1. 请求流程分析

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_01.png)

![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_02.png)

（1）okhttpClient的创建是通过Builder对象创建一个Call
（2）okhttp会通过Dispatcher对我们所有的RealCall（Call的具体实现类）进行统一管理，并通过execute()及enqueue()方法对同步或异步请求进行处理，
（3）RealCall中实现了异步（enqueue）和同步（execute）的方法，最终这些方法都会执行getResponseWithInterceptorChain方法，
该方法是处理5+1中拦截器的核心方法，通过chain.proceed(),将chain中的拦截器顺序的执行自己的intercept方法处理，
（4）拦截器链中，依次通过RetryAndFollowUpInterceptor（重定向拦截器）、BridgeInterceptor（桥接拦截器）、CacheInterceptor（缓存拦截器）
、ConnectInterceptor（连接拦截器）、CallServerInterceptor（网络拦截器）对请求依次处理，与服务的建立连接后，获取返回数据，
再经过上述拦截器依次处理后，最后将结果返回给getResponseWithInterceptorChain处理，然后流程结束。

### 2.拦截器

1. RetryAndFollowUpInterceptor： 失败重试和重定向拦截器
2. BridgeInterceptor: 桥拦截器,比较简单，添加或者移除一些header

负责将原始Requset转换给发送给服务端的Request以及将Response转化成对调用方友好的Response。

具体就是对request添加Content-Type、Content-Length、cookie、Connection、Host、Accept-Encoding等请求头以及对返回结果进行解压、保持cookie等。

3. CacheInterceptor 缓存拦截器,根据缓存策略，判断是否返回缓存数据，响应的数据是否要缓存起来

    负责读取缓存直接返回(根据请求的信息和缓存的响应的信息来判断是否存在缓存可用)、更新缓存
4. ConnectInterceptor： 负责和服务器建立连接
        
        
        ConnectionPool: 
        1、判断连接是否可用，不可用则从ConnectionPool获取连接，ConnectionPool无连接，创建新连
        接，握手，放入ConnectionPool。 
        2、它是一个Deque，add添加Connection，使用线程池负责定时清理缓存。 
        3、使用连接复用省去了进行 TCP 和 TLS 握手的一个过程。
        
5. CallServerInterceptor 发送和接收数据,数据由okio处理


      主要的工作就是把请求的Request写入到服务端，然后从服务端读取Response。
    （1）写入请求头
    （2）写入请求体
    （3）读取响应头
    （4）读取响应体

### 3. 连接池原理

由于HTTP是基于TCP，TCP连接时需要经过三次握手，为了加快网络访问速度，我们可以Reuqst的header中将Connection设置为keepalive来复用连接。
Okhttp支持5个并发KeepAlive，默认链路生命为5分钟(链路空闲后，保持存活的时间)，连接池有ConectionPool实现，对连接进行回收和管理。

### 网络请求缓存处理，okhttp如何处理网络缓存的?
服务器返回的Response的头信息Cache-Control有三种值：
（1）max-age：这个参数告诉浏览器将页面缓存多长时间，超过这个时间后才再次向服务器发起请求检查页面是否有更新。对于静态的页面，比如图片、CSS、Javascript，一般都不大变更，因此通常我们将存储这些内容的时间设置为较长的时间，这样浏览器会不会向浏览器反复发起请求，也不会去检查是否更新了。

（2）no-cache：不做缓存。

（3）max-stale：指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可以接收超出超时期指定值之内的响应消息。
 
**设置缓存**:
1. 设置缓存：


        File cacheFile = new File(content.getExternalCacheDir(),"fileName");
        Cache cache = new Cache(cacheFile,1024*1024*50);
 
2. 使用：

1）服务器支持缓存：如果服务器支持缓存，请求返回的Response会带有这样的Header:Cache-Control, max-age=xxx,这种情况下我们只需要手动给okhttp设置缓存就可以让okhttp自动帮你缓存了（加入缓存不需要我们自己来实现，Okhttp已经内置了缓存，默认是不使用的，如果想使用缓存我们需要手动设置。）。这里的max-age的值代表了缓存在你本地存放的时间，可以根据实际需要来设置其大小。
    
        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient newClient = okHttpClient.newBuilder()
                       .cache(cache)//直接使用cache
                       .connectTimeout(20, TimeUnit.SECONDS)
                       .readTimeout(20, TimeUnit.SECONDS)
                       .build();
                       
(2)如果服务器不支持或者指定的值是no-cache:这种情况下我们就需要使用Interceptor来重写Respose的头部信息，从而让okhttp支持缓存。

步骤：自定义Interceptor，将Respose头部信息添加Cache-Control的头信息;

接着将该Interceptor作为一个NetworkInterceptor加入到okhttpClient中;
    
设置好Cache我们就可以正常访问了。我们可以通过获取到的Response对象拿到它正常的消息和缓存的消息。这样我们就可以在服务器不支持缓存的情况下使用缓存了


   
        //自定义拦截器
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
     
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=60";
                }
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        };
        
        
        //创建OkHttpClient，并添加拦截器和缓存代码
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .cache(cache)
        .build();               


### 4. 子系统层级结构:(不重要)
![okhttp3子系统层级结构](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_03.png)

1. 网络配置层:利用Builder模式配置各种参数，例如:超时时间、拦截器等，这些参数都会由Okhttp分 发给各个需要的子系统。
2. 重定向层:负责重定向。 
3. Header拼接层:负责把用户构造的请求转换为发送给服务器的请求，把服务器返回的响应转换为对用 户友好的响应。
4. HTTP缓存层:负责读取缓存以及更新缓存。 
5. 连接层:连接层是一个比较复杂的层级，它实现了网络协议、内部的拦截器、安全性认证，连接与连接 池等功能，但这一层还没有发起真正的连接，它只是做了连接器一些参数的处理。 
6. 数据响应层:负责从服务器读取响应的数据。 

**在整个Okhttp的系统中，我们还要理解以下几个关键角色**:

1. OkHttpClient:通信的客户端，用来统一管理发起请求与解析响应。
2. Call:Call是一个接口，它是HTTP请求的抽象描述，具体实现类是RealCall，它由CallFactory创建。 
3. Request:请求，封装请求的具体信息，例如:url、header等。 
4. RequestBody:请求体，用来提交流、表单等请求信息。 
5. Response:HTTP请求的响应，获取响应信息，例如:响应header等。
6. ResponseBody:HTTP请求的响应体，被读取一次以后就会关闭，所以我们重复调用 responseBody.string()获取请求结果是会报错的。 
7. Interceptor:Interceptor是请求拦截器，负责拦截并处理请求，它将网络请求、缓存、透明压缩等功 能都统一起来，每个功能都是一个Interceptor，所有的Interceptor最 终连接成一个 Interceptor.Chain。典型的责任链模式实现。 
8. StreamAllocation:用来控制Connections与Streas的资源分配与释放。 
9. RouteSelector:选择路线与自动重连。
10. RouteDatabase:记录连接失败的Route黑名单。

### 5. 同步异步的实现原理(不重要)
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


## okhttp和volley比较

1. Volley:支持HTTPS。缓存、异步请求，不支持同步请求。协议类型是Http/1.0, Http/1.1，网络传输使 用的是 HttpUrlConnection/HttpClient，数据读写使用的IO。 

2. OkHttp:支持HTTPS。缓存、异步请求、同步请求。协议类型是Http/1.0, Http/1.1, SPDY, Http/2.0, WebSocket，网络传输使用的是封装的Socket，数据读写使用的NIO(Okio)。
 
 SPDY协议类似于HTTP，但旨在缩短网页的加载时间和提高安全性。SPDY协议通过压缩、多路复用和 优先级来缩短加载时间。

## OkHttp中的设计模式

责任链模式：**使用责任链模式实现拦截器的分层设计，每一个拦截器对应一个功能，充分实现了功能解耦，易维护**
单例模式：线程池
观察者模式：各种回调监听
策略模式：缓存策略
Builder模式：OkHttpClient的构建过程
外观模式：OkHttpClient封装了很对类对象
工厂模式：Socket的生产

## okio的原理

[android okio的原理 跳转](https://github.com/66668/Android_Interview/blob/master/lib_android/README_okio.md);


 