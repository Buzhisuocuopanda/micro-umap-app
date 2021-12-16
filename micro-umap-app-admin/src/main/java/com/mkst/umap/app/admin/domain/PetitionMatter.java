package com.mkst.umap.app.admin.domain;

import lombok.Data;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 信访民行申诉事项表 umap_petition_matter
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Data
public class PetitionMatter
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 事项类型 */
	private String type;
	/** 法院 */
	private String court;
	/** 法院 */
	private String referee;
	/** 裁决/调解日期 */
	private Date rulingTime;
	/** 裁决/调解书文号 */
	private String rulingSymbol;
	/** 法院受理再审日期 */
	private Date retrialTime;
	/** 法院受理再审申请通知书文号 */
	private String retrialSymbol;
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
}
