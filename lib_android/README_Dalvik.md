# Android Dalvik 总结

## JVM、 Davilk、ART三者的原理和区别

**JVM和Dalvik虚拟机的区别**

JVM:
1. .java -> javac -> .class -> jar -> .jar

2. 架构: 堆和栈的架构.

DVM:.
1. java -> javac -> .class -> dx.bat -> .dex 
2. 架构: 寄存器(cpu上的一块高速缓存)

## Android2个虚拟机的区别(一个5.0之前，一个5.0之后)
1. Dalvik概念:Dalvik是Google公司自己设计用于Android平台的Java虚拟机。Dalvik虚拟机是Google 等厂商合作开发的Android移动设备平台的核心组成部分之一，它可以支持已转换为.dex(即Dalvik Executable)格式的Java应用程序的运行，.dex格式是专为Dalvik应用设计的一种压缩格式，适合内存和 处理器速度有限的系统。Dalvik经过优化，允许在有限的内存中同时运行多个虚拟机的实例，并且每一 个Dalvik应用作为独立的Linux进程执行。独立的进程可以防止在虚拟机崩溃的时候所有程序都被关 闭。

2. ART概念:Android操作系统已经成熟，Google的Android团队开始将注意力转向一些底层组件，其中 之一是负责应用程序运行的Dalvik运行时。Google开发者已经花了两年时间开发更快执行效率更高更省 电的替代ART运行时。ART代表Android Runtime,其处理应用程序执行的方式完全不同于Dalvik， Dalvik是依靠一个Just-In-Time( JIT)编译器去解释字节码。开发者编译后的应用代码需要通过一个解释器 在用户的设备上运行，这一机制并不高效，但让应用能更容易在不同硬件和架构上运行。ART则完全改 变了这套做法，在应用安装的时候就预编译字节码为机器语言，这一机制叫Ahead-Of-Time(AOT)编 译。在移除解释代码这一过程后，应用程序执行将更有效率，启动更快。

            
            ART优点:
              系统性能的显著提升。
              应用启动更快、运行更快、体验更流畅、触感反馈更及时。
              更长的电池续航能力。
              支持更低的硬件。
                     
            ART缺点: 
            更大的存储空间占用，可能会增加10%-20%。
            更长的应用安装时间。






