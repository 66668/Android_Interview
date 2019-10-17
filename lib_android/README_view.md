# View 总结（自定义View等）


# View的绘制流程(简)
ViewRoot
-> performTraversal()
-> performMeasure()
-> performLayout()
-> perfromDraw()
-> View/ViewGroup measure()
-> View/ViewGroup onMeasure()
-> View/ViewGroup layout()
-> View/ViewGroup onLayout()
-> View/ViewGroup draw()
-> View/ViewGroup onDraw()

# View的绘制流程（详）

## DecorView被加载到Window中的流程：

1. 从Activity的startActivity开始，最终调用到ActivityThread的handleLaunchActivity方法来创建 Activity，首先，会调用performLaunchActivity方法，内部会执行Activity的onCreate方法，从而 完成DecorView和Activity的创建。然后，会调用handleResumeActivity，里面首先会调用 performResumeActivity去执行Activity的onResume()方法，执行完后会得到一个 ActivityClientRecord对象，然后通过r.window.getDecorView()的方式得到DecorView，然后会通 过a.getWindowManager()得到WindowManager，最终调用其addView()方法将DecorView加进 去。
2. WindowManager的实现类是WindowManagerImpl，它内部会将addView的逻辑委托给 WindowManagerGlobal，可见这里使用了接口隔离和委托模式将实现和抽象充分解耦。在 WindowManagerGlobal的addView()方法中不仅会将DecorView添加到Window中，同时会创建 ViewRootImpl对象，并将ViewRootImpl对象和DecorView通过root.setView()把DecorView加载 到Window中。这里的ViewRootImpl是ViewRoot的实现类，是连接WindowManager和 DecorView的纽带。View的三大流程均是通过ViewRoot来完成的。

## 了解绘制的整体流程

绘制会从根视图ViewRoot的performTraversals()方法开始，从上到下遍历整个视图树，每个View控件 负责绘制自己，而ViewGroup还需要负责通知自己的子View进行绘制操作。

## 理解MeasureSpec
MeasureSpec表示的是一个32位的整形值，它的高2位表示测量模式SpecMode，低30位表示某种测量 模式下的规格大小SpecSize。MeasureSpec是View类的一个静态内部类，用来说明应该如何测量这个 View。
它由三种测量模式，如下:
1. EXACTLY:精确测量模式，视图宽高指定为match_parent或具体数值时生效，表示父视图已经决 定了子视图的精确大小，这种模式下View的测量值就是SpecSize的值。
2. AT_MOST:最大值测量模式，当视图的宽高指定为wrap_content时生效，此时子视图的尺寸可以 是不超过父视图允许的最大尺寸的任何尺寸。
3. UNSPECIFIED:不指定测量模式, 父视图没有限制子视图的大小，子视图可以是想要的任何尺寸， 通常用于系统内部，应用开发中很少用到。

MeasureSpec通过将SpecMode和SpecSize打包成一个int值来避免过多的对象内存分配，为了方便操 作，其提供了打包和解包的方法，打包方法为makeMeasureSpec，解包方法为getMode和getSize。

普通View的MeasureSpec的创建规则如下:对于DecorView而言，它的MeasureSpec由窗口尺寸和其自身的LayoutParams共同决定;对于普通的 View，它的MeasureSpec由父视图的MeasureSpec和其自身的LayoutParams共同决定。

