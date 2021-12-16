package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionDetailResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import com.mkst.umap.app.admin.domain.ReceptionAppointment;
import com.mkst.umap.app.admin.dto.reception.ReceptionScheduleInfoDto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 接待预约 服务层
 *
 * @author wangyong
 * @date 2020-07-08
 */
public interface IReceptionAppointmentService {
	/**
	 * 查询接待预约信息
	 *
	 * @param id 接待预约ID
	 * @return 接待预约信息
	 */
	public ReceptionAppointment selectReceptionAppointmentById(Integer id);

	/**
	 * 查询接待预约列表
	 *
	 * @param receptionAppointment 接待预约信息
	 * @return 接待预约集合
	 */
	public List<ReceptionAppointment> selectReceptionAppointmentList(ReceptionAppointment receptionAppointment);

	/**
	 * 新增接待预约
	 *
	 * @param receptionAppointment 接待预约信息
	 * @return 结果
	 */
	public int insertReceptionAppointment(ReceptionAppointment receptionAppointment);

	/**
	 * 修改接待预约
	 *
	 * @param receptionAppointment 接待预约信息
	 * @return 结果
	 */
	public int updateReceptionAppointment(ReceptionAppointment receptionAppointment);

	/**
	 * 删除接待预约信息
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	int deleteReceptionAppointmentByIds(String ids);


	/**
	 * @return com.mkst.umap.app.admin.api.bean.result.reception.ReceptionDetailResult
	 * @Author 王勇
	 * @Description 获取详情
	 * @Date 17:14 2020-07-16
	 * @Param [id]
	 */
	ReceptionDetailResult getReceptionDetailById(Long id);

	/**
	 * @return java.util.LinkedList<com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult>
	 * @Author 王勇
	 * @Description 获取我的列表
	 * @Date 17:15 2020-07-16
	 * @Param [receptionParam]
	 */
	LinkedList<ReceptionInfoResult> getMyReceptionList(ReceptionParam receptionParam);

	/**
	 * @return java.util.LinkedList<com.mkst.umap.app.admin.dto.reception.ReceptionScheduleInfoDto>
	 * @Author 王勇
	 * @Description 获取排班情况
	 * @Date 17:15 2020-07-16
	 * @Param [receptionParam]
	 */
	LinkedList<ReceptionScheduleInfoDto> getSchedule(ReceptionParam receptionParam);

	/**
	 * @return java.util.Date
	 * @Author hsw
	 * @Description 当选择了开始时间后，获取可选的结束时间
	 * @Date 17:15 2020-07-16
	 * @Param [receptionParam]
	 */
	Date getNextStartTime(ReceptionParam receptionParam);

	/**
	 * @return boolean
	 * @Author 王勇
	 * @Description 检查当前的时间段是否被占用
	 * @Date 17:16 2020-07-16
	 * @Param [receptionParam]
	 */
	boolean checkIsOccupied(ReceptionParam receptionParam);

    List<NameCountResult> selectDayCount(ReceptionParam param);

	Map<String, List<TimeApplyResult>> selectTimeApplyList(ReceptionParam param);
}
