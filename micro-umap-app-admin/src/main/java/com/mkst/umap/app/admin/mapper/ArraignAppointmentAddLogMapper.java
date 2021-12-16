package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.ArraignAppointmentAddLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提审预约加号记录 数据层
 * 
 * @author wangyong
 * @date 2021-01-13
 */
@Repository
public interface ArraignAppointmentAddLogMapper 
{
	/**
     * 查询提审预约加号记录信息
     * 
     * @param id 提审预约加号记录ID
     * @return 提审预约加号记录信息
     */
	public ArraignAppointmentAddLog selectArraignAppointmentAddLogById(Integer id);
	
	/**
     * 查询提审预约加号记录列表
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 提审预约加号记录集合
     */
	public List<ArraignAppointmentAddLog> selectArraignAppointmentAddLogList(ArraignAppointmentAddLog arraignAppointmentAddLog);
	
	/**
     * 新增提审预约加号记录
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 结果
     */
	public int insertArraignAppointmentAddLog(ArraignAppointmentAddLog arraignAppointmentAddLog);
	
	/**
     * 修改提审预约加号记录
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 结果
     */
	public int updateArraignAppointmentAddLog(ArraignAppointmentAddLog arraignAppointmentAddLog);
	
	/**
     * 删除提审预约加号记录
     * 
     * @param id 提审预约加号记录ID
     * @return 结果
     */
	public int deleteArraignAppointmentAddLogById(Integer id);
	
	/**
     * 批量删除提审预约加号记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteArraignAppointmentAddLogByIds(String[] ids);
	
}