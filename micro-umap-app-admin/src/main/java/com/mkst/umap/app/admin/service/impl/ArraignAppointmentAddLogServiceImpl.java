package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.ArraignAppointmentAddLogMapper;
import com.mkst.umap.app.admin.domain.ArraignAppointmentAddLog;
import com.mkst.umap.app.admin.service.IArraignAppointmentAddLogService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 提审预约加号记录 服务层实现
 * 
 * @author wangyong
 * @date 2021-01-13
 */
@Service
public class ArraignAppointmentAddLogServiceImpl implements IArraignAppointmentAddLogService 
{
	@Autowired
	private ArraignAppointmentAddLogMapper arraignAppointmentAddLogMapper;

	/**
     * 查询提审预约加号记录信息
     * 
     * @param id 提审预约加号记录ID
     * @return 提审预约加号记录信息
     */
    @Override
	public ArraignAppointmentAddLog selectArraignAppointmentAddLogById(Integer id)
	{
	    return arraignAppointmentAddLogMapper.selectArraignAppointmentAddLogById(id);
	}
	
	/**
     * 查询提审预约加号记录列表
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 提审预约加号记录集合
     */
	@Override
	public List<ArraignAppointmentAddLog> selectArraignAppointmentAddLogList(ArraignAppointmentAddLog arraignAppointmentAddLog)
	{
	    return arraignAppointmentAddLogMapper.selectArraignAppointmentAddLogList(arraignAppointmentAddLog);
	}
	
    /**
     * 新增提审预约加号记录
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 结果
     */
	@Override
	public int insertArraignAppointmentAddLog(ArraignAppointmentAddLog arraignAppointmentAddLog)
	{
	    return arraignAppointmentAddLogMapper.insertArraignAppointmentAddLog(arraignAppointmentAddLog);
	}
	
	/**
     * 修改提审预约加号记录
     * 
     * @param arraignAppointmentAddLog 提审预约加号记录信息
     * @return 结果
     */
	@Override
	public int updateArraignAppointmentAddLog(ArraignAppointmentAddLog arraignAppointmentAddLog)
	{
	    return arraignAppointmentAddLogMapper.updateArraignAppointmentAddLog(arraignAppointmentAddLog);
	}

	/**
     * 删除提审预约加号记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteArraignAppointmentAddLogByIds(String ids)
	{
		return arraignAppointmentAddLogMapper.deleteArraignAppointmentAddLogByIds(Convert.toStrArray(ids));
	}
	
}
