# SurfaceView 总结

SurfaceView中采用了双缓冲机制，保证了UI界面的流畅性，同时SurfaceView不在主线程中绘制，而是另开辟一个线程去绘制，所以它不妨碍UI线程；

1. SurfaceView继承于View，他和View主要有以下三点区别：
（1）View底层没有双缓冲机制，SurfaceView有；
（2）view主要适用于主动更新，而SurfaceView适用与被动的更新，如频繁的刷新
（3）view会在主线程中去更新UI，而SurfaceView则在子线程中刷新；

SurfaceView的内容不在应用窗口上，所以不能使用变换（平移、缩放、旋转等）。也难以放在ListView或者ScrollView中，不能使用UI控件的一些特性比如View.setAlpha()

SurfaceView是基于view视图进行拓展的视图类，更适合2D游戏的开发；是view的子类，类似使用双缓机制，在新的线程中更新画面所以刷新界面速度比view快，
Camera预览界面使用SurfaceView。

2. View：显示视图，内置画布，提供图形绘制函数、触屏事件、按键事件函数等；必须在UI主线程内更新画面，速度较慢。


3. GLSurfaceView：基于SurfaceView视图再次进行拓展的视图类，专用于3D游戏开发的视图；是SurfaceView的子类，openGL专用。



