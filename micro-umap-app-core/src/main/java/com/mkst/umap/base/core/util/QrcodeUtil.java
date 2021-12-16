package com.mkst.umap.base.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.util.Sm4Util;
import com.mkst.mini.systemplus.util.SysConfigUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QrcodeUtil {

	private static final String CHARSET = "utf-8";

	public static boolean createQrcode(String content, String filePath) {
		/*
		 * 图片的宽度和高度
		 */
		int width = 500;
		int height = 500;
		// 图片的格式
		String format = "png";

		// 定义二维码的参数
		HashMap<EncodeHintType, Object> hints = new HashMap<>();
		// 定义字符集编码格式
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		// 纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 设置图片边距
		hints.put(EncodeHintType.MARGIN, 1);
//		hints.put(EncodeHintType.QR_VERSION, 30);

		try {
			// 最终生成 参数列表 （1.内容 2.格式 3.宽度 4.高度 5.二维码参数）
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			// 写入到本地
			File file = new File(filePath);
			File fileParent = file.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			Path path = file.toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);
			return true;
		} catch (Exception e) {
			log.error("生成二维码图片失败：", e);
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createQrcodeControl(String content, String filePath,Integer width,Integer height) {
		// 图片的格式
		String format = "png";

		// 定义二维码的参数
		HashMap<EncodeHintType, Object> hints = new HashMap<>();
		// 定义字符集编码格式
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		// 纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 设置图片边距
		hints.put(EncodeHintType.MARGIN, 1);
//		hints.put(EncodeHintType.QR_VERSION, 30);

		try {
			// 最终生成 参数列表 （1.内容 2.格式 3.宽度 4.高度 5.二维码参数）
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			// 写入到本地
			File file = new File(filePath);
			File fileParent = file.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			Path path = file.toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);
			return true;
		} catch (Exception e) {
			log.error("生成二维码图片失败：", e);
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 解析二维码
	 *
	 * @param file 二维码图片
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String decode(File file) throws Exception {
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable hints = new Hashtable();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * 解析二维码
	 *
	 * @param path 二维码图片地址
	 * @return 不是二维码的内容返回null,是二维码直接返回识别的结果
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception {
		return decode(new File(path));
	}

	/**
	 * 校验二维码
	 * @param qrcode
	 */
	public static com.mkst.mini.systemplus.common.base.Result validateQrcode(String qrcode) {
		com.mkst.mini.systemplus.common.base.Result result = ResultGenerator.genSuccessResult();
		
		String sm4_key = SysConfigUtil.getKey("sm4_key");
    	JSONObject jsonObject = new JSONObject();
		try {
			String qrcodeStr = Sm4Util.decryptEcb(sm4_key, qrcode);
			jsonObject = JSONObject.parseObject(qrcodeStr);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("二维码解析失败：" + e.getMessage());
		}
		
		String timestampStr = jsonObject.getString("timestamp");
		long timestamp = new Long(timestampStr);
		Date qrDate = new Date(timestamp);
		long num = DateUtils.pastMinutes(qrDate);
		if(num > 3){
			result = ResultGenerator.genFailResult("二维码超时");
			result.setCode(1);
			return result;
		}
    	result.setData(jsonObject);
    	return result;
	}

}
