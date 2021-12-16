package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.ArraignShift;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提审换班 数据层
 * 
 * @author wangyong
 * @date 2021-04-15
 */
@Repository
public interface ArraignShiftMapper 
{
	/**
     * 查询提审换班信息
     * 
     * @param id 提审换班ID
     * @return 提审换班信息
     */
	public ArraignShift selectArraignShiftById(Integer id);
	
	/**
     * 查询提审换班列表
     * 
     * @param arraignShift 提审换班信息
     * @return 提审换班集合
     */
	public List<ArraignShift> selectArraignShiftList(ArraignShift arraignShift);

	public Integer selectArraignShiftCount(ArraignShift arraignShift);
	
	/**
     * 新增提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	public int insertArraignShift(ArraignShift arraignShift);
	
	/**
     * 修改提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	public int updateArraignShift(ArraignShift arraignShift);
	
	/**
     * 删除提审换班
     * 
     * @param id 提审换班ID
     * @return 结果
     */
	public int deleteArraignShiftById(Integer id);
	
	/**
     * 批量删除提审换班
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteArraignShiftByIds(String[] ids);

	ArraignShift selectShiftRecord(Integer id);
}