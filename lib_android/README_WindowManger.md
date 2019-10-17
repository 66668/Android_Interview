# WindowManager 总结

## 理解Window和WindowManager

1. Window用于显示View和接收各种事件，Window有三种型:应用Window(每个Activity对应一个 Window)、子Widow(不能单独存在，附属于特定Window)、系统window(toast和状态栏)
2. Window分层级，应用Window在1-99、子Window在1000-1999、系统Window在2000- 2999.WindowManager提供了增改View的三个功能。
3. Window是个抽象概念:每一个Window对应着一个ViewRootImpl，Window通过ViewRootImpl来和 View建立联系，View是Window存在的实体，只能通过WindowManager来访问Window。
4. WindowManager的实现是WindowManagerImpl，其再委托WindowManagerGlobal来对Window 进行操作，其中有四种List分别储存对应的View、ViewRootImpl、WindowManger.LayoutParams和 正在被删除的View。
5. Window的实体是存在于远端的WindowMangerService，所以增删改Window在本端是修改上面的几 个List然后通过ViewRootImpl重绘View，通过WindowSession(每Window个对应一个)在远端修改 Window。
6. Activity创建Window:Activity会在attach()中创建Window并设置其回调(onAttachedToWindow()、 dispatchTouchEvent())，Activity的Window是由Policy类创建PhoneWindow实现的。然后通过 Activity#setContentView()调用PhoneWindow的setContentView。