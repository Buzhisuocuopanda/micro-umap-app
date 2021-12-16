package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.ImNoticeUserMapper;
import com.mkst.umap.app.admin.domain.ImNoticeUser;
import com.mkst.umap.app.admin.service.IImNoticeUserService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * IM消息通知用户映射 服务层实现
 * 
 * @author wangyong
 * @date 2021-04-12
 */
@Service
public class ImNoticeUserServiceImpl implements IImNoticeUserService 
{
	@Autowired
	private ImNoticeUserMapper imNoticeUserMapper;

	/**
     * 查询IM消息通知用户映射信息
     * 
     * @param id IM消息通知用户映射ID
     * @return IM消息通知用户映射信息
     */
    @Override
	public ImNoticeUser selectImNoticeUserById(Integer id)
	{
	    return imNoticeUserMapper.selectImNoticeUserById(id);
	}

	@Override
	public ImNoticeUser selectImNoticeUserByTypeKey(String typeKey)
	{
		return imNoticeUserMapper.selectImNoticeUserByTypeKey(typeKey);
	}
	
	/**
     * 查询IM消息通知用户映射列表
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return IM消息通知用户映射集合
     */
	@Override
	public List<ImNoticeUser> selectImNoticeUserList(ImNoticeUser imNoticeUser)
	{
	    return imNoticeUserMapper.selectImNoticeUserList(imNoticeUser);
	}
	
    /**
     * 新增IM消息通知用户映射
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return 结果
     */
	@Override
	public int insertImNoticeUser(ImNoticeUser imNoticeUser)
	{
	    return imNoticeUserMapper.insertImNoticeUser(imNoticeUser);
	}
	
	/**
     * 修改IM消息通知用户映射
     * 
     * @param imNoticeUser IM消息通知用户映射信息
     * @return 结果
     */
	@Override
	public int updateImNoticeUser(ImNoticeUser imNoticeUser)
	{
	    return imNoticeUserMapper.updateImNoticeUser(imNoticeUser);
	}

	/**
     * 删除IM消息通知用户映射对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteImNoticeUserByIds(String ids)
	{
		return imNoticeUserMapper.deleteImNoticeUserByIds(Convert.toStrArray(ids));
	}
	
}
