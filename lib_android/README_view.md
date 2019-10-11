# View的绘制流程

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

## postInvalidate()和invalidate()区别与联系

1. postInvalidate() 方法在非 UI 线程中调用，通知 UI 线程重绘。

2. invalidate()方法在 UI 线程中调用，重绘当前 UI。
Invalidate不能直接在线程中调用，因为他是违背了单线程模型：Android UI操作并不是线程安全的，并且这些操作必须在UI线程中调用。

## 如何实现局部重新测量，避免全局重新测量问题。

View（非容器类）调用invalidate方法只会重绘自身，ViewGroup调用则会重绘整个View树。

## requestLayout全局重绘

requestLayout()触发measure和layout，

View执行requestLayout方法，会向上递归到顶级父View中，再执行这个顶级父View的requestLayout方法，所以其他View的onMeasure，onLayout也可能会被调用。

## Canvas.save()跟Canvas.restore()的调用时机 

1. save:用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。 

2. restore:用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。

save和restore要配对使用(restore可以比save少，但不能多)，如果restore调用次数比save多，会引 发Error。save和restore操作执行的时机不同，就能造成绘制的图形不同。