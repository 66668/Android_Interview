# WebView 交互 总结


## 为什么WebView加载会慢呢
这是因为在客户端中，加载H5页面之前，需要先初始化WebView，在WebView完全初始化完成之前，后续的界面加载过程都是被阻塞的。

**优化手段**围绕着以下两个点进行:
1. 预加载WebView。 
2. 加载WebView的同时，请求H5页面数据。

因此常见的方法是:

1. 全局WebView。 
2. 客户端代理页面请求。WebView初始化完成后向客户端请求数据。 
3. asset存放离线包。

其他:

4. 脚本执行慢，可以让脚本最后运行，不阻塞页面解析。 
5. DNS链接慢，可以让客户端复用使用的域名与链接。 
6. React框架代码执行慢，可以将这部分代码拆分出来，提前进行解析。

## HybridApp WebView和JS交互
Android与JS通过WebView互相调用方法，实际上是：
Android去调用JS的代码
1. 通过WebView的loadUrl(),使用该方法比较简洁，方便。但是效率比较低，获取返回值比较困难。
2. 通过WebView的evaluateJavascript(),该方法效率高，但是4.4以上的版本才支持，4.4以下版本不支持。所以建议两者混合使用。
JS去调用Android的代码
1. 通过WebView的addJavascriptInterface（）进行对象映射 ，该方法使用简单，仅将Android对象和JS对象映射即可，但是存在比较大的漏洞。
 
漏洞产生原因是：当JS拿到Android这个对象后，就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行。
解决方式：
(1)Google 在Android 4.2 版本中规定对被调用的函数以 @JavascriptInterface进行注解从而避免漏洞攻击。
(2)在Android 4.2版本之前采用拦截prompt（）进行漏洞修复。
 
2. 通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url 。这种方式的优点：不存在方式1的漏洞；缺点：JS获取Android方法的返回值复杂。(ios主要用的是这个方式)
 
(1)Android通过 WebViewClient 的回调方法shouldOverrideUrlLoading ()拦截 url
(2)解析该 url 的协议
(3)如果检测到是预先约定好的协议，就调用相应方法
 
3. 通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
这种方式的优点：不存在方式1的漏洞；缺点：JS获取Android方法的返回值复杂。


