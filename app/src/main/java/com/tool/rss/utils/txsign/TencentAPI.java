package com.tool.rss.utils.txsign;

public class TencentAPI {
	//自己的APPID
	public static final Integer APP_ID_AI = 2118040378;
	//自己的APPKEY
	public static final String APP_KEY_AI = "sgv4xfadexwA3POE";
	/**
	 * 人脸检测接口地址
	 */
	public static final String FACE_DETECT = "https://api.ai.qq.com/fcgi-bin/face/face_detectface";
	/**
	 * 多人脸检测 	
	 * 识别上传图像上面的人脸位置，支持多人脸识别。
	 */
	public static final String FACE_DETECTMULTI = "https://api.ai.qq.com/fcgi-bin/face/face_detectmultiface";
	/**
	 * 人脸美妆接口地址
	 */
	public static final String FACE_COSMETIC = "https://api.ai.qq.com/fcgi-bin/face/face_detectface";
	/**
	 * 人脸变装
	 */
	public static final String FACE_DECORATION = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facedecoration";
	/**
	 * 人脸融合
	 */
	public static final String FACE_MERGE = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facemerge";
	/**
	 * 颜龄检测-给定图片，对原图进行人脸颜龄检测处理
	 */
	public static final String PTU_FACEAGE = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_faceage";
	//身份证识别接口地址
	public static final String OCR_IDCARD = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_idcardocr";
	public static final String OCR_GENERAL = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_generalocr";
	//基本文本分析-分词接口地址
	public static final String NLP_WORDSEG = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_wordseg";
	//基本文本分析-词性标注接口地址
	public static final String NLP_WORDPOS = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_wordpos";
	//基本文本分析-专有名词识别接口地址
	public static final String NLP_WORDNER = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_wordner";
	//基本文本分析-同义词识别接口地址
	public static final String NLP_WORDSYN = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_wordsyn";
	//情感分析识别 	
	public static final String NLP_TEXTPOLAR = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textpolar";
	//基础闲聊
	public static final String NLP_TEXTCHAT = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
	//文本翻译（AI Lab）
	public static final String NLP_TEXTTRANS = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttrans";
	//文本翻译（翻译君）
	public static final String NLP_TEXTTRANSLATE = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttranslate";
	//语种识别
	public static final String NLP_TEXTDETECT = "https://api.ai.qq.com/fcgi-bin/nlp/nlp_textdetect";
	//语音合成-AI LAB
	public static final String AAI_TTS = "https://api.ai.qq.com/fcgi-bin/aai/aai_tts";
	//语音合成-优图
	public static final String AAI_TTA = "https://api.ai.qq.com/fcgi-bin/aai/aai_tta";
	//语音识别-echo版
	public static final String AAI_ASR = "https://api.ai.qq.com/fcgi-bin/aai/aai_asr";
	//长语音识别	
	public static final String AAI_WXASRLONG = "https://api.ai.qq.com/fcgi-bin/aai/aai_wxasrlong";
	//语音识别-流式版(WeChat AI) 	对音频进行流式识别，轻松实现边录音边识别 	
	public static final String AAI_WXASRS = "https://api.ai.qq.com/fcgi-bin/aai/aai_wxasrs";
	//语音识别-流式版（AI Lab） 对音频进行流式识别，轻松实现边录音边识别 	
	public static final String AAI_ASRS = "https://api.ai.qq.com/fcgi-bin/aai/aai_asrs";
	//银行卡识别
	public static final String OCR_BANK = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_creditcardocr";
	//行驶证驾驶证OCR识别
	public static final String OCR_DRIVERLICENSEOCR="https://api.ai.qq.com/fcgi-bin/ocr/ocr_driverlicenseocr";
	//智能鉴黄  识别一个图像是否为色情图像
	public static final String VISION_PORN = "https://api.ai.qq.com/fcgi-bin/vision/vision_porn";
}