![MeasureSpec](https://github.com/66668/Android_Interview/blob/master/pictures/view_01.png)

## <1> View绘制流程-Measure

1. 首先，在ViewGroup中的measureChildren()方法中会遍历测量ViewGroup中所有的View，当 View的可见性处于GONE状态时，不对其进行测量。 
2. 然后，测量某个指定的View时，根据父容器的MeasureSpec和子View的LayoutParams等信息计 算子View的MeasureSpec。 
3. 最后，将计算出的MeasureSpec传入View的measure方法，这里ViewGroup没有定义测量的具体 过程，因为ViewGroup是一个抽象类，其测量过程的onMeasure方法需要各个子类去实现。不同 的ViewGroup子类有不同的布局特性，这导致它们的测量细节各不相同，如果需要自定义测量过 程，则子类可以重写这个方法。(setMeasureDimension方法用于设置View的测量宽高，如果 View没有重写onMeasure方法，则会默认调用getDefaultSize来获得View的宽高)

### getSuggestMinimumWidth分析
 
如果View没有设置背景，那么返回android:minWidth这个属性所指定的值，这个值可以为0;如果View设置了背景，则返回android:minWidth和背景的最小宽度这两者中的最大值。

### 自定义View时手动处理wrap_content时的情形

直接继承View的控件需要重写onMeasure方法并设置wrap_content时的自身大小，否则在布局中使用 wrap_content就相当于使用match_parent。此时，可以在wrap_content的情况下(对应 MeasureSpec.AT_MOST)指定内部宽/高(mWidth和mHeight)。

### LinearLayout的onMeasure方法实现解析(这里仅分析measureVertical核心源码)

系统会遍历子元素并对每个子元素执行measureChildBeforeLayout方法，这个方法内部会调用子元素 的measure方法，这样各个子元素就开始依次进入measure过程，并且系统会通过mTotalLength这个 变量来存储LinearLayout在竖直方向的初步高度。每测量一个子元素，mTotalLength就会增加，增加 的部分主要包括了子元素的高度以及子元素在竖直方向上的margin等。

### 在Activity中获取某个View的宽高
由于View的measure过程和Activity的生命周期方法不是同步执行的，如果View还没有测量完毕，那么 获得的宽/高就是0。所以在onCreate、onStart、onResume中均无法正确得到某个View的宽高信息。 

解决方式如下:

1. Activity/View#onWindowFocusChanged:此时View已经初始化完毕，当Activity的窗口得到焦点 和失去焦点时均会被调用一次，如果频繁地进行onResume和onPause，那么 onWindowFocusChanged也会被频繁地调用。
2. view.post(runnable): 通过post可以将一个runnable投递到消息队列的尾部，始化好了然后等待 Looper调用次runnable的时候，View也已经初始化好了。
3. ViewTreeObserver#addOnGlobalLayoutListener:当View树的状态发生改变或者View树内部的 View的可见性发生改变时，onGlobalLayout方法将被回调。
4. View.measure(int widthMeasureSpec, int heightMeasureSpec):match_parent时不知道 parentSize的大小，测不出;具体数值时，直接makeMeasureSpec固定值，然后调用 view..measure就可以了;wrap_content时，在最大化模式下，用View理论上能支持的最大值去 构造MeasureSpec是合理的。


## <2>View的绘制流程-Layout
1. 首先，会通过setFrame方法来设定View的四个顶点的位置，即View在父容器中的位置。
2.然后，会执行 到onLayout空方法，子类如果是ViewGroup类型，则重写这个方法，实现ViewGroup中所有View控件 布局流程。

### LinearLayout的onLayout方法实现解析(layoutVertical核心源码) 
其中会遍历调用每个子View的setChildFrame方法为子元素确定对应的位置。其中的childTop会逐渐增大，意味着后面的子元素会被放置在靠下的位置。

注意:在View的默认实现中，View的测量宽/高和最终宽/高是相等的，只不过测量宽/高形成于View的 measure过程，而最终宽/高形成于View的layout过程，即两者的赋值时机不同，测量宽/高的赋值时机 稍微早一些。
在一些特殊的情况下则两者不相等:

1. 重写View的layout方法,使最终宽度总是比测量宽/高大100px。 
2. View需要多次measure才能确定自己的测量宽/高，在前几次测量的过程中，其得出的测量宽/高有 可能和最终宽/高不一致，但最终来说，测量宽/高还是和最终宽/高相同。

## <3>View的绘制流程-Draw 

Draw的基本流程 绘制基本上可以分为六个步骤:

1. 首先绘制View的背景; 
2. 如果需要的话，保持canvas的图层，为fading做准备; 
3. 然后，绘制View的内容;
4. 接着，绘制View的子View; 
5. 如果需要的话，绘制View的fading边缘并恢复图层;
6. 最后，绘制View的装饰(例如滚动条等等)。

### setWillNotDraw的作用
 如果一个View不需要绘制任何内容，那么设置这个标记位为true以后，系统会进行相应的优化。
1. 默认情况下，View没有启用这个优化标记位，但是ViewGroup会默认启用这个优化标记位。 
2. 当我们的自定义控件继承于ViewGroup并且本身不具备绘制功能时，就可以开启这个标记位从而 便于系统进行后续的优化。 
3. 当明确知道一个ViewGroup需要通过onDraw来绘制内容时，我们需要显示地关闭 WILL_NOT_DRAW这个标记位。


## postInvalidate()和invalidate()和requestLayout(),onlayout，onDraw，DrawChild区别与联系?
 
invalidate()与postInvalidate()都用于刷新View，主要区别是invalidate()在主线程中调用，若在子线程 中使用需要配合handler;而postInvalidate()可在子线程中直接调用。

1. **postInvalidate()** 在非UI线程中调用，通知UI线程重绘。

2. **invalidate()** ,在UI线程中调用，重绘当前UI（即draw()过程），不会触发onMeasure（）方法（控制大小用）。如果是View就重绘View,如果是ViewGroup就全部重绘。

Invalidate不能直接在线程中调用，因为他是违背了单线程模型：Android UI操作并不是线程安全的，并且这些操作必须在UI线程中调用。


3. **requestLayout()** 触发measure和layout不触发onDraw,将会根据标志位判断是否需要ondraw。 

4. **requestFocus()** ，局部刷新，他只刷新你要刷新的地方。  他是让我们的某一部分获取焦点，获取焦点的会导致view的重绘。

5. **onLayout()**:如果该View是ViewGroup对象，需要实现该方法，对每个子视图进行布局。 
6. **onDraw()**:绘制视图本身 (每个View都需要重载该方法，ViewGroup不需要实现该方法)。 
7. **drawChild()**:去重新回调每个子视图的draw()方法。

## 如何实现局部重新测量，避免全局重新测量问题。

View（非容器类）调用invalidate方法只会重绘自身，ViewGroup调用则会重绘整个View树。

## requestLayout全局重绘

requestLayout()触发measure和layout，

View执行requestLayout方法，会向上递归到顶级父View中，再执行这个顶级父View的requestLayout方法，所以其他View的onMeasure，onLayout也可能会被调用。

## Canvas.save()跟Canvas.restore()的调用时机 

1. save:用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。 

2. restore:用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。

save和restore要配对使用(restore可以比save少，但不能多)，如果restore调用次数比save多，会引 发Error。save和restore操作执行的时机不同，就能造成绘制的图形不同。

## 自定义view效率高于xml定义吗?

自定义view效率高于xml定义:
 
1. 少了解析xml。
2. 自定义View 减少了ViewGroup与View之间的测量,包括父量子,子量自身,子在父中位置摆放,当子 view变化时,父的某些属性都会跟着变化。

## 自定义View优化

   为了加速你的view，对于频繁调用的方法，需要尽量减少不必要的代码。先从onDraw开始，需要特别 注意不应该在这里做内存分配的事情，因为它会导致GC，
   从而导致卡顿。在初始化或者动画间隙期间 做分配内存的动作。不要在动画正在执行的时候做内存分配的事情。
   
   你还需要尽可能的减少onDraw被调用的次数，大多数时候导致onDraw都是因为调用了invalidate().因 此请尽量减少调用invaildate()的次数。
   如果可能的话，尽量调用含有4个参数的invalidate()方法而不是 没有参数的invalidate()。没有参数的invalidate会强制重绘整个view。
   另外一个非常耗时的操作是请求layout。任何时候执行requestLayout()，会使得Android UI系统去遍历 整个View的层级来计算出每一个view的大小。
   如果找到有冲突的值，它会需要重新计算好几次。另外需 要尽量保持View的层级是扁平化的，这样对提高效率很有帮助。
   
   如果你有一个复杂的UI，你应该考虑写一个自定义的ViewGroup来执行他的layout操作。与内置的view 不同，自定义的view可以使得程序仅仅测量这一部分，
   这避免了遍历整个view的层级结构来计算大小。