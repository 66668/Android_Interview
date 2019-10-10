# Activity/Fragment 生命周期总结

## 正常生命周期执行

![Activity/Fragment 生命周期](https://github.com/66668/Android_Interview/blob/master/pictures/act_frag_life_01.png)

1. 程序正常启动：onCreate()->onStart()->onResume();
2. 正常退出：onPause()->onStop()->onDestory()
3. 当ActA切换ActB的所执行的方法

  ActA:onPouse()
       
  ActB:onCreate()->onStart()->onResume()
       
  ActA:onStop()->onDestory()
4.当ActA切换ActB（此activity是以dialog形式存在的）所执行的方法:

  A:onCreate()->onStart()->onResume()->onPouse()
      
  B:onCreate()->onStart()->onResume() 

5. 程序按home 退出： onPause()->onStop(),再进入：onRestart()->onStart()->onResume(); 
## 横竖屏切换时候Activity的生命周期

1. 不设置Activity的android:configChanges时，切屏会重新回调各个生命周期，切横屏时会执行一次，切 竖屏时会执行两次。
2. 设置Activity的android:configChanges=”orientation”时，切屏还是会调用各个生命周期，切换横竖屏 只会执行一次
3. 设置Activity的android:configChanges=”orientation |keyboardHidden”时，切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法





