package com.mkst.umap.app.admin.domain;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.umap.app.admin.service.ICarApplyService;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 车辆申请管理表 umap_car_apply
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Data
public class CarApply extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 车辆预约申请id */
	private Long carApplyId;
	/** 车辆id */
	private Long carId;
	/** 申请人 */
	private Long userId;
	/** 司机id */
	private Long driverId;
	/** 联系方式 */
	private String telphone;
	/** 是否需要司机 */
	private Boolean driverWhether;
	/** 人数（含司机） */
	private Integer peopleNumber;

	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm" , timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
	private Date startTime;
	/** 结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm",  timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH-mm")
	private Date endTime;
	/** 地区选择 */
	private String areaSelect;
	/** 起点位置id */
	private Long startLocationId;
	/** 终点位置id */
	private Long endLocationId;
	/** 审批状态  0 待审核  1 通过  2失败  3取消 */
	private String approveStatus;
	/** 接单状态 1待处理 2已完成 3已报销 */
	private Integer driverStatus;
	/** 逻辑删除标识（0：正常 1：删除）*/
	private String delFlag;

	private String applicant;

	/** 车牌号 */
	private String licensePlateNumber;
	private String userName;
	/**
	 * 进程
	 */
	private Integer progress;
	/** 申请状态 0 待审核  1 通过  2失败  3取消*/
	private Integer applyStatus;

	/**
	 * 审核人id
	 */
	private Integer approvalUserId;

	/**
	 * 费用类型
	 */
	private String feeType;
	/**
	 * 费用总额
	 */
	private String feeTotal;

	/**
	 * 司机姓名
	 */
	private String driverName;

	/**
	 * 接单状态 0为未完成 1为已完成
	 */
	private Integer orderState;
	private String status;

	private MapLocation startPoint;
	private MapLocation endPoint;

	/**
	 * 是否需要额外审批
	 */
	private Boolean needExtraApproval;

	private boolean isCarApprove;

	public Boolean getNeedExtraApproval() {
		Boolean isTimeOver1Day = isTimeOver1Day();
		if (this.needExtraApproval == null && isTimeOver1Day != null) {
			return "市外".equals(this.areaSelect) && isTimeOver1Day;
		} else {
			return needExtraApproval;
		}
	}

	public Boolean isTimeOver1Day() {
		if (this.startTime != null && this.endTime != null) {
			return !DateUtils.isSameDay(startTime, endTime);
		} else {
			return null;
		}
	}

	public void setNeedExtraApproval(Boolean needExtraApproval) {
		this.needExtraApproval = needExtraApproval;
	}

	public void setNeedExtraApproval() {
		this.needExtraApproval = this.areaSelect.equals("市外") && this.isTimeOver1Day();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
