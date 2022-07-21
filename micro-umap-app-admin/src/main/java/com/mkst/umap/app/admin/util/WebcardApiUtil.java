package com.mkst.umap.app.admin.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.uitls.StringUtils;
import com.mkst.mini.systemplus.util.SysConfigUtil;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 设备管理子系统接口工具类
 * 
 * @author lijinghui
 *
 */
@Slf4j
public class WebcardApiUtil {

	public static String sendPost(String url, Map<String, Object> reqParamMap) {
		String webCardUrl = SysConfigUtil.getKey("webcard_url");
		String webCardAppId = SysConfigUtil.getKey("webcard_appid");
		String webCardSecret = SysConfigUtil.getKey("webcard_secret");
		url = webCardUrl + url;
		long t = System.currentTimeMillis();
		StringBuilder query = new StringBuilder();
		query.append("appId").append(webCardAppId);
		query.append("t").append(t);
		query.append(webCardSecret);
		String sign = SecureUtil.md5(query.toString());
		url += "?appId=" + webCardAppId + "&t=" + t + "&sign=" + sign;
		String jsonParam = JSON.toJSONString(reqParamMap);
		try {
			String responseStr = HttpUtil.createPost(url).body(jsonParam, ContentType.JSON.getValue()).execute().body();
			if (StringUtils.isBlank(responseStr)) {
				log.error("设备管理子系统接口调用异常，返回结果为空。\n请求地址：{}\n请求参数：{}\n响应结果：{}", url, jsonParam, responseStr);
				throw new RuntimeException("设备管理子系统接口调用异常，返回结果为空。");
			}
			JSONObject response = JSON.parseObject(responseStr);
			if (response.getInteger("code") != 0) {
				log.error("设备管理子系统接口调用异常，返回结果为空。\n请求地址：{}\n请求参数：{}\n响应结果：{}", url, jsonParam, responseStr);
				throw new RuntimeException("设备管理子系统接口调用异常，错误描述：" + response.getString("msg"));
			}
			return response.getString("data");
		} catch (Exception e) {
			log.error("设备管理子系统接口调用异常，返回结果为空。\n请求地址：{}\n请求参数：{}\n响应结果：{}", url, jsonParam, null, e);
			throw new RuntimeException("设备管理子系统接口调用异常，错误描述：" + e.getMessage());
		}
	}

}
