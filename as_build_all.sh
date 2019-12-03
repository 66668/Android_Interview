#!/bin/bash
root=$PWD

apps="RJLocation"
sys_apps="RJHome120UI RJHome120UI_2 RJProvision RJLauncher RJService RJACService RJLogcat RJDMService RJWebAdmin"

#RJProvider 

key_name=script/rejia.keystore
key_pass=android
key_ alias_name=androiddebugkey
key_alias_pass=android

#test_apps="RJFace" 
install_dir=out

do_install() {
  echo "begin do_install..."

  for app in $apps
  do
      mkdir -p $root/$install_dir/$app
      echo "cp $root/$app/build/outputs/apk/release/$app-release.apk $root/$install_dir/$app/$app-unsigned.apk"
      cp $root/$app/build/outputs/apk/release/$app-*.apk $root/$install_dir/$app/$app-unsigned.apk
      echo "sign rejia key > $app"
      jarsigner -verbose -keystore script/rejia.keystore -storepass android -keypass android -digestalg SHA1 -sigalg MD5withRSA -signedjar    	    $root/$install_dir/$app/$app.apk $root/$install_dir/$app/$app-unsigned.apk $key_alias_name
      rm $root/$install_dir/$app/$app-unsigned.apk
      if [ ! $? -eq 0 ]
      then
          exit 1
      fi
  done

  for app in $sys_apps
  do
      mkdir -p $root/$install_dir/$app
      echo "cp $root/$app/build/outputs/apk/release/$app-*.apk $root/$install_dir/$app/$app-unsigned.apk"
      cp $root/$app/build/outputs/apk/release/$app-*.apk $root/$install_dir/$app/$app-unsigned.apk
      echo "singed platform $app"
      java -jar $root/script/signapk.jar $root/script/platform.x509.pem $root/script/platform.pk8 \
      $root/$install_dir/$app/$app-unsigned.apk \
      $root/$install_dir/$app/$app.apk
      rm $root/$install_dir/$app/$app-unsigned.apk
      if [ ! $? -eq 0 ]
      then
          exit 1
      fi
  done
  export AAPT=$ANDROID_HOME/build-tools/28.0.3/
  for app in $apps $sys_apps
  do
      abi="arm64"
      arch="arm64-v8a"
      count=`unzip -l $root/$install_dir/$app/$app.apk "*.so"|grep "[0-9]\ file"|awk '{print $2}'`
      native_abi=`$AAPT/aapt d badging $root/$install_dir/$app/$app.apk|grep "native-code"`
      array=(${native_abi/ / })
      for var in ${array[@]}
      do
        if [ "$var" == "'armeabi'" -o "$app" = "RJLauncher" ]; then
            abi="arm"
            arch="armeabi"
        fi
      done
      echo "native-abi $native_abi,target abi $abi"
      if [ $count -gt 0 ]
      then
          mkdir -p $root/$install_dir/$app/lib/$abi
          if [ $arch = "arm64-v8a" ]; then
            unzip -jo $root/$install_dir/$app/$app.apk "*/arm64-v8a/*.so" -d $root/$install_dir/$app/lib/$abi
          else
            unzip -jo $root/$install_dir/$app/$app.apk "*.so" -d $root/$install_dir/$app/lib/$abi
          fi
          if [ ! $? -eq 0 ]
          then
              echo "unzip so failure:$app"
              exit 1
          fi
      fi
  done
}

do_clean() {
  echo "gradlew clean begin..."
  ./gradlew clean
  echo "clear out dir..."
  rm -rf $root/$install_dir && mkdir $root/$install_dir
  if [ ! -f local.properties ]; then
    touch local.properties
    echo ndk.dir=$ANDROID_NDK > local.properties
    echo sdk.dir=$ANDROID_HOME >> local.properties
  fi
}

do_check(){
    echo "check Android Build Enviroment"
    if [ "$ANDROID_HOME""A" == "A" ]
then
    echo "!!!!!!!! Android SDK(API22) not found !!!!!!!!"
    echo ""
    echo "1)install Android SDK with 'android-22' platform"
    echo "2)set environment 'ANDROID_HOME'"
    echo ""
    exit 1
fi

if [ "$JDK_HOME""A" == "A" ]
then
    echo "!!!!!!!! JDK(1.7+) not found !!!!!!!!"
    echo ""
    echo "please install JDK(1.7+) and set environment 'JDK_HOME' "
    echo ""
    exit 1
fi

if [ "$ANDROID_NDK""A" == "A" ]
then
    echo "!!!!!!!! JDK(1.7+) not found !!!!!!!!"
    echo ""
    echo "please install NDK and set environment 'ANDROID_NKD' "
    echo ""
    exit 1
fi

}

do_build() {
  echo "begin build system apk"
  for app in $apps $sys_apps
  do
    echo "build system app->$app"
    ./gradlew :$app:assembleRelease
    if [ ! $? -eq 0 ]
    then
        echo "build error for $app"
        exit 1
    fi
  done
  echo "begin build apk"
  for app in $apps
  do
    echo "build app-> $app"
    ./gradlew :$app:assembleRelease
    if [ ! $? -eq 0 ]
    then
        echo "build error for $app"
        exit 1
    fi
  done
}

case $1 in
clean)
  do_clean
;;

install)
  do_clean
  do_install
;;

test)
    do_install
;;

build)
  do_clean
  do_build
;;

*)
  do_check
  do_clean
  do_build
  do_install
;;
esac
