package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import java.util.Map;

import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import com.mkst.umap.app.admin.statistics.AppAnalysisResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.VisitApplyMapper;
import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.service.IVisitApplyService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 拜访申请 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-13
 */
@Service
public class VisitApplyServiceImpl implements IVisitApplyService 
{
	@Autowired
	private VisitApplyMapper visitApplyMapper;

	/**
     * 查询拜访申请信息
     * 
     * @param id 拜访申请ID
     * @return 拜访申请信息
     */
    @Override
	public VisitApply selectVisitApplyById(Integer id)
	{
	    return visitApplyMapper.selectVisitApplyById(id);
	}
	
	/**
     * 查询拜访申请列表
     * 
     * @param visitApply 拜访申请信息
     * @return 拜访申请集合
     */
	@Override
	public List<VisitApply> selectVisitApplyList(VisitApply visitApply)
	{
	    return visitApplyMapper.selectVisitApplyList(visitApply);
	}
	
    /**
     * 新增拜访申请
     * 
     * @param visitApply 拜访申请信息
     * @return 结果
     */
	@Override
	public int insertVisitApply(VisitApply visitApply)
	{
	    return visitApplyMapper.insertVisitApply(visitApply);
	}
	
	/**
     * 修改拜访申请
     * 
     * @param visitApply 拜访申请信息
     * @return 结果
     */
	@Override
	public int updateVisitApply(VisitApply visitApply)
	{
	    return visitApplyMapper.updateVisitApply(visitApply);
	}

	/**
     * 删除拜访申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteVisitApplyByIds(String ids)
	{
		return visitApplyMapper.deleteVisitApplyByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<AnalysisCountResult> analysisData(Map<String, Object> params) {
		return visitApplyMapper.analysisData(params);
	}

}
