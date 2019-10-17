# ForceClose 总结

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

## FC(Force Close)什么时候会出现?
空指针，未处理异常，Error、OOM，StackOverFlowError、Runtime,等
 
解决的办法:
1. 注意内存的使用和管理 
2. 使用Thread.UncaughtExceptionHandler接口(如上)



