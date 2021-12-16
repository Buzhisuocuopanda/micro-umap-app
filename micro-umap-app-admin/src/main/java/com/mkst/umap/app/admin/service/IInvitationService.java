package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.Invitation;
import java.util.List;

/**
 * 邀请码 服务层
 * 
 * @author wangyong
 * @date 2020-08-13
 */
public interface IInvitationService 
{
	/**
     * 查询邀请码信息
     * 
     * @param id 邀请码ID
     * @return 邀请码信息
     */
	public Invitation selectInvitationById(Integer id);
	
	/**
     * 查询邀请码列表
     * 
     * @param invitation 邀请码信息
     * @return 邀请码集合
     */
	public List<Invitation> selectInvitationList(Invitation invitation);
	
	/**
     * 新增邀请码
     * 
     * @param invitation 邀请码信息
     * @return 结果
     */
	public int insertInvitation(Invitation invitation);
	
	/**
     * 修改邀请码
     * 
     * @param invitation 邀请码信息
     * @return 结果
     */
	public int updateInvitation(Invitation invitation);
		
	/**
     * 删除邀请码信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteInvitationByIds(String ids);
	
}
