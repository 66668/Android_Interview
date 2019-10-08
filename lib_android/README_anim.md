# 动画 简单总结

## 动画分类简介：

Android平台提供了强大的动画框架，使我们在完成复杂的特效时不用自己改变空间的位置或者设置属性通过简单的动画代码就可以实现。

android3.0之前，主要包括两种动画方式：**补间动画**（Tween Animation）和**帧动画**（Frame Animation 或者 Drawable Animation），这两种动画统称为view 动画。

针对视图动画存在的不足，3.0之后google增加了**属性动画**（Property Animation）。之后动画就被分成了**View Animation** 和**Property Animation**。

紧接着，5.0之后，为用户与app交互反馈他们的动作行为和提供了视觉上的连贯性，Material Design又补充了许多过渡动画：
1. **Ripple Effect 触摸反馈(Touch feedback)**
2. **Circular Reveal  圆形展示**
3. **Curved motion       曲线运动**
4. **View state changes  视图状态变化**
5. **Vector Drawables 矢量图动画**
6. **Activity transitions  转场动画(共享元素)**

所以分类时,以最小的功能块划分为：

### Tween Animation（补间动画--属于View Animation）
在一个视图容器内，执行一系列简单变化实现动画效果，可包括平移，旋转，缩放，透明（AlphaAnimation，RotateAnimation，ScaleAnimation，TranslateAnimation，AnimationSet）。
可用xml或android代码定义，通常使用xml设置,但是view的实际位置还在原来地方

### Drawable Animation / Frame Animation(帧动画--属于View Animation)
将res下的drawable/mipmap图，一个一个的播放

帧动画也是view动画的一种，帧动画是通过读取xml文件中设置的一系列Drawable，以类似幻听片的方式展示这些drawable,就形成了动画效果，当然也可以利用代码实现帧动画。
可能大家觉着帧动画不太常用，其实类似的原理可以借鉴，类似android手机开机的很多动画效果就是类似帧动画，加载一系列图片，实现开机的动画效果。

不要使用过多特别大的图，容导致内存不足

### Property Animation 属性动画
如果用xml样式，则在res/animator下创建xml。但是属性动画推荐使用code方式，效果方便控制。

插值器:作用是根据时间流逝的百分比来计算属性变化的百分比

估值器:在1的基础上由这个东西来计算出属性到底变化了多少数值的类

其实就是利用插值器和估值器，来计出各个时刻View的属性，然后通过改变View的属性来实现View的 动画效果。

当动画的 repeatCount 设置为无限循环时，如果在Activity退出时没有及时将动画停止，属性动画会导致Activity无法释放而导致内存泄漏，而补间动画却没问题。

## 区别总结

属性动画才是真正的实现了 view 的移动，补间动画对view 的移动更像是在不同地方绘制了一个影子， 实际对象还是处于原来的地方

![属性动画](https://github.com/66668/Android_Interview/blob/master/pictures/property_anim_01.png)




### Property Animation 动画有两个步聚: 

1. 计算属性值 
2. 为目标对象的属性设置属性值，即应用和刷新动画

计算属性分为3个过程:

        过程一:
        计算已完成动画分数 elapsed fraction。为了执行一个动画，你需要创建一个ValueAnimator，并且指 定目标对象属性的开始、结束和持续时间。
        在调用 start 后的整个动画过程中，ValueAnimator 会根据 已经完成的动画时间计算得到一个0 到 1 之间的分数，代表该动画的已完成动画百分比。0表示 0%，1 表示 100%。
        
        过程二:
        计算插值(动画变化率)interpolated fraction 。当 ValueAnimator计算完已完成的动画分数后，它会 调用当前设置的TimeInterpolator，去计算得到一个interpolated(插值)分数，在计算过程中，已完 成动画百分比会被加入到新的插值计算中。
        
        过程三:
        计算属性值当插值分数计算完成后，ValueAnimator会根据插值分数调用合适的 TypeEvaluator去计算 运动中的属性值。
        以上分析引入了两个概念:已完成动画分数(elapsed fraction)、插值分数( interpolated fraction )。


### 为什么属性动画移动后仍可点击?
播放补间动画的时候，我们所看到的变化，都只是临时的。而属性动画呢，它所改变的东西，却会更新 到这个View所对应的矩阵中，
所以当ViewGroup分派事件的时候，会正确的将当前触摸坐标，转换成矩 阵变化后的坐标，这就是为什么播放补间动画不会改变触摸区域的原因了。





