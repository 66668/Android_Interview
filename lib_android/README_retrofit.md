# retrofit 总结

## 1

retrofit的本质就是应用了大量的设计模式对Okhttp进行了封装，加入了网络数据转换、网络请求回调、网络请求适配这几个主要的功能
。它本身肯定不会去发请求的，发请求交给了OkHttp，它只对调用方式和返回结果进行处理,其他的不关它的事。

## 2
它内部通过动态代理生成接口的实体对象，然后通过解读注解来获得接口中定义的请求信息，通过CallAdapterFactory将OkHttpCall对象适配成接口中定义的返回类型
，通过ConverFactory来解读网络数据，其底层真正处理网络请求的还是OkHttp框架


## 3
其内部的设计结构非常清晰，

通过动态代理来处理接口，

通过OkHttp来处理网络请求，

通过CallAdapterFactory来适配OkHttpCall，

通过ConverterFactory来处理数据格式的转换，这符合面对对象设计思想的单一职责原则。
同时，Retrofit对CallAdpaterFactory和ConverterFactory的依赖都是依赖其接口的，这就让我们可以非常方便的扩展自己的CallAdpaterFactory和ConverterFactory，这符合依赖倒置原则；
不管Retrofit内部的实现如何复杂，比如动态代理的实现、针对注解的处理以及寻找合适的适配器等，Retrofit对开发者隐藏了这些实现细节，
只提供了简单的Api给开发者调用，开发者只需要关注通过的Api即可实现网络请求，这种对外隐藏具体的实现细节的思想符合迪米特原则。

另外，Retrofit内部大量使用了设计模式，

比如构造Retrofit对象时使用了Builder模式，

处理接口时是用来动态代理模式，

适配OkHttpCall时使用了Adapter模式，

生成CallAdpater和Converter时使用了工厂模式。

Retrofit的设计正是因为遵循了面向对象的思想，以及对设计模式的正确应用，才使得其具备结构清晰、易于扩展的特点！
 