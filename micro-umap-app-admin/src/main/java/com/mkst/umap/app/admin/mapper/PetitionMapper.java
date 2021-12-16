package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.Petition;

import java.util.List;

/**
 * 信访主体 数据层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface PetitionMapper 
{
	/**
     * 查询信访主体信息
     * 
     * @param id 信访主体ID
     * @return 信访主体信息
     */
	public Petition selectPetitionById(Long id);
	
	/**
     * 查询信访主体列表
     * 
     * @param petition 信访主体信息
     * @return 信访主体集合
     */
	public List<Petition> selectPetitionList(Petition petition);
	
	/**
     * 新增信访主体
     * 
     * @param petition 信访主体信息
     * @return 结果
     */
	public int insertPetition(Petition petition);
	
	/**
     * 修改信访主体
     * 
     * @param petition 信访主体信息
     * @return 结果
     */
	public int updatePetition(Petition petition);
	
	/**
     * 删除信访主体
     * 
     * @param id 信访主体ID
     * @return 结果
     */
	public int deletePetitionById(Long id);
	
	/**
     * 批量删除信访主体
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionByIds(String[] ids);

	public String getUserName();


	
}