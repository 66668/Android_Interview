# 类加载机制详解（插件化，热修复相关方向）/反射机制



## JVM平台提供的三层classLoader

Bootstrap classLoader：采用native code实现，是JVM的一部分，主要加载JVM自身工作需要的类，如java.lang.、java.uti.等。Bootstrap ClassLoader不继承自ClassLoader，因为它不是一个普通的Java类，底层由C++编写，已嵌入到了JVM内核当中，当JVM启动后，Bootstrap ClassLoader也随着启动，负责加载完核心类库后，并构造Extension ClassLoader和App ClassLoader类加载器。
ExtClassLoader：扩展的class loader，加载位于$JAVA_HOME/jre/lib/ext目录下的扩展jar。
AppClassLoader：系统class loader，父类是ExtClassLoader，加载$CLASSPATH下的目录和jar；它负责加载应用程序主函数类。

## Android中的类加载器

Android中的类加载器是BootClassLoader、PathClassLoader、DexClassLoader（InMemoryDexClassLoader），URLClassLoader
其中BootClassLoader是加载系统类需要用到的,是ClassLoader的内部类
PathClassLoader是App加载自身dex文件中的类用到的，
DexClassLoader可以加载直接或间接包含dex文件的文件，如APK等。
InMemoryDexClassLoader是Android O新增的一个类加载器，也是继承自BaseDexClassLoader。它提供了从内存中的dex文件加载class的能力。
URLClassLoader并不是直接继承ClassLoader，而是继承自SecureClassLoader类。只能用于加载jar文件，在 Android 中无法使用这个加载器。在Java开发中，我们可以利用URLClassLoader读取Jar包并反射类。

PathClassLoader和DexClassLoader都继承自BaseDexClassLoader，而BaseDexClassLoader下有一个  数组——DexPathList，是用来存放dex文件
BaseDexClassLoader加载一个类，最后调用的是DexFile的方法进行加载的。

无论是热修复还是插件化技术中都利用了类加载机制，所以深入理解Android中的类加载机制对于理解这些技术的原理很有帮助。

## 热修复原理

而他们加载类的时候都需要ClassLoader,ClassLoader有一个子类BaseDexClassLoader，而BaseDexClassLoader下有一个
数组——DexPathList，是用来存放dex文件，当BaseDexClassLoader通过调用findClass方法时，实际上就是遍历数组，
找到相应的dex文件，找到，则直接将它return。而热修复的解决方法就是将新的dex添加到该集合中，并且是在旧的dex的前面，
所以就会优先被取出来并且return返回。

## hook机制原理

在 Android 操作系统中系统维护着自己的一套事件分发机制。应用程序，包括应用触发事件和后台逻辑处理，也是根据事件流程一步步地向下执行。
而「钩子」的意思，就是在事件传送到终点前截获并监控事件的传输，像个钩子钩上事件一样，并且能够在钩上事件时，处理一些自己特定的事件。

    Hook 的选择点：静态变量和单例，因为一旦创建对象，它们不容易变化，非常容易定位。
    Hook 过程：
    寻找 Hook 点，原则是静态变量或者单例对象，尽量 Hook public 的对象和方法。
    选择合适的代理方式，如果是接口可以用动态代理。
    偷梁换柱——用代理对象替换原始对象。


## 双亲委派模型过程

先向上委托父加载器加载class，找不到再向下返回在自己的类路径下查找并加载目标类

使用双亲委派模型的好处在于Java类随着它的类加载器一起具备了一种带有优先级的层次关系。例如类java.lang.Object，它存在在rt.jar中，无论哪一个类加载器要加载这个类，
最终都是委派给处于模型最顶端的Bootstrap ClassLoader进行加载，因此Object类在程序的各种类加载器环境中都是同一个类。
相反，如果没有双亲委派模型而是由各个类加载器自行加载的话，如果用户编写了一个java.lang.Object的同名类并放在ClassPath中，那系统中将会出现多个不同的Object类，
程序将混乱。因此，如果开发者尝试编写一个与rt.jar类库中重名的Java类，可以正常编译，但是永远无法被加载运行。


## java的反射机制：

Java 反射机制在程序运行时，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性。
这种 动态的获取信息 以及 动态调用对象的方法 的功能称为java的反射机制。

反射机制很重要的一点就是“运行时”，其使得我们可以在程序运行时加载、探索以及使用编译期间完全未知的 .class 文件。
换句话说，Java 程序可以加载一个运行时才得知名称的 .class 文件，然后获悉其完整构造，并生成其对象实体、或对其 fields（变量）设值、或调用其 methods（方法）

## 动态代理：
为其他对象提供一种代理以控制对这个对象的访问。某些情况下，一个对象不适合或者不能直接引用另一个对象，而代理对象可以在两者之间起到中介作用。运行阶段才指定代理哪个对象。
组成元素：

抽象类接口
被代理类（具体实现抽象类接口的类）
动态代理类，实际调用被代理类的方法和属性

实现方式： JDK 自身提供的动态代理，就是主要利用了反射机制


        Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
        new InvocationHandler() {
          private final Platform platform = Platform.get();

          @Override public Object invoke(Object proxy, Method method, @Nullable Object[] args)
              throws Throwable {
            // If the method is a method from Object then defer to normal invocation.
            if (method.getDeclaringClass() == Object.class) {
              return method.invoke(this, args);
            }
            if (platform.isDefaultMethod(method)) {
              return platform.invokeDefaultMethod(method, service, proxy, args);
            }
            ServiceMethod<Object, Object> serviceMethod =
                (ServiceMethod<Object, Object>) loadServiceMethod(method);
            OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);
            return serviceMethod.callAdapter.adapt(okHttpCall);
          }
        });
        
其他实现方式：利用字节码操作机制，类似ASM、GGLB(基于ASM)、Javassist等
