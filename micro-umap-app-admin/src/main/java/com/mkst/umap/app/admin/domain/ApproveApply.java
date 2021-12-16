package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 审核申请表 umap_approve_apply
 * 
 * @author wangyong
 * @date 2020-07-21
 */
@Data
public class ApproveApply extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 审核id */
	private Long approveId;
	/** 审核人id */
	private Long useId;
	/** 申请id */
	private Long applyId;
	/** 审核状态 */
	private Integer approveStatus;
	/** 审核类型 */
	private String approveType;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新人 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 删除标识 */
	private String delFlag;
	/** 备注 */
	private String remark;
}
