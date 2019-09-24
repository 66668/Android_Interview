# Activity的冷启动/热启动总结

## 概念
1. app冷启动： 当应用启动时，后台没有该应用的进程，这时系统会重新创建一个新的进程分配给该应用， 这个启动方式就叫做冷启动（后台不存在该应用进程）。
冷启动因为系统会重新创建一个新的进程分配给它，所以会先创建和初始化Application类，再创建和初始化MainActivity类（包括一系列的测量、布局、绘制），最后显示在界面上。

2. app热启动： 当应用已经被打开， 但是被按下返回键、Home键等按键时回到桌面或者是其他程序的时候，再重新打开该app时， 这个方式叫做热启动（后台已经存在该应用进程）。
热启动因为会从已有的进程中来启动，所以热启动就不会走Application这步了，而是直接走MainActivity（包括一系列的测量、布局、绘制），
所以热启动的过程只需要创建和初始化一个MainActivity就行了，而不必创建和初始化Application

## 冷启动的流程

当点击app的启动图标时，安卓系统会从Zygote进程中fork创建出一个新的进程分配给该应用，之后会依次创建和初始化Application类、创建MainActivity类、
加载主题样式Theme中的windowBackground等属性设置给MainActivity以及配置Activity层级上的一些属性、再inflate布局、当onCreate/onStart/onResume方法都走完了后
最后才进行contentView的measure/layout/draw显示在界面上

##冷启动的生命周期简要流程：

Application构造方法 –> attachBaseContext()–>onCreate –>Activity构造方法 –> onCreate() –> 配置主体中的背景等操作 –>onStart() –> onResume() –> 测量、布局、绘制显示

冷启动的优化主要是视觉上的优化，解决白屏问题，提高用户体验，所以通过上面冷启动的过程。能做的优化如下：

    减少onCreate()方法的工作量
    不要让Application参与业务的操作
    不要在Application进行耗时操作
    不要以静态变量的方式在Application保存数据
    减少布局的复杂度和层级
    减少主线程耗时

## 为什么冷启动会有白屏黑屏问题？

原因在于加载主题样式Theme中的windowBackground等属性设置给MainActivity发生在inflate布局当onCreate/onStart/onResume方法之前，
而windowBackground背景被设置成了白色或者黑色，所以我们进入app的第一个界面的时候会造成先白屏或黑屏一下再进入界面。解决思路如下

1.给他设置windowBackground背景跟启动页的背景相同，如果你的启动页是张图片那么可以直接给windowBackground这个属性设置该图片那么就不会有一闪的效果了


        <style name=``"Splash_Theme"` `parent=``"@android:style/Theme.NoTitleBar"``>`
            <item name=``"android:windowBackground"``>@drawable/splash_bg</item>`
            <item name=``"android:windowNoTitle"``>``true``</item>`
        </style>`

2.采用世面的处理方法，设置背景是透明的，给人一种延迟启动的感觉。,将背景颜色设置为透明色,这样当用户点击桌面APP图片的时候，并不会"立即"进入APP，而且在桌面上停留一会，其实这时候APP已经是启动的了，只是我们心机的把Theme里的windowBackground的颜色设置成透明的，强行把锅甩给了手机应用厂商（手机反应太慢了啦）


    <style name=``"Splash_Theme"` `parent=``"@android:style/Theme.NoTitleBar"``>`
        <item name=``"android:windowIsTranslucent"``>``true``</item>`
        <item name=``"android:windowNoTitle"``>``true``</item>`
    </style>`

3.以上两种方法是在视觉上显得更快，但其实只是一种表象，让应用启动的更快，有一种思路，将Application中的不必要的初始化动作实现懒加载，比如，在SpashActivity显示后再发送消息到Application，去初始化，这样可以将初始化的动作放在后边，缩短应用启动到用户看到界面的时间

