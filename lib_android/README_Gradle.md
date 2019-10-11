# android Gradle 总结

https://blog.csdn.net/weixin_33859665/article/details/91368791

## 如何通过Gradle配置多渠道包? 

productFlavors 用于生成不同渠道的包

在build.gradle文件中加入productFlavors结构:

        
        android {
           productFlavors {
            xiaomi {}
            baidu {}
            wandoujia {}
            _360 {} // 或“"360"{}”，数字需下划线开头或加上双引号
             china { // 中国版
             }
             america { // 美国版
             }
             free { // 免费版
             }
           }
        }