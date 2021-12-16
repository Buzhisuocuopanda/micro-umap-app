package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.CaseType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 案件类型 数据层
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Repository
public interface CaseTypeMapper 
{
	/**
     * 查询案件类型信息
     * 
     * @param id 案件类型ID
     * @return 案件类型信息
     */
	public CaseType selectCaseTypeById(Integer id);
	
	/**
     * 查询案件类型列表
     * 
     * @param caseType 案件类型信息
     * @return 案件类型集合
     */
	public List<CaseType> selectCaseTypeList(CaseType caseType);
	
	/**
     * 新增案件类型
     * 
     * @param caseType 案件类型信息
     * @return 结果
     */
	public int insertCaseType(CaseType caseType);
	
	/**
     * 修改案件类型
     * 
     * @param caseType 案件类型信息
     * @return 结果
     */
	public int updateCaseType(CaseType caseType);
	
	/**
     * 删除案件类型
     * 
     * @param id 案件类型ID
     * @return 结果
     */
	public int deleteCaseTypeById(Integer id);
	
	/**
     * 批量删除案件类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCaseTypeByIds(String[] ids);
	
}