#!/bin/bash
cp ../build/generated/sources/headers/java/main/com_blubb_lavf_LAVFNative.h .

#cd native
docker build -t lavfbuild .
CID=`docker create lavfbuild`
docker cp $CID:/lavf/liblavf.so liblavf.so
docker cp $CID:/work/include .
mkdir -p ../src/main/resources/
cp liblavf.so ../src/main/resources/
echo copied