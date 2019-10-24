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
## SourceSet
1. sourceSet与项目结构有关，通过修改SourceSets中的属性，可以指定哪些源文件（或文件夹下的源文件）要被编译，哪些源文件要被排除。Gradle就是通过它实现**修改项目目录**

  



        android {
            ...
            sourceSets {
                main {
                    manifest.srcFile 'AndroidManifest.xml'
                    java.srcDirs = ['src']
                    java {
                         srcDir 'src/java' // 指定源码目录
                    }
                    aidl.srcDirs = ['src']
                    res.srcDirs = ['res']
                    assets.srcDirs = ['assets']
                    jniLibs.srcDirs = ['libs']// 指定so文件目录
                    jni.srcDirs = []// 设为空禁止AndroidStudio自己编译
                }
            }
        }
        
        
 2. sourceSets最主要的作用不是修改项目目录，而是**增加新的目录约定**，比如，你想要定义一个新的SourceSet来管理集成测试的源文件，这样可以将单元测试和集成测试分开管理
 
 3.  能修改目录，也能实现资源合并，sourceSets可以对不同的buildType, productFlavor,buildVariant设置不同的文件路径，进行多样化处理
    
    当打包对应的buildType/productFlavor/buildVariant的apk包时候，资源文件合并是sourceSets/main下的资源+sourcesSets/{其他配置}。
    
    当资源名在上述资源集里唯一存在时，直接打包进apk，当有多个相同资源名的存在，按照一定的优先级选取资源名对应的值打包进去。
    
            
            对资源会进行合并，有三种资源会进行合并：
            1.主资源集(src/main/)
            2.构建变体(buildType、productFlavor、buildVariant）
            3.三方依赖（aar）
            当上述资源名在上述资源集里唯一存在时，直接打包进apk，当有多个相同的资源名存在，按照如下优先级选取值：
            build variant > build type > product flavor > main source set > library dependencies
            当打包的是buildType/productFlavor/buildVariant在sourceSets下无特定配置的包时候，只取sourceSets/main下的资源文件
            如果sourceSets配置的文件是针对源码文件，assets目录下的文件，layout布局文件，则不会是合并，而是根据上面提到的优先级挑选一个打包到apk里
### 提问：Gradle的Flavor能否配置sourceset？
    能，见上 

## Gradle 构建生命周期

三个阶段：

1. 初始化阶段：解析settings.gradle文件，生成对应的project对象
2. 配置阶段：配置阶段就是去解析所有Project对象中的Task,构建好所有Task的拓扑图，即这些Task之间的执行顺序在配置阶段就确定了
3. 执行阶段: 执行具体的Task及其依赖Task