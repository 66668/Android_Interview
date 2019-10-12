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

## Fragment（3个）在 ViewPager 里面的生命周期，滑动 ViewPager 的页面时 Fragment 的生命周期的变化。

1. 刚打开时Act时，viewpager刚进入时刻：viewpager显示fragment1，fragment2同时被预加载,currentItem = 0
    
    
        Frag0: onAttach
        Frag0: onCreate 
        
        Frag1: onAttach
        Frag1: onCreate
        
        Frag0: onCreateView
        Frag0: onViewCreated
        Frag0: onActivityCreated
        Frag0: onStart
        Frag0: onResume
        
        Frag1: onCreateView
        Frag1: onViewCreated
        Frag1: onActivityCreated
        Frag1: onStart
        Frag1: onResume
        
2. 滑动到第二页 currentItem = 1,viewpager有个setOffscreenPageLimit()方法，用来确认左右两边同时各加载几个页面，默认为1，
这里adapter.getCount()==3所有滑倒中间的时候，所有页面都是在活跃的状态，所以前两个页面不会有生命周期的调用。  

    
        Frag2: onAttach
        Frag2: onCreate
        
        Frag2: onCreateView
        Frag2: onViewCreated
        Frag2: onActivityCreated
        Frag2: onStart
        Frag2: onResume

3. 滑动到第3页,currentItem = 2,第2、3个页面生命周期没有变化,第一个页面的视图被销毁了


        Frag0: onPause
        Frag0: onStop
        Frag0: onDestoryView
        
4. 再滑动到第二页 currentItem = 1，第一个页面的视图重建了，没有走onAttach-->onCreate 

        
        Frag0: onCreateView
        Frag0: onViewCreated
        Frag0: onActivityCreated
        Frag0: onStart
        Frag0: onResume       
        
5. Activity结束：所有的Fragment（包括销毁的Frag0）都走onDestory--> onDetach

        
        Frag1: onPause
        Frag1: onStop
        Frag1: onDestoryView
        Frag1: onDetach
        Frag1: onDestory
        
        Frag2: onPause
        Frag2: onStop
        Frag2: onDestoryView
        Frag2: onDestory
        Frag2: onDetach
        
        Frag0: onDestory
        Frag0: onDetach
          
        
        