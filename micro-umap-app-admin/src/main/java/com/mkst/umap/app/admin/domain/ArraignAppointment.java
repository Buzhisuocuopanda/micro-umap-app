package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.annotation.Excel;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 提审预约表 umap_arraign_appointment
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Data
public class ArraignAppointment extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
     * ID
     */
	@Excel(
			name = "序号"
	)
	private Long id;
	/**
	 * 排班id
	 */
	private Long scheduleId;
	/**
	 * 房间ID
	 */
	private String roomId;

	private String roomName;
	/**
	 * 开始时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Excel(
			name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss"
	)
	private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Excel(
			name = "结束时间",width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss"
	)
	private Date endTime;
	/**
	 * 罪犯姓名
	 */
	private String criminalName;
	/**
	 * 犯罪类型
	 */
	private String criminalType;
	/**
	 * 罪犯生日
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Excel(
			name = "罪犯生日",width = 30, dateFormat = "yyyy-MM-dd"
	)
	private Date criminalBirth;
	/**
	 * 罪犯认罪认罚
	 */
	@Excel(
			name = "罪犯认罪认罚", readConverterExp = "1=是,2=否"
	)
	private String criminalAdmit;
	/**
	 * 需要法律援助
	 */
	@Excel(
			name = "需要法律援助", readConverterExp = "1=是,2=否"
	)
	private String needLegalAid;
	/**
	 * 案件进展：批捕、已抓捕
	 */
	@Excel(
			name = "案件进展", readConverterExp = "0=批捕,1=起诉"
	)
	private String stage;
	/**
	 * 检查官id
	 */
	@Excel(
			name = "检查官证件号"
	)
	private String prosecutorId;

	private Long prosecutorUserId;

	private Long checkProsecutorUserId;
	/**
	 * 检察官姓名
	 */
	@Excel(
			name = "检察官"
	)
	private String prosecutorName;
	/**
	 * 审核状态
	 */
	@Excel(
			name = "审核状态", readConverterExp = "0=待审核,1=审核通过,2=审核未通过,4=取消"
	)
	private String status;
	/**
	 * 按日期查询时的日期
	 */
	private String appointmentDate;
	/**
	 * 创建人
	 */
	@Excel(
			name = "创建人"
	)
	private String createBy;

	private String checkCreateBy;

	/**
	 * 创建时间
	 */
	@Excel(
			name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss"
	)
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
	@Excel(
			name = "案由"
	)
    private String remark;
    /**
     * 删除标识
     */
    private String delFlag;
    /**数据库处理参数*/
    /**
     * 是否是待处理状态，此参数赋值时不可带status查询
     */
    private String needDeal;

    private String auditStatus;
    /** 提审日期查询参数 */
    private Date checkDate;

	@ApiModelProperty(value = "打印机名")
	private String printerName;


	/**
	 * @Description 提审类型（0：办案提审 1：社工调查）
	 */
	private String arraignType;

	private Integer numOrder;

	private String timePie;

	private String orderBy;

	private String cancelReason;

	/**
	 * 结束状态
	 */
	private String finishStatus;
	/**
	 * 结束时间
	 */
	private Date finishTime;
	/**
	 * 结束人
	 */
	private String finishBy;

}
