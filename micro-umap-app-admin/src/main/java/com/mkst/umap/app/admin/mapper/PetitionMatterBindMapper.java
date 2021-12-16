package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.PetitionMatterBind;
import java.util.List;	

/**
 * 信访民行申诉与事项连接 数据层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface PetitionMatterBindMapper 
{
	/**
     * 查询信访民行申诉与事项连接信息
     * 
     * @param id 信访民行申诉与事项连接ID
     * @return 信访民行申诉与事项连接信息
     */
	public PetitionMatterBind selectPetitionMatterBindById(Long id);
	
	/**
     * 查询信访民行申诉与事项连接列表
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 信访民行申诉与事项连接集合
     */
	public List<PetitionMatterBind> selectPetitionMatterBindList(PetitionMatterBind petitionMatterBind);
	
	/**
     * 新增信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	public int insertPetitionMatterBind(PetitionMatterBind petitionMatterBind);
	
	/**
     * 修改信访民行申诉与事项连接
     * 
     * @param petitionMatterBind 信访民行申诉与事项连接信息
     * @return 结果
     */
	public int updatePetitionMatterBind(PetitionMatterBind petitionMatterBind);
	
	/**
     * 删除信访民行申诉与事项连接
     * 
     * @param id 信访民行申诉与事项连接ID
     * @return 结果
     */
	public int deletePetitionMatterBindById(Long id);
	
	/**
     * 批量删除信访民行申诉与事项连接
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionMatterBindByIds(String[] ids);
	
}