package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 随手拍/公益举报表 umap_report
 * 
 * @author wangyong
 * @date 2020-08-27
 */
@Data
public class Report
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 举报类型 */
	private String type;
	/** 是否已回复 */
	private String hasReplied;
	/** 举报地址 */
	private String address;
	/** 是否实名（默认匿名） */
	private String realName;
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
