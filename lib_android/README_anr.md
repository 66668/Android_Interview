# ANR 总结

ANR全称：Application Not Responding，也就是应用程序无响应。

## 日志位置

data/anr/traces.txt

## 原因

1. Android系统中，ActivityManagerService(简称AMS)和WindowManagerService(简称WMS)会检测App的响应时间，
如果App在特定时间无法相应屏幕触摸或键盘输入事件，或者特定事件没有处理完毕，就会出现ANR。

2. 耗时/死锁

## 常见事件

1. 按键或触摸事件在特定时间内无响应， 5s
2. BroadcastReceiver）的onReceive() 10s
3. Service Timeout 20s
4. ContentProvider Timeout ：ContentProvider的publish 10s

## 解决办法

耗时操作在子线程进行




