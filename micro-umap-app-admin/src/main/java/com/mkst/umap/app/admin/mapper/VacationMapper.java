package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.result.vacation.VacationResult;
import com.mkst.umap.app.admin.domain.Vacation;
import java.util.List;	

/**
 * 请假 数据层
 * 
 * @author wangyong
 * @date 2020-08-24
 */
public interface VacationMapper 
{
	/**
     * 查询请假信息
     * 
     * @param id 请假ID
     * @return 请假信息
     */
	public Vacation selectVacationById(Long id);
	
	/**
     * 查询请假列表
     * 
     * @param vacation 请假信息
     * @return 请假集合
     */
	public List<Vacation> selectVacationList(Vacation vacation);
	
	/**
     * 新增请假
     * 
     * @param vacation 请假信息
     * @return 结果
     */
	public int insertVacation(Vacation vacation);
	
	/**
     * 修改请假
     * 
     * @param vacation 请假信息
     * @return 结果
     */
	public int updateVacation(Vacation vacation);
	
	/**
     * 删除请假
     * 
     * @param id 请假ID
     * @return 结果
     */
	public int deleteVacationById(Long id);
	
	/**
     * 批量删除请假
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteVacationByIds(String[] ids);

    List<VacationResult> selectVacationInfo(Vacation selectVacation);
}