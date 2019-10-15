# android ListView 总结（老控件）

## ListView卡顿原因 

1. Adapter的getView方法里面convertView没有使用setTag和getTag方式;
2. 在getView方法里面ViewHolder初始化后的赋值或者是多个控件的显示状态和背景的显示没有优化好， 抑或是里面含有复杂的计算和耗时操作;
3. 在getView方法里面 inflate的row 嵌套太深(布局过于复杂)或者是布局里面有大图片或者背景所致; 
4. Adapter多余或者不合理的notifySetDataChanged;
5. listview 被多层嵌套，多次的onMessure导致卡顿，如果多层嵌套无法避免，建议把listview的高和宽设 置为match_parent. 如果是代码继承的listview，那么也请你别忘记为你的继承类添加上 LayoutPrams，注意高和宽都mactch_parent的;

## ListView异步加载图片乱序问题，原因分析及解决方案

https://blog.csdn.net/guolin_blog/article/details/45586553

**原因**：

ListView之所以能够实现加载成百上千条数据都不会OOM，最主要在于它内部优秀的实现机制。虽然作为普通的使用者，我们大可不必关心ListView内部到底是怎么实现的，
但是当你了解了它的内部原理之后，很多之前难以解释的问题都变得有理有据了。


ListView在借助RecycleBin机制的帮助下，实现了一个生产者和消费者的模式，不管有任意多条数据需要显示，ListView中的子View其实来来回回就那么几个，
移出屏幕的子View会很快被移入屏幕的数据重新利用起来，原理示意图如下所示：

那么这里我们就可以思考一下了，目前数据源当中大概有60个图片的URL地址，而根据ListView的工作原理，显然不可能为每张图片都单独分配一个ImageView控件，
ImageView控件的个数其实就比一屏能显示的图片数量稍微多一点而已，移出屏幕的ImageView控件会进入到RecycleBin当中，而新进入屏幕的元素则会从RecycleBin中
获取ImageView控件。


那么，每当有新的元素进入界面时就会回调getView()方法，而在getView()方法中会开启异步请求从网络上获取图片，注意网络操作都是比较耗时的，也就是说当我们快速滑动
ListView的时候就很有可能出现这样一种情况，某一个位置上的元素进入屏幕后开始从网络上请求图片，但是还没等图片下载完成，它就又被移出了屏幕。这种情况下会产
生什么样的现象呢？根据ListView的工作原理，被移出屏幕的控件将会很快被新进入屏幕的元素重新利用起来，而如果在这个时候刚好前面发起的图片请求有了响应，就
会将刚才位置上的图片显示到当前位置上，因为虽然它们位置不同，但都是共用的同一个ImageView实例，这样就出现了图片乱序的情况。


但是还没完，新进入屏幕的元素它也会发起一条网络请求来获取当前位置的图片，等到图片下载完的时候会设置到同样的ImageView上面，因此就会出现先显示一张图片，
然后又变成了另外一张图片的情况，那么刚才我们看到的图片会自动变来变去的情况也就得到了解释。

**方案1：使用findViewWithTag**

findViewWithTag的工作原理，其实顾名思义，这个方法就是通过Tag的名字来获取具备该Tag名的控件，我们先要调用控件的setTag()方法来给控件设置一个Tag，然后再调用ListView的findViewWithTag()方法使用相同的Tag名来找回控件。


那么为什么用了findViewWithTag()方法之后，图片就不会再出现乱序情况了呢？其实原因很简单，由于ListView中的ImageView控件都是重用的，移出屏幕的控件很快会被进入屏幕的图片重新利用起来，那么getView()方法就会再次得到执行，而在getView()方法中会为这个ImageView控件设置新的Tag，这样老的Tag就会被覆盖掉，于是这时再调用findVIewWithTag()方法并传入老的Tag，就只能得到null了，而我们判断只有ImageView不等于null的时候才会设置图片，这样图片乱序的问题也就不存在了。

**方案2：弱引用关联**

实际上弱引用只是辅助手段而已，最主要的还是关联，这种解决方案的本质是要让ImageView和BitmapWorkerTask之间建立一个双向关联，互相持有对方的引用，
再通过适当的逻辑判断来解决图片乱序问题，然后为了防止出现内存泄漏的情况，双向关联要使用弱引用的方式建立。相比于第一种解决方案，第二种解决方案要明显复杂不少，
但在性能和效率方面都会有更好的表现。

**解决方案三  使用Volley的NetworkImageView控件**

