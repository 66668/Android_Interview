# android 性能优化-工具篇

## LeakCanary及原理

LeakCanary是Square公司基于MAT开发的一款监控Android内存泄漏的开源框架。

其工作的原理是：
监测机制利用了Java的WeakReference和ReferenceQueue，通过将Activity包装到WeakReference中，被WeakReference包装过的Activity对象如果被回收，
该WeakReference引用会被放到ReferenceQueue中，通过监测ReferenceQueue里面的内容就能检查到Activity是否能够被回收（在ReferenceQueue中说明可以被回收，不存在泄漏；
否则，可能存在泄漏，LeakCanary是执行一遍GC，若还未在ReferenceQueue中，就会认定为泄漏）。如果Activity被认定为泄露了，就抓取内存dump文件(Debug.dumpHprofData)；
之后通过HeapAnalyzerService.runAnalysis进行分析内存文件分析；接着通过HeapAnalyzer (checkForLeak—findLeakingReference---findLeakTrace)来进行内存泄漏分析。
最后通过DisplayLeakService进行内存泄漏的展示。

## Android Lint 工具（as自带）：
Android Lint Tool 是Android Sutido种集成的一个Android代码提示工具，它可以给你布局、代码提供非常强大的帮助。
硬编码会提示以级别警告，例如：在布局文件中写了三层冗余的LinearLayout布局、直接在TextView中写要显示的文字、字体大小使用dp而不是sp为单位，就会在编辑器右边看到提示。

## Profiler 工具（as自带）：
可以监测 cpu memory network energy


## Battery Historian工具（开源go语言）：




