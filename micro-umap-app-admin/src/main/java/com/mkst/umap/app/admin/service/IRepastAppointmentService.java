package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.RepastAppointmentParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.RepastAppointment;
import java.util.List;

/**
 * 就餐预约 服务层
 * 
 * @author wangyong
 * @date 2020-11-30
 */
public interface IRepastAppointmentService 
{
	/**
     * 查询就餐预约信息
     * 
     * @param id 就餐预约ID
     * @return 就餐预约信息
     */
	public RepastAppointment selectRepastAppointmentById(Integer id);
	
	/**
     * 查询就餐预约列表
     * 
     * @param repastAppointment 就餐预约信息
     * @return 就餐预约集合
     */
	public List<RepastAppointment> selectRepastAppointmentList(RepastAppointment repastAppointment);
	
	/**
     * 新增就餐预约
     * 
     * @param repastAppointment 就餐预约信息
     * @return 结果
     */
	public int insertRepastAppointment(RepastAppointment repastAppointment);
	
	/**
     * 修改就餐预约
     * 
     * @param repastAppointment 就餐预约信息
     * @return 结果
     */
	public int updateRepastAppointment(RepastAppointment repastAppointment);
		
	/**
     * 删除就餐预约信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteRepastAppointmentByIds(String ids);

    List<NameCountResult> selectDayCount(RepastAppointmentParam param);

    List<RepastAppointment> countRepast(RepastAppointment repastAppointment);
}
