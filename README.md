# 知识点总结

1. android相关：

[android相关 跳转](https://github.com/66668/Android_Interview/blob/master/lib_android/README.md);

面试要求总结：

2.Activity的启动模式以及使用场景 
（1）manifest设置，（2）startActivity flag 
http://blog.csdn.net/CodeEmperor/article/details/50481726 
此处延伸：栈(First In Last Out)与队列(First In First Out)的区别

3.Service的两种启动方式 
（1）startService()，（2）bindService() 
http://www.jianshu.com/p/2fb6eb14fdec

4.Broadcast注册方式与区别 
（1）静态注册(minifest)，（2）动态注册 
http://www.jianshu.com/p/ea5e233d9f43 
此处延伸：什么情况下用动态注册

15.JNI 
http://www.jianshu.com/p/aba734d5b5cd 
此处延伸：项目中使用JNI的地方，如：核心逻辑，密钥，加密逻辑

16.java虚拟机和Dalvik虚拟机的区别 
http://www.jianshu.com/p/923aebd31b65

17.线程sleep和wait有什么区别 
http://blog.csdn.net/liuzhenwen/article/details/4202967

18.View，ViewGroup事件分发 
http://blog.csdn.net/guolin_blog/article/details/9097463 
http://blog.csdn.net/guolin_blog/article/details/9153747

19.保存Activity状态 
onSaveInstanceState() 
http://blog.csdn.net/yuzhiboyi/article/details/7677026

7.http与https的区别 
http://blog.csdn.net/whatday/article/details/38147103 
此处延伸：https的实现原理

9.进程保活（不死进程） 
http://www.jianshu.com/p/63aafe3c12af 
此处延伸：进程的优先级是什么（下面这篇文章，都有说） 
https://segmentfault.com/a/1190000006251859

10.进程间通信的方式 
（1）AIDL，（2）广播，（3）Messenger 
AIDL : http://www.jianshu.com/p/ae97c3ceea8d 
Messenger : http://blog.csdn.net/lmj623565791/article/details/47017485 
此处延伸：简述Binder ， http://blog.csdn.net/luoshengyang/article/details/6618363/

23.自定义view和动画 
以下两个讲解都讲得很透彻，这部分面试官多数不会问很深，要么就给你一个效果让你讲原理。 
（1）http://www.gcssloop.com/customview/CustomViewIndex 
（2）http://blog.csdn.net/yanbober/article/details/50577855

24.设计模式（单例，工厂，观察者。作用，使用场景） 
一般说自己会的就ok，不要只记得名字就一轮嘴说出来，不然有你好受。 
http://blog.csdn.net/jason0539/article/details/23297037/ 
此处延伸：Double Check的写法被要求写出来。

25.String，Stringbuffer，Stringbuilder 区别 
http://blog.csdn.net/kingzone_2008/article/details/9220691

26.开源框架，为什么使用，与别的有什么区别 
这个问题基本必问。在自己简历上写什么框架，他就会问什么。 
如：Volley，面试官会问我Volley的实现原理，与okhttp和retrofit的区别。 
开源框架很多，我就选几个多数公司都会用的出来（框架都是针对业务和性能，所以不一定出名的框架就有人用） 
网络请求：Volley，okhttp，retrofit 
异步：RxJava，AsyncTask 
图片处理：Picasso，Glide 
消息传递：EventBus 
以上框架请自行查找，太多了就不贴出来了。

27.RecyclerView 

熟悉常见设计模式

熟悉Android UI绘制、事件响应、数据存储、网络请求等；

熟悉Android网络通信，对TCP/IP，Socket和HTTP有较深刻的理解，有网络编程经验；熟悉网络编程，对Http等网络协议有深入认识 

熟悉 Android Framework 层，有通过源码阅读定位问题的经验；

熟悉计算机操作系统及计算机网络知识。

掌握调试技能和方法，能独立分析和解决崩溃，性能优化，ANR等问题；

在内存优化、绘制效率优化、IO优化或数据库、电量等调优方面有丰富的经验者优先 布局优化，熟悉多线程、高性能编程及性能调优；

熟悉JS语法，有分析及使用过ReactNative/weex等框架优先

具备插件化，多进程，JNI使用经验者优先考虑

有音视频or跨平台开发经验者优先；

对客户端产品进行性能分析与优化（启动速度、FPS、内存、CPU、流量、电量、安装包大小）；对产品进行性能优化，控制App对内存、CPU、流量、电量的消耗，让用户放心使用你开发的产品
                                             岗位要求
                                             

熟悉Android SDK 

调试工具和方法

有理财、社区、资讯类产品研发经验优先。 

熟悉kotlin，有过kotlin项目经验优先

有微信开放SDK（授权/支付/分享等）经验优先。

有扎实的C++/Java语言基础和Android技术：线程池、消息机制、View的事件体系、View 工作原理、
动画以及Android性能调优经验，熟悉Android平台单元测试和自动化测试工具3、Retrofit、RxJava/rxAndroid 、RxBus/EventBus、Arouter、butterknife、glide/Picasso/Fresco
/、greendao/realm 、zxing
4、极光/个推 、埋点、微信支付、支付宝支付、
5、对webview加载h5 , js 交互有自己独到的见解

android 组件化、插件化、热修复；MVP/MVVM设计模式

熟悉Android平台软件配置管理，能独立完成android平台软件持续集成方案和策略，熟悉android软件编译机制，svn、 git常 用代码管理工具。

有RN, Weex等框架开发经验的加分考虑；

熟练掌握JAVA, 熟悉Android SDK, 熟悉Android的UI/网络/数据库框架； 

熟悉Android系统源码者优先,有NDK 环境下C/C++开发经验者优先；

## 移动开发知识点总结

思维导图：http://naotu.baidu.com/file/82fda7a3ad479fbff45908c463fdcd62?token=8ecbfaab1cc83b72

![总结](https://github.com/66668/Android_Interview/blob/master/pictures/app_all.svg)

![总结](https://github.com/66668/Android_Interview/blob/master/pictures/android_all_node.jpg)



## 数据结构和算法 总结
[数据结构和算法 跳转](https://github.com/66668/Android_Interview/blob/master/lib_structure/README.md);
## java 总结

## android 总结
[android 跳转](https://github.com/66668/Android_Interview/blob/master/lib_android/README.md);

熟悉基本的算法和数据结构，拥有良好的编程习惯和基本功，能独立解决大部分开发中遇到的问题。