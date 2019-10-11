# android Fragment 总结
建议和ViewPager一起看

## Fragment状态保存 

Fragment状态保存入口:
1. Activity的状态保存, 在Activity的onSaveInstanceState()里, 调用了FragmentManger的 saveAllState()方法,
 其中会对mActive中各个Fragment的实例状态和View状态分别进行保存.
 
2. FragmentManager还提供了public方法: saveFragmentInstanceState(), 可以对单个Fragment进行 状态保存, 这是提供给我们用的。
3. FragmentManager的moveToState()方法中, 当状态回退到ACTIVITY_CREATED, 会调用 saveFragmentViewState()方法, 保存View的状态.

## activty和Fragmengt之间怎么通信，Fragmengt和Fragmengt怎么通信? 

1. Handler
2. 广播 
3. 事件总线:EventBus、RxBus、Otto 
4. 接口回调 
5. Bundle和setArguments(bundle)

