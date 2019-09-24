# http总结

## 面试点：
1. tcp/ip协议和http协议在哪个层定义的
2. OSI的七层模型,tcp/ip的4层模型是什么
3. 一次完整HTTP请求的过程
4. DNS域名解析原理
5. HTTP和HTTPS的区别
6. HTTP协议报文结构
7. HTTP头域及http的缓存机制
8. Http状态码

## 概念


1. TCP 传输控制协议   Transmission Control Protocol
2. IP 因特网互联协议  Internet Protocol
3. ICMP 控制报文协议 （ping命令使用）
4. IGMP iternet组管理协议
5. ARP 地址解析协议
6. RARP 反向地址转化协议
7. HTTP 超文本传输协议 Hyper Text Transfer Protocol ,适用于从万维网服务器传输超文本，到本地浏览器的传输协议

    历史版本：
    
    1990 www
    
    1991 http 0.9
    
    1996 http 1.0
    
    1999 http 1.1
    
    2000 https
    
    2015 http 2.0
    
8. HTTP 超文本传输协议 端口80  Hyper Text Transfer Protocol 
9. HTTPS  安全的超文本传输协议 端口443 Hyper Text Transfer Protocol over Secure Socket Layer
10. HTTPS中的SSL/TLS协议:

HTTPS = HTTP + SSL/TLS协议

SSL的全称是Secure Sockets Layer，即安全套接层协议，是为网络通信提供安全及数据完整性的一种安全协议。SSL协议在1994年被Netscape发明，后来各个浏览器均支持SSL，其最新的版本是3.0;

TLS的全称是Transport Layer Security，即安全传输层协议，最新版本的TLS建立在SSL 3.0协议规范之上.在理解HTTPS时候,可以把SSL和TLS看做是同一个协议。

11. URI和URL：URL是URI的子集

   RUI:统一资源标识符
   
   URL:统一资源定位符 （用定位的方式，标识一个资源）
   
12. DNS 域名系统 Domain Name System:用于 TCP/IP 网络，它从事将主机名或域名转换为实际 IP 地址的工作

##  OSI参考模型/7层模型()

从底层到应用层：（物理层-->数据链路层）-->网络层-->传输层-->（会话层-->表示层-->应用层）

## TCP/IP 参考模型/4层模型

主机到网络层(比特)-->网络互联层（数据帧）-->传输层（数据包）-->应用层（数据段）

## TCP/IP协议族（了解）

