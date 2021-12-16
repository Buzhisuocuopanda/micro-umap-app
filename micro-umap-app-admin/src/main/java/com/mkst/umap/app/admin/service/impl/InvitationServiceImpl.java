package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.InvitationMapper;
import com.mkst.umap.app.admin.domain.Invitation;
import com.mkst.umap.app.admin.service.IInvitationService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 邀请码 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-13
 */
@Service
public class InvitationServiceImpl implements IInvitationService 
{
	@Autowired
	private InvitationMapper invitationMapper;

	/**
     * 查询邀请码信息
     * 
     * @param id 邀请码ID
     * @return 邀请码信息
     */
    @Override
	public Invitation selectInvitationById(Integer id)
	{
	    return invitationMapper.selectInvitationById(id);
	}
	
	/**
     * 查询邀请码列表
     * 
     * @param invitation 邀请码信息
     * @return 邀请码集合
     */
	@Override
	public List<Invitation> selectInvitationList(Invitation invitation)
	{
	    return invitationMapper.selectInvitationList(invitation);
	}
	
    /**
     * 新增邀请码
     * 
     * @param invitation 邀请码信息
     * @return 结果
     */
	@Override
	public int insertInvitation(Invitation invitation)
	{
	    return invitationMapper.insertInvitation(invitation);
	}
	
	/**
     * 修改邀请码
     * 
     * @param invitation 邀请码信息
     * @return 结果
     */
	@Override
	public int updateInvitation(Invitation invitation)
	{
	    return invitationMapper.updateInvitation(invitation);
	}

	/**
     * 删除邀请码对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteInvitationByIds(String ids)
	{
		return invitationMapper.deleteInvitationByIds(Convert.toStrArray(ids));
	}
	
}
