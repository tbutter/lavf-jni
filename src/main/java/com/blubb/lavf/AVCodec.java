package com.blubb.lavf;

public class AVCodec implements AutoCloseable {
	private long codec_ctx;
	private long codec;

	public AVCodec(long codec) {
		this.codec = codec;
		codec_ctx = LAVFNative.INSTANCE.avcodec_alloc_context3(codec);
	}

	public void setCodecParameters(AVFormat avf, int streamidx) {
		if (LAVFNative.INSTANCE.avcodec_stream_to_context(avf.fmt_ctx, streamidx, codec_ctx) > 0)
			throw new RuntimeException("could not call stream to context");
	}

	public void open() {
		if (LAVFNative.INSTANCE.avcodec_open2(codec_ctx, codec) < 0)
			throw new RuntimeException("error opening codec");
	}

	public static AVCodec fromStream(AVFormat avf, int streamidx) {
		long codec = LAVFNative.INSTANCE.avcodec_find_decoder(avf.fmt_ctx, streamidx);
		if (codec == 0)
			throw new RuntimeException("did not find decoder");
		AVCodec avcodec = new AVCodec(codec);
		avcodec.setCodecParameters(avf, streamidx);
		avcodec.open();
		return avcodec;
	}

	public String getName() {
		return LAVFNative.INSTANCE.avcodec_name(codec_ctx);
	}

