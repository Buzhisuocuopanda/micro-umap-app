package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import java.util.Date;

/**
 * 就餐预约表 umap_repast_appointment
 * 
 * @author wangyong
 * @date 2020-11-30
 */
public class RepastAppointment extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 就餐时间 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date repastDate;
	/** 参数 */
	private String type;
	/** 是否有为不人员 */
	private String outsider;
	/** 外来人员人数 */
	private Integer outsiderNum;
	/** 申请人 */
	private Integer userId;
	/** 创建人 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;
	/** 删除标识 */
	private String delFlag;

	private String userName;

	private String countUser;

	public String getCountUser() {
		return countUser;
	}

	public void setCountUser(String countUser) {
		this.countUser = countUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setRepastDate(Date repastDate) 
	{
		this.repastDate = repastDate;
	}

	public Date getRepastDate() 
	{
		return repastDate;
	}
	public void setType(String type) 
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	public void setOutsider(String outsider) 
	{
		this.outsider = outsider;
	}

	public String getOutsider() 
	{
		return outsider;
	}
	public void setOutsiderNum(Integer outsiderNum) 
	{
		this.outsiderNum = outsiderNum;
	}

	public Integer getOutsiderNum() 
	{
		return outsiderNum;
	}
	public void setUserId(Integer userId) 
	{
		this.userId = userId;
	}

	public Integer getUserId() 
	{
		return userId;
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
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}
	public void setDelFlag(String delFlag) 
	{
		this.delFlag = delFlag;
	}

	public String getDelFlag() 
	{
		return delFlag;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("repastDate", getRepastDate())
            .append("type", getType())
            .append("outsider", getOutsider())
            .append("outsiderNum", getOutsiderNum())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
