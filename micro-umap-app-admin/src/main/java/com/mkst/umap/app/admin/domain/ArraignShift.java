package com.mkst.umap.app.admin.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 提审换班表 umap_arraign_shift
 * 
 * @author wangyong
 * @date 2021-04-15
 */
@ApiModel(value = "换班申请对象")
public class ArraignShift extends BaseEntity
{
	/**初始化*/
	public static final String STATUS_INIT = "0";
	/**同意*/
	public static final String STATUS_AGREE = "1";
	/**驳回*/
	public static final String STATUS_REJECT = "2";


	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	@ApiModelProperty("流水号")
	private Integer id;
	private Long applyArraignAppointmentId;//申请ID
	private Long targetArraignAppointmentId;//被申请ID
	/** 申请人ID */
	@ApiModelProperty("申请人ID")
	private Long applyUserId;
	@ApiModelProperty("申请人姓名")
	private String applyUserName;
	/** 申请源日期 */
	@ApiModelProperty("申请源日期")
	private String applySourceDate;
	/** 申请时间段1上午，2下午 */
	@ApiModelProperty("申请源时间段，1上午，2下午")
	private String applyTimePie;
	/** 申请源场次 */
	@ApiModelProperty("申请源场次")
	private Integer applyNumOrder;
	/** 被申请人ID */
	@ApiModelProperty("被申请人ID")
	private Long targetUserId;
	@ApiModelProperty("被申请人姓名")
	private String targetUserName;
	/** 被申请人日期 */
	@ApiModelProperty("申请更换日期")
	private String targetDate;
	/** 被申请时间段1上午，2下午 */
	@ApiModelProperty("申请更换时间段，1上午，2下午")
	private String targetTimePie;
	/**  */
	@ApiModelProperty("申请更换场次")
	private Integer targetNumOrder;
	/** 状态 */
	@ApiModelProperty("状态，0初始化，1同意，2驳回")
	private String status;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 修改人 */
	private String updateBy;
	/** 修改时间 */
	private Date updateTime;

	private Date checkDate;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setApplyUserId(Long applyUserId)
	{
		this.applyUserId = applyUserId;
	}

	public Long getApplyUserId()
	{
		return applyUserId;
	}
	public void setApplySourceDate(String applySourceDate)
	{
		this.applySourceDate = applySourceDate;
	}

	public String getApplySourceDate()
	{
		return applySourceDate;
	}
	public void setApplyTimePie(String applyTimePie) 
	{
		this.applyTimePie = applyTimePie;
	}

	public String getApplyTimePie() 
	{
		return applyTimePie;
	}
	public void setApplyNumOrder(Integer applyNumOrder) 
	{
		this.applyNumOrder = applyNumOrder;
	}

	public Integer getApplyNumOrder() 
	{
		return applyNumOrder;
	}
	public void setTargetUserId(Long targetUserId)
	{
		this.targetUserId = targetUserId;
	}

	public Long getTargetUserId()
	{
		return targetUserId;
	}
	public void setTargetDate(String targetDate)
	{
		this.targetDate = targetDate;
	}

	public String getTargetDate()
	{
		return targetDate;
	}
	public void setTargetTimePie(String targetTimePie) 
	{
		this.targetTimePie = targetTimePie;
	}

	public String getTargetTimePie() 
	{
		return targetTimePie;
	}
	public void setTargetNumOrder(Integer targetNumOrder) 
	{
		this.targetNumOrder = targetNumOrder;
	}

	public Integer getTargetNumOrder() 
	{
		return targetNumOrder;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setCreateBy(String createBy) 
	{
		this.createBy = createBy;
	}

	public String getCreateBy() 
	{
		return createBy;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setUpdateBy(String updateBy) 
	{
		this.updateBy = updateBy;
	}

	public String getUpdateBy() 
	{
		return updateBy;
	}
	public void setUpdateTime(Date updateTime) 
	{
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() 
	{
		return updateTime;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Long getApplyArraignAppointmentId() {
		return applyArraignAppointmentId;
	}

	public void setApplyArraignAppointmentId(Long applyArraignAppointmentId) {
		this.applyArraignAppointmentId = applyArraignAppointmentId;
	}

	public Long getTargetArraignAppointmentId() {
		return targetArraignAppointmentId;
	}

	public void setTargetArraignAppointmentId(Long targetArraignAppointmentId) {
		this.targetArraignAppointmentId = targetArraignAppointmentId;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("applyUserId", getApplyUserId())
			.append("applyArraignAppointmentId", getApplyArraignAppointmentId())
			.append("targetArraignAppointmentId", getTargetArraignAppointmentId())
			.append("applyUserName", getApplyUserName())
            .append("applySourceDate", getApplySourceDate())
            .append("applyTimePie", getApplyTimePie())
            .append("applyNumOrder", getApplyNumOrder())
            .append("targetUserId", getTargetUserId())
			.append("targetUserName", getTargetUserName())
            .append("targetDate", getTargetDate())
            .append("targetTimePie", getTargetTimePie())
            .append("targetNumOrder", getTargetNumOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
