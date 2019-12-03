#!/usr/bin/env bash
root=$PWD

libs="LibAcSdk LibFace LibAC"
dep_libs="LibCore LibUVCCamera LibRPCVolley"

echo "begin build depend libs------"

for lib in $dep_libs
    do
    echo "build dependLib $lib"
    ./gradlew $lib:assemble

done

echo "begin build Android AAR ------"
for lib in $libs
    do
	echo "build Lib------$lib-------"
	mkdir -p $root/out/$lib
	./gradlew $lib:assembleRelease
	cp $root/$lib/build/outputs/aar/$lib-release.aar $root/out/$lib/$lib-release.aar
done


