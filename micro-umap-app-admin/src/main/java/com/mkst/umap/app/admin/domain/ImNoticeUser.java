package com.mkst.umap.app.admin.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.mkst.mini.systemplus.common.base.BaseEntity;

/**
 * IM消息通知用户映射表 umap_im_notice_user
 * 
 * @author wangyong
 * @date 2021-04-12
 */
public class ImNoticeUser extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 消息类型 */
	private String msgTypeKey;
	/**  */
	private Integer userId;

	private String username;
	/** 是否置顶 */
	private Integer isTop;
	/**  */
	private Integer topOrder;

	private String avatar;//头像

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setMsgTypeKey(String msgTypeKey) 
	{
		this.msgTypeKey = msgTypeKey;
	}

	public String getMsgTypeKey() 
	{
		return msgTypeKey;
	}
	public void setUserId(Integer userId) 
	{
		this.userId = userId;
	}

	public Integer getUserId() 
	{
		return userId;
	}
	public void setIsTop(Integer isTop) 
	{
		this.isTop = isTop;
	}

	public Integer getIsTop() 
	{
		return isTop;
	}
	public void setTopOrder(Integer topOrder) 
	{
		this.topOrder = topOrder;
	}

	public Integer getTopOrder() 
	{
		return topOrder;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("msgTypeKey", getMsgTypeKey())
            .append("userId", getUserId())
			.append("username", getUsername())
            .append("isTop", getIsTop())
            .append("topOrder", getTopOrder())
			.append("avatar", getAvatar())
            .toString();
    }
}
