#  Context总结

## 继承关系：
    
      Activity--> ContextThemeWrapper |
                                      |
                 Application          |-->  ContextWrapper  |         
                                      |                     |
                  Service             |                     |
                                                            | --> Context
                                                            |
                                             ContextImpl    |                 

1. 每一个Activity和Service以及Application的Context是一个新的ContextImpl对象
2. Context数量=Activity个数 + Service个数 +1个Application

## Context作用域

| Context作用域 | Activity | Application |Service|
| :----- | :------- | :----- |:----- |
| 弹窗| 是 | 否（1）| 否（1）|
| 启动Act | 是 | 支持（2） |支持（2）|
| Layout Inflation | 是 | 支持 |支持|
| 启动Service | 是 | 是 |是|
| 广播| 是 | 是 |是|
| res| 是 | 是 |是|

1. android不允许Act或dialog凭空产生，只能使用act类型的context，否则出错
2. 用application/service的context去启动act抛异常，是因为非Act的cotnext没有所谓的任务栈。

## getApplication()和getApplicationContext()在作用域的区别
1. getApplication获取的是Application的实例，返回Applicaiton实例，只在Act/service中使用
2. getApplicationContext()在特殊场景获取Application使用的，作用域更大，如广播。

任何一个Context的实例，只要调用getApplicationContext()方法都可以拿到我们的 Application对象。

## ApplicationContext和ActivityContext的区别
1. 第一种中context的生命周期与Application的生命周期相关的，context随着Application的销毁而销毁，伴随application的一生，与activity的生命周期无关.
2. 第二种中的context跟Activity的生命周期是相关的，但是对一个Application来说，Activity可以销毁几 次，那么属于Activity的context就会销毁多次

## 获取Context的方法
1. view.getContext() 
2. Activity.this
3. Activity.getApplcitaionContext() 优先
4. ContextWrapper.getBaseContext()  

## 正确使用Context
1. Application的context能搞定的情况下，且生命周期长的对象，优先使用Application的context
2. 不要让长生命周期对象持有Act的引用
3. 不要再Act中使用非静态内部类，会持有act引用导致内存泄漏

## 怎么在Service中创建Dialog对话框?

1.在我们取得Dialog对象后，需给它设置类型，即:


    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
    
2.在Manifest中加上权限:


    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINOW" />






