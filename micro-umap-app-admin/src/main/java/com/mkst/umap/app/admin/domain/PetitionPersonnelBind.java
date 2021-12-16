package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;

/**
 * 信访与人员连接表 umap_petition_personnel_bind
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Data
public class PetitionPersonnelBind extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 人员表id */
	private Long personnelId;
	/** 信访主键 */
	private Long petitionId;
}
