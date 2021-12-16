package com.mkst.umap.app.admin.util;

import java.io.File;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.framework.util.FileUploadUtils;
import com.mkst.mini.systemplus.util.SysConfigUtil;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageDownloadUtil {

	/**
	 * 下载图文内容中的图片并替换图片url为下载后的本地图片url
	 * 
	 */
	public static String downloadAndReplaceImage(String urlPrefix, String content, String[] oldImgSrcs) {
		String[] newImgSrcs = new String[oldImgSrcs.length];

		for (int i = 0; i < oldImgSrcs.length; i++) {
			String imgpath = oldImgSrcs[i];
			if(!imgpath.startsWith("http:") && !imgpath.startsWith("https:")) {
				imgpath = urlPrefix + "/" + imgpath;
			}
			String imgExt = imgpath.split("\\.")[imgpath.split("\\.").length - 1];
			String fileName = DateUtils.datePath() + "/" + UUID.randomUUID().toString().replaceAll("-", "") + "." + imgExt;
			try {
				File imageFile = FileUploadUtils.getAbsoluteFile(Global.getProfile(), fileName);
				HttpUtil.downloadFile(imgpath, imageFile);
				newImgSrcs[i] = SysConfigUtil.getKey("image_url") + FileUploadUtils.getPathFileName(Global.getProfile(), fileName);
			} catch (Exception e) {
				newImgSrcs[i] = "";
				log.error("下载文章图片异常，图片地址：{}，错误原因：{}", imgpath, e.getMessage(), e);
			}
		}
		for (int j = 0; j < newImgSrcs.length; j++) {
			content = content.replace(oldImgSrcs[j], newImgSrcs[j]);
		}
		return content;
	}
	
	/**
	 * 替换图文中的视频地址为绝对路径
	 * @param content
	 */
	public static String replaceVideoUrl(String urlPrefix, String content) {
		content = content.replaceAll("'", "\"");
		//正则匹配所有视频标签
		String subVideoSrc = "";
		if (content.contains("video")) {
		    Pattern p = Pattern.compile("<video[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
		    Matcher m = p.matcher(content);

		    while (m.find()) {
		    	subVideoSrc += m.group(1) + ",";
		    }
		}
		// 替换地址
		if (StringUtils.isNotBlank(subVideoSrc)) {
			String[] oldVideoSrcs = subVideoSrc.split(",");
			String[] newVideoSrcs = new String[oldVideoSrcs.length];
			
			for (int i = 0; i < oldVideoSrcs.length; i++) {
				String videopath = oldVideoSrcs[i];
				if(!videopath.startsWith("http:") && !videopath.startsWith("https:")) {
					videopath = urlPrefix + videopath;
				}
				newVideoSrcs[i] = videopath;
			}
			for (int j = 0; j < newVideoSrcs.length; j++) {
				content = content.replaceAll(oldVideoSrcs[j], newVideoSrcs[j]);
		    }
		}
		return content;
	}
}
