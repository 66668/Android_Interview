# android 总结
https://blog.csdn.net/tgbus18990140382/article/details/88025363


//基础

作为一个Android开发，这方面可谓是必须掌握的，也是个十分宽泛的话题了，还是简单来说一下吧：

了解一个Android App工程的组成，了解每个目录都代表了什么（这是基础中的基础，你去面试Android开发，不可能连这个都不懂的）。

Activity：了解基本的生命周期，以及每个周期到底对用户和开发者意味着什么。如果能从源码级别了解Activity那就更好了。熟悉页面之间的跳转，数据传递，要是还能带上动画那就很出彩了。

Service：了解Service的生命周期和启动流程，知道什么情况下使用Service，跟Activity之间的数据交互。

ContentProvider：知道ContentProvider的原理，知道简单的使用。

BroadcastReceiver：知道如何发广播和接收广播，了解动态与静态注册广播的区别，懂得使用本地广播。

Fragment：熟悉Fragment的生命周期，与父Activity的关系，彼此的数据传递。

Layout：LinearLayout、FrameLayout、RelativeLayout和ConstraintLayout，知道它们的区别，清楚适合它们的应用场景。在完成符合产品原型与UI设计的布局同时，还要做到屏幕适配。

Wigdet：TextView、ImageView、EditText、ScrollView、ListView、RecyclerView、Button等，最好能够做到自己实现一个高性能的View。

第三方SDK：要熟悉一些几乎是“必备”的第三方SDK的集成和使用，比如支付宝和微信支付，极光或友盟的推送，友盟、腾讯等的统计与BUG分析，百度或高德地图，第三方分享（微信、QQ、微博等），IM即时通讯（如环信、融云等）。其中有的SDK不仅要会使用，还要做到一些自定义。

开源库：RxJava（非常重要，能让我们在开发时实现响应式编程，避免无穷无尽的缩进，十分推荐深入研究源码。我一看到简历上写着熟悉使用AsyncTask实现异步的就很烦……）、Retrofit+OkHttp（非常重要，现在你如果还在用Async-Http-Client或者Volley都根本不好意思跟人说话，尽管后两者也能实现功能需求）、ButterKnife、图片加载（Glide、Picasso、Fresco等总得熟悉一种）、数据库（GreenDao、Realm、Ormlite等，自己写SQLite还是麻烦了点）、Json解析（Gson、FastJson、Jackson、LoganSquare等）、性能分析（LeakCanary、BlockCanary等）。

架构：MVC、MVP、MVVM、Android Flux等，不管你选择哪个，基本要求是写出来的App架构清晰、别人理解起来容易。

工具：Android Studio这个IDE是不用多说了，不仅能开发，而且还进行性能分析等（如果你还在用Eclipse，那就当我啥都没说吧……）。最常见的团队代码管理Git也要熟练使用