	public int getWidth() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 1);
	}

	public int getHeight() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 2);
	}

	public int getPixFmt() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 3);
	}

	public int getSampleFmt() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 4);
	}

	public int getChannels() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 5);
	}

	public boolean sendPacket(AVPacket p) {
		return LAVFNative.INSTANCE.avcodec_send_packet(codec_ctx, p.packet_ptr);
	}

	public int receiveFrame(AVFrame frame) {
		frame.unref();
		return LAVFNative.INSTANCE.avcodec_receive_frame(codec_ctx, frame.frame_ptr);
	}

	public void close() {
		finalize();
	}

	@Override
	public void finalize() {
		if (codec_ctx == 0)
			return;
		LAVFNative.INSTANCE.avcodec_free_context(codec_ctx);
		codec_ctx = 0;
	}

	public static int getCodecId(String codec) {
		switch (codec) {
		case "NONE":
			return 0;
		case "MPEG1VIDEO":
			return 1;
		case "MPEG2VIDEO":
			return 2;
		case "MPEG2VIDEO_XVMC":
			return 3;
		case "H261":
			return 4;
		case "H263":
			return 5;
		case "RV10":
			return 6;
		case "RV20":
			return 7;
		case "MJPEG":
			return 8;
		case "MJPEGB":
			return 9;
		case "LJPEG":
			return 10;
		case "SP5X":
			return 11;
		case "JPEGLS":
			return 12;
		case "MPEG4":
			return 13;
		case "RAWVIDEO":
			return 14;
		case "MSMPEG4V1":
			return 15;
		case "MSMPEG4V2":
			return 16;
		case "MSMPEG4V3":
			return 17;
		case "WMV1":
			return 18;
		case "WMV2":
			return 19;
		case "H263P":
			return 20;
		case "H263I":
			return 21;
		case "FLV1":
			return 22;
		case "SVQ1":
			return 23;
		case "SVQ3":
			return 24;
		case "DVVIDEO":
			return 25;
		case "HUFFYUV":
			return 26;
		case "CYUV":
			return 27;
		case "H264":
			return 28;
		case "INDEO3":
			return 29;
		case "VP3":
			return 30;
		case "THEORA":
			return 31;
		case "ASV1":
			return 32;
		case "ASV2":
			return 33;
		case "FFV1":
			return 34;
		case "4XM":
			return 35;
		case "VCR1":
			return 36;
		case "CLJR":
			return 37;
		case "MDEC":
			return 38;
		case "ROQ":
			return 39;
		case "INTERPLAY_VIDEO":
			return 40;
		case "XAN_WC3":
			return 41;
		case "XAN_WC4":
			return 42;
		case "RPZA":
			return 43;
		case "CINEPAK":
			return 44;
		case "WS_VQA":
			return 45;
		case "MSRLE":
			return 46;
		case "MSVIDEO1":
			return 47;
		case "IDCIN":
			return 48;
		case "8BPS":
			return 49;
		case "SMC":
			return 50;
		case "FLIC":
			return 51;
		case "TRUEMOTION1":
			return 52;
		case "VMDVIDEO":
			return 53;
		case "MSZH":
			return 54;
		case "ZLIB":
			return 55;
		case "QTRLE":
			return 56;
		case "SNOW":
			return 57;
		case "TSCC":
			return 58;
		case "ULTI":
			return 59;
		case "QDRAW":
			return 60;
		case "VIXL":
			return 61;
		case "QPEG":
			return 62;
		case "PNG":
			return 63;
		case "PPM":
			return 64;
		case "PBM":
			return 65;
		case "PGM":
			return 66;
		case "PGMYUV":
			return 67;
		case "PAM":
			return 68;
		case "FFVHUFF":
			return 69;
		case "RV30":
			return 70;
		case "RV40":
			return 71;
		case "VC1":
			return 72;
		case "WMV3":
			return 73;
		case "LOCO":
			return 74;
		case "WNV1":
			return 75;
		case "AASC":
			return 76;
		case "INDEO2":
			return 77;
		case "FRAPS":
			return 78;
		case "TRUEMOTION2":
			return 79;
		case "BMP":
			return 80;
		case "CSCD":
			return 81;
		case "MMVIDEO":
			return 82;
		case "ZMBV":
			return 83;
		case "AVS":
			return 84;
		case "SMACKVIDEO":
			return 85;
		case "NUV":
			return 86;
		case "KMVC":
			return 87;
		case "FLASHSV":
			return 88;
		case "CAVS":
			return 89;
		case "JPEG2000":
			return 90;
		case "VMNC":
			return 91;
		case "VP5":
			return 92;
		case "VP6":
			return 93;
		case "VP6F":
			return 94;
		case "TARGA":
			return 95;
		case "DSICINVIDEO":
			return 96;
		case "TIERTEXSEQVIDEO":
			return 97;
		case "TIFF":
			return 98;
		case "GIF":
			return 99;
		case "FFH264":
			return 100;
		case "DXA":
			return 101;
		case "DNXHD":
			return 102;
		case "THP":
			return 103;
		case "SGI":
			return 104;
		case "C93":
			return 105;
		case "BETHSOFTVID":
			return 106;
		case "PTX":
			return 107;
		case "TXD":
			return 108;
		case "VP6A":
			return 109;
		case "AMV":
			return 110;
		case "VB":
			return 111;
		case "PCX":
			return 112;
		case "SUNRAST":
			return 113;
		case "INDEO4":
			return 114;
		case "INDEO5":
			return 115;
		case "MIMIC":
			return 116;
		case "RL2":
			return 117;
		case "8SVX_EXP":
			return 118;
		case "8SVX_FIB":
			return 119;
		case "ESCAPE124":
			return 120;
		case "DIRAC":
			return 121;
		case "BFI":
			return 122;
		case "CMV":
			return 123;
		case "MOTIONPIXELS":
			return 124;
		case "TGV":
			return 125;
		case "TGQ":
			return 126;
		case "TQI":
			return 127;
		case "AURA":
			return 128;
		case "AURA2":
			return 129;
		case "V210X":
			return 130;
		case "TMV":
			return 131;
		case "V210":
			return 132;
		case "DPX":
			return 133;
		case "MAD":
			return 134;
		case "FRWU":
			return 135;
		case "FLASHSV2":
			return 136;
		case "CDGRAPHICS":
			return 137;
		case "R210":
			return 138;
		case "ANM":
			return 139;
		case "BINKVIDEO":
			return 140;
		case "IFF_ILBM":
			return 141;
		case "IFF_BYTERUN1":
			return 142;
		case "KGV1":
			return 143;
		case "YOP":
			return 144;
		case "VP8":
			return 145;
		case "PCM_S16LE":
			return 0x10000;
		case "PCM_S16BE":
			return 0x10001;
		case "PCM_U16LE":
			return 0x10002;
		case "PCM_U16BE":
			return 0x10003;
		case "PCM_S8":
			return 0x10004;
		case "PCM_U8":
			return 0x10005;
		case "PCM_MULAW":
			return 0x10006;
		case "PCM_ALAW":
			return 0x10007;
		case "PCM_S32LE":
			return 0x10008;
		case "PCM_S32BE":
			return 0x10009;
		case "PCM_U32LE":
			return 0x1000A;
		case "PCM_U32BE":
			return 0x1000B;
		case "PCM_S24LE":
			return 0x1000C;
		case "PCM_S24BE":
			return 0x1000D;
		case "PCM_U24LE":
			return 0x1000E;
		case "PCM_U24BE":
			return 0x1000F;
		case "PCM_S24DAUD":
			return 0x10010;
		case "PCM_ZORK":
			return 0x10011;
		case "PCM_S16LE_PLANAR":
			return 0x10012;
		case "PCM_DVD":
			return 0x10013;
		case "PCM_F32BE":
			return 0x10014;
		case "PCM_F32LE":
			return 0x10015;
		case "PCM_F64BE":
			return 0x10016;
		case "PCM_F64LE":
			return 0x10017;
		case "PCM_BLURAY":
			return 0x10018;
		case "ADPCM_IMA_QT":
			return 0x11000;
		case "ADPCM_IMA_WAV":
			return 0x11001;
		case "ADPCM_IMA_DK3":
			return 0x11002;
		case "ADPCM_IMA_DK4":
			return 0x11003;
		case "ADPCM_IMA_WS":
			return 0x11004;
		case "ADPCM_IMA_SMJPEG":
			return 0x11005;
		case "ADPCM_MS":
			return 0x11006;
		case "ADPCM_4XM":
			return 0x11007;
		case "ADPCM_XA":
			return 0x11008;
		case "ADPCM_ADX":
			return 0x11009;
		case "ADPCM_EA":
			return 0x1100A;
		case "ADPCM_G726":
			return 0x1100B;
		case "ADPCM_CT":
			return 0x1100C;
		case "ADPCM_SWF":
			return 0x1100D;
		case "ADPCM_YAMAHA":
			return 0x1100E;
		case "ADPCM_SBPRO_4":
			return 0x1100F;
		case "ADPCM_SBPRO_3":
			return 0x11010;
		case "ADPCM_SBPRO_2":
			return 0x11011;
		case "ADPCM_THP":
			return 0x11012;
		case "ADPCM_IMA_AMV":
			return 0x11013;
		case "ADPCM_EA_R1":
			return 0x11014;
		case "ADPCM_EA_R3":
			return 0x11015;
		case "ADPCM_EA_R2":
			return 0x11016;
		case "ADPCM_IMA_EA_SEAD":
			return 0x11017;
		case "ADPCM_IMA_EA_EACS":
			return 0x11018;
		case "ADPCM_EA_XAS":
			return 0x11019;
		case "ADPCM_EA_MAXIS_XA":
			return 0x1101A;
		case "ADPCM_IMA_ISS":
			return 0x1101B;
		case "AMR_NB":
			return 0x12000;
		case "AMR_WB":
			return 0x12001;
		case "RA_144":
			return 0x13000;
		case "RA_288":
			return 0x13001;
		case "ROQ_DPCM":
			return 0x14000;
		case "INTERPLAY_DPCM":
			return 0x14001;
		case "XAN_DPCM":
			return 0x14002;
		case "SOL_DPCM":
			return 0x14003;
		case "MP2":
			return 0x15000;
		case "MP3":
			return 0x15001;
		case "AAC":
			return 0x15002;
		case "AC3":
			return 0x15003;
		case "DTS":
			return 0x15004;
		case "VORBIS":
			return 0x15005;
		case "DVAUDIO":
			return 0x15006;
		case "WMAV1":
			return 0x15007;
		case "WMAV2":
			return 0x15008;
		case "MACE3":
			return 0x15009;
		case "MACE6":
			return 0x1500A;
		case "VMDAUDIO":
			return 0x1500B;
		case "SONIC":
			return 0x1500C;
		case "SONIC_LS":
			return 0x1500D;
		case "FLAC":
			return 0x1500E;
		case "MP3ADU":
			return 0x1500F;
		case "MP3ON4":
			return 0x15010;
		case "SHORTEN":
			return 0x15011;
		case "ALAC":
			return 0x15012;
		case "WESTWOOD_SND1":
			return 0x15013;
		case "GSM":
			return 0x15014;
		case "QDM2":
			return 0x15015;
		case "COOK":
			return 0x15016;
		case "TRUESPEECH":
			return 0x15017;
		case "TTA":
			return 0x15018;
		case "SMACKAUDIO":
			return 0x15019;
		case "QCELP":
			return 0x1501A;
		case "WAVPACK":
			return 0x1501B;
		case "DSICINAUDIO":
			return 0x1501C;
		case "IMC":
			return 0x1501D;
		case "MUSEPACK7":
			return 0x1501E;
		case "MLP":
			return 0x1501F;
		case "GSM_MS":
			return 0x15020; /* as found in WAV */
		case "ATRAC3":
			return 0x15021;
		case "VOXWARE":
			return 0x15022;
		case "APE":
			return 0x15023;
		case "NELLYMOSER":
			return 0x15024;
		case "MUSEPACK8":
			return 0x15025;
		case "SPEEX":
			return 0x15026;
		case "WMAVOICE":
			return 0x15027;
		case "WMAPRO":
			return 0x15028;
		case "WMALOSSLESS":
			return 0x15029;
		case "ATRAC3P":
			return 0x1502A;
		case "EAC3":
			return 0x1502B;
		case "SIPR":
			return 0x1502C;
		case "MP1":
			return 0x1502D;
		case "TWINVQ":
			return 0x1502E;
		case "TRUEHD":
			return 0x1502F;
		case "MP4ALS":
			return 0x15030;
		case "ATRAC1":
			return 0x15031;
		case "BINKAUDIO_RDFT":
			return 0x15032;
		case "BINKAUDIO_DCT":
			return 0x15033;
		case "DVD_SUBTITLE":
			return 0x17000;
		case "DVB_SUBTITLE":
			return 0x17001;
		case "TEXT":
			return 0x17002;
		case "XSUB":
			return 0x17003;
		case "SSA":
			return 0x17004;
		case "MOV_TEXT":
			return 0x17005;
		case "HDMV_PGS_SUBTITLE":
			return 0x17006;
		case "DVB_TELETEXT":
			return 0x17007;
		case "TTF":
			return 0x18000;
		case "PROBE":
			return 0x19000;
		case "MPEG2TS":
			return 0x20000;
		}
		throw new IllegalArgumentException();
	}
}
