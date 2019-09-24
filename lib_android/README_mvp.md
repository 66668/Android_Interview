# MVC MVP和MVVM框架 原理总结

## MVC框架

简单来说就是通过controller的控制去操作model层的数据，并且返回给view层展示
1. M层：model层，数据操作，网络请求,并通知View改变
2. V层：view层，xml布局，自定义的view,viewGroup,负责将用户的请求通知Controller，并根据model更新界面
3. C层：controller层，activity/fragment层,接收用户请求并更新model

### 缺点：

1. view层和model层是相互可知的，这意味着两层之间存在耦合。
2. 业务增多，c层代码更多，Activity或者Fragment ，我们不能很清晰的区分它是View还是Controller，既有交互又有页面绘制，这就导致了activity和fragment很“庞大”



## MVP框架
解决mvc的缺点：避免了m层和c层的交互，完全解耦。

1. M层：model层，数据操作，网络请求
2. V层：和mvc不同，该v层包括 xml布局，act/fragment
3. P层：连接m层和c层的桥梁，p层和v层通讯通过接口方式，p层拿到处理结果通过接口返回给v层，v层再做UI的操作。

![MVP框架](https://github.com/66668/Android_Interview/blob/master/pictures/mvp_01.png)

## MVVM框架

1. M层：model层，数据操作，网络请求
2. V层： xml布局，act/fragment
3. VM层：viewModel层：实现view层和viewmodel层的双向绑定，databinding

适合中小项目，

### 核心介绍：
在MVVM中有个ViewModel，它的作用就是与View进行双向绑定，当View或者ViewModel有一方变动时，另一方也会跟着改变，其实就是观察者模式，
同时ViewModel也会处理一些轻量的业务逻辑，具有和MVP中的Presenter的一些类似功能。当用户对View进行操作时，ViewModel就会直接收到指令，
然后调用Model处理业务逻辑，当Model返回数据给ViewModel时，因为ViewModel与View双向绑定的缘故，ViewModel接收到数据后，View也会跟着改变，
省去了ViewModel特意调用View来改变View的状态这一步骤

![MVVM框架](https://github.com/66668/Android_Interview/blob/master/pictures/mvvm_01.png)

## 框架使用推荐

MVP 支持中大项目
MVVM 支持中小项目，大项目重构比较难
MVC 建议不用

重构开发老项目，推荐使用MVP+databinding


