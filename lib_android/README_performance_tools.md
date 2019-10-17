# android 性能优化-工具篇

## LeakCanary及原理

LeakCanary是Square公司基于MAT开发的一款监控Android内存泄漏的开源框架。

其工作的原理是：
监测机制利用了Java的WeakReference和ReferenceQueue，通过将Activity包装到WeakReference中，被WeakReference包装过的Activity对象如果被回收，
该WeakReference引用会被放到ReferenceQueue中，通过监测ReferenceQueue里面的内容就能检查到Activity是否能够被回收（在ReferenceQueue中说明可以被回收，不存在泄漏；
否则，可能存在泄漏，LeakCanary是执行一遍GC，若还未在ReferenceQueue中，就会认定为泄漏）。如果Activity被认定为泄露了，就抓取内存dump文件(Debug.dumpHprofData)；
之后通过HeapAnalyzerService.runAnalysis进行分析内存文件分析；接着通过HeapAnalyzer (checkForLeak—findLeakingReference---findLeakTrace)来进行内存泄漏分析。
最后通过DisplayLeakService进行内存泄漏的展示。

## Android Lint 工具（as自带）：
Android Lint Tool 是Android Sutido种集成的一个Android代码提示工具，它可以给你布局、代码提供非常强大的帮助。
硬编码会提示以级别警告，例如：在布局文件中写了三层冗余的LinearLayout布局、直接在TextView中写要显示的文字、字体大小使用dp而不是sp为单位，就会在编辑器右边看到提示。




## Battery Historian工具（开源go语言）：
Go配置+电量分析
（一）Go配置
安装网站（使用pkg文件为默认安装）
https://studygolang.com/dl 
https://golang.google.cn/dl/ 
https://golang.org/dl/
安装成功后，配置
open ~/.bash_profile

说明：
（1）使用pkg文件为默认安装，配置如下
加入如下2条：
export GOROOT=/usr/local/go
export PATH=$PATH:$GOROOT/bin
（2）使用自定义安装较好，可以结合GOPATH做代码测试：将代码放到自定义的workspace_sjy文件夹下即可
加入如下配置：
export GOROOT=/Users/sjy/go（自定义的go源码目录）
export GOPATH=$GOROOT/workspace_sjy（go的工作目录）
export PATH=$PATH:$GOROOT/bin（方便执行go命令）


source ~/.bash_profile

（二）电量分析
执行步骤（必须这么执行）：
1.下载源码 https://github.com/google/battery-historian，到指定目录，
不可以出错：/Users/sjy/go/workspace_sjy/src/github.com/google/battery-historian
2.按顺序执行着两行代码：
go run cmd/battery-historian/battery-historian.go
go get -u github.com/golang/protobuf/protoc-gen-go
（其实有第三行代码的，但是有问题，就将源码下载下来了，就是第1步骤）
2.cd 到下载battery-historian的目录下
cd /Users/sjy/go/workspace_sjy/src/github.com/google/battery-historian
3.执行go语言即可
go run setup.go
4.成功后，执行如下一行，用于在网页查看
go run cmd/battery-historian/battery-historian.go
5.打开浏览器，查看：http://localhost:9999

## Profiler 工具（as自带）：
可以监测 cpu memory network energy

## 性能优化--APP启动速度优化

意义：避免app安装后因启动原因卸载

分类：冷启动/温启动/热启动

启动流程

### 优化方向：

大部分是由Android系统完成，主要是Application和Activity的生命周期

1. Theme主体优化

解决：黑屏白屏问题

操作：windowBackground预设一个bg或透明样式,app启动后，在act的oncreate前，再setTheme

缺点：设置bg，治标不治本，给人一种快的感觉/透明样式，是慢的感觉

2. 避免过重的app初始化

准则：能异步的异步，不能异步的延迟加载，先让应用启动，再操作

操作：延迟初始化：广告页/logo页时初始化，

     异步初始化：使用前初始化
     
3. multiDex 预加载初始化

操作：（1）启动时，单独开启一个进程进行multidex的第一次初始化，即dex的提取和dexopt操作
    （2）此时，主进程在后台的等待，（1）执行完后通知主进程继续执行，此时再次执行到multidex时，发现已经优化初始化完成了，直接执行
    
### 优化工具

1. traceView的使用

获取两种数据：（1）可获取执行最耗时方法（2）获取执行最多次的方法(主要关注Calls + Recur Calls / Total和（该方法调用次数+递归次数）和Cpu Time / Call（该方法耗时）这两个值)

 操作：（1）代码中添加：Debug.startMethodTracing()/Debug.stopMethodTracing(),会生成一个.trace文件（分析该文件（2））
    （2）profiler--cpu--record--stop 分析top down/bottom up 找出耗时的热点方法

2. 耗时方法内打印log：

    (1)自己加log(2)使用第三方aop开源AspectJ，添加无入侵式打印

