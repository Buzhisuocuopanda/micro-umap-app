package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 知识产权表 umap_intellectual_property
 *
 * @author wangyong
 * @date 2020-08-12
 */
@Data
public class IntellectualProperty extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 *
	 */
	private String title;
	/**
	 * 详细内容
	 */
	private String content;
	/**
	 * 是否被回复
	 */
	private String hasReplied;
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

	private Date checkDate;

}
