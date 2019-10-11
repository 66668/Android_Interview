# android ListView 总结

## ListView卡顿原因 

1. Adapter的getView方法里面convertView没有使用setTag和getTag方式;
2. 在getView方法里面ViewHolder初始化后的赋值或者是多个控件的显示状态和背景的显示没有优化好， 抑或是里面含有复杂的计算和耗时操作;
3. 在getView方法里面 inflate的row 嵌套太深(布局过于复杂)或者是布局里面有大图片或者背景所致; 
4. Adapter多余或者不合理的notifySetDataChanged;
5. listview 被多层嵌套，多次的onMessure导致卡顿，如果多层嵌套无法避免，建议把listview的高和宽设 置为match_parent. 如果是代码继承的listview，那么也请你别忘记为你的继承类添加上 LayoutPrams，注意高和宽都mactch_parent的;
