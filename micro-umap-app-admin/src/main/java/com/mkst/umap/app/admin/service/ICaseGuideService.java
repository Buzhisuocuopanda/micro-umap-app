package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.CaseGuide;
import java.util.List;

/**
 * 指导案例 服务层
 * 
 * @author wangyong
 * @date 2021-06-25
 */
public interface ICaseGuideService 
{
	/**
     * 查询指导案例信息
     * 
     * @param id 指导案例ID
     * @return 指导案例信息
     */
	public CaseGuide selectCaseGuideById(Integer id);
	
	/**
     * 查询指导案例列表
     * 
     * @param caseGuide 指导案例信息
     * @return 指导案例集合
     */
	public List<CaseGuide> selectCaseGuideList(CaseGuide caseGuide);
	
	/**
     * 新增指导案例
     * 
     * @param caseGuide 指导案例信息
     * @return 结果
     */
	public int insertCaseGuide(CaseGuide caseGuide);
	
	/**
     * 修改指导案例
     * 
     * @param caseGuide 指导案例信息
     * @return 结果
     */
	public int updateCaseGuide(CaseGuide caseGuide);
		
	/**
     * 删除指导案例信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCaseGuideByIds(String ids);
	
}
