# 图片加载框架 总结 （Glide/Fresco）

# Glide总结

## 库用途

Glide是Android中的一个图片加载库，用于实现图片加载

## 库优点：
1. 多样化媒体加载:不仅支持图片缓存，还支持Gif、WebP、缩略图，甚至是Video。 
2. 通过设置绑定生命周期:可以使加载图片的生命周期动态管理起来。
3. 高效的缓存策略:支持内存缓存，Disk缓存，并且Picasso只会缓存原始尺寸的图片，而Glide缓存的是多种规格，也就是Glide会根据你ImageView的大小来缓存相应大小的图片尺寸。
4. 内存开销小:默认的Bitmap格式是RGB_565格式，而Picasso默认的是ARGB_8888格式，内存开销小一半。
## 缺点：

库比较大，源码实现复杂。

## 这个库都有哪些用法?对应什么样的使用场景?
1. 图片加载:Glide.with(this).load(imageUrl).override(800, 800).placeholder().error().animate().into()。
2. 多样式媒体加载:asBitamp、asGif。
3. 生命周期集成。
4. 可以配置磁盘缓存策略ALL、NONE、SOURCE、RESULT。

## 核心实现原理是什么?如果让你实现这个库的某些核心功能，你会考虑怎么去实现?

### Glide/with()
1. 初始化各式各样的配置信息(包括缓存，请求线程池，大小，图片格式等等)以及glide对象。
2. 将glide请求和application/SupportFragment/Fragment的生命周期绑定在一块。
### Glide/load()
1. 设置请求url，并记录url已设置的状态。
### Glide/into()
1. 首先根据转码类transcodeClass类型返回不同的ImageViewTarget,BitmapImageViewTarget或DrawableImageViewTarget。
2. 递归建立缩略图请求，没有缩略图请求，则直接进行正常请求
3. 如果没指定宽高，会根据ImageView的宽高计算出图片宽高，最终执行到onSizeReay()方法中的engine.load()方法。
4. engine是一个负责加载和管理缓存资源的类

### 常规三级缓存的流程:强引用->软引用->硬盘缓存

当我们的APP中想要加载某张图片时，先去LruCache中寻找图片，如果LruCache中有，则直接取出来 使用，如果LruCache中没有，则去SoftReference中寻找(软引用适合当cache，当内存吃紧的时候才 会被回收。而weakReference在每次system.gc()就会被回收)(当LruCache存储紧张时，会把最近 最少使用的数据放到SoftReference中)，如果SoftReference中有，则从SoftReference中取出图片使 用，同时将图片重新放回到LruCache中，如果SoftReference中也没有图片，则去硬盘缓存中中寻找， 如果有则取出来使用，同时将图片添加到LruCache中，如果没有，则连接网络从网上下载图片。图片 下载完成后，将图片保存到硬盘缓存中，然后放到LruCache中。

### Glide的三层缓存机制:

Glide缓存机制大致分为三层:内存缓存、弱引用缓存、磁盘缓存。

取的顺序是:内存、弱引用、磁盘。 

存的顺序是:弱引用、内存、磁盘。

三层存储的机制在Engine中实现的。先说下Engine是什么?Engine这一层负责加载时做管理内存缓存 的逻辑。持有MemoryCache、Map<Key, WeakReference<EngineResource<?>>>。通过load()来 加载图片，加载前后会做内存存储的逻辑。如果内存缓存中没有，那么才会使用EngineJob这一层来进 行异步获取硬盘资源或网络资源。EngineJob类似一个异步线程或observable。Engine是一个全局唯一 的，通过Glide.getEngine()来获取。

需要一个图片资源，如果Lrucache中有相应的资源图片，那么就返回，同时从Lrucache中清除，放到 activeResources中。activeResources map是盛放正在使用的资源，以弱引用的形式存在。同时资源 内部有被引用的记录。如果资源没有引用记录了，那么再放回Lrucache中，同时从activeResources中 清除。如果Lrucache中没有，就从activeResources中找，找到后相应资源引用加1。如果Lrucache和 activeResources中没有，那么进行资源异步请求(网络/diskLrucache)，请求成功后，资源放到 diskLrucache和activeResources中。

### Glide源码机制的核心思想:

使用一个弱引用map activeResources来盛放项目中正在使用的资源。Lrucache中不含有正在使用的资 源。资源内部有个计数器来显示自己是不是还有被引用的情况，把正在使用的资源和没有被使用的资源 分开有什么好处呢??因为当Lrucache需要移除一个缓存时，会调用resource.recycle()方法。注意到 该方法上面注释写着只有没有任何consumer引用该资源的时候才可以调用这个方法。那么为什么调用 resource.recycle()方法需要保证该资源没有任何consumer引用呢?glide中resource定义的 recycle()要做的事情是把这个不用的资源(假设是bitmap或drawable)放到bitmapPool中。 bitmapPool是一个bitmap回收再利用的库，在做transform的时候会从这个bitmapPool中拿一个 bitmap进行再利用。这样就避免了重新创建bitmap，减少了内存的开支。而既然bitmapPool中的 bitmap会被重复利用，那么肯定要保证回收该资源的时候(即调用资源的recycle()时)，要保证该 资源真的没有外界引用了。这也是为什么glide花费那么多逻辑来保证Lrucache中的资源没有外界引用 的原因。

## 学到什么有价值的或者说可借鉴的设计思想?

Glide的**高效的三层缓存机制**，如上。

## Glide如何确定图片加载完毕?
1. 添加监听 RequestListener 
2. 使用静态图代替没加载出来的图

