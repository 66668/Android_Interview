# android 打包流程总结

## 安装的7大步骤

![apk打包流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_package_01.png)

### 1. 打包资源文件，生成R.java文件

aapt来打包res资源文件，生成R.java、resources.arsc和res文件。

1. res目录

文件 	    描述

animator 	放属性动画

anim 	    放补间动画

color 	     颜色资源

drawable 	存放XML、Bitmap文件，或者.png, .9.png, .jpg, .gif文件等图像资源。打包过程中可能会被优化，比如色彩数可能会被优化，256色变8色。

layout 	    布局资源，布局的xml文件。

menu 	     应用程序菜单

raw 	  直接复制到设备中的任意文件。用参数是资源的ID调用，R.raw.somefilename。和assets 不同的是，raw下面不能有目录，而和assets可以有目录。

values 	   数值XML文件用来描述数组、颜色、尺寸、字符串和样式值等

xml 	   应用配置信息

2. R.java文件:R.java是我们在编写代码的时候会用到的，里面有静态内部类，资源等。

3. resources.arsc文件:resources.arsc这个文件记录了所有的应用程序资源目录的信息，包括每一个资源名称、类型、值、ID以及所配置的维度信息。是一个资源索引表，在给定资源ID和设备配置信息的情况下能快速找到资源。

### 2.处理aidl文件，生成相应的Java文件

aidl（Android Interface Definition Language，Android接口描述语言），位于android-sdk/platform-tools目录下。aidl工具解析接口定义文件然后生成相应的Java代码接口供程序调用。如果项目没用到aidl则跳过这一步。
### 3.编译项目源代码，生成class文件

Java Compiler阶段。项目中所有的Java代码，包括R.java和.aidl文件，都会被Java编译器（javac-->Java Compiler）编译成.class文件，生成的class文件位于工程中的bin/classes目录下。
### 4.转换所有的class文件，生成classes.dex文件

dex阶段。通过dx工具，将.class文件和第三方库中的.class文件处理生成classes.dex文件。该工具位于android-sdk/platform-tools 目录下。dx工具的主要工作是将Java字节码转成成Dalvik字节码、压缩常量池、消除冗余信息等。
### 5.打包生成APK文件

apkbuilder阶段。通过apkbuilder工具，将aapt生成的resources.arsc和res文件、assets文件和classes.dex一起打包生成apk。打包的工具apkbuilder位于 android-sdk/tools目录下。
### 6.对APK文件进行签名

Jarsigner阶段。通过Jarsigner工具，对上面的apk进行debug或release签名。
### 7. 对签名后的APK文件进行对齐处理

通过zipalign工具，将签名后的apk进行对齐处理。工具位于android-sdk/tools目录下。对齐的主要过程是将APK包中所有的资源文件距离文件起始偏移为4字节整数倍，这样通过内存映射访问apk文件时的速度会更快。对齐的作用就是减少运行时内存的使用。

## apk详细打包流程图

![apk详细打包流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_package_02.png)

## apk组成

1. assets
2. lib
3. des
4. res
5. androidManifest
6. resources.arsc ：编译后的二进制资源文件
7. META-INFO 存放签名信息:

MANIFEST.MF(清单文件):其中每一个资源文件都有一个SHA-256-Digest签名，MANIFEST.MF文件 的SHA256(SHA1)并base64编码的结果即为CERT.SF中的SHA256-Digest-Manifest值。

CERT.SF(待签名文件):除了开头处定义的SHA256(SHA1)-Digest-Manifest值，后面几项的值是 对MANIFEST.MF文件中的每项再次SHA256并base64编码后的值。

CERT.RSA(签名结果文件):其中包含了公钥、加密算法等信息。首先对前一步生成的MANIFEST.MF 使用了SHA256(SHA1)-RSA算法，用开发者私钥签名，然后在安装时使用公钥解密。最后，将其与未 加密的摘要信息(MANIFEST.MF文件)进行对比，如果相符，则表明内容没有被修改。



## 什么是签名?

在Apk中写入一个“指纹”。指纹写入以后，Apk中有任何修改，都会导致这个指纹无效，Android系统在 安装Apk进行签名校验时就会不通过，从而保证了安全性。

## 为什么要加签名机制。
1. 确保Apk来源的真实性。 
2. 确保Apk没有被第三方篡改。

**给apk签名可以带来以下好处**：

1. 应用程序升级：能无缝升级到新的版本，必须要同一个证书进行签名并且包名称要相同。（如果证书不同，可能会被系统认为是不同的应用）
2. 应用程序模块化：Android系统可以允许同一个证书签名的多个应用程序在一个进程里运行（系统实际把他们作为一个单个的应用程序），此时就可以把我们的应用程序以模块的方式进行部署，而用户可以独立的升级其中的一个模块
3. 代码或者数据共享：Android提供了基于签名的权限机制，那么一个应用程序就可以为另一个以相同证书签名的应用程序公开自己的功能。 

