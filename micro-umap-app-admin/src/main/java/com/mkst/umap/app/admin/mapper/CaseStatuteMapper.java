package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.CaseStatute;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 案件相关法规 数据层
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Repository
public interface CaseStatuteMapper 
{
	/**
     * 查询案件相关法规信息
     * 
     * @param id 案件相关法规ID
     * @return 案件相关法规信息
     */
	public CaseStatute selectCaseStatuteById(Integer id);
	
	/**
     * 查询案件相关法规列表
     * 
     * @param caseStatute 案件相关法规信息
     * @return 案件相关法规集合
     */
	public List<CaseStatute> selectCaseStatuteList(CaseStatute caseStatute);
	
	/**
     * 新增案件相关法规
     * 
     * @param caseStatute 案件相关法规信息
     * @return 结果
     */
	public int insertCaseStatute(CaseStatute caseStatute);
	
	/**
     * 修改案件相关法规
     * 
     * @param caseStatute 案件相关法规信息
     * @return 结果
     */
	public int updateCaseStatute(CaseStatute caseStatute);
	
	/**
     * 删除案件相关法规
     * 
     * @param id 案件相关法规ID
     * @return 结果
     */
	public int deleteCaseStatuteById(Integer id);
	
	/**
     * 批量删除案件相关法规
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCaseStatuteByIds(String[] ids);
	
}