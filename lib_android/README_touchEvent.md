# 事件分发机制

事件分发机制：
-> dispatchTouchEvent()
-> onInterceptTouchEvent()
-> onTouchEvent()
requestDisallowInterceptTouchEvent(boolean)

## onTouchEvent()、onTouchListener、onClickListener的执行顺序：

OnTouchListener-->onTouchEvent-->onClickListener

OnTouchListener和onTouchEvent执行了两次，是因为在DOWN和UP时两个方法都被调用，至于onClickListener则只在UP的时候调用


