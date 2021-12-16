package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 接待预约表 umap_reception_appointment
 *
 * @author wangyong
 * @date 2020-07-08
 */
@Data
public class ReceptionAppointment extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 *
	 */
	private String roomId;
	/**
	 *
	 */
	private Date startTime;
	/**
	 *
	 */
	private Date endTime;
	/**
	 *
	 */
	private String type;
	/**
	 *
	 */
	private Long deptId;
	/**
	 *
	 */
	private Long useBy;
	/**
	 *
	 */
	private String status;
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
