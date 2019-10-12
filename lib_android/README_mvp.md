# MVC MVP和MVVM框架 原理总结

## MVC框架

简单来说就是通过controller的控制去操作model层的数据，并且返回给view层展示
1. M层：model层，数据操作，网络请求,I/O操作，并通知View改变
2. V层：view层，xml布局 自定义的view,viewGroup的动态view部分；负责将用户的请求通知Controller，并根据model更新界面；
3. C层：controller层，activity/fragment层,接收用户请求并更新model,Activity本来主要是作为初始化页面，展示数据的操作，
但是因为XML视图功能太弱，所以Activity既要负责视图的显示又要加入控制逻辑，承担的功能过多。


说明：图有错误，补充：还包括c与v的互相交互
![MVC框架](https://github.com/66668/Android_Interview/blob/master/pictures/mvc_01.png)

### 缺点：

1. model层完全解耦，但controller层和view层并没有解耦，这意味着两层之间存在耦合。
2. 业务增多，c层代码更多，Activity或者Fragment ，我们不能很清晰的区分它是View还是Controller，既有交互又有页面绘制，这就导致了activity和fragment很“庞大”


## MVP框架
解决mvc的缺点：避免了m层和c层的交互，完全解耦。

1. M层：model层，数据操作，网络请求
2. V层：和mvc不同，该v层包括 xml布局，act/fragment，
3. P层：连接m层和c层的桥梁，p层和v层通讯通过接口方式，p层拿到处理结果通过接口返回给v层，v层再做UI的操作。

通过中间层Preseter实现了Model和View的完全解耦，但是随着业务增多，p层的接口会更多更复杂

![MVP框架](https://github.com/66668/Android_Interview/blob/master/pictures/mvp_01.png)


## MVVM框架

1. M层：model层，数据操作，网络请求
2. V层： xml布局，act/fragment
3. VM层：viewModel层：实现view层和viewmodel层的双向绑定，databinding

MVP中我们说过随着业务逻辑的增加，UI的改变多的情况下，会有非常多的跟UI相关的case，这样就会 造成View的接口会很庞大。
而MVVM就解决了这个问题，通过双向绑定的机制，实现数据和UI内容，只 要想改其中一方，另一方都能够及时更新的一种设计理念，
这样就省去了很多在View层中写很多case的情况，只需要改变数据就行。

在MVVM中有个ViewModel，它的作用就是与View进行双向绑定，当View或者ViewModel有一方变动时，另一方也会跟着改变，其实就是观察者模式，
同时ViewModel也会处理一些轻量的业务逻辑，具有和MVP中的Presenter的一些类似功能。当用户对View进行操作时，ViewModel就会直接收到指令，
然后调用Model处理业务逻辑，当Model返回数据给ViewModel时，因为ViewModel与View双向绑定的缘故，ViewModel接收到数据后，View也会跟着改变，
省去了ViewModel特意调用View来改变View的状态这一步骤

看起来MVVM很好的解决了MVC和MVP的不足，但是由于数据和视图的双向绑定，导致出现问题时不太 好定位来源，有可能数据问题导致，也有可能业务逻辑中
对视图属性的修改导致。如果项目中打算用 MVVM的话可以考虑使用官方的架构组件ViewModel、LiveData、DataBinding去实现MVVM。

![MVVM框架](https://github.com/66668/Android_Interview/blob/master/pictures/mvvm_01.png)

**三者如何选择**：

MVP 支持中大项目，对于工具类或者需要写很多业务逻辑app，使用mvp或者mvvm都可。

MVVM 支持中小项目，大项目重构比较难；对于偏向展示型的app，绝大多数业务逻辑都在后端，app主要功能就是展示数据，交互等，建议 使用mvvm。

MVC 建议不用

重构开发老项目，推荐使用MVP+databinding


