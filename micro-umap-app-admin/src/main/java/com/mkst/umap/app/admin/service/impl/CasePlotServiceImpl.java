package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CasePlotMapper;
import com.mkst.umap.app.admin.domain.CasePlot;
import com.mkst.umap.app.admin.service.ICasePlotService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 案件计量 服务层实现
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Service
public class CasePlotServiceImpl implements ICasePlotService 
{
	@Autowired
	private CasePlotMapper casePlotMapper;

	/**
     * 查询案件计量信息
     * 
     * @param id 案件计量ID
     * @return 案件计量信息
     */
    @Override
	public CasePlot selectCasePlotById(Integer id)
	{
	    return casePlotMapper.selectCasePlotById(id);
	}
	
	/**
     * 查询案件计量列表
     * 
     * @param casePlot 案件计量信息
     * @return 案件计量集合
     */
	@Override
	public List<CasePlot> selectCasePlotList(CasePlot casePlot)
	{
	    return casePlotMapper.selectCasePlotList(casePlot);
	}

	@Override
	public List<CasePlot> selectCasePlotListByCaseId(Integer caseId){
		return casePlotMapper.selectCasePlotListByCaseId(caseId);
	}

	@Override
	public List<CasePlot> selectParamCasePlotListByCaseId(Integer caseId){
		return casePlotMapper.selectParamCasePlotListByCaseId(caseId);
	}
	
    /**
     * 新增案件计量
     * 
     * @param casePlot 案件计量信息
     * @return 结果
     */
	@Override
	public int insertCasePlot(CasePlot casePlot)
	{
	    return casePlotMapper.insertCasePlot(casePlot);
	}
	
	/**
     * 修改案件计量
     * 
     * @param casePlot 案件计量信息
     * @return 结果
     */
	@Override
	public int updateCasePlot(CasePlot casePlot)
	{
	    return casePlotMapper.updateCasePlot(casePlot);
	}

	/**
     * 删除案件计量对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCasePlotByIds(String ids)
	{
		return casePlotMapper.deleteCasePlotByIds(Convert.toStrArray(ids));
	}
	
}
