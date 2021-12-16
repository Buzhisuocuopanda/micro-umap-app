package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 龙华文书同步表 umap_lh_ws
 * 
 * @author wangyong
 * @date 2020-09-15
 */
@Data
public class LhWs extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 龙华文书id */
	private String wsId;
	/** 区域 */
	private String area;
	/** 内容 */
	private String content;
	/** 标题 */
	private String title;
	/** 类型 */
	private String type;
	/** 发布时间 */
	private String publishTime;
	/** 图片 */
	private String img;
	/** 作者 */
	private String author;
	/** 同步时间 */
	private Date syncTime;
	/** 扩展字段2 */
	private Long replyNum;
	/** 扩展字段3 */
	private String extend3;
	/** 扩展字段4 */
	private String extend4;
	/** 逻辑删除标志 0 正常  1删除 */
	private String delFlag;

}
