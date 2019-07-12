# jetpack系列 总结

官网：https://developer.android.google.cn/jetpack/

接本介绍和详细讲解：https://blog.csdn.net/Alexwll/article/details/83302173

 
Android Jetpack组件的优势：
 
     轻松管理应用程序的生命周期
     构建可观察的数据对象，以便在基础数据库更改时通知视图
     存储在应用程序轮换中未销毁的UI相关数据，在界面重建后恢复数据
     轻松的实现SQLite数据库
     系统自动调度后台任务的执行，优化使用性能
  
google推荐架构：
   
![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/okhttp3_02.png)

上面架构组件的功能如下：

    Activity和Fragment负责产品与用户的交互
    ViewModel作为数据的存储和驱动
    Resposity负责调度数据的获取
    Room储存本地序列化的数据
    Retrofit获取远程数据的数据

