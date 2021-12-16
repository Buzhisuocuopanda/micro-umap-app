package com.mkst.umap.app.admin.task;

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
 * 最高检指导性案例爬虫定时任务
 *
 * @author gin
 */
@Slf4j
@Component("zdCaseSpiderTask")
public class ZdCaseSpiderTask {

	@Autowired
	private ArticleMapper articleMapper;

	public void spiderSpp(String params) {
		log.info("爬取最高检指导性案例定时任务开始");
		try {
			HunterConfig config = new HunterConfig();
			config.setEntryUrls("https://www.spp.gov.cn/spp/jczdal/index.shtml")
					.setTargetLinksRegex("https://www\\.spp\\.gov\\.cn/spp/jczdal/[0-9]{6}/[\\w\\d]+\\.shtml")
					.setDomain("www.spp.gov.cn").setTitleRegex("//div[@class=detail_tit]/html()")
					.setAuthorRegex("//div[@class=detail_extend1]/html()")
					.setReleaseDateRegex("//div[@class=detail_extend1]/html()/regex('\\d{4}-\\d{2}-\\d{2}')")
					.setContentRegex("//div[@id=fontzoom]/html()")
					.setConvertImg(true);
			config.setExitWay(ExitWayEnum.URL_COUNT).setCount(Convert.toInt(params));
			HunterProcessor hunter = new BlogHunterProcessor(config);
			CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
			int n = 0;
			if (null == list || list.isEmpty()) {
				log.warn("爬取最高检指导性案例定时任务结束：获取数据为空");
			} else {
				Article article = null;
				for (VirtualArticle virtualArticle : list) {
					// 检查文章是否已经存在
					article = new Article();
					article.setCategoryId("39"); // 典型案例分类：39
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
			log.info("爬取最高检指导性案例定时任务结束：同步数据{}条", n);
		} catch (Exception e) {
			log.error("爬取最高检指导性案例定时任务结束：获取数据失败：{}", e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		new ZdCaseSpiderTask().spiderSpp("5");
	}
}
