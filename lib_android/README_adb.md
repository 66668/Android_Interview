# adb 总结

ADB，即 Android Debug Bridge，它存在于 sdk/platform-tools 目录下


1. 查看当前连接设备：adb devices
2. 如果发现多个设备: adb -s 设备号 其他指令
3. 日志： adb logcat -v time |grep -s TAG
4. 安装/卸载apk： adb install/uninstall 包名
5. 向手机拷贝文件： adb push 电脑文件 手机位置
6. 从手机拷贝文件 adb pull 手机文件位置 电脑位置
7. 所有app包名: adb shell pm list packages
8. 启动Activity: adb shell am start 包名/完整Activity路径 -e 参数 值
9. 启动服务: adb shell am startservice "com.zhy.aaa/com.zhy.aaa.MyService"
10. 屏幕截图 : adb shell screencap /sdcard/screen.png
11. 录制视频: adb shell screenrecord /sdcard/demo.mp4
12. 使用adb shell input命令向屏幕输入一些信息: adb shell input text "insert%stext%shere" 
13. 点击事件: adb shell input tap 500 1450
14. 滑动事件: adb shell input swipe 100 500 100 1450 100 :表示从（100 500）到（100 1450）耗时100ms
15. 长按：adb shell input swipe 100 500 100 500 500
16. 实体按钮的命令: adb shell input keyevent 25
17. act运行信息： adb shell dumpsys activity | grep -i 'run'
18. Wifi连接设备

    当usb接口被别的占用时，可以用wifi连接设备，打印log步骤：
    
    adb tcpip 5555
    
    adb shell
    
    netcfg  
    
    (wlan0    UP                              192.168.100.29/24  0x00001043 68:9c:5f:复制192.168.100.29)
    
    exit
    
    adb connect 192.168.100.29：5555
    
    如果 连接失败直接
    
    adb connect 192.168.100.29
    
    DM上命令行设置
    
    setprop service.adb.tcp.port 5555
    
    stop adbd 
    
    start adbd 
    
    电脑端命令
    
    adb connect 192.168.xx.xx：5555 (ip地址请查看dm)
19. cpu查看
    
    **方式1:top命令行**
    
        
        
             第一组数据的含义：
                User  处于用户态的运行时间，不包含优先值为负进程 
                Nice  优先值为负的进程所占用的CPU时间 
                Sys   处于核心态的运行时间 
                Idle  除IO等待时间以外的其它等待时间 
                IOW   IO等待时间 
                IRQ   硬中断时间 
                SIRQ  软中断时间 
                
                （1）adb shell 后，使用top命令即可
                （2）信息过多，添加过滤：
                （-t 显示进程名称，-s 按指定行排序，-n 在退出前刷新几次，-d 刷新间隔，-m 显示最大数量）
                
                eg:查看前10的cpu情况
                top -m 10 -s cpu
                eg：筛选出指定的应用cpu
                adb shell 
                top -m 10 -n 1 -s cpu
                
  **方式2:dumpsys 命令**
  
            
            
              1 dumpsys meminfo 显示内存信息  （内存泄漏分析）
              2 dumpsys cpuinfo 显示CPU信息  （cpu占用）
              3 dumpsys account 显示accounts信息  
              4 dumpsys activity 显示所有的activities的信息  
              5 dumpsys window 显示键盘，窗口和它们的关系  
              6 dumpsys wifi 显示wifi信息  
              
              
              需要root权限,adb shell后，执行dumpsys cpuinfo即可

  
  
  **方式3 as profiler /cpu Monitor**
  
  **方式4  adb shell procrank**
  
   VSS - Virtual Set Size 虚拟耗用内存（包括共享库占用的内存）
   RSS - Resident Set Size 实际使用物理内存（包括共享库占用的内存）
   PSS - Proportional Set Size 实际使用的物理内存（比例分配共享库占用的内存）
   USS - Unique Set Size 进程独自占用的物理内存（不包括共享库占用的内存）
     
