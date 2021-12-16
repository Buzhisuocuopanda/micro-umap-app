package com.mkst.umap.app.admin.service.impl;

import java.util.List;

import com.mkst.umap.app.admin.api.bean.param.RepastAppointmentParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.RepastAppointmentMapper;
import com.mkst.umap.app.admin.domain.RepastAppointment;
import com.mkst.umap.app.admin.service.IRepastAppointmentService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 就餐预约 服务层实现
 * 
 * @author wangyong
 * @date 2020-11-30
 */
@Service
public class RepastAppointmentServiceImpl implements IRepastAppointmentService 
{
	@Autowired
	private RepastAppointmentMapper repastAppointmentMapper;

	/**
     * 查询就餐预约信息
     * 
     * @param id 就餐预约ID
     * @return 就餐预约信息
     */
    @Override
	public RepastAppointment selectRepastAppointmentById(Integer id)
	{
	    return repastAppointmentMapper.selectRepastAppointmentById(id);
	}
	
	/**
     * 查询就餐预约列表
     * 
     * @param repastAppointment 就餐预约信息
     * @return 就餐预约集合
     */
	@Override
	public List<RepastAppointment> selectRepastAppointmentList(RepastAppointment repastAppointment)
	{
	    return repastAppointmentMapper.selectRepastAppointmentList(repastAppointment);
	}
	
    /**
     * 新增就餐预约
     * 
     * @param repastAppointment 就餐预约信息
     * @return 结果
     */
	@Override
	public int insertRepastAppointment(RepastAppointment repastAppointment)
	{
	    return repastAppointmentMapper.insertRepastAppointment(repastAppointment);
	}
	
	/**
     * 修改就餐预约
     * 
     * @param repastAppointment 就餐预约信息
     * @return 结果
     */
	@Override
	public int updateRepastAppointment(RepastAppointment repastAppointment)
	{
	    return repastAppointmentMapper.updateRepastAppointment(repastAppointment);
	}

	/**
     * 删除就餐预约对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteRepastAppointmentByIds(String ids)
	{
		return repastAppointmentMapper.deleteRepastAppointmentByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<NameCountResult> selectDayCount(RepastAppointmentParam param) {
		return repastAppointmentMapper.selectDayCount(param);
	}

	@Override
	public List<RepastAppointment> countRepast(RepastAppointment repastAppointment) {
		return repastAppointmentMapper.countRepast(repastAppointment);
	}

}
