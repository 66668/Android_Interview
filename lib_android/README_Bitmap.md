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

## 怎么保证加载Bitmap不OOM/Bitmap的压缩策略(重要)

为了保证在加载Bitmap的时候不产生内存溢出，可以使用BitmapFactory进行图片压缩，主要有以下几 个参数:

1. BitmapFactory.Options.**inPreferredConfig**:将ARGB_8888改为RGB_565，改变编码方式，节约内存。 
2. BitmapFactory.Options.**inSampleSize**:缩放比例，可以参考Luban那个库，根据图片宽高计算出合适的缩放比例(当使用ImageView的时候，可能图片的像素大于ImageView)
3. 设置Options.**inPurgeable**和**inInputShareable**:让系统能及时回收内存。

        
        inPurgeable:设置为True时，表示系统内存不足时可以被回收，设置为False时，表示不能被回收。
        inInputShareable:设置是否深拷贝，与inPurgeable结合使用，inPurgeable为false时，该参 数无意义。
4. 使用decodeStream代替decodeResource等其他方法。
5. 下边的问题

## 从网络加载一个10M的图片，说下注意事项?

我们首先获得目标View所需的大小，然后获得图片的大小，最后通过计算屏幕与图片的缩放比，按照缩放比来解析位图。

高效加载Bitmap具体步骤如下：


    将BitmapFactory.Options的inJustDecodeBounds参数设为true并加载图片
    从BitmapFactory.Options中取出图片的原始宽高信息，他们对应于outWidth和outHeight参数
    根据采样率的规律并结合目标View的所需大小计算出采样率inSampleSize
    将BitmapFactory.Options的inJustDecodeBounds参数设为false,然后重新加载图片

 
两个方法比较重要，在这里我们进行解析：

options.inJustDecodeBounds：如果给它赋值true，那么它就不会解析图片。使用它的目的是为了获得图片的一些信息，如图片高度和宽度，然后进行下一步工作，也就是计算缩放比。将options.inJustDecodeBounds设置为false，将会加载图片

options.inSampleSize ：给图片赋予缩放比，当它的值大于1的时候，它就会按照缩放比返回一个小图片用来节省内存。

 

除了因为图片大小自身的原因之外，还有Android对图片解码的因素在内。

在Android中使用ARGB来展示颜色的，一般情况下使用的是ARGB_8888，每个像素的大小约为4byte。如果对质量不做太大要求，可以使用ARGB_4444或者RGB_565，他们都是2个字节的。

 

**如果图片涉及到放大功能，则也需要注意以下事项**：
1. 图片分块加载：
 
图片的分块加载在地图绘制的情况上最为明显，当想获取一张尺寸很大的图片的某一小块区域时，就用到了图片的分块加载，在Android中BitmapRegionDecoder类的功能就是加载一张图片的指定区域。BitmapRegionDecoder类的使用非常简单，API很少并且一目了然，如下：

（1）创建BitmapRegionDecoder实例

（2）获取图片宽高

（3）加载特定区域内的原始精度的Bitmap对象

（4）调用BitmapRegionDecoder类中的recycle（）,回收释放Native层内存

 

 
2. 使用LruCache

继承并使用LruCache，利用其来缓存加载过的图片区域，需要重写一些方法，如sizeOf()、entryRemoved（）等

其中sizeOf()用于处理获取缓存对象的大小，比如缓存Bitmap对象时，可以使用Bitmap的字节数作为Bitmap大小的表示。

entryRemoved()用于回收某个对象时调用，这样当回收Bitmap对象时可以调用Bitmap对象的recycle()方法主动释放Bitmap对象的内存。

3. 手势处理：

主要用到两个手势处理类，分别是ScaleGestureDetector和GestureDetector，前者用于处理缩放手势，后者用于处理其余手势，如移动，快速滑动，点击，双击，长按等。

ScaleGestureDetector专门处理缩放手势，其比较重要的方法是onScale(ScaleGestureDetector detector)，当缩放时会不停地回调这个方法，需要注意的一点是detector.getScaleFactor()获取到的缩放比例是相对上一次的





