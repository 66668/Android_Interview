# android RecyclerView相关 总结

## Recycleview和ListView的区别


1. **ViewHolder编写规范化**

    在ListView中，**ViewHolder需要自己来定义**，且这只是一种推荐的使用方式，不使用当然也可以，这不是必须的。只不过不使用ViewHolder的话，
    ListView每次getView的时候都会调用findViewById(int)，这将导致ListView性能展示迟缓
    
    **RecyclerView封装了ViewHolder编写规范**，且必须要实现
    
    **RecyclerView复用item全部搞定， ListView需要设置setTag()与getTag()**；

2. **滚动方向**：

ListView只能在上下滚动，api没提供横向滑动

RecyclerView相较于ListView，在滚动上面的功能扩展了许多。还支持多种类型列表的展示要求，主要如下：

（1）LinearLayoutManager，可以支持水平和竖直方向上滚动的列表。

（2） StaggeredGridLayoutManager，可以支持交叉网格风格的列表，类似于瀑布流或者Pinterest。
 
（3）GridLayoutManager，支持网格展示，可以水平或者竖直滚动，如展示图片的画廊。
  
3. **item动画**：相比较于ListView，RecyclerView.ItemAnimator则被提供用于在RecyclerView添加、删除或移动item时处理动画效果。

4. 嵌套滚动机制：

在事件分发机制中，Touch事件在进行分发的时候，由父View向子View传递，一旦子View消费这个事件的话，那么接下来的事件分发的时候，父View将不接受，
由子View进行处理；但是与Android的事件分发机制不同，嵌套滚动机制（Nested Scrolling）可以弥补这个不足，能让子View与父View同时处理这个Touch事件，
主要实现在于**NestedScrollingChild与NestedScrollingParent**这两个接口；而在RecyclerView中，实现的是**NestedScrollingChild**，所以能实现嵌套滚动机制；

ListView就没有实现嵌套滚动机制；

5. **HeaderView 与 FooterView**：

在ListView中可以通过addHeaderView() 与 addFooterView()来添加头部item与底部item，来当我们需要实现下拉刷新或者上拉加载的情况；
而且这两个API不会影响Adapter的编写；

但是RecyclerView中并没有这两个API，所以当我们需要在RecyclerView添加头部item或者底部item的时候，我们可以在Adapter中自己编写，
根据ViewHolder的Type与View来实现自己的Header，Footter与普通的item，但是这样就会影响到Adapter的数据，比如position，
添加了Header与Footter后，实际的position将大于数据的position；

6. 设置分割

 在ListView中如果我们想要在item之间添加间隔符，我们只需要在布局文件中对ListView添加如下属性即可：

        android:divider="@android:color/transparent"
        android:dividerHeight="5dp"

7. Item点击事件：

   在ListView中有onItemClickListener(), onItemLongClickListener(), onItemSelectedListener(), 
   但是添加HeaderView与FooterView后就不一样了，因为HeaderView与FooterView都会算进position中，这时会发现position会出现变化，
   可能会抛出数组越界，为了解决这个问题，我们在getItemId()方法（在该方法中HeaderView与FooterView返回的值是-1）
   中通过返回id来标志对应的item，而不是通过position来标记；但是我们可以在Adapter中针对每个item写在getView()中会比较合适；
   
   而在RecyclerView中，提供了唯一一个API：RecyclerView.OnItemTouchListener接口来监听item的触摸事件；需要自定义监听事件。

    


