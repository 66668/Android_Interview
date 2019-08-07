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