## Glide内存缓存如何控制大小
一种是Resource缓存，一类是Bitmap缓存。
1. **Resource缓存**：

图片从网络加载，将图片缓存到本地，当需要再次使用时，直接从缓存中取出而无需再次请求网络。

Glide在缓存Resource使用三层缓存，包括：

    一级缓存：缓存被回收的资源，使用LRU算法（Least Frequently Used，最近最少使用算法）。当需要再次使用到被回收的资源，直接从内存返回。
    二级缓存：使用弱引用缓存正在使用的资源。当系统执行gc操作时，会回收没有强引用的资源。使用弱引用缓存资源，既可以缓存正在使用的强引用资源，也不阻碍系统需要回收无引用资源。
    三级缓存：磁盘缓存。网络图片下载成功后将以文件的形式缓存到磁盘中。

2. **Bitmap缓存**

通过Bitmap压缩质量参数：Glide默认使用RGB_565，比系统默认使用的ARGB_8888节省一半的资源，但RGB_565无法显示透明度。

**Bitmap缓存算法**：

在Glide中，使用**BitmapPool**来缓存Bitmap,使用的也是LRU算法。当需要使用Bitmap时，从Bitmap的池子中取出合适的Bitmap,若取不到合适的，则再新创建。当Bitmap使用完后，不直接调用Bitmap.recycler()回收，而是放入Bitmap的池子。


## Universal-ImageLoader，Picasso，Fresco，Glide对比
### Fresco 
 Facebook 推出的开源图片缓存工具，主要特点包括：两个内存缓存加上 Native 缓存构成了三级缓存，
 
**优点**：
1. 图片存储在安卓系统的匿名共享内存, 而不是虚拟机的堆内存中, 图片的中间缓冲数据也存放在本地堆内存, 所以, 应用程序有更多的内存使用, 不会因为图片加载而导致oom, 同时也减少垃圾回收器频繁调用回收 Bitmap 导致的界面卡顿, 性能更高。
 
2. 渐进式加载 JPEG 图片, 支持图片从模糊到清晰加载。
 
3. 图片可以以任意的中心点显示在 ImageView, 而不仅仅是图片的中心。
 
4. JPEG 图片改变大小也是在 native 进行的, 不是在虚拟机的堆内存, 同样减少 OOM。

5. 很好的支持 GIF 图片的显示。
 
**缺点**:
1. 框架较大, 影响 Apk 体积
2. 使用较繁琐
 
### Universal-ImageLoader：（估计由于HttpClient被Google放弃，作者就放弃维护这个框架）

优点：
1.支持下载进度监听
2.可以在 View 滚动中暂停图片加载，通过 PauseOnScrollListener 接口可以在 View 滚动中暂停图片加载。
3.默认实现多种内存缓存算法 这几个图片缓存都可以配置缓存算法，不过 ImageLoader 默认实现了较多缓存算法，如 Size 最大先删除、使用最少先删除、最近最少使用、先进先删除、时间最长先删除等。
4.支持本地缓存文件名规则定义
     
### Picasso 优点

1. 自带统计监控功能。支持图片缓存使用的监控，包括缓存命中率、已使用内存大小、节省的流量等。
 
2. 支持优先级处理。每次任务调度前会选择优先级高的任务，比如 App 页面中 Banner 的优先级高于 Icon 时就很适用。
 
3. 支持延迟到图片尺寸计算完成加载
 
4. 支持飞行模式、并发线程数根据网络类型而变。 手机切换到飞行模式或网络类型变换时会自动调整线程池最大并发数，比如 wifi 最大并发为 4，4g 为 3，3g 为 2。  这里 Picasso 根据网络类型来决定最大并发数，而不是 CPU 核数。
 
5. “无”本地缓存。无”本地缓存，不是说没有本地缓存，而是 Picasso 自己没有实现，交给了 Square 的另外一个网络库 okhttp 去实现，这样的好处是可以通过请求 Response Header 中的 Cache-Control 及 Expired 控制图片的过期时间。
 
### Glide 优点

1. 不仅仅可以进行图片缓存还可以缓存媒体文件。Glide 不仅是一个图片缓存，它支持 Gif、WebP、缩略图。甚至是 Video，所以更该当做一个媒体缓存。
 
2. 支持优先级处理。
 
3. 与 Activity/Fragment 生命周期一致，支持 trimMemory。Glide 对每个 context 都保持一个 RequestManager，通过 FragmentTransaction 保持与 Activity/Fragment 生命周期一致，并且有对应的 trimMemory 接口实现可供调用。
 
4. 支持 okhttp、Volley。Glide 默认通过 UrlConnection 获取数据，可以配合 okhttp 或是 Volley 使用。实际 ImageLoader、Picasso 也都支持 okhttp、Volley。
 
5. 内存友好。Glide 的内存缓存有个 active 的设计，从内存缓存中取数据时，不像一般的实现用 get，而是用 remove，再将这个缓存数据放到一个 value 为软引用的 activeResources map 中，并计数引用数，在图片加载完成后进行判断，如果引用计数为空则回收掉。内存缓存更小图片，Glide 以 url、view_width、view_height、屏幕的分辨率等做为联合 key，将处理后的图片缓存在内存缓存中，而不是原始图片以节省大小与 Activity/Fragment 生命周期一致，支持 trimMemory。图片默认使用默认 RGB_565 而不是 ARGB_888，虽然清晰度差些，但图片更小，也可配置到 ARGB_888。
 
6. Glide 可以通过 signature 或不使用本地缓存支持 url 过期