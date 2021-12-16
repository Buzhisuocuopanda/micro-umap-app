package com.mkst.umap.app.admin.service.impl;

import java.util.List;

import com.mkst.umap.app.admin.api.bean.result.basecase.PointResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CaseBaseMapper;
import com.mkst.umap.app.admin.domain.CaseBase;
import com.mkst.umap.app.admin.service.ICaseBaseService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 案件 服务层实现
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Service
public class CaseBaseServiceImpl implements ICaseBaseService 
{
	@Autowired
	private CaseBaseMapper caseBaseMapper;

	/**
     * 查询案件信息
     * 
     * @param id 案件ID
     * @return 案件信息
     */
    @Override
	public CaseBase selectCaseBaseById(Integer id)
	{
	    return caseBaseMapper.selectCaseBaseById(id);
	}
	
	/**
     * 查询案件列表
     * 
     * @param caseBase 案件信息
     * @return 案件集合
     */
	@Override
	public List<CaseBase> selectCaseBaseList(CaseBase caseBase)
	{
	    return caseBaseMapper.selectCaseBaseList(caseBase);
	}
	
    /**
     * 新增案件
     * 
     * @param caseBase 案件信息
     * @return 结果
     */
	@Override
	public int insertCaseBase(CaseBase caseBase)
	{
	    return caseBaseMapper.insertCaseBase(caseBase);
	}
	
	/**
     * 修改案件
     * 
     * @param caseBase 案件信息
     * @return 结果
     */
	@Override
	public int updateCaseBase(CaseBase caseBase)
	{
	    return caseBaseMapper.updateCaseBase(caseBase);
	}

	/**
     * 删除案件对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCaseBaseByIds(String ids)
	{
		return caseBaseMapper.deleteCaseBaseByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<PointResult> statisticsPrisonTerm(CaseBase caseBase){
		return caseBaseMapper.statisticsPrisonTerm(caseBase);
	}

	/**
	 * 罚金统计
	 * @param caseBase
	 * @return
	 */
	@Override
	public List<PointResult> statisticsPenalties(CaseBase caseBase){
		return caseBaseMapper.statisticsPenalties(caseBase);
	}

	/**
	 * 缓刑统计
	 * @param caseBase
	 * @return
	 */
	@Override
	public List<PointResult> statisticsSuspension(CaseBase caseBase){
		return caseBaseMapper.statisticsSuspension(caseBase);
	}
	
}
