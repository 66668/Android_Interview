# LowMemoryKiller原理分析

通过Google文档，对不同进程的重要程度有了一个直观的认识，下面看一下量化到内存是什么样的呈现形式，这里针对不同的重要程度，做了进一步的细分，
定义了重要级别ADJ，并将优先级存储到内核空间的进程结构体中去，供LowmemoryKiller参考：
| ADJ优先级| 优先级 |对应场景  | 
| ---------- | -------------| ------------- | 
|UNKNOWN_ADJ|16|一般指将要会缓存进程，无法获取确定值|
|CACHED_APP_MAX_ADJ|15|不可见进程的adj最大值（不可见进程可能在任何时候被杀死）|
|CACHED_APP_MIN_ADJ|0|不可见进程的adj最小值（不可见进程可能在任何时候被杀死）|
|SERVICE_B_AD|8|B List中的Service（较老的、使用可能性更小）|
|PREVIOUS_APP_ADJ|7|上一个App的进程(比如APP_A跳转APP_B,APP_A不可见的时候，A就是属于PREVIOUS_APP_ADJ)|
|HOME_APP_ADJ|6|Home进程|
|SERVICE_ADJ|5|服务进程(Service process)|
|HEAVY_WEIGHT_APP_ADJ|4|后台的重量级进程，system/rootdir/init.rc文件中设置|
|BACKUP_APP_ADJ|3|备份进程（这个不太了解）|
|PERCEPTIBLE_APP_ADJ|2|可感知进程，比如后台音乐播放<|
|VISIBLE_APP_ADJ|1|可见进程(可见，但是没能获取焦点，比如新进程仅有一个悬浮Activity，Visible process)|
|FOREGROUND_APP_ADJ|0|前台进程（正在展示的APP，存在交互界面，Foreground process）|
|PERSISTENT_SERVICE_ADJ|-11|关联着系统或persistent进程|
|PERSISTENT_PROC_ADJ|-12|系统persistent进程，比如电话|
|SYSTEM_ADJ|-16|系统进程|
|NATIVE_ADJ|-17|native进程（不被系统管理 ）|


在内存不足的时候扫描所有的用户进程，找到不是太重要的进程杀死。Android5.0将设置进程优先级的入口封装成了一个独立的服务lmkd服务，
AMS不再直接访问proc文件系统，而是通过lmkd服务来进行设置，就是AMS通过socket向lmkd服务发送请求，让lmkd去更新进程的优先级，
lmkd收到请求后，会通过/proc文件系统去更新内核中的进程优先级

![Android5.0之后的LMKD服务](https://github.com/66668/Android_Interview/blob/master/pictures/lmkd_01.png)

LowMemoryKiller内核代码，一句话：被动扫描，找到低优先级的进程，杀死。
    
        static int lowmem_shrink(int nr_to_scan, gfp_t gfp_mask)
        {
            struct task_struct *p;
            
            //关键点1 找到当前的内存对应的阈值
            for(i = 0; i < array_size; i++) {
                if (other_free < lowmem_minfree[i] &&
                    other_file < lowmem_minfree[i]) {
                    min_adj = lowmem_adj[i];
                    break;
                }
            }
            //关键点2 找到优先级低于这个阈值的进程，并杀死
            read_lock(&tasklist_lock);
            for_each_process(p) {
                if (p->oomkilladj < min_adj || !p->mm)
                    continue;
                tasksize = get_mm_rss(p->mm);
                if (tasksize <= 0)
                    continue;
                if (selected) {
                    if (p->oomkilladj < selected->oomkilladj)
                        continue;
                    if (p->oomkilladj == selected->oomkilladj &&
                        tasksize <= selected_tasksize)
                        continue;
                }
                selected = p;
                selected_tasksize = tasksize;
        
            }
            if(selected != NULL) {
                force_sig(SIGKILL, selected);//杀死进程
                rem -= selected_tasksize;
            }
            lowmem_print(4, "lowmem_shrink %d, %x, return %d\n", nr_to_scan, gfp_mask, rem);
            read_unlock(&tasklist_lock);
            return rem;
        }