20. adb-logcat
    
    常用命令：
    
    (1)保存到指定文件
    adb logcat -v time >/Users/sjy/Desktop/logcat.txt
    或 adb logcat -f 文件名
    (2)显示某一TAG的日志信息：
    
    adb logcat -s TAG名称
    adb logcat -v time |grep -s 你的tag名
    Eg：adb logcat -v time |grep -s SJY
    (3) 显示某一TAG的某一级别的日志信息：
    adb logcat TAG名称:级别.....TAG名称：级别 *:S
    adb logcat ActivityManager:I MyApp:D *:S
    ＊:S（确保日志输出的时候是按照过滤器的说明限制）
    (4) 显示某一级别以上的全部日志信息：
    adb logcat *:级别
    (5) 以某种格式显示日志信息：
    adb logcat -v 格式
    (6) 显示缓冲区中的日志信息：
    adb logcat -b 缓冲区类型
    (7) 清理已经存在的日志：
    adb logcat -c
    (8) 将日志显示在控制台后退出：
    adb logcat -d
    
    
    *详细解析
    
     操作：adb logcat [options] [filterspecs]
    *（1）命令列表:
      -s              Set default filter to silent.
                      Like specifying filterspec '*:s'
      -f <filename>  将日志输出到文件
    	eg:adb logcat -f /sdcard/test.txt
    
      -r [<kbytes>]   Rotate log every kbytes. (16 if unspecified). Requires -f
      -n <count>      Sets max number of rotated logs to <count>, default 4
      -v <format>    设置日志输入格式控制输出字段，默认的是brief格式
    	brief — 显示优先级/标记和原始进程的PID (默认格式) 	process — 仅显示进程PID 	tag — 仅显示优先级/标记 	thread — 仅显示进程：线程和优先级/标记 	raw — 显示原始的日志信息，没有其他的元数据字段 	time — 显示日期，调用时间，优先级/标记，PID 	long —显示所有的元数据字段并且用空行分隔消息内容
    
    	注意-v 选项中只能指定一种格式。
    
    	eg:adb logcat -v thread   //使用 thread 输出格式 	
    
      -c              清理已存在的日志
      -d            将日志显示在控制台后退出
      -t <count>      print only the most recent <count> lines (implies -d)
      -t '<time>'     print most recent lines since specified time (implies -d)
      -T <count>      print only the most recent <count> lines (does not imply -d)
      -T '<time>'     print most recent lines since specified time (not imply -d)
                      count is pure numerical, time is 'MM-DD hh:mm:ss.mmm'
      -g              get the size of the log's ring buffer and exit
      -b <buffer>    加载一个可使用的日志缓冲区供查看，默认值是main。
    	radio — 查看包含在无线/电话相关的缓冲区消息 	events — 查看事件相关的消息 	main — 查看主缓冲区 (默认缓冲区)
    
    	eg:adb logcat -b radio     //查看radio缓冲区
    
      -B              output the log in binary.
      -S              output statistics.
      -G <size>       set size of log ring buffer, may suffix with K or M.
      -p              print prune white and ~black list. Service is specified as
                      UID, UID/PID or /PID. Weighed for quicker pruning if prefix
                      with ~, otherwise weighed for longevity if unadorned. All
                      other pruning activity is oldest first. Special case ~!
                      represents an automatic quicker pruning for the noisiest
                      UID as determined by the current statistics.
      -P '<list> ...' set prune white and ~black list, using same format as
                      printed above. Must be quoted.
    
    *（2）优先级设置（由低到高）
      V    Verbose
      D    Debug
      I    Info
      W    Warn
      E    Error
      F    Fatal
      S    Silent (supress all output)
    eg:adb logcat *:W   //显示所有优先级大于等于“warning”的日志
    
    '*' means '*:d' and <tag> by itself means <tag>:v
    
    If not specified on the commandline, filterspec is set from ANDROID_LOG_TAGS.
    If no filterspec is found, filter defaults to '*:I'
    
    If not specified with -v, format is set from ANDROID_PRINTF_LOG
    or defaults to "brief"
