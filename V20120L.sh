#!/bin/bash
i=1
while ((i=1))
do
    #echo $i
	# ����ˢ��
    input tap 115 1170
	sleep 0.2
	#input keyevent KEYCODE_STAR
    #sleep 10
	#������Աˢ��
	input tap 680 1170
    #sleep 0.1
	input tap 315 1000
	#sleep 0.1
	# ���з���ſ���
	input tap 495 1170
	#sleep 0.1
	input tap 400 1065
	#sleep 0.1
	input keyevent KEYCODE_STAR
	#sleep 0.1
	# �����ֻ��ſ���
	input tap 300 1170
	#sleep 0.1
	input text 13321188954
	#sleep 0.1
	input tap 660 1050
	#sleep 0.1
	input keyevent KEYCODE_STAR
	#sleep 0.1
	# �ÿ����뿪��
	input tap 680 1170
	#sleep 0.1
	input tap 130 1000
	#sleep 0.1
	input text 332565
	input keyevent KEYCODE_STAR
	#sleep 0.1
	# ������ʱ����
	input tap 680 1170
   # sleep 0.1
	input tap 500 1000
	#sleep 0.1
	input text 33256512
	#sleep 0.1
	input tap 660 830
	#sleep 0.1
	#������ʱ����
	#input tap 700 1200
    #sleep 1
	#input tap 660 160
	#sleep 2
	#input text 12345678
	#sleep 1
	#input keyevent KEYCODE_POUND
	#input tap 400 740
	#sleep 60
    let "i++"
	#adb shell input text 2
	#sleep 2
	#adb shell input text 08
	#sleep 1
	#adb shell input text 12183
	#sleep 1
	#adb shell input keyevent KEYCODE_F1
	#sleep 5
	#adb shell input keyevent KEYCODE_STAR
	#sleep 5
	#adb shell input text 3
	#sleep 30
done
