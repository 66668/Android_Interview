# 图片加载库对比
1. 比较：

Picasso：120K

Glide：475K

Fresco：3.4M

Android-Universal-Image-Loader：162K

图片函数库的选择需要根据APP的具体情况而定，对于严重依赖图片缓存的APP，例如壁纸类，图片社交类APP来说，可以选择最专业的Fresco。
对于一般的APP，选择Fresco会显得比较重，毕竟Fresco3.4M的体量摆在这。根据APP对图片的显示和缓存的需求从低到高，我们可以对以上函数库做一个排序。


Picasso < Android-Universal-Image-Loader < Glide < Fresco

2. 介绍:

Picasso ：和Square的网络库一起能发挥最大作用，因为Picasso可以选择将网络请求的缓存部分交给了okhttp实现。
Glide：模仿了Picasso的API，而且在他的基础上加了很多的扩展(比如gif等支持)，Glide默认的Bitmap格式是RGB_565，比 Picasso默认的ARGB_8888格式的内存开销要小一半；
Picasso缓存的是全尺寸的(只缓存一种)，而Glide缓存的是跟ImageView尺寸相同的(即5656和128128是两个缓存) 。
FB的图片加载框架Fresco：最大的优势在于5.0以下(最低2.3)的bitmap加载。在5.0以下系统，Fresco将图片放到一个特别的内存区域(Ashmem区)。
当然，在图片不显示的时候，占用的内存会自动被释放。这会使得APP更加流畅，减少因图片内存占用而引发的OOM。
为什么说是5.0以下，因为在5.0以后系统默认就是存储在Ashmem区了。

3. 总结：
Picasso所能实现的功能，Glide都能做，无非是所需的设置不同。但是Picasso体积比起Glide小太多如果项目中网络请求本身用的就是okhttp或者retrofit(本质还是okhttp)，
那么建议用Picasso，体积会小很多(Square全家桶的干活)。Glide的好处是大型的图片流，比如gif、Video，如果你们是做美拍、爱拍这种视频类应用，建议使用。
Fresco在5.0以下的内存优化非常好，代价就是体积也非常的大，按体积算Fresco>Glide>Picasso
不过在使用起来也有些不便（小建议：他只能用内置的一个ImageView来实现这些功能，用起来比较麻烦，我们通常是根据Fresco自己改改，直接使用他的Bitmap层）
