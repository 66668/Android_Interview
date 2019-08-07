# ConstraintLayout 约束布局

约束布局ConstraintLayout 是一个ViewGroup，可以在Api9以上的Android系统使用它，它的出现主要是为了解决布局嵌套过多的问题，
以灵活的方式定位和调整小部件。

ConstraintLayout可以按照比例约束控件位置和尺寸，能够更好地适配屏幕大小不同的机型

就是减少了布局的嵌套，减少了布局渲染的层数，降低了CPU的消耗，提高了程序的性能

## 使用依赖

implementation 'com.android.support.constraint:constraint-layout:1.1.3'

## 相对位置约束

app:layout_constraintLeft_toRightOf="@+id/TextView1"

1. layout_constraintLeft_toLeftOf

2. layout_constraintLeft_toRightOf

3. layout_constraintRight_toLeftOf

4. layout_constraintRight_toRightOf

5. layout_constraintTop_toTopOf

6. layout_constraintTop_toBottomOf

7. layout_constraintBottom_toTopOf

8. layout_constraintBottom_toBottomOf

9. layout_constraintBaseline_toBaselineOf

10. layout_constraintStart_toEndOf

11. layout_constraintStart_toStartOf

12. layout_constraintEnd_toStartOf

13. layout_constraintEnd_toEndOf

14. 基线对齐：layout_constraintBaseline_toBaselineOf

## 角度定位

角度定位指的是可以用一个角度和一个距离来约束两个空间的中心。举个例子：

app:layout_constraintCircle="@+id/TextView1"
app:layout_constraintCircleAngle="120"（角度）
app:layout_constraintCircleRadius="150dp"（距离）
指的是TextView2的中心在TextView1的中心的120度，距离为150dp，

## 边距

## 设置边距必须约束一个相对位置

1. android:layout_marginStart
2. android:layout_marginEnd
3. android:layout_marginLeft
4. android:layout_marginTop
5. android:layout_marginRight
6. android:layout_marginBottom


用法：
只使用layout_marginLeft不生效，必须设置一个相对位置才行：layout_constraintLeft_toLeftOf

        <TextView
        android:id="@+id/TextView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginTop="10dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>


### goneMargin

goneMargin主要用于:控件可见性为 gone 的时候使用的margin值，属性如下：

layout_goneMarginStart
layout_goneMarginEnd
layout_goneMarginLeft
layout_goneMarginTop
layout_goneMarginRight
layout_goneMarginBottom

### 居中和偏移
在RelativeLayout中，把控件放在布局中间的方法是把layout_centerInParent设为true，
而在ConstraintLayout中的写法是：

    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"

同理RelativeLayout中的水平居中layout_centerHorizontal相当于在ConstraintLayout约束控件的左右为parent的左右；
RelativeLayout中的垂直居中layout_centerVertical相当于在ConstraintLayout约束控件的上下为parent的上下。

由于ConstraintLayout中的居中已经为控件约束了一个相对位置，所以可以使用margin,用于偏移位置

app:layout_constraintHorizontal_bias="0.3" 在0--1赋值，表示偏移比例
 
### 尺寸约束

1. 使用指定的尺寸

2. 使用wrap_content，让控件自己计算大小

当控件的高度或宽度为wrap_content时，可以使用下列属性来控制最大、最小的高度或宽度：

android:minWidth 最小的宽度

android:minHeight 最小的高度

android:maxWidth 最大的宽度

android:maxHeight 最大的高度

注意！当ConstraintLayout为1.1版本以下时，使用这些属性需要加上强制约束，如下所示：

    app:constrainedWidth=”true”
    app:constrainedHeight=”true”
    
3. 使用 0dp (MATCH_CONSTRAINT)官方不推荐在ConstraintLayout中使用match_parent，可以设置 0dp (MATCH_CONSTRAINT) 配合约束代替match_parent

4. 宽高比
当宽或高至少有一个尺寸被设置为0dp时，可以通过属性layout_constraintDimensionRatio设置宽高比，

除此之外，在设置宽高比的值的时候，还可以在前面加W或H，分别指定宽度或高度限制。 例如：

app:layout_constraintDimensionRatio="H,2:3"指的是  高:宽=2:3

app:layout_constraintDimensionRatio="W,2:3"指的是  宽:高=2:3

### 链

如果两个或以上控件通过下图的方式约束在一起，就可以认为是他们是一条链（图为横向的链，纵向同理）。

layout_constraintHorizontal_chainStyle来改变整条链的样式。chains提供了3种样式，分别是：
CHAIN_SPREAD —— 展开元素 (默认)；
CHAIN_SPREAD_INSIDE —— 展开元素，但链的两端贴近parent；
CHAIN_PACKED —— 链的元素将被打包在一起。

样式链外，还可以创建一个权重链。
可以留意到上面所用到的3个TextView宽度都为wrap_content，如果我们把宽度都设为0dp，这个时候可以在每个TextView中设置横向权重layout_constraintHorizontal_weight(constraintVertical为纵向)来创建一个权重链


### 辅助工具-1 Optimizer

当我们使用 MATCH_CONSTRAINT 时，ConstraintLayout 将对控件进行 2 次测量，ConstraintLayout在1.1中可以通过设置 layout_optimizationLevel 进行优化，可设置的值有：

none：无优化

standard：仅优化直接约束和屏障约束（默认）

direct：优化直接约束

barrier：优化屏障约束

chain：优化链约束

dimensions：优化尺寸测量


### 辅助工具-2 Barrier

假设有3个控件ABC，C在AB的右边，但是AB的宽是不固定的，这个时候C无论约束在A的右边或者B的右边都不对。当出现这种情况可以用Barrier来解决。Barrier可以在多个控件的一侧建立一个屏障

### 辅助工具-3 Group

Group可以把多个控件归为一组，方便隐藏或显示一组控件

        <android.support.constraint.Group
        android:id="@+id/group"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="invisible"
        app:constraint_referenced_ids="TextView1,TextView3" />
        
 该效果是只显示TextView2


### 辅助工具-4 Placeholder
Placeholder指的是占位符。在Placeholder中可使用setContent()设置另一个控件的id，使这个控件移动到占位符的位置

新建一个Placeholder约束在屏幕的左上角，新建一个TextView约束在屏幕的右上角，
在Placeholder中设置 app:content="@+id/textview"，这时TextView会跑到屏幕的左上角。

        <android.support.constraint.Placeholder
                android:id="@+id/placeholder"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:content="@+id/textview"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
        
            <TextView
                android:id="@+id/textview"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="#cccccc"
                android:padding="16dp"
                android:text="TextView"
                android:textColor="#000000"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
                
  
### 辅助工具-5 Guideline           

Guildline像辅助线一样，在预览的时候帮助你完成布局（不会显示在界面上）。
Guildline的主要属性：
android:orientation 垂直vertical，水平horizontal
layout_constraintGuide_begin 开始位置
layout_constraintGuide_end 结束位置
layout_constraintGuide_percent 距离顶部的百分比(orientation = horizontal时则为距离左边)


# 命名空间

在Android中，
命名空间可分为3种:
xmlns:android=”http://schemas.android.com/apk/res/android”

xmlns:tools=”http://schemas.android.com/tools”

xmlns:app=”http://schemas.android.com/apk/res-auto”

其中，1和2命名空间里的属性是系统封装好的，第3种命名空间里的属性是用户自定义的
