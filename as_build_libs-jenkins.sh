#!/usr/bin/env bash
root=$PWD

while true;do
echo "Please input number to build aar file"
echo "0 -build contains LibAC,LibAcSdk，LibFace,LibRJSdk "
echo "1 -build LibAC"
echo "2 -build LibAcSdk"
echo "3 -build LibFace "
echo "4 -build LibRJSdk"
echo "5 -build LibRJSdk"

if [ -z $1 ]; then
        echo "Please input target !!!";
      	exit 1
fi

case $1 in
    0 )
        libs="LibAC LibAcSdk LibFace LibRJSdk"
        break ;;
    1 )
        libs="LibAC"
        break ;;
    2 )
        libs="LibAcSdk"
        break ;;
    3 )
        libs="LibFace"
        break ;;
    4 )
        libs="LibRJSdk"
        break ;;
   5 )
	libs="LibAcsControl"
	break ;;
    *)
        echo "Invalid target $1 !!!";
        exit 1
        ;;
esac
done


echo "begin build Android AAR ------"
for lib in $libs
    do	
	echo "clean Lib------$lib-------"
	./gradlew $lib:clean     
	echo "build Lib------$lib-------"
	mkdir -p $root/out/$lib
	./gradlew $lib:assembleRelease
	cp $root/$lib/build/outputs/aar/$lib-release.aar $root/out/$lib/$lib-release.aar
done


