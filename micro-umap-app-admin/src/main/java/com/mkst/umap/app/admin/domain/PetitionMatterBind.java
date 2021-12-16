package com.mkst.umap.app.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;

/**
 * 信访民行申诉与事项连接表 umap_petition_matter_bind
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Data
public class PetitionMatterBind extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** id */
	private Long id;
	/** 事项表主键 */
	private Long matterId;
	/** 信访主键 */
	private Long petitionId;
}
