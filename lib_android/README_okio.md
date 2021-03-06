# okio 总结

## 1.基本特性
1. 紧凑的封装 是对Java IO/NIO 的一个非常优秀的封装，绝对的“短小精焊”，不仅支持文件读写，也支持Socket通信的读写。
2. 使用简单 不用区分字符流或者字节流，也不用记住各种不同的输入/输出流，统统只有一个输入流Source和一个输出流Sink。
3. API丰富 其封装了大量的API接口用于读/写字节或者一行文本
4. 读写速度快 这得益于其优秀的缓冲机制和处理内存的技巧，使I/O在缓冲区得到更高的复用处理，从而尽量减少I/O的实际发生。
## 2.支撑机制
与这些特性相比，就是其有强大的保障机制保驾护航
1. 超时机制 在读/写时增加了超时机制，且有同步与异步之分。
2. 缓冲机制 读/写都是基于缓冲来来实现，尽量减少实际的I/O。
3. 压缩机制 写数据时，会对缓冲的每个Segment进行压缩，避免空间的浪费。当然，这是其内部的优化技巧，提高内存利用率。
4. 共享机制 主要是针对 Segment 而言的，对于不同的 buffer 可以共享同一个 Segment。这也是其内部的优化技巧。
## 3.两个核心基础类
然而，在正式分析之前有两个核心基础类ByteString和Buffer和两个核心API类需要提前理解一下，因为大量的API都是以这4个类为基础来实现的。了解它们，以便有助于后面的分析。
ByteString 是一个不可变的字节序列。对于字符数据，String是基础，ByteString则是String失散多年的好兄弟。其可以很容易地将二进制数据视为一个值来处理。如用十六进制，base64，和UTF-8来进行编码和解码。
Buffer是一个可变的字节序列。就像ArrayList一样，可以进行灵活的访问，插入与移除，完全不需要自己去动手管理。
这两个类也是上面机制的实现，正是上面机制的实现，才使得该库以最少的实际IO来实现快速的IO需求。

#知识点补充-BIO/NIO/AIO

## 同步阻塞IO（BIO）
我们熟知的Socket编程就是BIO，一个socket连接一个处理线程（这个线程负责这个Socket连接的一系列数据传输操作）。
阻塞的原因在于：操作系统允许的线程数量是有限的，多个socket申请与服务端建立连接时，服务端不能提供相应数量的处理线程，
没有分配到处理线程的连接就会阻塞等待或被拒绝。

## 同步非阻塞IO（NIO）
New IO是对BIO的改进，基于Reactor模型。我们知道，一个socket连接只有在特点时候才会发生数据传输IO操作，大部分时间这个“数据通道”是空闲的，
但还是占用着线程。NIO作出的改进就是“一个请求一个线程”，在连接到服务端的众多socket中，只有需要进行IO操作的才能获取服务端的处理线程进行IO。
这样就不会因为线程不够用而限制了socket的接入。客户端的socket连接到服务端时，就会在事件分离器注册一个 IO请求事件 和 IO 事件处理器。
在该连接发生IO请求时，IO事件处理器就会启动一个线程来处理这个IO请求，不断尝试获取系统的IO的使用权限，一旦成功（即：可以进行IO），
则通知这个socket进行IO数据传输。

NIO还提供了两个新概念：**Buffer和Channel**

Buffer:

1. – 是一块连续的内存块。
2. – 是 NIO 数据读或写的中转地。

Channel:

1. – 数据的源头或者数据的目的地
2. – 用于向 buffer 提供数据或者读取 buffer 数据 ,buffer 对象的唯一接口。
3. – 异步 I/O 支持

Buffer作为IO流中数据的缓冲区，而Channel则作为socket的IO流与Buffer的传输通道。客户端socket与服务端socket之间的IO传输不直接把数据交给CPU使用，
而是先经过Channel通道把数据保存到Buffer，然后CPU直接从Buffer区读写数据，一次可以读写更多的内容。
使用Buffer提高IO效率的原因（这里与IO流里面的BufferedXXStream、BufferedReader、BufferedWriter提高性能的原理一样）：
IO的耗时主要花在数据传输的路上，普通的IO是一个字节一个字节地传输，
而采用了Buffer的话，通过Buffer封装的方法（比如一次读一行，则以行为单位传输而不是一个字节一次进行传输）就可以实现“一大块字节”的传输。
比如：IO就是送快递，普通IO是一个快递跑一趟，采用了Buffer的IO就是一车跑一趟。很明显，buffer效率更高，花在传输路上的时间大大缩短。
 
## 异步阻塞IO（AIO）

NIO是同步的IO，是因为程序需要IO操作时，必须获得了IO权限后亲自进行IO操作才能进行下一步操作。
AIO是对NIO的改进（所以AIO又叫NIO.2），它是基于Proactor模型的。每个socket连接在事件分离器注册 IO完成事件 和 IO完成事件处理器。
程序需要进行IO时，向分离器发出IO请求并把所用的Buffer区域告知分离器，分离器通知操作系统进行IO操作，
操作系统自己不断尝试获取IO权限并进行IO操作（数据保存在Buffer区），操作完成后通知分离器；分离器检测到 IO完成事件，
则激活 IO完成事件处理器，处理器会通知程序说“IO已完成”，程序知道后就直接从Buffer区进行数据的读写。


### 总结： 
1. AIO:发出IO请求后，由操作系统自己去获取IO权限并进行IO操作；
2. NIO:发出IO请求后，由线程不断尝试获取IO权限，获取到后通知应用程序自己进行IO操作
 