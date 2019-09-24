# HTTP 头域 详解和http的缓存机制和原理

HTTP的头域包括**通用头，请求头，响应头，实体头**四个部分

面试点(okhttp中使用的http缓存机制和原理)：

**Expires** 指定缓存到期的GTM绝地时间，从而确认该文档到期而不在缓存它
    
**Cache-Control**  缓存策略

**Last-Modified** ：(缓存相关)指定服务器上保存内容的最后修改时间，精确到秒，客户端可以通过If-Modifed-Since请求头提供一个时间，该请求将被视作一个条件GET,只有改动时间迟于指定时间的文档才会返回，否则304

**If-Unmodifed-Since** (缓存相关)

**Etag** :表示资源是否变化

**If-None-Match** 将Etag的值传递给服务端，用于对比Etag的值是否相等，判断资源是否变化

**Range** Range头域可以请求实体的一个或多个子范围，**用于实现断点续传**配合IO流的RandomAccessFile实现


## 1. 实体头
请求消息和响应消息都可以包含实体信息，实体信息一般由实体头域和实体组成。实体头域包含关于实体的原信息，实体头包括Allow、Content- Base、Content-Encoding、Content-Language、 Content-Length、Content-Location、Content-MD5、Content-Range、Content-Type、 Etag、Expires、Last-Modified、extension-header。extension-header允许客户端定义新的实体头，但是这些域可能无法未接受方识别。实体可以是一个经过编码的字节流，它的编码方式由Content-Encoding或Content-Type定义，它的长度由Content-Length或Content-Range定义。

