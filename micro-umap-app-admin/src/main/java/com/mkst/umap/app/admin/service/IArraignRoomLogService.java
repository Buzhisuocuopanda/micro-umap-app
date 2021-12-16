package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.ArraignRoomLog;
import java.util.List;

/**
 * 提审室操作日志 服务层
 * 
 * @author wangyong
 * @date 2020-09-21
 */
public interface IArraignRoomLogService 
{
	/**
     * 查询提审室操作日志信息
     * 
     * @param id 提审室操作日志ID
     * @return 提审室操作日志信息
     */
	public ArraignRoomLog selectArraignRoomLogById(Long id);
	
	/**
     * 查询提审室操作日志列表
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 提审室操作日志集合
     */
	public List<ArraignRoomLog> selectArraignRoomLogList(ArraignRoomLog arraignRoomLog);
	
	/**
     * 新增提审室操作日志
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 结果
     */
	public int insertArraignRoomLog(ArraignRoomLog arraignRoomLog);
	
	/**
     * 修改提审室操作日志
     * 
     * @param arraignRoomLog 提审室操作日志信息
     * @return 结果
     */
	public int updateArraignRoomLog(ArraignRoomLog arraignRoomLog);
		
	/**
     * 删除提审室操作日志信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteArraignRoomLogByIds(String ids);

	public String importArraignRoomLog(String id,List<ArraignRoomLog> list);
	
}
