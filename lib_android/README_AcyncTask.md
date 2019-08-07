# AsyncTask优缺点

AsyncTask封装了线程池和Handler，是一种轻量级的异步任务类，它可以在线程池中执行后台任务，然后把执行的进度和最终结果传递给主线程，
并在主线程中更新UI。简单讲就是方便开发者在子线程中更新UI（因为内部集成了Handler，所以它可以很灵活的在UI线程和子线程之间进行切换）

protected void onPreExecute() {}   //预执行
protected abstract Result doInBackground(Params... params); //执行后台任务
protected void onProgressUpdate(Progress... values) {}//执行进度反馈
protected void onPostExecute(Result result) {} //执行完毕
protected void cancel() {} //终止执行


四.AsyncTask的缺点及注意点

必须在主线程中加载，不然在API 16以下不可用，但目前来说，大部分app最低版本也到16了，这个缺点可以忽略了
1. 内存泄露
在Activity中使用非静态匿名内部AsyncTask类，会持有外部类的隐式引用。由于AsyncTask的生命周期可能比Activity的长，
当Activity进行销毁AsyncTask还在执行时，由于AsyncTask持有Activity的引用，导致Activity对象无法回收，进而产生内存泄露。
-->改为静态内部类
2. Activity 已被销毁，doInBackground还没有执行完，执行完后再执行 onPostResult, 导致产生异常 
-->记得在Activity的 onDestroy 方法中调 cancel方法取消 AsyncTask
（1.内存泄漏 2.生命周期没有跟Activity同步，建议用线程池）

3. 每个AsyncTask实例只能执行一次。


