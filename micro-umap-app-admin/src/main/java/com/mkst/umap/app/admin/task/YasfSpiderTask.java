package com.mkst.umap.app.admin.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mkst.mini.systemplus.cms.domain.Article;
import com.mkst.mini.systemplus.cms.mapper.ArticleMapper;
import com.mkst.mini.systemplus.common.utils.Guid;
import com.mkst.mini.systemplus.spider.config.HunterConfig;
import com.mkst.mini.systemplus.spider.entity.ImageLink;
import com.mkst.mini.systemplus.spider.entity.VirtualArticle;
import com.mkst.mini.systemplus.spider.enums.ExitWayEnum;
import com.mkst.mini.systemplus.spider.processor.BlogHunterProcessor;
import com.mkst.mini.systemplus.spider.processor.HunterProcessor;
import com.mkst.umap.app.admin.util.ImageDownloadUtil;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 普法宣传 - 以案释法爬虫定时任务
 *
 * @author gin
 */
@Slf4j
@Component("yasfSpiderTask")
public class YasfSpiderTask {

	@Autowired
	private ArticleMapper articleMapper;

	public void spiderSpp(String params) {
		log.info("爬取最高检以案释法定时任务开始");
		try {
			List<String> startUrls = new ArrayList<String>();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			String entryUrl = "https://www.spp.gov.cn/spp/qwfb/"+ year +"nian/index.shtml";
			startUrls.add(entryUrl);
			
			// 被迫无奈，不得已按照这么笨的方法初始化数据，分页钻取不支持
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2014nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2015nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2016nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2017nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2018nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2019nian/index.shtml");
//			startUrls.add("https://www.spp.gov.cn/spp/qwfb/2020nian/index.shtml");
//			for (int i = 2; i<= 10; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2014nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 18; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2015nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 14; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2016nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 11; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2017nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 11; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2018nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 16; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2019nian/index_"+i+".shtml");
//			}
//			for (int i = 2; i<= 13; i++) {
//				startUrls.add("https://www.spp.gov.cn/spp/qwfb/2020nian/index_"+i+".shtml");
//			}
			
			HunterConfig config = new HunterConfig();
			config.setEntryUrls(startUrls)
					.setTargetLinksRegex("https://www\\.spp\\.gov\\.cn/spp/qwfb/[0-9]{6}/[\\w\\d]+\\.shtml")
					.setDomain("www.spp.gov.cn")
					.setAuthorRegex("//div[@class=detail_extend1]/html()")
					.setTitleRegex("//div[@class=detail_tit]/html()")
					.setReleaseDateRegex("//div[@class=detail_extend1]/html()/regex('\\d{4}-\\d{2}-\\d{2}')")
					.setContentRegex("//div[@id=fontzoom]/html()")
					.setConvertImg(true);
			config.setExitWay(ExitWayEnum.URL_COUNT).setCount(Convert.toInt(params));
			HunterProcessor hunter = new BlogHunterProcessor(config);
			CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
			int n = 0;
			if (null == list || list.isEmpty()) {
				log.warn("爬取最高检以案释法定时任务结束：获取数据为空");
			} else {
				Article article = null;
				for (VirtualArticle virtualArticle : list) {
					// 检查文章是否已经存在
					article = new Article();
					article.setCategoryId("69"); // 以案释法分类：69
					article.setTitle(virtualArticle.getTitle());
					List<Article> articleList = articleMapper.selectArticleList(article);
					if(articleList != null && articleList.size() > 0) {
						continue;
					}
					// 不存在则插入
					article.setId(Guid.get());
					String author = virtualArticle.getAuthor();
					if(StringUtils.isNotBlank(author)) {
						String[] authorStr = author.split("作者：");
						if(authorStr.length > 1) {
							author = authorStr[1].split("来源：")[0].trim();
							if(StringUtils.isNotBlank(author)) {
								author = author.replaceAll("　", "");
								article.setAuthor(author);
							}
						}
					}
					// 下载图文中的图片
					String content = virtualArticle.getContent();
					String urlPrefix = virtualArticle.getSource().substring(0, virtualArticle.getSource().lastIndexOf("/"));
					if(virtualArticle.getImageLinks() != null && virtualArticle.getImageLinks().size() > 0) {
						String[] imgUrls = new String[virtualArticle.getImageLinks().size()];
						int i = 0;
						for (ImageLink imageLink : virtualArticle.getImageLinks()) {
							imgUrls[i] = imageLink.getSrcLink();
							i++;
						}
						content = ImageDownloadUtil.downloadAndReplaceImage(urlPrefix, content, imgUrls);
					}
					// 替换图文中的视频地址
					String videoUrlPrefix = virtualArticle.getSource().substring(0, virtualArticle.getSource().indexOf("/",8));
					content = ImageDownloadUtil.replaceVideoUrl(videoUrlPrefix, content);
					article.setContent(content);
					article.setCreateTime(virtualArticle.getReleaseDate());
					article.setUpdateTime(virtualArticle.getReleaseDate());
					article.setExtra1("高检网");
					article.setCopyFlag(1);
					article.setLink(virtualArticle.getSource());

					articleMapper.insertArticle(article);
					n += articleMapper.insertArticleContent(article);
				}
			}
			log.info("爬取最高检以案释法定时任务结束：同步数据{}条", n);
		} catch (Exception e) {
			log.error("爬取最高检以案释法定时任务结束：获取数据失败：{}", e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		new YasfSpiderTask().spiderSpp("50");
	}
}
