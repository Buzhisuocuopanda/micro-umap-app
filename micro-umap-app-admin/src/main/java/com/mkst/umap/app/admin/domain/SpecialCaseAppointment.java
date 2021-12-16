package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 专案预约表 umap_special_case_appointment
 *
 * @author wangyong
 * @date 2020-07-02
 */
@Data
public class SpecialCaseAppointment extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
     * 排班id
     */
    private Long scheduleId;
    /**
     * 专案tittle
     */
    private String title;
    /**
     * 房间Id
     */
    private String roomId;
	/**
	 * 开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
    /**
     * 使用部门id
     */
    private Long deptId;
    /**
     * 使用者
     */
    private String useBy;
    /**
     * 审核状态
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


	/**
	 * 查询时的参数-查询某天的记录
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date checkDate;

}
