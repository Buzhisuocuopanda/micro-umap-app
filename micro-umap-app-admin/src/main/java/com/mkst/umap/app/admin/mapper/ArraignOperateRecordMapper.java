package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.ArraignOperateRecord;

import java.util.List;

/**
 * 提审操作日志 数据层
 * 
 * @author wangyong
 * @date 2021-04-06
 */
public interface ArraignOperateRecordMapper 
{
	/**
     * 查询提审操作日志信息
     * 
     * @param id 提审操作日志ID
     * @return 提审操作日志信息
     */
	public ArraignOperateRecord selectArraignOperateRecordById(Integer id);
	
	/**
     * 查询提审操作日志列表
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 提审操作日志集合
     */
	public List<ArraignOperateRecord> selectArraignOperateRecordList(ArraignOperateRecord arraignOperateRecord);
	
	/**
     * 新增提审操作日志
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 结果
     */
	public int insertArraignOperateRecord(ArraignOperateRecord arraignOperateRecord);
	
	/**
     * 修改提审操作日志
     * 
     * @param arraignOperateRecord 提审操作日志信息
     * @return 结果
     */
	public int updateArraignOperateRecord(ArraignOperateRecord arraignOperateRecord);
	
	/**
     * 删除提审操作日志
     * 
     * @param id 提审操作日志ID
     * @return 结果
     */
	public int deleteArraignOperateRecordById(Integer id);
	
	/**
     * 批量删除提审操作日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteArraignOperateRecordByIds(String[] ids);
	
}