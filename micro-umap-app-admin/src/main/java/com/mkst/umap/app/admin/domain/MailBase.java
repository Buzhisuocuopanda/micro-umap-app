package com.mkst.umap.app.admin.domain;

import lombok.Data;

import java.util.Date;

/**
 * 邮件基础表 umap_mail_base
 * 
 * @author wangyong
 * @date 2020-09-24
 */
@Data
public class MailBase
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 类型 */
	private String type;
	private String[] typeArr;
	/** 是否已回复 */
	private String hasReplied;
	/** 标题 */
	private String title;
	/** 详细内容 */
	private String content;
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

	private Date checkDate;
}
