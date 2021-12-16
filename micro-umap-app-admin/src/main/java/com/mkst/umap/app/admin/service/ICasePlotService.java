package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.CasePlot;
import java.util.List;

/**
 * 案件计量 服务层
 * 
 * @author wangyong
 * @date 2021-06-22
 */
public interface ICasePlotService 
{
	/**
     * 查询案件计量信息
     * 
     * @param id 案件计量ID
     * @return 案件计量信息
     */
	public CasePlot selectCasePlotById(Integer id);
	
	/**
     * 查询案件计量列表
     * 
     * @param casePlot 案件计量信息
     * @return 案件计量集合
     */
	public List<CasePlot> selectCasePlotList(CasePlot casePlot);

	public List<CasePlot> selectCasePlotListByCaseId(Integer caseId);

	public List<CasePlot> selectParamCasePlotListByCaseId(Integer caseId);
	
	/**
     * 新增案件计量
     * 
     * @param casePlot 案件计量信息
     * @return 结果
     */
	public int insertCasePlot(CasePlot casePlot);
	
	/**
     * 修改案件计量
     * 
     * @param casePlot 案件计量信息
     * @return 结果
     */
	public int updateCasePlot(CasePlot casePlot);
		
	/**
     * 删除案件计量信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCasePlotByIds(String ids);
	
}
