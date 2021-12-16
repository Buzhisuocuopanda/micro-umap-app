package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.result.basecase.PointResult;
import com.mkst.umap.app.admin.domain.CaseBase;
import java.util.List;

/**
 * 案件 服务层
 * 
 * @author wangyong
 * @date 2021-06-22
 */
public interface ICaseBaseService 
{
	/**
     * 查询案件信息
     * 
     * @param id 案件ID
     * @return 案件信息
     */
	public CaseBase selectCaseBaseById(Integer id);
	
	/**
     * 查询案件列表
     * 
     * @param caseBase 案件信息
     * @return 案件集合
     */
	public List<CaseBase> selectCaseBaseList(CaseBase caseBase);
	
	/**
     * 新增案件
     * 
     * @param caseBase 案件信息
     * @return 结果
     */
	public int insertCaseBase(CaseBase caseBase);
	
	/**
     * 修改案件
     * 
     * @param caseBase 案件信息
     * @return 结果
     */
	public int updateCaseBase(CaseBase caseBase);
		
	/**
     * 删除案件信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCaseBaseByIds(String ids);

	/**
	 * 刑期统计
	 * @param caseBase
	 * @return
	 */
	public List<PointResult> statisticsPrisonTerm(CaseBase caseBase);

	/**
	 * 罚金统计
	 * @param caseBase
	 * @return
	 */
	public List<PointResult> statisticsPenalties(CaseBase caseBase);

	/**
	 * 缓刑统计
	 * @param caseBase
	 * @return
	 */
	public List<PointResult> statisticsSuspension(CaseBase caseBase);
	
}
