package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 回复表 umap_reply
 *
 * @author wangyong
 * @date 2020-08-12
 */
@Data
public class Reply extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 父id
	 */
	private Long parentId;
	/**
	 * 业务类型
	 */
	private String businessType;
	/**
	 * 关联对象id
	 */
	private String objectId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 删除标识
	 */
	private String delFlag;
}
