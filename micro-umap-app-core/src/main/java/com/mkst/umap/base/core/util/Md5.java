package com.mkst.umap.base.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法 用于用户密码的加密
 */

public class Md5 {
	private static final String DEFAULT_ENCODING = "utf-8";

	private static final Log LOG = LogFactory.getLog(Md5.class);

	// 加密函数
	public final static String MD5(String s) {
		return MD5(s, "");
	}

	
	
	
	public static String MD5(String strSrc, String key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] b = strSrc.getBytes(DEFAULT_ENCODING);

			md5.update(b);

			String result = "";
			byte[] temp = md5.digest(key.getBytes(DEFAULT_ENCODING));
			String s = "";
			for (byte bb : temp) {
				s += (bb + " ");
			}

			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			return result;

		} catch (NoSuchAlgorithmException e) {
			LOG.error("md5异常", e);

		} catch (Exception e) {
			LOG.error("md5异常", e);
		}
		return "";
	}

	// 二进制转字符串
	private final static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
				// if (n<b.length-1) hs=hs+":";
			}
		}
		return hs.toUpperCase();
	}
 

	// 测试
	public static void main(String[] args)throws Exception {
		System.out.println(Md5.MD5("XX").length());
		System.out.println(Md5.MD5("123456").substring(8,24));
		System.out.println(Md5.Md5("123456"));
		// System.out.println(EncryptUtil.MD5("qweqwe"));
		// System.out.println(isEquale("XX",
		// "C51B57A703BA1C5869228690C93E1701"));
		System.out.println("33+====="+Md5.Md5("13410071023111111.0324124fbc4d145e39e5dd549824fbc4d145e39e5dd54982"));

	}
	
 
	 
	private static String Md5(String plainText)throws Exception {
	  String result = null;
	  try {
	   MessageDigest md = MessageDigest.getInstance("MD5");
	   md.update(plainText.getBytes(DEFAULT_ENCODING));
	   byte b[] = md.digest();
	   int i;
	   StringBuffer buf = new StringBuffer("");
	   for (int offset = 0; offset < b.length; offset++) {
	    i = b[offset];
	    if (i < 0) {
            i += 256;
        }
	    if (i < 16) {
            buf.append("0");
        }
	    buf.append(Integer.toHexString(i));
	   }
	   // result = buf.toString();  //md5 32bit
	   // result = buf.toString().substring(8, 24))); //md5 16bit
	   result = buf.toString().substring(8, 24);
	   System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
	   System.out.println("md5 32bit: " + buf.toString() );
	  } catch (NoSuchAlgorithmException e) {
	   e.printStackTrace();
	  }
	  return result;
	}
 
}
