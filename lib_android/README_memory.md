# Android进程内存管理

## 进程的地址空间

在32位操作系统中，进程的地址空间为0到4GB，

示意图如下：

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/memory_01.png)

这里主要说明一下Stack和Heap：

**Stack空间**（进栈和出栈）由操作系统控制，其中主要存储函数地址、函数参数、局部变量等等，所以Stack空间不需要很大，一般为几MB大小。

**Heap空间**的使用由程序员控制，程序员可以使用malloc、new、free、delete等函数调用来操作这片地址空间。Heap为程序完成各种复杂任务提供内存空间，所以空间比较大，一般为几百MB到几GB。正是因为Heap空间由程序员管理，所以容易出现使用不当导致严重问题。


## 进程内存空间和RAM之间的关系

**进程的内存空间**只是虚拟内存（或者叫作逻辑内存），而程序的运行需要的是实实在在的内存，即物理内存（RAM）。在必要时，操作系统会将程序运行中申请的内存（虚拟内存）映射到RAM，让进程能够使用物理内存。

**RAM**作为进程运行不可或缺的资源，对系统性能和稳定性有着决定性影响。另外，RAM的一部分被操作系统留作他用，比如显存等等，内存映射和显存等都是由操作系统控制，我们也不必过多地关注它，进程所操作的空间都是虚拟地址空间，无法直接操作RAM。

示意图如下：


![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/memory_02.png)

## Android中的进程

### Native进程：
采用C/C++实现，不包含Dalvik实例的Linux进程，/system/bin/目录下面的程序文件运行后都是以native进程形式存在的。上图 /system/bin/surfaceflinger、/system/bin/rild、procrank等就是Native进程。

### Java进程：
实例化了Dalvik虚拟机实例的Linux进程，进程的入口main函数为Java函数。Dalvik虚拟机实例的宿主进程是fork()系统调用创建的Linux进程，所以每一个Android上的Java进程实际上就是一个Linux进程，只是进程中多了一个Dalvik虚拟机实例。因此，Java进程的内存分配比Native进程复杂。下图，Android系统中的应用程序基本都是Java进程，如桌面、电话、联系人、状态栏等等。

## Android中进程的堆内存

进程空间中的Heap空间是我们需要重点关注的。Heap空间完全由程序员控制，我们使用的C Malloc、C++ new和Java new所申请的空间都是Heap空间， C/C++申请的内存空间在Native Heap中，而Java申请的内存空间则在Dalvik Heap中。

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/memory_03.png)

## Android的 Java程序为什么容易出现OOM

这个是因为Android系统对Dalvik的VM Heapsize作了硬性限制，当java进程申请的Java空间超过阈值时，就会抛出OOM异常（这个阈值可以是48M、24M、16M等，视机型而定），可以通过adb shell getprop | grep dalvik.vm.heapgrowthlimit查看此值。

也就是说，程序发生OOM并不表示RAM不足，而是因为程序申请的Java Heap对象超过了Dalvik VM Heap Growth Limit。也就是说，在RAM充足的情况下，也可能发生OOM。

### 这样的设计似乎有些不合理，但是Google为什么这样做呢？
这样设计的目的是为了让Android系统能同时让比较多的进程常驻内存，这样程序启动时就不用每次都重新加载到内存，能够给用户更快的响应。迫使每个应用程序使用较小的内存，移动设备非常有限的RAM就能使比较多的App常驻其中。但是有一些大型应用程序是无法忍受VM Heap Growth Limit的限制的。

## Android如何应对RAM不足

Java程序发生OMM并不是表示RAM不足，如果RAM真的不足，会发生什么呢？这时Android的Memory Killer会起作用，当RAM所剩不多时，Memory Killer会杀死一些优先级比较低的进程来释放物理内存，让高优先级程序得到更多的内存。对于一些大型的应用程序（比如游戏），内存使用会比较多，很容易超超出VM Heapsize的限制，这时怎么保证程序不会因为OOM而崩溃呢？

1. 创建子进程

创建一个新的进程，那么我们就可以把一些对象分配到新进程的Heap上了，从而达到一个应用程序使用更多的内存的目的，当然，创建子进程会增加系统开销，而且并不是所有应用程序都适合这样做，视需求而定。

创建子进程的方法：使用android:process标签

2. 使用JNI在Native Heap上申请空间（推荐使用）

Native Heap的增长并不受Dalvik VM heapsize的限制，它的Native Heap Size已经远远超过了Dalvik Heap Size的限制。

只要RAM有剩余空间，程序员可以一直在Native Heap上申请空间，当然如果RAM快耗尽，Memory Killer会杀进程释放RAM。大家使用一些软件时，有时候会闪退，就可能是软件在Native层申请了比较多的内存导致的。比如，我就碰到过UC Web在浏览内容比较多的网页时闪退，原因就是其Native Heap增长到比较大的值，占用了大量的RAM，被Memory Killer杀掉了。

3. 使用显存（操作系统预留RAM的一部分作为显存）

使用OpenGL Textures等API，Texture Memory不受Dalvik VM Heapsize限制。再比如Android中的GraphicBufferAllocator申请的内存就是显存。

## Bitmap分配在Native Heap还是Dalvik Heap上？

一种流行的观点是这样的：

Bitmap是JNI层创建的，所以它应该是分配到Native Heap上，并且为了解释Bitmap容易导致OOM，提出了这样的观点：native heap size + dalvik heapsize <= dalvik vm heapsize。但是Native Heap Size远远超过Dalvik VM Heap Size，所以，事实证明以上观点是不正确的。

正确的观点：

大家都知道，过多地创建Bitmap会导致OOM异常，且Native Heap Size不受Dalvik限制，所以可以得出结论：

**Bitmap只能是分配在Dalvik Heap上的，因为只有这样才能解释Bitmap容易导致OOM**。
