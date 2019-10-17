# app瘦身 总结

## 分析apk组成

1. assets
2. lib
3. des
4. res
5. META-INFO 存放签名信息
6. androidManifest
7. resources.arsc ：编译后的二进制资源文件

![apk组成](https://github.com/66668/Android_Interview/blob/master/pictures/apk_install_03.png)

## apk优化
1. 图片：使用一套资源（hdpi）,特殊机型特殊图片开多套资源（如logo,大背景图等）,大图使用jpg格式，能用drawable样式尽量不用图片(如渐变色设置)
2. 统一应用风格，尺寸颜色等资源统一调用，（如shape,selector等drawable资源）
3. so库优化：建议实际工作的配置是只保留armable、armable-x86下的so文件，其余不用
4. 图片压缩：tinypng有损压缩,.9图不压缩，大大缩减文件大小，
5. gradle中开启混淆（android/buildType/minifyEnabled）,开启shrinkResources去除无用资源（android/buildType/shrinkResources）
6. 删除无用资源
7. 使用插件化开发
8. 动态下载资源：字体，js代码，图片等
9. 只用一套中文资源
10. lint检测
