#  UI相关

## 更新UI方式

1. Activity.runOnUiThread(Runnable)
2. View.post(Runnable)，View.postDelay(Runnable, long)(可以理解为在当前操作视图UI线程添 加队列)
3. Handler
4. AsyncTask
5. Rxjava
6. LiveData

## Merge/ViewStub/include 的作用。 

1. Merge: 减少视图层级，可以删除多余的层级。
2. ViewStub: 按需加载，减少内存使用量、加快渲染速度、不支持 merge 标签
3. include:xml优化布局，可以公用公共布局，merge比include少一层布局。

## activity的startActivity和context的startActivity区别?

1. 从Activity中启动新的Activity时可以直接mContext.startActivity(intent)就好 
2. 如果从其他Context中启动Activity则必须给intent设置Flag:
    
    
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
    mContext.startActivity(intent);
    
## Asset目录与res目录的区别?

1. assets:不会在 R 文件中生成相应标记，存放到这里的资源在打包时会打包到程序安装包中。(通过AssetManager 类访问这些文件)

2. res:会在 R 文件中生成 id 标记，资源在打包时如果使用到则打包到安装包中，未用到不会打入安装包
    中。
    res/anim:存放动画资源。
    res/raw:和 asset 下文件一样，打包时直接打入程序安装包中(会映射到 R 文件中)。
    
## Android怎么加速启动Activity?
1. onCreate() 中不执行耗时操作

把页面显示的 View 细分一下，放在 AsyncTask 里逐步显示，用 Handler 更好。这样用户的看到 的就是有层次有步骤的一个个的 View 的展示，不会是先看到一个黑屏，然后一下显示所有 View。最好做成动画，效果更自然。

2. 利用多线程的目的就是尽可能的减少 onCreate() 和 onReume() 的时间，使得用户能尽快看到页 面，操作页面。
3. 减少主线程阻塞时间。
4. 提高 Adapter 和 AdapterView 的效率。 
5. 优化布局文件。

## 程序A能否接收到程序B的广播?
 
能，使用全局的BroadCastRecevier能进行跨进程通信，但是注意它只能被动接收广播。此外，
LocalBroadCastRecevier只限于本进程的广播间通信。

## 下拉状态栏是不是影响activity的生命周期

Android下拉通知栏不会影响Activity的生命周期方法

## Android长连接，怎么处理心跳机制。
长连接:长连接是建立连接之后, 不主动断开. 双方互相发送数据, 发完了也不主动断开连接, 之后有需要发送的数据就继续通过这个连接发送.

心跳包:其实主要是为了防止NAT超时，客户端隔一段时间就主动发一个数据，探测连接是否断开。

服务器处理心跳包:假如客户端心跳间隔是固定的, 那么服务器在连接闲置超过这个时间还没收到心跳 时, 可以认为对方掉线, 关闭连接. 

如果客户端心跳会动态改变, 应当设置一个最大值, 超过这个最大值才认为对方掉线. 还有一种情况就是服务器通过TCP连接主动给客户端发消息出现写超时, 可以直接认为对方掉线.

## CrashHandler实现原理?

获取app crash的信息保存在本地然后在下一次打开app的时候发送到服务器。

具体实现在Application的onCreate()中初始化如下代码即可。

        //捕捉未catch的异常，
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, final Throwable ex) {
                    //自定义处理即可  
                }
            });