# android RecyclerView相关 总结

## Recycleview和ListView的区别

1. 使用方面: 
ListView的基础使用:

(1)继承重写 BaseAdapter 类
(2)自定义 ViewHolder 和 convertView 一起完成复用优化工作

RecyclerView 基础使用关键点同样有两点:
(1)继承重写 RecyclerView.Adapter 和 RecyclerView.ViewHolder
(2)设置布局管理器，控制布局效果

RecyclerView 相比 ListView 在基础使用上的区别主要有如下几点:

(1)ViewHolder 的编写规范化了
(2)RecyclerView 复用 Item 的工作 Google 全帮你搞定，不再需要像 ListView 那样自己调用 setTag getTag()
(3)RecyclerView 需要多出一步 LayoutManager 的设置工作

2. 布局方面:
RecyclerView 支持 线性布局、网格布局、瀑布流布局 三种，而且同时还能够控制横向还是纵向滚动。
ListView只能在上下滚

3. API提供方面:
ListView 提供了 setEmptyView ，addFooterView 、 addHeaderView;而RecycleView没有实现，根据ViewHolder的Type与View来实现自己的Header，Footter与普通的item，但是这样就会影响到Adapter的数据，比如position，添加了Header与Footter后，实际的position将大于数据的position

RecyclerView 供了notifyItemChanged 用于更新单个 Item View 的刷新，我们可以省去自己写**局部更新**的工作

4. 动画效果:
RecyclerView 在做局部刷新的时候有一个渐变的动画效果。继承 **RecyclerView.ItemAnimator** 类，并 实现相应的方法，再调用 RecyclerView的 setItemAnimator(RecyclerView.ItemAnimator animator) 方法设置完即可实现自定义的动画效果。

5. 监听 Item 的事件:
ListView 提供了单击、长按、选中某个 Item 的监听设置。
RecyclerView中: 提供了唯一一个API：RecyclerView.OnItemTouchListener接口来监听item的触摸事件；需要自定义监听事件。

6. 缓存机制不同：
（1）RecyclerView比ListView多两级缓存，支持多个离ItemView缓存，支持开发者自定义缓存处理逻辑，支持所有RecyclerView共用同一个RecyclerViewPool(缓存池)。
（2） RecyclerView缓存RecyclerView.ViewHolder，抽象可理解为：View + ViewHolder(避免每次createView时调用findViewById) + flag(标识状态).而ListView缓存View。

7. 嵌套滚动机制：

在事件分发机制中，Touch事件在进行分发的时候，由父View向子View传递，一旦子View消费这个事件的话，那么接下来的事件分发的时候，父View将不接受，
由子View进行处理；但是与Android的事件分发机制不同，嵌套滚动机制（Nested Scrolling）可以弥补这个不足，能让子View与父View同时处理这个Touch事件，
主要实现在于**NestedScrollingChild与NestedScrollingParent**这两个接口；而在RecyclerView中，实现的是**NestedScrollingChild**，所以能实现嵌套滚动机制；

ListView就没有实现嵌套滚动机制；

### 提问：Recycleview如何解决滑动卡顿,优化滑动体验?

1. 熟悉recyclerview的item加载顺序的模版方法

    
        getItemViewType(获取显示类型，返回值可在onCreateViewHolder中拿到，以决定加载哪种ViewHolder)
        
        onCreateViewHolder(加载ViewHolder的布局)
        
        onViewAttachedToWindow（当Item进入这个页面的时候调用）
        
        onBindViewHolder(将数据绑定到布局上，以及一些逻辑的控制就写这啦)
        
        onViewDetachedFromWindow（当Item离开这个页面的时候调用）
        
        onViewRecycled(当Item被回收的时候调用)
2. 复杂布局优化：
        
        
        1.尽量减少布局嵌套，层级越深，每次测量时间久越久。
        2. 如果布局很复杂，可以考虑自定义布局能不能实现。
        3.尽量减少过度绘制区域。这个可以在开发者选项中看到：调试GPU过度绘制。

3. 优化图片加载,考虑滚动的时候不做复杂布局及图片的加载

### RecyclerView的ItemTouchHelper的实现原理（侧滑删除/item交互动画）
v7包的ItemTouchHelper创建后和RecycleView绑定即可实现
参考：https://www.jianshu.com/p/e3426dcc8ef1
    
        // 创建ItemTouchHelper，并跟RecyclerView绑定
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRv);