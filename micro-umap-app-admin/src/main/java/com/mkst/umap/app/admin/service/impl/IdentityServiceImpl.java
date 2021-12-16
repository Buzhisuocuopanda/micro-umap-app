package com.mkst.umap.app.admin.service.impl;

import java.util.List;

import com.mkst.mini.systemplus.system.domain.SysPost;
import com.mkst.mini.systemplus.system.domain.SysUserPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.IdentityMapper;
import com.mkst.umap.app.admin.domain.Identity;
import com.mkst.umap.app.admin.service.IIdentityService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 申请身份 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-19
 */
@Service
public class IdentityServiceImpl implements IIdentityService 
{
	@Autowired
	private IdentityMapper identityMapper;

	/**
     * 查询申请身份信息
     * 
     * @param id 申请身份ID
     * @return 申请身份信息
     */
    @Override
	public Identity selectIdentityById(Integer id)
	{
	    return identityMapper.selectIdentityById(id);
	}
	
	/**
     * 查询申请身份列表
     * 
     * @param identity 申请身份信息
     * @return 申请身份集合
     */
	@Override
	public List<Identity> selectIdentityList(Identity identity)
	{
	    return identityMapper.selectIdentityList(identity);
	}
	
    /**
     * 新增申请身份
     * 
     * @param identity 申请身份信息
     * @return 结果
     */
	@Override
	public int insertIdentity(Identity identity)
	{
	    return identityMapper.insertIdentity(identity);
	}
	
	/**
     * 修改申请身份
     * 
     * @param identity 申请身份信息
     * @return 结果
     */
	@Override
	public int updateIdentity(Identity identity)
	{
	    return identityMapper.updateIdentity(identity);
	}

	/**
     * 删除申请身份对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteIdentityByIds(String ids)
	{
		return identityMapper.deleteIdentityByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<SysPost> selectPostList(Long userId) {
		return identityMapper.selectPostList(userId);
	}

	@Override
	public SysUserPost selectUserPost(Long userId, Long postId) {
		return identityMapper.selectUserPost(userId,postId);
	}

}
