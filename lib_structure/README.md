# 数据结构和算法 总结

术语：

**稳定**：如果a原本在b前面，而a=b，排序之后a仍然在b的前面；

**不稳定**：如果a原本在b的前面，而a=b，排序之后a可能会出现在b的后面；

**内排序**：所有排序操作都在内存中完成；

**外排序**：由于数据太大，因此把数据放在磁盘中，而排序通过磁盘和内存的数据传输才能进行；

**时间复杂度**： 一个算法执行所耗费的时间。

**空间复杂度**：运行完一个程序所需内存的大小。

![总结](https://github.com/66668/Android_Interview/blob/master/pictures/structure_02.png)


![常见排序](https://github.com/66668/Android_Interview/blob/master/pictures/structure_01.jpg)

  ## 1 插入排序算法 原理+代码  
  
  动画：
  
  ![插入排序](https://github.com/66668/Android_Interview/blob/master/pictures/insert_01.gif)
  
  代码：
  
     /**
         * 01 插入排序
         *
         * @param array
         * @return
         */
        public static int[] insertSort_01(int[] array) {
            if (array.length == 0)
                return array;
            int current;
            for (int i = 0; i < array.length - 1; i++) {
                current = array[i + 1];
                int preIndex = i;
                while (preIndex >= 0 && current < array[preIndex]) {
                    array[preIndex + 1] = array[preIndex];
                    preIndex--;
                }
                array[preIndex + 1] = current;
            }
            System.out.println("01插入排序：" + Arrays.toString(array));
            return array;
        }
    
        /**
         * 02 插入排序
         *
         * @param array
         */
        private static int[] insertSort_02(int[] array) {
            for (int i = 1; i < array.length; i++) {
                int temp = array[i];
                int j = i - 1;
                for (; j >= 0 && array[j] > temp; j--) {
                    array[j + 1] = array[j];
                }
                array[j + 1] = temp;
            }
            System.out.println("02插入排序：" + Arrays.toString(array));
            return array;
        }
    
        //03 插入排序
        public static int[] insertSort_03(int[] array) {
            int len = array.length;
            for (int i = 1; i < len; i++) {
                for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                    int temp = array[j];
                    array[j] = array[j - 1];
                    array[j - 1] = temp;
                }
            }
            System.out.println("03插入排序：" + Arrays.toString(array));
            return array;
        }
  
  
  ## 2 选择排序算法 原理+代码  
  
  ## 3 交换排序算法 
  
  ### 3—1 冒泡排序 原理+代码  
  
   动画：
    
   ![冒泡排序](https://github.com/66668/Android_Interview/blob/master/pictures/bobble_01.gif)
  
  
   代码：

          public static int[] bubbleSort(int[] array) {
                if (array.length == 0)
                    return array;
                for (int i = 0; i < array.length; i++)
                    for (int j = 0; j < array.length - 1 - i; j++)
                        if (array[j + 1] < array[j]) {
                            int temp = array[j + 1];
                            array[j + 1] = array[j];
                            array[j] = temp;
                        }
                System.out.println("01冒泡排序：" + Arrays.toString(array));
                return array;
            }
  ## 4 归并排序 原理+代码  
  
  ## 5 基数排序 原理+代码  
  
  