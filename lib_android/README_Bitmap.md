# Bitmap 总结

## Bitmap 使用时候注意什么?
1. 要选择合适的图片规格(bitmap类型):
2. 降低采样率。

    BitmapFactory.Options 参数inSampleSize的使用，先把 options.inJustDecodeBounds设为true，只是去读取图片的大小，
    在拿到图片的大小之后和要显示的大小做比较通过calculateInSampleSize()函数计算inSampleSize的具体值，得到值之后。
    options.inJustDecodeBounds设为false读图片资源。
    
3. 复用内存。

    即，通过软引用(内存不够的时候才会回收掉)，复用内存块，不需要再重新给这个 bitmap申请一块新的内存，避免了一次内存的分配和回收，从而改善了运行效率。
4. 使用recycle()方法及时回收内存。 
5. 压缩图片。

## bitmap recycler 相关

在Android中，Bitmap的存储分为两部分，一部分是Bitmap的数据，一部分是Bitmap的引用。
 
在Android2.3时代，Bitmap的引用是放在堆中的，而Bitmap的数据部分是放在栈中的，需要用户调用 recycle方法手动进行内存回收，

而在Android2.3之后，整个Bitmap，包括数据和引用，都放在了堆 中，这样，整个Bitmap的回收就全部交给GC了，这个recycle方法就再也不需要使用了。

bitmap recycler引发的问题:当图像的旋转角度小余两个像素点之间的夹角时，图像即使旋转也无法显 示，因此，系统完全可以认为图像没有发生变化。
这时系统就直接引用同一个对象来进行操作，避免内存浪费。

## 如何计算一个Bitmap占用内存的大小

1. Bitamp占用内存大小 = 
                    （宽度像素 x (inTargetDensity / inDensity)） x（ 高度像素 x (inTargetDensity / inDensity）) x 一个像素所占的内存
                    
                    
          注:inDensity表示：目标图片的dpi(放在哪个资源文件夹下)，
                inTargetDensity表示：目标屏幕的dpi(屏幕像素密度)
                所以你可以发现inDensity和inTargetDensity会对Bitmap的宽高进行拉伸，进而改变Bitmap占用 内存的大小。
                图片占用内存大小和手机的密度成正比，所在文件夹密度成反比
                
2. 在Bitmap里有两个获取内存占用大小的方法:

(1)etByteCount():API12 加入，代表存储 Bitmap 的像素需要的最少内存。 

(2)getAllocationByteCount():API19 加入，代表在内存中为 Bitmap 分配的内存大小，代替了 getByteCount() 方法。

在不复用 Bitmap 时，getByteCount() 和 getAllocationByteCount 返回的结果是一样的。

在复用Bitmap来解码图片时，那么 getByteCount() 表示新解码图片占用内存的大 小， getAllocationByteCount() 表示被复用Bitmap真实占用的内存大小(即 mBuffer的长度)

## 怎么保证加载Bitmap不产生内存溢出?

为了保证在加载Bitmap的时候不产生内存溢出，可以使用BitmapFactory进行图片压缩，主要有以下几 个参数:

1. BitmapFactory.Options.inPreferredConfig:将ARGB_8888改为RGB_565，改变编码方式，节约内存。 
2. BitmapFactory.Options.inSampleSize:缩放比例，可以参考Luban那个库，根据图片宽高计算出合适的缩放比例。
3. BitmapFactory.Options.inPurgeable:让系统可以内存不足时回收内存。





