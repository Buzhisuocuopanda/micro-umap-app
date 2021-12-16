package com.mkst.umap.app.admin.domain;

import lombok.Data;

import java.util.Date;

/**
 * 信访主体表 umap_petition
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Data
public class Petition
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 信访类型 */
	private String type;
	/** 标题 */
	private String title;
	/** 详细内容 */
	private String content;
	/** 已被回复 */
	private String hasReplied;
	/** 创建人 */
	private Long createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private Long updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;
	/** 删除标识 */
	private String delFlag;
	/*0.不匿名 1.匿名*/
	private String anonymous;
}
