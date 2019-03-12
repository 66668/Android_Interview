# 数据结构和算法 总结


![常见排序](https://github.com/66668/Android_Interview/blob/master/pictures/structure_01.jpg)

术语：

**稳定**：如果a原本在b前面，而a=b，排序之后a仍然在b的前面；

**不稳定**：如果a原本在b的前面，而a=b，排序之后a可能会出现在b的后面；

**内排序**：所有排序操作都在内存中完成；

**外排序**：由于数据太大，因此把数据放在磁盘中，而排序通过磁盘和内存的数据传输才能进行；

**时间复杂度**： 一个算法执行所耗费的时间。

**空间复杂度**：运行完一个程序所需内存的大小。

![总结](https://github.com/66668/Android_Interview/blob/master/pictures/structure_02.png)



  ## 1 插入排序算法 原理+代码  
  
  ### 1-1 直接插入排序 
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
  
  
  ### 1-2 希尔排序 
  插入排序的高效版，它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫缩小增量排序。
  
  原理：
  
   ![希尔排序 ](https://github.com/66668/Android_Interview/blob/master/pictures/hashSort_01.png)
   
   代码：
    
       /**
        * 希尔排序
        *
        * @param array
        */
       public static int[] shellSort(int[] array) {
           int j = 0;
           int temp = 0;
           for (int increment = array.length / 2; increment > 0; increment /= 2) {
               for (int i = increment; i < array.length; i++) {
                   temp = array[i];
                   for (j = i; j >= increment; j -= increment) {
                       if (temp < array[j - increment]) {
                           array[j] = array[j - increment];
                       } else {
                           break;
                       }
   
                   }
                   array[j] = temp;
               }
           }
           System.out.println("01希尔排序：" + Arrays.toString(array));
           return array;
       }

  ## 2 选择排序算法 原理+代码  
  
  ### 2-1简单选择排序
  
   动画：
      
   ![选择排序](https://github.com/66668/Android_Interview/blob/master/pictures/selectSort_01.gif)
   
   代码：
    
      /**
         * 选择排序
         *
         * @param array
         * @return
         */
        public static int[] selectionSort(int[] array) {
            if (array.length == 0)
                return array;
            for (int i = 0; i < array.length; i++) {
                int minIndex = i;
                for (int j = i; j < array.length; j++) {
                    if (array[j] < array[minIndex]) // 找到最小的数
                        minIndex = j; // 将最小数的索引保存
                }
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
            }
            System.out.println("01选择排序：" + Arrays.toString(array));
            return array;
        }
    
    
    
  ## 3 交换排序算法 
  快速排序之所比较快，因为相比冒泡排序，每次交换是跳跃式的。每次排序的时候设置一个基准点，将小于等于基准点的数全部放到基准点的左边，
  将大于等于基准点的数全部放到基准点的右边。这样在每次交换的时候就不会像冒泡排序一样每次只能在相邻的数之间进行交换，交换的距离就大的多了。
  因此总的比较和交换次数就少了，速度自然就提高了
  
  ### 3—1 冒泡排序 
  
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
            
  
  
  ### 3-2 快速排序
  
    
  快速排序的基本思想：通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。
  6.1 算法描述
  
  快速排序使用分治法来把一个串（list）分为两个子串（sub-lists）。具体算法描述如下：
  
  从数列中挑出一个元素，称为 “基准”（pivot）；
  
  重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
  
  递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
  
  动画：
      
  ![快速排序](https://github.com/66668/Android_Interview/blob/master/pictures/quickSort_01.gif)
  
  ![快速排序](https://github.com/66668/Android_Interview/blob/master/pictures/quickSort_02.jpg)
  
  
  
  代码1：
  
        /**
         * 01快速排序方法
         * @param array
         * @param start
         * @param end
         * @return
         */
        public static int[] QuickSort(int[] array, int start, int end) {
            if (array.length < 1 || start < 0 || end >= array.length || start > end) return null;
            int smallIndex = partition(array, start, end);
            if (smallIndex > start)
                QuickSort(array, start, smallIndex - 1);
            if (smallIndex < end)
                QuickSort(array, smallIndex + 1, end);
            return array;
        }
        /**
         * 快速排序算法——partition
         * @param array
         * @param start
         * @param end
         * @return
         */
        public static int partition(int[] array, int start, int end) {
            int pivot = (int) (start + Math.random() * (end - start + 1));
            int smallIndex = start - 1;
            swap(array, pivot, end);
            for (int i = start; i <= end; i++)
                if (array[i] <= array[end]) {
                    smallIndex++;
                    if (i > smallIndex)
                        swap(array, i, smallIndex);
                }
            return smallIndex;
        }
    
        /**
         * 交换数组内两个元素
         * @param array
         * @param i
         * @param j
         */
        public static void swap(int[] array, int i, int j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
  
  代码2：
    
    
        /**
    	 * 02快速排序方法
    	 * 
    	 * @param arr
    	 * @param low
    	 * @param high
    	 */
    	public static int[] quickSort_02(int[] arr, int low, int high) {
    		int i, j, temp, t;
    		if (low > high) {
    			return null;
    		}
    		i = low;
    		j = high;
    		// temp就是基准位
    		temp = arr[low];
    
    		while (i < j) {
    			// 先看右边，依次往左递减
    			while (temp <= arr[j] && i < j) {
    				j--;
    			}
    			// 再看左边，依次往右递增
    			while (temp >= arr[i] && i < j) {
    				i++;
    			}
    			System.out.println("-----i=" + i + "--j=" + j);
    			// 如果满足条件则交换
    			if (i < j) {
    				t = arr[j];
    				arr[j] = arr[i];
    				arr[i] = t;
    			}
    
    		}
    		System.out.println("i=" + i + "--j=" + j);
    		// 最后将基准为与i和j相等位置的数字交换
    		arr[low] = arr[i];
    		arr[i] = temp;
    		// 递归调用左半数组
    		quickSort_02(arr, low, j - 1);
    		// 递归调用右半数组
    		quickSort_02(arr, j + 1, high);
    		return arr;
    	}
  
  
  ## 4 归并排序 原理+代码  
  和选择排序一样，归并排序的性能不受输入数据的影响，但表现比选择排序好的多，因为始终都是O(n log n）的时间复杂度。代价是需要额外的内存空间。
  
  归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。归并排序是一种稳定的排序方法。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。 
  
  5.1 算法描述
  
  把长度为n的输入序列分成两个长度为n/2的子序列；
  
  对这两个子序列分别采用归并排序；
  
  将两个排序好的子序列合并成一个最终的排序序列。

   动画：
      
   ![归并排序](https://github.com/66668/Android_Interview/blob/master/pictures/bobble_01.gif)
   
   代码：
   
       /**
        * 归并排序
        *
        * @param array
        * @return
        */
       public static int[] MergeSort(int[] array) {
           if (array.length < 2) return array;
           int mid = array.length / 2;
           int[] left = Arrays.copyOfRange(array, 0, mid);
           int[] right = Arrays.copyOfRange(array, mid, array.length);
           return merge(MergeSort(left), MergeSort(right));
       }
       /**
        * 归并排序——将两段排序好的数组结合成一个排序数组
        *
        * @param left
        * @param right
        * @return
        */
       public static int[] merge(int[] left, int[] right) {
           int[] result = new int[left.length + right.length];
           for (int index = 0, i = 0, j = 0; index < result.length; index++) {
               if (i >= left.length)
                   result[index] = right[j++];
               else if (j >= right.length)
                   result[index] = left[i++];
               else if (left[i] > right[j])
                   result[index] = right[j++];
               else
                   result[index] = left[i++];
           }
           return result;
       }
  
  ## 5 基数排序 原理+代码  
  