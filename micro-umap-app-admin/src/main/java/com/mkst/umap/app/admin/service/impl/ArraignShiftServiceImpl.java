package com.mkst.umap.app.admin.service.impl;

import java.util.*;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.domain.ArraignAppointment;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.mapper.ArraignAppointmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.ArraignShiftMapper;
import com.mkst.umap.app.admin.domain.ArraignShift;
import com.mkst.umap.app.admin.service.IArraignShiftService;
import com.mkst.mini.systemplus.common.support.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提审换班 服务层实现
 * 
 * @author wangyong
 * @date 2021-04-15
 */
@Service
@Slf4j
public class ArraignShiftServiceImpl implements IArraignShiftService 
{
	@Autowired
	private ArraignShiftMapper arraignShiftMapper;
	@Autowired
	private ArraignAppointmentMapper arraignAppointmentMapper;

	/**
     * 查询提审换班信息
     * 
     * @param id 提审换班ID
     * @return 提审换班信息
     */
    @Override
	public ArraignShift selectArraignShiftById(Integer id)
	{
	    return arraignShiftMapper.selectArraignShiftById(id);
	}
	
	/**
     * 查询提审换班列表
     * 
     * @param arraignShift 提审换班信息
     * @return 提审换班集合
     */
	@Override
	public List<ArraignShift> selectArraignShiftList(ArraignShift arraignShift)
	{
	    return arraignShiftMapper.selectArraignShiftList(arraignShift);
	}

	@Override
	public Integer selectArraignShiftCount(ArraignShift arraignShift){
		return arraignShiftMapper.selectArraignShiftCount(arraignShift);
	}

	@Override
	public Integer selectArraignShiftCountByTargetUserId(SysUser sysUser, String status){
		if(sysUser == null || StrUtil.isEmpty(status)){
			return 0;
		}
		ArraignShift arraignShift = new ArraignShift();
		//arraignShift.setTargetUserId(targetUserId);
		arraignShift.setStatus(status);
		Map<String,Object> queryMap = new HashMap<>();
		queryMap.put("approveUserId",sysUser.getUserId());
		queryMap.put("approveUserLoginName",sysUser.getLoginName());
		arraignShift.setParams(queryMap);
		return selectArraignShiftCount(arraignShift);
	}
	
    /**
     * 新增提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	@Override
	public int insertArraignShift(ArraignShift arraignShift)
	{
	    return arraignShiftMapper.insertArraignShift(arraignShift);
	}
	
	/**
     * 修改提审换班
     * 
     * @param arraignShift 提审换班信息
     * @return 结果
     */
	@Override
	public int updateArraignShift(ArraignShift arraignShift)
	{
	    return arraignShiftMapper.updateArraignShift(arraignShift);
	}

	@Override
	@Transactional
	public int auditArraignShift(Integer id,String status){
		ArraignShift shift = arraignShiftMapper.selectArraignShiftById(id);
		if(shift == null || !ArraignShift.STATUS_INIT.equals(shift.getStatus())){
			log.info("提审更换申请审核失败，更换申请ID："+ id);
			return 0;
		}
		if(ArraignShift.STATUS_AGREE.equals(status)){
			ArraignAppointment param = new ArraignAppointment();
			param.setProsecutorUserId(shift.getApplyUserId());
			param.setAppointmentDate(shift.getApplySourceDate());
			param.setTimePie(shift.getApplyTimePie());
			param.setNumOrder(shift.getApplyNumOrder());
			List<ArraignAppointment> list = arraignAppointmentMapper.selectArraignAppointmentList(param);
			if(list == null || list.size() == 0){
				return 0;
			}
			ArraignAppointment apply = list.get(0);
			param.setProsecutorUserId(shift.getTargetUserId());
			param.setAppointmentDate(shift.getTargetDate());
			param.setTimePie(shift.getTargetTimePie());
			param.setNumOrder(shift.getTargetNumOrder());
			List<ArraignAppointment> list2 = arraignAppointmentMapper.selectArraignAppointmentList(param);
			if(list2 == null || list2.size() == 0){
				return 0;
			}
			ArraignAppointment target = list2.get(0);
			apply.setTimePie(shift.getTargetTimePie());
			apply.setNumOrder(shift.getTargetNumOrder());
			arraignAppointmentMapper.updateArraignAppointment(apply);
			target.setTimePie(shift.getApplyTimePie());
			target.setNumOrder(shift.getApplyNumOrder());
			arraignAppointmentMapper.updateArraignAppointment(target);
		}
		shift.setStatus(status);
		arraignShiftMapper.updateArraignShift(shift);
		return 1;
	}

	/**
     * 删除提审换班对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteArraignShiftByIds(String ids)
	{
		return arraignShiftMapper.deleteArraignShiftByIds(Convert.toStrArray(ids));
	}

	@Override
	public void changeShift(Integer id) {

		ArraignShift shift = arraignShiftMapper.selectShiftRecord(id);
		Long[] ids = new Long[]{shift.getApplyArraignAppointmentId(), shift.getTargetArraignAppointmentId()};

		List<ArraignAppointment> appoints = arraignAppointmentMapper.selectAppoint(ids);

		if(appoints.size() == 2){
			ArraignAppointment e = appoints.get(0);
			ArraignAppointment e2 = appoints.get(1);
			Date startTime = e.getStartTime();
			Date endTime = e.getEndTime();
			e.setStartTime(e2.getStartTime());
			e.setEndTime(e2.getEndTime());
			e2.setStartTime(startTime);
			e2.setEndTime(endTime);
			arraignAppointmentMapper.changeTime(e);
			arraignAppointmentMapper.changeTime(e2);
			ArraignRoomSchedule r = new ArraignRoomSchedule();
			r.setId(e.getScheduleId());
			r.setStartTime(e.getStartTime());
			r.setEndTime(e.getEndTime());
			ArraignRoomSchedule r2 = new ArraignRoomSchedule();
			r2.setId(e2.getScheduleId());
			r2.setStartTime(e2.getStartTime());
			r2.setEndTime(e2.getEndTime());
			arraignAppointmentMapper.changeRoomTime(r);
			arraignAppointmentMapper.changeRoomTime(r2);
		}
	}


}
