package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 邀请码表 umap_invitation
 * 
 * @author wangyong
 * @date 2020-08-13
 */
public class Invitation extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 邀请人 */
	private Integer invitationUser;
	/** 邀请码 */
	private String code;
	/** 状态 */
	private String status;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setInvitationUser(Integer invitationUser) 
	{
		this.invitationUser = invitationUser;
	}

	public Integer getInvitationUser() 
	{
		return invitationUser;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("invitationUser", getInvitationUser())
            .append("code", getCode())
            .append("status", getStatus())
            .toString();
    }
}
