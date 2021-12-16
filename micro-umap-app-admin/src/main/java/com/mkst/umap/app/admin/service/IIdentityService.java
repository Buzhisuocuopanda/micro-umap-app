package com.mkst.umap.app.admin.service;

import com.mkst.mini.systemplus.system.domain.SysPost;
import com.mkst.mini.systemplus.system.domain.SysUserPost;
import com.mkst.umap.app.admin.domain.Identity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申请身份 服务层
 * 
 * @author wangyong
 * @date 2020-08-19
 */
public interface IIdentityService 
{
	/**
     * 查询申请身份信息
     * 
     * @param id 申请身份ID
     * @return 申请身份信息
     */
	public Identity selectIdentityById(Integer id);
	
	/**
     * 查询申请身份列表
     * 
     * @param identity 申请身份信息
     * @return 申请身份集合
     */
	public List<Identity> selectIdentityList(Identity identity);
	
	/**
     * 新增申请身份
     * 
     * @param identity 申请身份信息
     * @return 结果
     */
	public int insertIdentity(Identity identity);
	
	/**
     * 修改申请身份
     * 
     * @param identity 申请身份信息
     * @return 结果
     */
	public int updateIdentity(Identity identity);
		
	/**
     * 删除申请身份信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIdentityByIds(String ids);

	List<SysPost> selectPostList(Long userId);

	SysUserPost selectUserPost(Long userId, Long postId);
	
}
