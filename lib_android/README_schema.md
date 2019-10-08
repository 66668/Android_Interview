# android Scheme协议跳转 总结


## 什么是 URL Scheme？

    android中的scheme是一种页面内跳转协议，是一种非常好的实现机制，通过定义自己的scheme协议，可以非常方便跳转app中的各个页面；通过scheme协议，服务器可以定制化告诉App跳转那个页面，可以通过通知栏消息定制化跳转页面，可以通过H5页面跳转页面等。
## URL Scheme应用场景

    客户端应用可以向操作系统注册一个 URL scheme，该 scheme 用于从浏览器或其他应用中启动本应用。通过指定的 URL 字段，可以让应用在被调起后直接打开某些特定页面，比如商品详情页、活动详情页等等。也可以执行某些指定动作，如完成支付等。也可以在应用内通过 html 页来直接调用显示 app 内的某个页面。综上URL Scheme使用场景大致分以下几种：

    服务器下发跳转路径，客户端根据服务器下发跳转路径跳转相应的页面
    H5页面点击锚点，根据锚点具体跳转路径APP端跳转具体的页面
    APP端收到服务器端下发的PUSH通知栏消息，根据消息的点击跳转路径跳转相关页面
    APP根据URL跳转到另外一个APP指定页面

## URL Scheme协议格式：

   android的scheme协议下的uri格式：
   
   scheme://host:port/path/queryParameter=queryString

   xl://goods:8888/goodsDetail?goodsId=10011002

通过上面的路径 Scheme、Host、port、path、query全部包含，基本上平时使用路径就是这样子的。

    xl代表该Scheme 协议名称
    goods代表Scheme作用于哪个地址域
    goodsDetail代表Scheme指定的页面
    goodsId代表传递的参数
    8888代表该路径的端口号

## URL Scheme调用方式：
    

1. 网页上

<a href="xl://goods:8888/goodsDetail?goodsId=10011002">打开商品详情</a>
2. 原生调用

  Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("xl://goods:8888/goodsDetail?goodsId=10011002"));
  startActivity(intent);
  
  
 一.唤起外部应用的activity
  1.目标activity必须在其AndroidManifest.xml中配置如下过滤器：
  
        
        <intent-filter>
              <action android:name="android.intent.action.VIEW"/>
              <category android:name="android.intent.category.DEFAULT"/>
              <data android:scheme="artist"
                  android:host="first"
                  android:path="/enter"/>
          </intent-filter>
  
  2.然后启动方式就为：
  
        
        Intent intent = new Intent();
          intent.setData(Uri.parse("artist://first/enter"));
          startActivity(intent);
  
  二.唤起应用内部activity
  
  方式一：唤起外部应用的activity一样的步骤
  
  方式二：
  
  1.目标activity必须在其AndroidManifest.xml中配置如下过滤器：
  
    
     <intent-filter>
          <action android:name="android.intent.action.VIEW"/>
          <category android:name="android.intent.category.DEFAULT"/>
          <category android:name="android.intent.category.BROWSABLE"/>
          <data android:scheme="artist"
              android:host="first"
              android:path="/enter"/>
      </intent-filter>
  
  2.然后启动方式为：
  
    WebView.loadUrl("artist://first/enter");