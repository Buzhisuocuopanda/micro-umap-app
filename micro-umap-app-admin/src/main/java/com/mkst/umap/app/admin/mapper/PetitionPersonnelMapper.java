package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.PetitionPersonnel;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 信访相关人员 数据层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface PetitionPersonnelMapper 
{
	/**
     * 查询信访相关人员信息
     * 
     * @param id 信访相关人员ID
     * @return 信访相关人员信息
     */
	public PetitionPersonnel selectPetitionPersonnelById(Long id);
	
	/**
     * 查询信访相关人员列表
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 信访相关人员集合
     */
	public List<PetitionPersonnel> selectPetitionPersonnelList(PetitionPersonnel petitionPersonnel);
	
	/**
     * 新增信访相关人员
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 结果
     */
	public int insertPetitionPersonnel(PetitionPersonnel petitionPersonnel);
	
	/**
     * 修改信访相关人员
     * 
     * @param petitionPersonnel 信访相关人员信息
     * @return 结果
     */
	public int updatePetitionPersonnel(PetitionPersonnel petitionPersonnel);
	
	/**
     * 删除信访相关人员
     * 
     * @param id 信访相关人员ID
     * @return 结果
     */
	public int deletePetitionPersonnelById(Long id);
	
	/**
     * 批量删除信访相关人员
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionPersonnelByIds(String[] ids);

    List<PetitionPersonnel> getPetitionPersonnelBingList(Map paramMap);
}