## 性能优化--APP绘制优化
### 可能面试点：android系统显示原理（SurfaceFlinger相关问题）
### 优化工具
1. 开发者选项/调试GPU过度绘制，打开涂层渲染，无、蓝、绿、淡红、深红，分别对应0-4次过度绘制。
2. traceView的使用主要关注Calls + Recur Calls / Total和（该方法调用次数+递归次数）和Cpu Time / Call（该方法耗时）这两个值
3. Systrace分析：

    作用：
     收集Android关键子系统（如surfaceflinger、WindowManagerService等Framework部分关键模块、服务、View系统等）的运行信息，这样可以更直观
     地分析系统瓶颈，改进性能。
     
     跟踪系统的I/0操作、内核工作队列、CPU负载等，在UI显示性能分析上提供很好的数据，特别是在动画播放不流畅、渲染卡等问题上。
     
     注意：Systrace是以系统的角度返回一些信息，并不能定位到具体耗时的方法，建议使用TraceView。
     
### 优化操作：

1. 减少布局层级嵌套:

合理使用RelativeLayout和LinearLayout，使用约束布局， 合理使用Merge，include，ViewStub等标签:

尽可能少用wrap_content，wrap_content会增加布局measure时的计算成本，已知宽高为固定值时，不用wrap_content。

使用TextView替换RL、LL。

使用低端机进行优化，以发现性能瓶颈。

使用TextView的行间距替换多行文本：lineSpacingExtra/lineSpacingMultiplier。

使用Spannable/Html.fromHtml替换多种不同规格文字。

尽可能使用LinearLayout自带的分割线。

使用Space添加间距。

多利用lint + alibaba规约修复问题点。

嵌套层级过多可以考虑使用约束布局。

2. 避免过度绘制


    导致原因是：

    XML布局：控件有重叠且都有设置背景。

    View自绘：View.OnDraw里面同一个区域被绘制多次。
    
    操作：

    （1）布局上的优化
    移除XML中非必需的背景，或根据条件设置。

    有选择性地移除窗口背景：getWindow().setBackgroundDrawable(null)。

    按需显示占位背景图片。比如：在获取Avatar的图像之后，把ImageView的Background设置为Transparent，只有当图像没有获取到时，才设置对应的Background占位图片。
    （2）自定义View优化
    通过canvas.clipRect()来帮助系统识别那些可见的区域。这个方法可以指定一块矩形区域，只有在这个区域内才会被绘制。并且，它还可以节约CPU和GPU资源，
    在clipRect区域之外的绘制指令都不会被执行。

    在绘制一个单元之前，首先判断该单元的区域是否在Canvas的剪切域内。若不在，直接返回，避免CPU和GPU的计算和渲染工作。


## 性能优化--内存优化（见OOM篇）

## 性能优化--app瘦身（见app瘦身篇README_app_slim）

## 性能优化--App电量优化

### 优化工具
1. 手机设置-电量--应用，查看应用耗电情况
2. 使用开源 battery historain分析

### 具体优化：
1. traceView分析高cpu问题，对cpu时间片优化
2. 网络传输：（1）压缩传输数据，（2）尽量使用wifi(3)低电量尽量不传输（4）无网络不请求
3. 定位优化：后台时注销定位监听，多模块时，功能要复用
4. 谨慎使用WakeLock,是一种锁机制，只要进程app持有WakeLock，手机就不会休眠：
    （1）前台app不申请WakeLock(2)app后台使用带超时参数的方法，防止未释放锁
       （3）任务结束时，及时释放（4）屏幕常亮设置，使用FLAG_KEEP_SCREEN_ON即可
5. 使用传感器，及时注销
6. JobScheduler使用的场景优化：
    （1）定期数据库数据更新
    （2）当预置的条件被满足时才执行
    （3）多任务打包一起执行，wifi下才执行等
    
  
## 性能优化--网络优化

1. 连接复用:节省连接建立时间，如开启 keep-alive。于Android来说默认情况下 HttpURLConnection和HttpClient都开启了keep-alive。
只是2.2之前HttpURLConnection存在影 响连接池的Bug。
2. 请求合并:即将多个请求合并为一个进行请求，比较常见的就是网页中的CSS Image Sprites。 如果某个页面内请求过多，也可以考虑做一定的请求合并。
3. 减少请求数据的大小:对于post请求，body可以做gzip压缩的，header也可以做数据压缩(不 过只支持http 2.0)。 返回数据的body也可以做gzip压缩，
body数据体积可以缩小到原来的30%左右(也可以考虑压缩 返回的json数据的key数据的体积，尤其是针对返回数据格式变化不大的情况，支付宝聊天返回的 数据用到了)。
4. 根据用户的当前的网络质量来判断下载什么质量的图片(电商用的比较多)。
5. 使用HttpDNS优化DNS:DNS存在解析慢和DNS劫持等问题，DNS 不仅支持 UDP，它还支持 TCP，但是大部分标准的 DNS 都是基于 UDP 与 DNS 服务器的 53
 端口进行交互。HTTPDNS 则不 同，顾名思义它是利用 HTTP 协议与 DNS 服务器的 80 端口进行交互。不走传统的 DNS 解析，从 而绕过运营商的 LocalDNS 服务器，
 有效的防止了域名劫持，提高域名解析的效率。
       
   
      
 






