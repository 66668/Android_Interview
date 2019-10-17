# View 总结（自定义View等）


## View 的绘制流程
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

## postInvalidate()和invalidate()和requestLayout()区别与联系

1. **postInvalidate()** 在非UI线程中调用，通知UI线程重绘。

2. **invalidate()** ,在UI线程中调用，重绘当前UI（即draw()过程），不会触发onMeasure（）方法（控制大小用）。如果是View就重绘View,如果是ViewGroup就全部重绘。

Invalidate不能直接在线程中调用，因为他是违背了单线程模型：Android UI操作并不是线程安全的，并且这些操作必须在UI线程中调用。

3. **requestLayout()** 触发measure和layout不触发onDraw

4. **requestFocus()** ，局部刷新，他只刷新你要刷新的地方。  他是让我们的某一部分获取焦点，获取焦点的会导致view的重绘。

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