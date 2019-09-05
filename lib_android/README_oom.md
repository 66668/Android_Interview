# 内存泄漏OOM的分析总结(LMK原理分析)

## 使用as内存分析
https://developer.android.google.cn/studio/profile/memory-profiler

## 分析工具总结：

### 1 as自带-静态代码分析工具 —— Lint
  
1. 位置:Analyze--Inspec Code--选择检测模块，ok即可。

### adb shell 命令检测

使用 adb shell dumpsys meminfo [PackageName]，可以打印出指定包名的应用内存信息
只需要关注Activities和Views两个信息即可，打开act和关闭act，查看act和view的对应关系是否变化异常，判断是否内存泄漏




