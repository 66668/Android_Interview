# ANR 总结

ANR全称：Application Not Responding，也就是应用程序无响应。

## 日志位置

data/anr/traces.txt

## 原因

回答1：

1. Android系统中，ActivityManagerService(简称AMS)和WindowManagerService(简称WMS)会检测App的响应时间，
如果App在特定时间无法相应屏幕触摸或键盘输入事件，或者特定事件没有处理完毕，就会出现ANR。
2. 耗时/死锁


回答2：
1. 耗时计算：网络访问，大量IO数据读写,数据库操作 
2. 硬件操作（比如camera)
3. 调用thread的join()方法、sleep()方法、wait()方法或者等待线程锁的时候
4. service binder的数量达到上限
5. system server中发生WatchDog ANR
6. service忙导致超时无响应
7. 其他线程持有锁，导致主线程等待超时
8. 其它线程终止或崩溃导致主线程一直等待

## 常见事件

1. 按键或触摸事件在特定时间内无响应， 5s
2. BroadcastReceiver）的onReceive() 10s
3. Service Timeout 20s
4. ContentProvider Timeout ：ContentProvider的publish 10s

## 解决办法

1. onCreate()/onReceve()的耗时操作，在子线程进行
2. 使用handler处理线程的结果，而不是用thread.wait/sleep阻塞
3. 使用Thread或者HandlerThread时，调用 Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)设置优先级，
否则仍然会降低 程序响应，因为默认Thread的优先级和主线程相同




