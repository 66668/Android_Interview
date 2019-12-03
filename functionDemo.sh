#!/bin/bash
## 无参数函数执行
function myName(){
    echo "my name is JackSong"
}

echo "Call function myName"
myName

function readFile(){
cat students.txt | while read line
do
echo $line
done
}
readFile

FILE=/etc/asa.txt
function checkFileExist(){
if [ -f $FILE ]; then
    return 0
else
    return 1
fi
}

checkFileExist

if [ $? -eq 0 ]; then
    echo "$FILE exist"
else
    echo "$FILE not exist"
fi


set 1 2 3 4 5 6
COUNT=1
for i in $@
do
    echo "Here \$$COUNT is $i"
    let "COUNT++"
done
