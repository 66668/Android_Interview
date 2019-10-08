# onSaveInstanceState() 与 onRestoreIntanceState() 总结


## 简单理解

Activity的 onSaveInstanceState() 和 onRestoreInstanceState()并不是生命周期方法，它们不同于 onCreate()、onPause()等生命周期方法，它们并不一定会被触发。

当应用遇到意外情况(如:内存不 足、用户直接按Home键)由系统销毁一个Activity时，onSaveInstanceState() 会被调用。

但是当用户 主动去销毁一个Activity时，例如在应用中按返回键，onSaveInstanceState()就不会被调用。

因为在这 种情况下，用户的行为决定了不需要保存Activity的状态。通常onSaveInstanceState()只适合用于保存 一些临时性的状态，而onPause()适合用于数据的持久化保存。 

在activity被杀掉之前调用保存每个实例的状态,以保证该状态可以在onCreate(Bundle)或者 onRestoreInstanceState(Bundle) 

(传入的Bundle参数是由onSaveInstanceState封装好的)中恢复。这 个方法在一个activity被杀死前调用，当该activity在将来某个时刻回来时可以恢复其先前状态。
  
例如，如果activity B启用后位于activity A的前端，在某个时刻activity A因为系统回收资源的问题要被 杀掉，A通过onSaveInstanceState将有机会保存其用户界面状态，

使得将来用户返回到activity A时能 通过onCreate(Bundle)或者onRestoreInstanceState(Bundle)恢复界面的状态






