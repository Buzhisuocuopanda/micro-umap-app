package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.PetitionMatter;

import java.util.HashMap;
import java.util.List;

/**
 * 信访民行申诉事项 数据层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface PetitionMatterMapper 
{
	/**
     * 查询信访民行申诉事项信息
     * 
     * @param id 信访民行申诉事项ID
     * @return 信访民行申诉事项信息
     */
	public PetitionMatter selectPetitionMatterById(Long id);
	
	/**
     * 查询信访民行申诉事项列表
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 信访民行申诉事项集合
     */
	public List<PetitionMatter> selectPetitionMatterList(PetitionMatter petitionMatter);
	
	/**
     * 新增信访民行申诉事项
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 结果
     */
	public int insertPetitionMatter(PetitionMatter petitionMatter);
	
	/**
     * 修改信访民行申诉事项
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 结果
     */
	public int updatePetitionMatter(PetitionMatter petitionMatter);
	
	/**
     * 删除信访民行申诉事项
     * 
     * @param id 信访民行申诉事项ID
     * @return 结果
     */
	public int deletePetitionMatterById(Long id);
	
	/**
     * 批量删除信访民行申诉事项
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionMatterByIds(String[] ids);

    PetitionMatter getPetitionMatterBingList(HashMap<String, Object> daoParam);
}