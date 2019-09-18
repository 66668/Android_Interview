# HTTP 请求头详解

## 整体头信息
1. Allow
2. Content-Encoding 
3. Content-Language
4. Content-Length
5. Content-Location
6. Content-MD5
7. Content-Range
8. Content-Type
9. Expires 指定缓存到期的GTM绝地时间，从而确认该文档到期而不在缓存它
10. Last-Modified ：(缓存相关)指定服务器上保存内容的最后修改时间，精确到秒，客户端可以通过If-Modifed-Since请求头提供一个时间，该请求将被视作一个条件GET,只有改动时间迟于指定时间的文档才会返回，否则304
11. Extansion-header


## 通用 

1. Cache-Control 缓存策略
2. Connection 是否需要持久连接
3. Date
4. Pragma
5. Trailer
6. Transfer-Encoding
7. Upgrade
8. Via
9. Warning
10. Cookie 

## 请求头特有

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
11. If-Unmodifed-Since
12. If-None-Match
13. If-Range
14. Max-Forwards
15. Proxy-Authorization
16. **Range** **面试考点： Range头域可以请求实体的一个或多个子范围，**用于实现断点续传**配合IO流的RandomAccessFile实现
17. Referer
18. TE
19. User-Agent ：发出请求的用户信息

## 响应头特有
1. Accept-Range
2. Age
3. ETag :表示资源是否变化
4. Location
5. Proxy-Authenticate
6. Server
7. Vary
8. WWW-Authenticate


