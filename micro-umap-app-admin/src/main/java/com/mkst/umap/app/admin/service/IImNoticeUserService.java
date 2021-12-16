package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.ImNoticeUser;
import java.util.List;

/**
 * IM消息通知用户映射 服务层
 * 
 * @author wangyong
 * @date 2021-04-12
 */
public interface IImNoticeUserService 
{
	/**
     * 查询IM消息通知用户映射信息
     * 
     * @param id IM消息通知用户映射ID
     * @return IM消息通知用户映射信息
     */
	public ImNoticeUser selectImNoticeUserById(Integer id);

	public ImNoticeUser selectImNoticeUserByTypeKey(String typeKey);
	
	/**
     * 查询IM消息通知用户映射列表
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return IM消息通知用户映射集合
     */
	public List<ImNoticeUser> selectImNoticeUserList(ImNoticeUser imNoticeUser);
	
	/**
     * 新增IM消息通知用户映射
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return 结果
     */
	public int insertImNoticeUser(ImNoticeUser imNoticeUser);
	
	/**
     * 修改IM消息通知用户映射
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return 结果
     */
	public int updateImNoticeUser(ImNoticeUser imNoticeUser);
		
	/**
     * 删除IM消息通知用户映射信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteImNoticeUserByIds(String ids);
	
}
