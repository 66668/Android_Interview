# ContentProvider使用 总结

进行跨进程通信，实现进程间的数据交互和共享。通过Context 中 getContentResolver() 获得实例，通 过 Uri匹配进行数据的增删改查。
ContentProvider使用表的形式来组织数据，无论数据的来源是什么， ConentProvider 都会认为是一种表，然后把数据组织成表格。




