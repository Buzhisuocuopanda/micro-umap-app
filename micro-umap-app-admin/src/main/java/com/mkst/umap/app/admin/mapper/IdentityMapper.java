package com.mkst.umap.app.admin.mapper;

import com.mkst.mini.systemplus.system.domain.SysPost;
import com.mkst.mini.systemplus.system.domain.SysUserPost;
import com.mkst.umap.app.admin.domain.Identity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 申请身份 数据层
 * 
 * @author wangyong
 * @date 2020-08-19
 */
public interface IdentityMapper 
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
     * 删除申请身份
     * 
     * @param id 申请身份ID
     * @return 结果
     */
	public int deleteIdentityById(Integer id);
	
	/**
     * 批量删除申请身份
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteIdentityByIds(String[] ids);

	@Select("SELECT " +
			"post_id AS PostId, " +
			"post_name AS postName " +
			"FROM " +
			"sys_post " +
			"WHERE " +
			"post_code != 'zhjw' " +
			"AND post_code != 'lawyer' " +
			"AND post_code != 'ptyy' " +
			"AND post_code != 'user' " +
			"AND status = '0' " +
			"AND post_id not in( select post_id from sys_user_post where user_id = ${userId} )")
	List<SysPost> selectPostList(@Param("userId") Long userId);

	@Select("select post_id AS PostId, user_id AS userId from sys_user_post where user_id = ${userId} and post_id = ${postId} ")
	SysUserPost selectUserPost(@Param("userId") Long userId, @Param("postId") Long postId);

}