## 数字摘要
对一个任意长度的数据，通过一个Hash算法计算后，都可以得到一个固定长度的二进制数据，这个数 据就称为“摘要”。

**补充**:

1. 散列算法的基础原理:将数据(如一段文字)运算变为另一固定长度值。 
2. SHA-1:在密码学中，SHA-1(安全散列算法1)是一种加密散列函数，它接受输入并产生一个160 位(20 字节)散列值，称为消息摘要 。
3. MD5:MD5消息摘要算法(英语:MD5 Message-Digest Algorithm)，一种被广泛使用的密码 散列函数，可以产生出一个128位(16字节)的散列值(hash value)，用于确保信息传输完整一 致。
4. SHA-2:名称来自于安全散列算法2(英语:Secure Hash Algorithm 2)的缩写，一种密码散列函 数算法标准，其下又可再分为六个不同的算法标准，包括了:SHA-224、SHA-256、SHA-384、SHA-512、SHA-512/224、SHA-512/256。 

**特征**

1. 唯一性 
2. 固定长度:比较常用的Hash算法有MD5和SHA1，MD5的长度是128拉，SHA1的长度是160位。 
3. 不可逆性

## 签名和校验的主要过程

签名就是在摘要的基础上再进行一次加密，对摘要加密后的数据就可以当作数字签名。

**签名过程**:

1. 计算摘要:通过Hash算法提取出原始数据的摘要。 
2. 计算签名:再通过基于密钥(私钥)的非对称加密算法对提取出的摘要进行加密，加密后的数 据就是签名信息。
3. 写入签名:将签名信息写入原始数据的签名区块内。

**校验过程**:
1. 首先用同样的Hash算法从接收到的数据中提取出摘要。
2. 解密签名:使用发送方的公钥对数字签名进行解密，解密出原始摘要。 
3. 比较摘要:如果解密后的数据和提取的摘要一致，则校验通过;如果数据被第三方篡改过，解 密后的数据和摘要将会不一致，则校验不通过。

## 数字证书

如何保证公钥的可靠性呢?答案是数字证书，数字证书是身份认证机构(Certificate Authority)颁发 的，
包含了以下信息:
1. 证书颁发机构
2. 证书颁发机构签名
3. 证书绑定的服务器域名
4. 证书版本、有效期 
5. 签名使用的加密算法(非对称算法，如RSA) 
6. 公钥等

接收方收到消息后，先向CA验证证书的合法性，再进行签名校验。

注意:Apk的证书通常是自签名的，也就是由开发者自己制作，没有向CA机构申请。Android在安装 Apk时并没有校验证书本身的合法性，只是从证书中提取公钥和加密算法，这也正是对第三方Apk重新 签名后，还能够继续在没有安装这个Apk的系统中继续安装的原因。

## keystore和证书格式
keystore文件中包含了私钥、公钥和数字证书。根据编码不同，keystore文件分为很多种，Android使 用的是Java标准keystore格式JKS(Java Key Storage)，所以通过Android Studio导出的keystore文件是 以.jks结尾的。

keystore使用的证书标准是X.509，X.509标准也有多种编码格式，常用的有两种:pem(Privacy Enhanced Mail)和der(Distinguished Encoding Rules)。jks使用的是der格式，Android也支持直 接使用pem格式的证书进行签名。

**两种证书编码格式的区别**:
1. DER(Distinguished Encoding Rules)二进制格式，所有类型的证书和私钥都可以存储为der格式。 
2. PEM(Privacy Enhanced Mail) base64编码，内容以-----BEGIN xxx----- 开头，以-----END xxx----- 结尾。

## jarsigner和apksigner的区别
Android提供了两种对Apk的签名方式，一种是基于JAR的签名方式，另一种是基于Apk的签名方式，它 们的主要区别在于使用的签名文件不一样:jarsigner使用keystore文件进行签名;apksigner除了支持 使用keystore文件进行签名外，还支持直接指定pem证书文件和私钥进行签名。

## 在签名时，除了要指定keystore文件和密码外，也要指定alias和key的密码，这是为什么呢?

keystore是一个密钥库，也就是说它可以存储多对密钥和证书，keystore的密码是用于保护keystore本 身的，一对密钥和证书是通过alias来区分的。所以jarsigner是支持使用多个证书对Apk进行签名的， apksigner也同样支持。

## Android Apk V1 签名原理
1. 解析出 CERT.RSA 文件中的证书、公钥，解密 CERT.RSA 中的加密数据。
2. 解密结果和 CERT.SF 的指纹进行对比，保证 CERT.SF 没有被篡改。
3. 而 CERT.SF 中的内容再和 MANIFEST.MF 指纹对比，保证 MANIFEST.MF 文件没有被篡改。 
4. MANIFEST.MF 中的内容和 APK 所有文件指纹逐一对比，保证 APK 没有被篡改。