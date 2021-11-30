# use pkg-config for getting CFLAGS and LDLIBS
FFMPEG_LIBS=    libavdevice                        \
                libavformat                        \
                libavfilter                        \
                libavcodec                         \
                libswresample                      \
                libswscale                         \
                libavutil                          \

CFLAGS += -Wall -c -fpic -static -g -I$(shell dirname $$(dirname $$(readlink -f $$(which javac))))/include/ -I$(shell dirname $$(dirname $$(readlink -f $$(which javac))))/include/linux/ -Bsymbolic -Wl,-Bsymbolic-functions
CFLAGS := $(shell pkg-config --static --cflags $(FFMPEG_LIBS)) $(CFLAGS)
LDLIBS := $(shell pkg-config --static --libs $(FFMPEG_LIBS)) $(LDLIBS)

liblavf.so: com_blubb_lavf_LAVFNative.o
	clang -v -fpic -Wl,-Bsymbolic-functions -Wl,-Bsymbolic -shared -o liblavf.so com_blubb_lavf_LAVFNative.o $(LDLIBS) -lpthread -lc

#	cc $(CFLAGS) -c -fPIC -DVPX_ENCODER -I/usr/lib/jvm/java-11-openjdk-amd64/include/ -I/usr/lib/jvm/java-11-openjdk-amd64/include/linux/ -I. -o com_blubb_lavf_LAVFNative.o com_blubb_lavf_LAVFNative.c
