FROM ghcr.io/tbutter/ffmpeg-static-docker/ffmpeg-static-dev:4.4

RUN apt update && apt install -y openjdk-11-jdk
RUN mkdir /lavf
COPY Makefile /lavf/Makefile
COPY com_blubb_lavf_LAVFNative.h /lavf/com_blubb_lavf_LAVFNative.h
COPY com_blubb_lavf_LAVFNative.c /lavf/com_blubb_lavf_LAVFNative.c
WORKDIR /lavf
RUN make