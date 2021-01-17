#!/bin/bash
cd ..
./gradlew compileJava
cp build/generated/sources/headers/java/main/com_blubb_lavf_LAVFNative.h native

cd native
docker build -t lavfbuild .
CID=`docker create lavfbuild`
docker cp $CID:/lavf/liblavf.so liblavf.so
mkdir -p ../src/main/resources/
cp liblavf.so ../src/main/resources/