#!/bin/bash
#This line is a comment
# 第一次执行 hello world
echo "hello world!"

# read 示例
#echo -n "输入分数判断你是第几段："
#read SCORE
#if [ "$SCORE" -lt 60 ]; then
#    echo "等级C"
#fi
#if [ "$SCORE" -lt 80 -a "$SCORE" -ge 60 ]; then
#    echo "等级B"
#fi
#if [ "$SCORE" -gt 80 ]; then
#    echo "等级A"
#fi
#
## 连续判断
#if [ "$SCORE" -lt 60 ]; then
#    echo "等级C"
#elif [ "$SCORE" -lt 80 -a "$SCORE" -ge 60 ]; then
#    echo "等级B"
#elif [  "$SCORE" -gt 80 ]; then
#    echo "等级A"
#fi


# case示例：大小写数字判断
#read -p "请输入一段字符串" input
#echo -en "你输入的字符串是:"
#case $input in
#    *[[:lower:]]*) echo -en "小写字母" ;;
#    *[[:upper:]]*) echo -en "大写字母" ;;
#    *[[:digit:]]*) echo -en "数字" ;;
#    *) echo "unknown input" ;;
#esac


# （1）类C的for
for((i=0,j=100; i<=10; i++,j--))
do
    echo -n "类C的for=$i $j "
done

# （2）带列表的for
# 使用空格表示列表
for FRUIT in apple orange banana pear
do
    echo "$FRUIT is JackSong's favorite "
done
echo "No more fruits"

# 使用{}表示列表
for VAR in {1..10}
do
    echo "for循环打印：$VAR"
done

# 使用seq表示列表
sum=0
#for mVar in 'seq 1 100'
for Var in $(seq 1 10)
do
   echo "$Var"
done
echo "累加=$sum"

# declare声明示例
declare -a arr='([0]="a" [1]="b" [2]="c")'
echo ${arr[0]}
echo ${arr[2]}

#使用-f显示函数示例

fun_myName()
{
	echo "This is Function echo my name :JackSong"
}

echo "declare -F:"
declare -F

echo "declare -f:"
declare -f

# while示例
COUNT=5
while [[ $COUNT -gt 0 ]]
do
    echo -n "$COUNT "
    let "COUNT-=1"
done
echo "while循环结束"

# while/until示例
sum01=0
sum02=0
i=1
j=1
while [[ "$i" -le "100" ]]
do
    let "sum01+=i"
    let "j=i%2"
    if [[ $j -ne 0 ]];then
        let "sum02+=i"
    fi
    let "i+=1"
done
echo "sum01=$sum01"
echo "sum02=$sum02"

# while示例:读取文件
while READ LINE
do
    NAME=`echo $LINE | awk '{print $1}'`
    AGE=`echo $LINE | awk '{print $2}'`
    Sex=`echo $LINE | awk '{print $3}'`
    echo "My name is $NAME,I'm $AGE years old, I'm $Sex"
done < students.txt


cat students.txt | while read line
do
echo $line
done

#break 控制
for ((i=1; i<9;i++))
do
    for((j=1;j<9;j++))
    do
        if [[ $j -le $i ]];then
               let "multi=$i*$j"
               echo -n " $i*$j=$multi "
        else
            break
        fi
    done
done

	