1. Allow 服务器支持哪些请求方法(如GET、POST等)。
2. Content-Encoding  文档的编码(Encode)方法。只有在解码之后才可以得到Content-Type头指定的内容类型。利用gzip压缩文档能够显著地减少HTML文档的下载时间。Java的GZIPOutputStream可以很方便地进行gzip压缩，但只有Unix上的Netscape和Windows上的IE4、IE5才支持它。因此，Servlet应该通过查看Accept-Encoding头(即request.getHeader(“Accept- Encoding”))检查浏览器是否支持gzip，为支持gzip的浏览器返回经gzip压缩的HTML页面，为其他浏览器返回普通页面。
3. Content-Language
4. Content-Length 表示内容长度。只有当浏览器使用持久HTTP连接时才需要这个数据。如果你想要利用持久连接的优势，可以把输出文档写入 ByteArrayOutputStram，完成后查看其大小，然后把该值放入Content-Length头，最后通过byteArrayStream.writeTo(response.getOutputStream()发送内容。

5. Content-Location
6. Content-MD5
7. Content-Range :用于指定整个实体中的一部分的插入位置，他也指示了整个实体的长度。在服务器向客户返回一个部分响应，它必须描述响应覆盖的范围和整个实体长度。一般格式：

                 Content-Range:bytes-unitSPfirst-byte-pos-last-byte-pos/entity-legth

                 例如，传送头500个字节次字段的形式：Content-Range:bytes0- 499/1234，如果一个http消息包含此节(例如，对范围请求的响应或对一系列范围的重叠请求)，Content-Range表示传送的范围， Content-Length表示实际传送的字节数。

8. Content-Type 表示后面的文档属于什么MIME类型。Servlet默认为text/plain，但通常需要显式地指定为text/html。由于经常要设置 Content-Type，因此HttpServletResponse提供了一个专用的方法setContentType。
9. **Expires** 指定缓存到期的GTM绝地时间，从而确认该文档到期而不在缓存它 告诉浏览器把回送的资源缓存多长时间，-1或0则是不缓存。
10. **Last-Modified** ：(缓存相关)指定服务器上保存内容的最后修改时间，精确到秒，客户端可以通过If-Modifed-Since请求头提供一个时间，该请求将被视作一个条件GET,只有改动时间迟于指定时间的文档才会返回，否则304
    文档的最后改动时间。客户可以通过If-Modified-Since请求头提供一个日期，该请求将被视为一个条件GET，只有改动时间迟于指定时间的文档才会返回，否则返回一个304(Not Modified)状态。Last-Modified也可用setDateHeader方法来设置。
11. Extansion-header
12. Content-Disposition：告诉浏览器以下载方式打开数据。




## 2. 通用头 

通用头域包含请求和响应消息都支持的头域，通用头域包含Cache-Control、 Connection、Date、Pragma、Transfer-Encoding、Upgrade、Via。对通用头域的扩展要求通讯双方都支持此扩展，如果存在不支持的通用头域，一般将会作为实体头域处理。下面简单介绍几个通用头域。

1. **Cache-Control**  指定请求和响应遵循的缓存机制。在请求消息或响应消息中设置 Cache-Control并不会修改另一个消息处理过程中的缓存处理过程。请求时的缓存指令包括no-cache、no-store、max-age、 max-stale、min-fresh、only-if-cached，响应消息中的指令包括public、private、no-cache、no- store、no-transform、must-revalidate、proxy-revalidate、max-age。各个消息中的指令含义如下：
                      
                      Public指示响应可被任何缓存区缓存。 Private指示对于单个用户的整个或部分响应消息，不能被共享缓存处理。这允许服务器仅仅描述当用户的部分响应消息，此响应消息对于其他用户的请求无效。 no-cache指示请求或响应消息不能缓存 no-store用于防止重要的信息被无意的发布。在请求消息中发送将使得请求和响应消息都不使用缓存。 max-age指示客户机可以接收生存期不大于指定时间(以秒为单位)的响应。 min-fresh指示客户机可以接收响应时间小于当前时间加上指定时间的响应。 max-stale指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可以接收超出超时期指定值之内的响应消息。
2. Connection 处理完这次请求后是否断开连接还是继续保持连接。如果Servlet看到这里的值为“Keep- Alive”，或者看到请求使用的是HTTP 1.1(HTTP 1.1默认进行持久连接)，它就可以利用持久连接的优点，当页面包含多个元素时(例如Applet，图片)，显著地减少下载所需要的时间。要实现这一点，Servlet需要在应答中发送一个Content-Length头，最简单的实现方法是：先把内容写入 ByteArrayOutputStream，然后在正式写出内容之前计算它的大小。
3. Date 当前的GMT时间（世界标准时间），例如，Date:Mon,31Dec200104:25:57GMT。Date描述的时间表示世界标准时，换算成本地时间，需要知道用户所在的时区。你可以用setDateHeader来设置这个头以避免转换时间格式的麻烦。
4. Pragma 用来包含实现特定的指令，最常用的是Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache-Control:no-cache相同。指定“no-cache”值表示服务器必须返回一个刷新后的文档，即使它是代理服务器而且已经有了页面的本地拷贝。
5. Trailer
6. Transfer-Encoding 告诉浏览器数据的传送格式。
7. Upgrade
8. Via
9. Warning
10. Cookie 客户机通过这个头可以向服务器带数据，这是最重要的请求头信息之一。

## 3. 请求头

1. Accept  浏览器可接受的MIME类型。
2. Accept-Charset 浏览器可接受的字符集。
3. Accept-Encoding 浏览器能够进行解码的数据编码方式，比如gzip。Servlet能够向支持gzip的浏览器返回经gzip编码的HTML页面。许多情形下这可以减少5到10倍的下载时间。
4. Accept-Language 浏览器所希望的语言种类，当服务器能够提供一种以上的语言版本时要用到。
5. Authorization 授权信息，通常出现在对服务器发送的WWW-Authenticate头的应答中。
6. Expect
7. From  请求发送者的email地址，由一些特殊的Web客户程序使用，浏览器不会用到它。
8. Host  客户机通过这个头告诉服务器，想访问的主机名。Host头域指定请求资源的Intenet主机和端口号，必须表示请求url的原始服务器或网关的位置。HTTP/1.1请求必须包含主机头域，否则系统会以400状态码返回。
9. If-Match
10. **If-Modifed-Since** 客户机通过这个头告诉服务器，资源的缓存时间。只有当所请求的内容在指定的时间后又经过修改才返回它，否则返回304“Not Modified”应答（与缓存有关）
11. If-Unmodifed-Since
12. **If-None-Match** 
13. If-Range
14. Max-Forwards
15. Proxy-Authorization
16. **Range** **面试考点： Range头域可以请求实体的一个或多个子范围，**用于实现断点续传**配合IO流的RandomAccessFile实现

    Range头域可以请求实体的一个或者多个子范围。例如，

        表示头500个字节：bytes=0-499

        表示第二个500字节：bytes=500-999

        表示最后500个字节：bytes=-500

        表示500字节以后的范围：bytes=500-

        第一个和最后一个字节：bytes=0-0,-1

    同时指定几个范围：bytes=500-600,601-999

    但是服务器可以忽略此请求头，如果无条件GET包含Range请求头，响应会以状态码206(PartialContent)返回而不是以200 (OK)。

17. Referer 客户机通过这个头告诉服务器，它是从哪个资源来访问服务器的(防盗链)。包含一个URL，用户从该URL代表的页面出发访问当前请求的页面。
18. TE
19. User-Agent ：User-Agent头域的内容包含发出请求的用户信息。浏览器类型，如果Servlet返回的内容与浏览器类型有关则该值非常有用。
20. Content-Length：表示请求消息正文的长度。
21. UA-Pixels，UA-Color，UA-OS，UA-CPU：由某些版本的IE浏览器所发送的非标准的请求头，表示屏幕大小、颜色深度、操作系统和CPU类型。

## 4. 响应头

1. Accept-Range
2. Age
3. **Etag** :表示当前资源在服务器的唯一标识（服务器决定etag值） （缓存相关）
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


## HTTP 缓存机制和原理（面试重点，和okhttp结合）
流程图简述：
1.  当第一次请求时，先判断缓存是否有数据，无，请求服务器，服务器返回数据，并按照缓存规则缓存数据。
2. 当第二次请求时，判断出缓存中有数据，而且按照规则没有过期，则直接使用缓存数据。
3. 再次请求时，发现本地缓存过期，则发送请求，如果请求中有Etag,则，请求中带 If-None-Match请求服务器，判断Etag是否变化，没有变化就返回304，使用本地资源，否则返回数据。
4. 上次的请求中，如果没有Etag,  但是有Last-Modified，请求中带If-Modified-Since,比较Last-Modified的时间是否改动，没有改动过就返回304，使用本地资源，否则返回数据。                               
5. 缓存中要用到两对头信息  Etag+If-None-Match, Last-Modified+If-Modified-Since。都是做缓存处理使用的。

![HTTP请求报文格式](https://github.com/66668/Android_Interview/blob/master/pictures/http_cache_01.png)