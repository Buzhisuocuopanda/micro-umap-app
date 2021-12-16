package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import com.mkst.umap.app.admin.statistics.AppAnalysisResult;

import java.util.List;
import java.util.Map;

/**
 * 拜访申请 服务层
 * 
 * @author wangyong
 * @date 2020-08-13
 */
public interface IVisitApplyService 
{
	/**
     * 查询拜访申请信息
     * 
     * @param id 拜访申请ID
     * @return 拜访申请信息
     */
	public VisitApply selectVisitApplyById(Integer id);
	
	/**
     * 查询拜访申请列表
     * 
     * @param visitApply 拜访申请信息
     * @return 拜访申请集合
     */
	public List<VisitApply> selectVisitApplyList(VisitApply visitApply);
	
	/**
     * 新增拜访申请
     * 
     * @param visitApply 拜访申请信息
     * @return 结果
     */
	public int insertVisitApply(VisitApply visitApply);
	
	/**
     * 修改拜访申请
     * 
     * @param visitApply 拜访申请信息
     * @return 结果
     */
	public int updateVisitApply(VisitApply visitApply);
		
	/**
     * 删除拜访申请信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteVisitApplyByIds(String ids);

    List<AnalysisCountResult> analysisData(Map<String, Object> params);
}
