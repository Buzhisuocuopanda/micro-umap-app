package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.PetitionPersonnelBind;
import java.util.List;	

/**
 * 信访与人员连接 数据层
 * 
 * @author wangyong
 * @date 2020-08-25
 */
public interface PetitionPersonnelBindMapper 
{
	/**
     * 查询信访与人员连接信息
     * 
     * @param id 信访与人员连接ID
     * @return 信访与人员连接信息
     */
	public PetitionPersonnelBind selectPetitionPersonnelBindById(Long id);
	
	/**
     * 查询信访与人员连接列表
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 信访与人员连接集合
     */
	public List<PetitionPersonnelBind> selectPetitionPersonnelBindList(PetitionPersonnelBind petitionPersonnelBind);
	
	/**
     * 新增信访与人员连接
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 结果
     */
	public int insertPetitionPersonnelBind(PetitionPersonnelBind petitionPersonnelBind);
	
	/**
     * 修改信访与人员连接
     * 
     * @param petitionPersonnelBind 信访与人员连接信息
     * @return 结果
     */
	public int updatePetitionPersonnelBind(PetitionPersonnelBind petitionPersonnelBind);
	
	/**
     * 删除信访与人员连接
     * 
     * @param id 信访与人员连接ID
     * @return 结果
     */
	public int deletePetitionPersonnelBindById(Long id);
	
	/**
     * 批量删除信访与人员连接
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePetitionPersonnelBindByIds(String[] ids);
}