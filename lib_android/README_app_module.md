# android 组件化，模块化开发总结

## 组件化,模块化总结

1. 概念：
组件化：是将一个APP分成多个module，每个module都是一个组件，也可以是一个基础库供组件依赖，开发中可以单独调试部分组件，组件中不需要相互依赖但是可以相互调用，最终发布的时候所有组件以lib的形式被主APP工程依赖打包成一个apk。
2. 由来：

APP版本迭代，新功能不断增加，业务变得复杂，维护成本高
业务耦合度高，代码臃肿，团队内部多人协作开发困难
Android编译代码卡顿，单一工程下代码耦合严重，修改一处需要重新编译打包，耗时耗力。
方便单元测试，单独改一个业务模块，不需要着重关注其他模块。

3. 优势：

组件化将通用模块独立出来，统一管理，以提高复用，将页面拆分为粒度更小的组件，组件内部出了包含UI实现，还可以包含数据层和逻辑层
每个组件度可以独立编译、加快编译速度、独立打包。
每个工程内部的修改，不会影响其他工程。
业务库工程可以快速拆分出来，集成到其他App中。
迭代频繁的业务模块采用组件方式，业务线研发可以互不干扰、提升协作效率，并控制产品质量，加强稳定性。
并行开发，团队成员只关注自己的开发的小模块，降低耦合性，后期维护方便等。

4. 考虑问题：

(1)模式切换：如何使得APP在单独调试跟整体调试自由切换

组件化后的每一个业务的module都可以是一个单独的APP（isModuleRun=false）， release 包的时候各个业务module作为lib依赖，这里完全由一个变量控制，在根项目 gradle.properties里面isModuleRun=true。isModuleRun状态不同，加载application和AndroidManifest都不一样，以此来区分是独立的APK还是lib。
在build.grade里面配置：

![build.grade里面配置](https://github.com/66668/Android_Interview/blob/master/pictures/module_01.png)

(2)资源冲突

当我们创建了多个Module的时候，如何解决相同资源文件名合并的冲突，业务Module和BaseModule资源文件名称重复会产生冲突，解决方案在于：
每个 module 都有 app_name，为了不让资源名重名，在每个组件的 build.gradle 中增加 resourcePrefix “xxx_强行检查资源名称前缀。固定每个组件的资源前缀。但是 resourcePrefix 这个值只能限定 xml 里面的资源，并不能限定图片资源。

（3）依赖关系

多个Module之间如何引用一些共同的library以及工具类

（4）组件通信

组件化之后，Module之间是相互隔离的，如何进行UI跳转以及方法调用，具体可以使用阿里巴巴ARouter或者美团的WMRouter等路由框架。
各业务Module之前不需要任何依赖可以通过路由跳转，完美解决业务之间耦合。

（5）入口参数

我们知道组件之间是有联系的，所以在单独调试的时候如何拿到其它的Module传递过来的参数

（6）Application

当组件单独运行的时候，每个Module自成一个APK，那么就意味着会有多个Application，很显然我们不愿意重复写这么多代码，所以我们只需要定义一个BaseApplication即可，其它的Application直接继承此BaseApplication就OK了，BaseApplication里面还可定义公用的参数。
关于如何进行组件化，可以参考：安居客Android项目架构演进

## 插件化

参考链接：https://www.jianshu.com/p/b6d0586aab9f

1. 概述
提到插件化，就不得不提起方法数超过65535的问题，我们可以通过Dex分包来解决，同时也可以通过使用插件化开发来解决。插件化的概念就是由宿主APP去加载以及运行插件APP。
2. 优点
在一个大的项目里面，为了明确的分工，往往不同的团队负责不同的插件APP，这样分工更加明确。各个模块封装成不同的插件APK，不同模块可以单独编译，提高了开发效率。
解决了上述的方法数超过限制的问题。可以通过上线新的插件来解决线上的BUG，达到“热修复”的效果。
减小了宿主APK的体积。
3. 缺点
插件化开发的APP不能在Google Play上线，也就是没有海外市场。

## 插件化、热修复（思想）的发展历程

2012年7月，AndroidDynamicLoader，大众点评，陶毅敏：思想是通过Fragment以及schema的方式实现的，这是一种可行的技术方案，但是还有限制太多，这意味这你的activity必须通过Fragment去实现，这在activity跳转和灵活性上有一定的不便，在实际的使用中会有一些很奇怪的bug不好解决，总之，这还是一种不是特别完备的动态加载技术。

2013年，23Code，自定义控件的动态下载：主要利用 Java ClassLoader 的原理，可动态加载的内容包括 apk、dex、jar等。

2014年初，Altas，阿里伯奎的技术分享：提出了插件化的思想以及一些思考的问题，相关资料比较少。

2014年底，Dynamic-load-apk，任玉刚：动态加载APK，通过Activity代理的方式给插件Activity添加生命周期。

2015年4月，OpenAltas/ACCD：Altas的开源项目，一款强大的Android非代理动态部署框架，目前已经处于稳定状态。

2015年8月，DroidPlugin，360的张勇：DroidPlugin 是360手机助手在 Android 系统上实现了一种新的插件机制：通过Hook思想来实现，它可以在无需安装、修改的情况下运行APK文件,此机制对改进大型APP的架构，实现多团队协作开发具有一定的好处。

2015年9月，AndFix，阿里：通过NDK的Hook来实现热修复。

2015年11月，Nuwa，大众点评：通过dex分包方案实现热修复。

2015年底，Small，林光亮：打通了宿主与插件之间的资源与代码共享。

2016年4月，ZeusPlugin，掌阅：ZeusPlugin最大特点是：简单易懂，核心类只有6个，类总数只有13个。