![tcp/ip协议族](https://github.com/66668/Android_Interview/blob/master/pictures/tcpip_01.jpg)

![tcp/ip协议族](https://github.com/66668/Android_Interview/blob/master/pictures/tcpip_02.jpg)

## IP地址/端口号

1. IP地址：为了实现网络中不同终端的通信，每个终端必须有一个唯一的标识--IP地址
2. 端口号：端口号规定为16位，一个IP主机允许有65535个（2^16）端口号：0--1023为系统端口号（不可以乱用）/1024--49152 登记端口号（给第三方应用使用）/49153--65535 短暂端口号（客户进程短暂使用，使用完给其他进程使用）

socket可以使用1024--65535的端口号

通过源IP地址，目标IP地址，协议号，源端口号，目标端口号五个元素，可以确认一个通信

![tcp/ip协议族](https://github.com/66668/Android_Interview/blob/master/pictures/tcpip_02.jpg)

## URL的组成部分
eg: http://www.aspxfans.com:8080/news/index.asp?boardID=5&ID=24618&page=1#r_70732423

一个完整的URL包括：**协议部分、域名部分、端口部分、虚拟目录部分、文件名部分、参数部分、锚部分**

1. 协议部分：该URL的协议部分为“http：”，这代表网页使用的是HTTP协议。在Internet中可以使用多种协议，如HTTP，FTP等等本例中使用的是HTTP协议。在”HTTP”后面的“//”为分隔符

2. 域名部分：该URL的域名部分为“www.aspxfans.com”。一个URL中，也可以使用IP地址作为域名使用

3. 端口部分：跟在域名后面的是端口，域名和端口之间使用“:”作为分隔符。端口不是一个URL必须的部分，如果省略端口部分，将采用默认端口

4. 虚拟目录部分：从域名后的第一个“/”开始到最后一个“/”为止，是虚拟目录部分。虚拟目录也不是一个URL必须的部分。本例中的虚拟目录是“/news/”

5. 文件名部分：从域名后的最后一个“/”开始到“？”为止，是文件名部分，如果没有“?”,则是从域名后的最后一个“/”开始到“#”为止，是文件部分，如果没有“？”和“#”，那么从域名后的最后一个“/”开始到结束，都是文件名部分。本例中的文件名是“index.asp”。文件名部分也不是一个URL必须的部分，如果省略该部分，则使用默认的文件名

7. 参数部分：从“？”开始到“#”为止之间的部分为参数部分，又称搜索部分、查询部分。本例中的参数部分为“boardID=5&ID=24618&page=1”。参数可以允许有多个参数，参数与参数之间用“&”作为分隔符。

6. 锚部分：HTTP请求不包括锚部分，从“#”开始到最后，都是锚部分。本例中的锚部分是“r_70732423“。锚部分也不是一个URL必须的部分。

    锚点作用：打开用户页面时滚动到该锚点位置。如：一个html页面中有一段代码，该url的hash为r_70732423
    
## HTTP请求的传输过程（非面试重点）   


![HTTP请请求的传输过程](https://github.com/66668/Android_Interview/blob/master/pictures/http_01.png)

## 一次完整HTTP请求的过程（面试重点）

1. 域名解析
2. 发起TCP的3次握手
3. 建立TCP连接后发起http请求
4. 服务器端响应http请求，返回响应信息资源
5. TCP的4次挥手
6. 客户端解析服务端发送的数据，展示信息给用户

涉及到的协议:
(1) 应用层：HTTP、DNS

(2) 传输层：TCP、UDP

(3)网络层：IP(IP数据数据包传输和路由选择)，ICMP(提供网络传输过程中的差错检测)，ARP(将本机的默认网关IP地址映射成物理MAC地址)

### DNS域名解析原理：
基本解析流程是：浏览器缓存-->操作系统缓存-->DNS服务器
    
    1. 在浏览器中输入 www.qq.com 域名，操作系统会先检查自己本地的 hosts 文件是否有这个网址映射关系，如果有就先调用这个 IP 地址映射完成域名解析。
    2. 如果 hosts 里没有这个域名的映射，则查找本地 DNS 解析器缓存是否有这个网址映射关系，如果有直接返回，完成域名解析。
    3. 如果 hosts 与本地 DNS 解析器缓存都没有相应的网址映射关系，首先会找 TCP/IP 参数中设置的首选 DNS 服务器，在此我们叫它本地 DNS 服务器，此服务器收到查询时，如果要查询的域名，包含在本地配置区域资源中，则返回解析结果给客户机，完成域名解析，此解析具有权威性。
    4. 如果要查询的域名，不由本地 DNS 服务器区域解析，但该服务器已缓存了此网址映射关系，则调用这个 IP 地址映射，完成域名解析，此解析不具有权威性。
    5. 如果本地 DNS 服务器本地区域文件与缓存解析都失效，则根据本地 DNS 服务器的设置（是否设置转发器）进行查询，如果未用转发模式，本地 DNS 就把请求发至 “根 DNS 服务器”，“根 DNS 服务器”收到请求后会判断这个域名(.com)是谁来授权管理，并会返回一个负责该顶级域名服务器的一个 IP。本地 DNS 服务器收到 IP 信息后，将会联系负责 .com 域的这台服务器。这台负责 .com 域的服务器收到请求后，如果自己无法解析，它就会找一个管理 .com 域的下一级 DNS 服务器地址 (qq.com) 给本地 DNS 服务器。当本地 DNS 服务器收到这个地址后，就会找 qq.com 域服务器，重复上面的动作，进行查询，直至找到 www.qq.com 主机。
    6. 如果用的是转发模式，此 DNS 服务器就会把请求转发至上一级 DNS 服务器，由上一级服务器进行解析，上一级服务器如果不能解析，或找根 DNS 或把转请求转至上上级，以此循环。不管是本地 DNS 服务器用是是转发，还是根提示，最后都是把结果返回给本地 DNS 服务器，由此 DNS 服务器再返回给客户机。
    
### HTTP协议特点和HTTPS的区别

Http协议是超文本传输协议，是一个应用层协议，由请求和响应构成，是一个标准的客户端服务器模型。特点主要有：

    1、简单快速：客户向服务器请求服务时，只需传送请求方法和路径。请求方法常用的有GET、HEAD、POST。每种方法规定了客户与服务器联系的类型不同。由于HTTP协议简单，使得HTTP服务器的程序规模小，因而通信速度很快。
    2、灵活：HTTP允许传输任意类型的数据对象。正在传输的类型由Content-Type加以标记。
    3.无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
    4.无状态：HTTP协议是无状态协议。协议对于请求处理没有记忆能力，下一次请求重新发送传输数据 
        缺点：每次发送请求的时候都需要不断的重新传输数据。   
        优点：server服务器只是作为应答，时效速度比较快。为了解决上述无状态的特性：可以使用cookie和session，来支持客户端和服务端的动态交互。
    5、支持B/S及C/S模式。
    
    https 是 http over ssl(Secure Socket Layer)，简单讲就是 http 的安全版本，在 http 的基础上通过传输加密和身份认证保证了传输过程中的安全性。你通常访问的网站大部分都是 http 的，最简单的方法可以看看网址是以 http:// 开头还是 https:// 开头。
    http 不安全，主要是因为它传输的是明文内容 , 也不对传输双方进行身份验证。只要在数据传输路径的任何一个环节上，都能看到传输的内容，甚至对其进行修改。例如一篇文章”攻下隔壁女生路由器后 , 我都做了些什么” 中，很多攻击的环节，都是通过分析 http 的内容来进行。而在现实生活中呢，你很有可能泄露你的论坛高级会员账号 / 密码，游戏 vip 账号 / 密码，隐私的聊天内容，邮件，在线购物信息，等等。
    https 之所以安全，是因为他利用 ssl/tls 协议传输。
    
    https握手的时候耗性能，建好连接之后就不太耗了。
   
### TCP耗时的三次握手原理和四次挥手，以及为什么三次握手和为什么四次挥手？


# HTTP协议报文结构
报文格式一般是 报文头部+空格+报文主体组成，具体要分请求和响应的报文：

1. 一个HTTP请求报文由四个部分组成：**请求行、请求头、空行、请求数据**

    (1)请求行由 **请求方法字段**+空格+**URL字段**+空格+**HTTP协议版本字段**+回车符+换行符3个字段组成
    

2. 一个HTTP响应报文由四部分组成：**响应行、响应头、空行、响应体**

    (1)响应行一般由**协议版本**、**状态码**及其**描述**组成 比如 HTTP/1.1 200 OK
    
见如下详解：（1）HTTP请求/响应报文 头部结构 （2）http 头域详解

## HTTP请求/响应报文 头部结构

参考 https://blog.csdn.net/ulike_mfy/article/details/79550241

## HTTP请求 报文格式

一个HTTP请求由四个部分组成：**请求行、请求头部、空行、请求数据**

![HTTP请求报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_req_01.png)

### 请求行

请求行由 **请求方法字段**+空格+**URL字段**+空格+**HTTP协议版本字段**+回车符+换行符3个字段组成，它们用空格分隔。比如 GET /data/info.html HTTP/1.1

方法字段就是HTTP使用的请求方法，比如常见的GET/POST

其中HTTP协议版本有两种：HTTP1.0/HTTP1.1 可以这样区别：

HTTP1.0对于每个连接都只能传送一个请求和响应，请求就会关闭，HTTP1.0没有Host字段;而HTTP1.1在同一个连接中可以传送多个请求和响应，多个请求可以重叠和同时进行，HTTP1.1必须有Host字段。

### 请求头部

HTTP客户程序(例如浏览器)，向服务器发送请求的时候必须指明请求类型(一般是GET或者 POST)。如有必要，客户程序还可以选择发送其他的请求头。大多数请求头并不是必需的，但Content-Length除外。对于POST请求来说 Content-Length必须出现。

常见的请求头字段含义：
1. Accept
2. Accept-Charset 可接受字符类型
3. Accept-Encoding 编码方式 
4. Accept-Language
5. Authorization 授权信息
6. Expect
7. From
8. Host 
9. If-Match
10. If-Modifed-Since 只有当所请求的信息在指定的日期以后，又经过修改才返回它，否则返回304（与缓存有关）
11. **If-Unmodifed-Since**
12. **If-None-Match**
13. If-Range
14. Max-Forwards
15. Proxy-Authorization
16. **Range** **面试考点： Range头域可以请求实体的一个或多个子范围，**用于实现断点续传**配合IO流的RandomAccessFile实现
17. Referer
18. TE
19. User-Agent ：发出请求的用户信息


### 空行
它的作用是通过一个空行，告诉服务器请求头部到此为止。

### 请求数据

若方法字段是GET，则此项为空，没有数据

若方法字段是POST,则通常来说此处放置的就是要提交的数据

比如要使用POST方法提交一个表单，其中有user字段中数据为“admin”, password字段为123456，那么这里的请求数据就是 user=admin&password=123456，使用&来连接各个字段。

总的来说，HTTP请求报文格式就如下图所示
![报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_header_01.png)
![报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_header_02.png)

上面是POST方法，它的请求行URL段中一般是没有参数的，参数放在了报文体中。而GET方法的参数直接置于请求行URL中，报文体则为空。

## 响应报文

HTTP响应报文格式就如下图所示
![响应报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_resp_01.png)
![响应报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_header_03.png)

响应报文由三部分组成：**响应行、响应头、空行、响应体**

### 响应行
响应行一般由**协议版本**、**状态码**及其**描述**组成 比如 HTTP/1.1 200 OK

其中协议版本HTTP/1.1或者HTTP/1.0，200就是它的状态码，OK则为它的描述。

//常见状态码：

100~199：表示成功接收请求，要求客户端继续提交下一次请求才能完成整个处理过程。

200~299：表示成功接收请求并已完成整个处理过程。常用200

300~399：为完成请求，客户需进一步细化请求。例如：请求的资源已经移动一个新地址、常用302(意味着你请求我，我让你去找别人),307和304(我不给你这个资源，自己拿缓存)

400~499：客户端的请求有错误，常用404(意味着你请求的资源在web服务器中没有)403(服务器拒绝访问，权限不够)

500~599：服务器端出现错误，常用500

更详细的状态码信息

### 响应头
响应头用于描述服务器的基本信息，以及数据的描述，服务器通过这些数据的描述信息，可以通知客户端如何处理等一会儿它回送的数据。

设置HTTP响应头往往和状态码结合起来。例如，有好几个表示“文档位置已经改变”的状态代码都伴随着一个Location头，而401(Unauthorized)状态代码则必须伴随一个WWW-Authenticate头。然而，即使在没有设置特殊含义的状态代码时，指定应答头也是很有用的。应答头可以用来完成：设置Cookie，指定修改日期，指示浏览器按照指定的间隔刷新页面，声明文档的长度以便利用持久HTTP连接，……等等许多其他任务。

常见的响应头字段含义：

1. Accept-Range
2. Age
3. **Etag** :表示资源是否变化
4. Location 这个头配合302状态码使用，用于重定向接收者到一个新URI地址。表示客户应当到哪里去提取文档。Location通常不是直接设置的，而是通过HttpServletResponse的sendRedirect方法，该方法同时设置状态代码为302。
5. Proxy-Authenticate
6. Server 服务器通过这个头告诉浏览器服务器的类型。Server响应头包含处理请求的原始服务器的软件信息。此域能包含多个产品标识和注释，产品标识一般按照重要性排序。Servlet一般不设置这个值，而是由Web服务器自己设置。
7. Vary
8. WWW-Authenticate 客户应该在Authorization头中提供什么类型的授权信息?在包含401(Unauthorized)状态行的应答中这个头是必需的。例如，response.setHeader(“WWW-Authenticate”, “BASIC realm=\”executives\”“)。注意Servlet一般不进行这方面的处理，而是让Web服务器的专门机制来控制受密码保护页面的访问。
                    
                    注：设置应答头最常用的方法是HttpServletResponse的setHeader，该方法有两个参数，分别表示应答头的名字和值。和设置状态代码相似，设置应答头应该在发送任何文档内容之前进行。
                    
                    setDateHeader方法和setIntHeadr方法专门用来设置包含日期和整数值的应答头，前者避免了把Java时间转换为GMT时间字符串的麻烦，后者则避免了把整数转换为字符串的麻烦。

9. Refresh：告诉浏览器隔多久刷新一次，以秒计。
10. Set-Cookie：设置和页面关联的Cookie。Servlet不应使用response.setHeader(“Set-Cookie”, …)，而是应使用HttpServletResponse提供的专用方法addCookie。

11. setContentType：设置Content-Type头。大多数Servlet都要用到这个方法。

12. setContentLength：设置Content-Length头。对于支持持久HTTP连接的浏览器来说，这个函数是很有用的。

13. addCookie：设置一个Cookie(Servlet API中没有setCookie方法，因为应答往往包含多个Set-Cookie头)。

### 空行
### 响应体
响应体就是响应的消息体，如果是纯数据就是返回纯数据，如果请求的是HTML页面，那么返回的就是HTML代码，如果是JS就是JS代码，如此之类。

## HTTP 头域详解

## POST/GET区别

Http定义了与服务器交互的不同方法，最基本的方法有4种：GET、POST、PUT、DELETE。

而HTTP中的 GET，POST，PUT，DELETE 就对应着对URL资源的 查，改，增，删 4个操作。所以说：GET一般用于获取/查询资源信息，而POST一般用于更新资源信息。

主要区分一下get和post

1. 提交数据形式

GET请求的数据会附在URL之后(就是把数据放置在HTTP协议头中)，会直接展现在地址栏中，以?分割URL和传输数据，参数之间以&相连，如：login.action?name=hyddd&password=idontknow&verify=%E4%BD%A0%E5 %A5%BD。

如果数据是英文字母/数字，原样发送，如果是空格，转换为+，如果是中文/其他字符，则直接把字符串用BASE64加密，

得出如：%E4 %BD%A0%E5%A5%BD，其中%XX中的XX为该符号以16进制表示的ASCII。

而POST方法则会把数据放到请求数据字段中以&分隔各个字段，请求行不包含数据参数，地址栏也不会额外附带参数

2. 提交数据大小

get方法提交数据的大小直接影响到了URL的长度，但HTTP协议规范中其实是没有对URL限制长度的，限制URL长度的是客户端或服务器的支持的不同所影响：
比如IE对URL长度的限制是2083字节(2K+35)。对于其他浏览器，如Netscape、FireFox等，理论上没有长度限制，其限制取决于操作系统的支持。

post方式HTTP协议规范中也没有限定，起限制作用的是服务器的处理程序的处理能力。

所以大小的限制还是得受各个web服务器配置的不同而影响。

3. 提交数据安全

POST比GET方式的安全性要高

通过GET提交数据，用户名和密码将明文出现在URL上，因为一下几个原因get方式安全性会比post弱：

(1)登录页面有可能被浏览器缓存

(2)其他人查看浏览器的历史纪录，那么别人就可 以拿到你的账号和密码了

(3)当遇上跨站的攻击时，安全性的表现更差了



