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
  选择排序(Selection-sort)是一种简单直观的排序算法。它的工作原理：首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，
  
  然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
  
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
    
   
    
  ## 2-2 堆排序
  堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。
  
  算法描述
  
      将初始待排序关键字序列(R1,R2….Rn)构建成大顶堆，此堆为初始的无序区；
      将堆顶元素R[1]与最后一个元素R[n]交换，此时得到新的无序区(R1,R2,……Rn-1)和新的有序区(Rn),且满足R[1,2…n-1]<=R[n]；
      由于交换后新的堆顶R[1]可能违反堆的性质，因此需要对当前无序区(R1,R2,……Rn-1)调整为新堆，然后再次将R[1]与无序区最后一个元素交换，得到新的无序区(R1,R2….Rn-2)和新的有序区(Rn-1,Rn)。不断重复此过程直到有序区的元素个数为n-1，则整个排序过程完成
    
   动画：
     
  ![冒泡排序](https://github.com/66668/Android_Interview/blob/master/pictures/bobble_01.gif)
  
  代码：
  
     public static int[] HeapSort(int[] array) {
            len = array.length;
            if (len < 1) return array;
            //1.构建一个最大堆
            buildMaxHeap(array);
            //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
            while (len > 0) {
                swap(array, 0, len - 1);
                len--;
                adjustHeap(array, 0);
            }
            return array;
        }
        /**
         * 建立最大堆
         *
         * @param array
         */
        public static void buildMaxHeap(int[] array) {
            //从最后一个非叶子节点开始向上构造最大堆
            for (int i = (len/2 - 1); i >= 0; i--) { //感谢 @让我发会呆 网友的提醒，此处应该为 i = (len/2 - 1) 
                adjustHeap(array, i);
            }
        }
        /**
         * 调整使之成为最大堆
         *
         * @param array
         * @param i
         */
        public static void adjustHeap(int[] array, int i) {
            int maxIndex = i;
            //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
            if (i * 2 < len && array[i * 2] > array[maxIndex])
                maxIndex = i * 2;
            //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
            if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex])
                maxIndex = i * 2 + 1;
            //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
            if (maxIndex != i) {
                swap(array, maxIndex, i);
                adjustHeap(array, maxIndex);
            }
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

  
 ## 5-1计数排序  原理+代码  
 
 这三种排序算法都利用了桶的概念，但对桶的使用方法上有明显差异：
 
     计数排序：每个桶只存储单一键值
     桶排序：每个桶存储一定范围的数值
     基数排序：根据键值的每位数字来分配桶


 计数排序(Counting sort)是一种稳定的排序算法。计数排序使用一个额外的数组C，其中第i个元素是待排序数组A中值等于i的元素的个数。
 然后根据数组C来将A中的元素排到正确的位置。它只能对整数进行排序。
 
 8.1 算法描述
 
     找出待排序的数组中最大和最小的元素；
     统计数组中每个值为i的元素出现的次数，存入数组C的第i项；
     对所有的计数累加（从C中的第一个元素开始，每一项和前一项相加）；
     反向填充目标数组：将每个元素i放在新数组的第C(i)项，每放一个元素就将C(i)减去1。

  动画：
       
  ![计数排序](https://github.com/66668/Android_Interview/blob/master/pictures/countSort_01.gif)
  
    
   代码：
   
   
        public static int[] CountingSort(int[] array) {
                if (array.length == 0) return array;
                int bias, min = array[0], max = array[0];
                for (int i = 1; i < array.length; i++) {
                    if (array[i] > max)
                        max = array[i];
                    if (array[i] < min)
                        min = array[i];
                }
                bias = 0 - min;
                int[] bucket = new int[max - min + 1];
                Arrays.fill(bucket, 0);
                for (int i = 0; i < array.length; i++) {
                    bucket[array[i] + bias]++;
                }
                int index = 0, i = 0;
                while (index < array.length) {
                    if (bucket[i] != 0) {
                        array[index] = i - bias;
                        bucket[i]--;
                        index++;
                    } else
                        i++;
                }
                return array;
            }
   
            
  ## 5-2桶排序 
  
  桶排序 (Bucket sort)的工作的原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排
  
  9.1 算法描述
  
      人为设置一个BucketSize，作为每个桶所能放置多少个不同数值（例如当BucketSize==5时，该桶可以存放｛1,2,3,4,5｝这几种数字，但是容量不限，即可以存放100个3）；
      遍历输入数据，并且把数据一个一个放到对应的桶里去；
      对每个不是空的桶进行排序，可以使用其它排序方法，也可以递归使用桶排序；
      从不是空的桶里把排好序的数据拼接起来。
  
  注意，如果递归使用桶排序为各个桶排序，则当桶数量为1时要手动减小BucketSize增加下一循环桶的数量，否则会陷入死循环，导致内存溢出 

   动画：
       
  ![桶排序](https://github.com/66668/Android_Interview/blob/master/pictures/bucketsort_01.png)
  
  代码：
        
         public static ArrayList<Integer> BucketSort(ArrayList<Integer> array, int bucketSize) {
                if (array == null || array.size() < 2)
                    return array;
                int max = array.get(0), min = array.get(0);
                // 找到最大值最小值
                for (int i = 0; i < array.size(); i++) {
                    if (array.get(i) > max)
                        max = array.get(i);
                    if (array.get(i) < min)
                        min = array.get(i);
                }
                int bucketCount = (max - min) / bucketSize + 1;
                ArrayList<ArrayList<Integer>> bucketArr = new ArrayList<>(bucketCount);
                ArrayList<Integer> resultArr = new ArrayList<>();
                for (int i = 0; i < bucketCount; i++) {
                    bucketArr.add(new ArrayList<Integer>());
                }
                for (int i = 0; i < array.size(); i++) {
                    bucketArr.get((array.get(i) - min) / bucketSize).add(array.get(i));
                }
                for (int i = 0; i < bucketCount; i++) {
                    if (bucketSize == 1) { // 如果带排序数组中有重复数字时  感谢 @见风任然是风 朋友指出错误
                        for (int j = 0; j < bucketArr.get(i).size(); j++)
                            resultArr.add(bucketArr.get(i).get(j));
                    } else {
                        if (bucketCount == 1)
                            bucketSize--;
                        ArrayList<Integer> temp = BucketSort(bucketArr.get(i), bucketSize);
                        for (int j = 0; j < temp.size(); j++)
                            resultArr.add(temp.get(j));
                    }
                }
                return resultArr;
            }
  
  

  
  ## 5-3 基数排序 原理+代码 
  基数排序也是非比较的排序算法，对每一位进行排序，从最低位开始排序，复杂度为O(kn),为数组长度，k为数组中的数的最大的位数；
  基数排序是按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，
  再按高优先级排序。最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前。基数排序基于分别排序，分别收集，所以是稳定的。
  
 算法描述
  
      取得数组中的最大数，并取得位数；
      arr为原始数组，从最低位开始取每个位组成radix数组；
      对radix进行计数排序（利用计数排序适用于小范围数的特点）；

   
   动画：
       
  ![基数排序](https://github.com/66668/Android_Interview/blob/master/pictures/radixsort.gif)
  
  代码：
    
     public static int[] RadixSort(int[] array) {
            if (array == null || array.length < 2)
                return array;
            // 1.先算出最大数的位数；
            int max = array[0];
            for (int i = 1; i < array.length; i++) {
                max = Math.max(max, array[i]);
            }
            int maxDigit = 0;
            while (max != 0) {
                max /= 10;
                maxDigit++;
            }
            int mod = 10, div = 1;
            ArrayList<ArrayList<Integer>> bucketList = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < 10; i++)
                bucketList.add(new ArrayList<Integer>());
            for (int i = 0; i < maxDigit; i++, mod *= 10, div *= 10) {
                for (int j = 0; j < array.length; j++) {
                    int num = (array[j] % mod) / div;
                    bucketList.get(num).add(array[j]);
                }
                int index = 0;
                for (int j = 0; j < bucketList.size(); j++) {
                    for (int k = 0; k < bucketList.get(j).size(); k++)
                        array[index++] = bucketList.get(j).get(k);
                    bucketList.get(j).clear();
                }
            }
            return array;
        }
  
  # 二分查找法的两种实现
  
  有序的序列，每次都是以序列的中间位置的数来与待查找的关键字进行比较，每次缩小一半的查找范围，直到匹配成功。
  
  ## 优缺点
  
  优点是比较次数少，查找速度快，平均性能好；
  
  其缺点是要求待查表为有序表，且插入删除困难。
  
  因此，折半查找方法适用于不经常变动而查找频繁的有序列表。
  
  ## 方式1：递归调用
  
    public static int recursionBinarySearch(int[] arr,int key,int low,int high){
    		
    		if(key < arr[low] || key > arr[high] || low > high){
    			return -1;				
    		}
    		
    		int middle = (low + high) / 2;			//初始中间位置
    		if(arr[middle] > key){
    			//比关键字大则关键字在左区域
    			return recursionBinarySearch(arr, key, low, middle - 1);
    		}else if(arr[middle] < key){
    			//比关键字小则关键字在右区域
    			return recursionBinarySearch(arr, key, middle + 1, high);
    		}else {
    			return middle;
    		}	
    		
    	}

 ## 方式2：while循环
 
    public static int commonBinarySearch(int[] arr,int key){
    		int low = 0;
    		int high = arr.length - 1;
    		int middle = 0;			//定义middle
    		
    		if(key < arr[low] || key > arr[high] || low > high){
    			return -1;				
    		}
    		
    		while(low <= high){
    			middle = (low + high) / 2;
    			if(arr[middle] > key){
    				//比关键字大则关键字在左区域
    				high = middle - 1;
    			}else if(arr[middle] < key){
    				//比关键字小则关键字在右区域
    				low = middle + 1;
    			}else{
    				return middle;
    			}
    		}
    		
    		return -1;		//最后仍然没有找到，则返回-1
    	}
