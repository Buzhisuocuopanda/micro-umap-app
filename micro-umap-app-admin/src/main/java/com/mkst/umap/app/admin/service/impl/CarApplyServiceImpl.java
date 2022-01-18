package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.framework.web.domain.server.Sys;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IEventAuditRecordService;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.CarApproveParam;
import com.mkst.umap.app.admin.api.bean.result.car.CarApplyResult;
import com.mkst.umap.app.admin.api.bean.result.car.CarDetailResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.CarApply;
import com.mkst.umap.app.admin.domain.CarInfo;
import com.mkst.umap.app.admin.domain.MapLocation;
import com.mkst.umap.app.admin.dto.carApply.CarApplyInfoDto;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.mapper.CarApplyMapper;
import com.mkst.umap.app.admin.service.ICarApplyService;
import com.mkst.umap.app.admin.service.ICarInfoService;
import com.mkst.umap.app.admin.service.IMapLocationService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.enums.*;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 车辆申请管理 服务层实现
 *
 * @author wangyong
 * @date 2020-07-20
 */
@Service
public class CarApplyServiceImpl implements ICarApplyService {
	@Autowired
	private CarApplyMapper carApplyMapper;
	@Autowired
	private IMapLocationService mapLocationService;
	@Autowired
	private ICarInfoService carInfoService;
	@Autowired
	private AuditRecordMapper auditRecordMapper;
	@Autowired
	private IWfEventDetailService eventDetailService;
	@Autowired
	private IEventAuditRecordService eventAuditRecordService;
	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService sysRoleService;

	@Autowired
	private ICarApplyService carApplyService;

	@Autowired
	ISysUserService sysUserService;

	/**
	 * 查询车辆申请管理信息
	 *
	 * @param carApplyId 车辆申请管理ID
	 * @return 车辆申请管理信息
	 */
	@Override
	public CarApply selectCarApplyById(Long carApplyId) {
		return carApplyMapper.selectCarApplyById(carApplyId);
	}

	@Override
	public CarDetailResult selectCarDetailById(Long carApplyId) {
		return carApplyMapper.selectCarDetailById(carApplyId);
	}

	@Override
	public List<Long> checkCarUseByTime(CarApply carApply) {
		return carApplyMapper.checkCarUseByTime(carApply);
	}

	@Override
	public List<Long> checkDriverByTime(CarApply carApply) {
		return carApplyMapper.checkDriverByTime(carApply);
	}

	@Override
	public List<CarApply> selectCarApplyByStatus(CarApply carApply) {
		return carApplyMapper.selectCarApplyByStatus(carApply);
	}

	@Override
	public List<CarApply> selectCarApplyByDriver(CarApply carApply) {
		return carApplyMapper.selectCarApplyByDriver(carApply);
	}

	/**
	 * 查询车辆申请管理列表
	 *
	 * @param carApply 车辆申请管理信息
	 * @return 车辆申请管理集合
	 */
	@Override
	public List<CarApply> selectCarApplyList1(CarApply carApply) {
		return carApplyMapper.selectCarApplyList(carApply);
	}

	@Override
	public List<CarApply> selectCarApplyList(CarApply carApply) {
		return carApplyMapper.selectCarApplyList(carApply);
	}

	@Override
	public List<CarApplyInfoDto> selectCarApplyDtoList(CarApply carApply) {
		return carApplyMapper.selectCarApplyDtoList(carApply);
	}

	@Override
	public List<CarApplyResult> selectResultList(CarApply carApply) {
		List<CarApply> carApplyList = selectCarApplyByStatus(carApply);
		return getResultList(carApplyList);
	}

	@Override
	public List<CarApplyResult> getResultList(List<CarApply> carApplyList) {
		List<CarApplyResult> resultList = new LinkedList<>();
		for (CarApply c : carApplyList) {

			MapLocation startPoint = mapLocationService.selectMapLocationById(c.getStartLocationId());
			MapLocation endPoint = mapLocationService.selectMapLocationById(c.getEndLocationId());
			CarInfo carInfo = carInfoService.selectCarInfoById(c.getCarId());
			CarApplyResult result = new CarApplyResult();
			result.setApproveStatus(c.getApproveStatus());
			result.setEndPoint(endPoint);
			if (carInfo != null) {
				result.setLicensePlateNumber(carInfo.getLicensePlateNumber());
			}
			result.setStartPoint(startPoint);
			result.setEndTime(c.getEndTime());
			result.setStartTime(c.getStartTime());
			result.setApproveId(c.getApprovalUserId());
			result.setCarApplyId(c.getCarApplyId());
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * 新增车辆申请管理
	 *
	 * @param carApply 车辆申请管理信息
	 * @return 结果
	 */
	@Override
	public int insertCarApply(CarApply carApply) {
		return carApplyMapper.insertCarApply(carApply);
	}

	@Override
	public Integer addSave(CarApply carApply, SysUser nowUser, MapLocation startPoint, MapLocation endPoint) {
		//先插入坐标  获取到id后 set近申请表
		int row = mapLocationService.insertMapLocation(startPoint);
		if (row <= 0) {
			return row;
		}
		row = mapLocationService.insertMapLocation(endPoint);
		if (row <= 0) {
			return row;
		}
		carApply.setStartLocationId(startPoint.getLocationId());
		carApply.setEndLocationId(endPoint.getLocationId());
		//先插入申请表数据
		row = insertCarApply(carApply);
		return row;
	}


	/**
	 * 修改车辆申请管理
	 *
	 * @param carApply 车辆申请管理信息
	 * @return 结果
	 */
	@Override
	public int updateCarApply(CarApply carApply) {
		return carApplyMapper.updateCarApply(carApply);
	}

	@Override
	public int updateCarApplyFee(CarApply carApply) {
		return carApplyMapper.updateCarApplyFee(carApply);
	}

	/**
	 * 删除车辆申请管理对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteCarApplyByIds(String ids) {
		return carApplyMapper.deleteCarApplyByIds(Convert.toStrArray(ids));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(Long id, String status, String reason) {

		CarApply carApply = selectCarApplyById(id);

		if (!Convert.toStr(ApproveStatusEnum.Pending.getValue()).equals(carApply.getApproveStatus())) {
			return 0;
		}

		//新增审核记录
		AuditRecord auditRecord = new AuditRecord(carApply.getCarApplyId(), AuditRecordTypeEnum.CarAudit.getValue(), status, "");
		auditRecord.setCreateBy(ShiroUtils.getLoginName());
		auditRecord.setUpdateBy(ShiroUtils.getLoginName());
		if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			auditRecord.setReason(reason);
		}
		auditRecordMapper.insertAuditRecord(auditRecord);

		if (AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)) {
			carApply.setApproveStatus(ApproveStatusEnum.SUCCESS.getValue().toString());
		} else {
			carApply.setApproveStatus(ApproveStatusEnum.FAIL.getValue().toString());
		}

		int row = this.updateCarApply(carApply);

		return row;
	}

	/**
	 * 审核
	 *
	 * @param param
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int auditByParam(CarApproveParam param) {

		//查询申请记录
		CarApply carApply = carApplyMapper.selectCarApplyById(param.getId());

		String approvalType = carApply.getNeedExtraApproval() ? ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue() : ApproveTypeEnum.APPROVE_CAR_SIMPLE.getValue();

		//审核人
		Long approvalUserId = param.getApprovalUserId();

		//申请人
		SysUser user = sysUserService.selectUserById(carApply.getUserId());

		//修改审核记录
		eventAuditRecordService.updateEventAuditRecordStatus(
				StringUtil.toString(carApply.getProgress()), //进程
				Math.toIntExact(carApply.getCarApplyId()),  //申请id
				approvalType,    //申请类型
//				ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue(),    //申请类型
				StringUtil.toString(param.getAuditStatus()), //审核状态
				param.getReason()
		);

		if (StringUtils.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL, param.getAuditStatus().toString())) {
			//审核结果是驳回
			carApply.setApproveStatus(param.getAuditStatus().toString());
			sendAppMsg(user.getLoginName(), carApply.getCarApplyId(), "公车预约", "您的公务车预约申请被拒绝，进入APP查看拒绝原因");
			return carApplyMapper.updateCarApply(carApply);
		}

		//查询下一个流程
		WfEventDetail nextEvent = eventDetailService.selectNextEventDetail(approvalType, carApply.getProgress());
		if (BeanUtil.isNotEmpty(nextEvent)) {
			//存在下一个流程

			//判断下一级审核人是否存在
			SysUser sysUser = userService.selectUserById(approvalUserId);
			if (BeanUtil.isEmpty(sysUser)) {
				throw new NullPointerException("当前系统中不存在该审核员（用户ID：" + approvalUserId + "），请选择其他人或联系管理员！！");
			}
			carApply.setProgress(Integer.valueOf(nextEvent.getApprovalOrder()));
			carApply.setApprovalUserId(param.getApprovalUserId().intValue());

			EventAuditRecord eventAuditRecord = new EventAuditRecord();
			eventAuditRecord.setApplyId(Math.toIntExact(carApply.getCarApplyId()));
			eventAuditRecord.setApplyType(approvalType);
			eventAuditRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
			eventAuditRecord.setApprovalOrder(nextEvent.getApprovalOrder());
			eventAuditRecord.setApprovalUserId(param.getApprovalUserId().intValue());
			eventAuditRecord.setCreateBy(sysUser.getLoginName());
			eventAuditRecord.setUpdateBy(sysUser.getLoginName());
			eventAuditRecordService.insertEventAuditRecord(eventAuditRecord);

			//给下一级审批人发送APP消息提醒审批
			sendAppMsg(sysUser.getLoginName(), carApply.getCarApplyId(), "公车预约", "您有新的公务车预约申请待审批!");
		} else {
			// 最后一步
			CarApply select = new CarApply();

			select.setStartTime(carApply.getStartTime());
			select.setEndTime(carApply.getEndTime());
			//判断车是否可用
			List<Long> checkList = carApplyService.checkCarUseByTime(select);
			for (Long id : checkList) {
				if (id.equals(param.getCarId())) {
					return 2;
				}
			}
			carApply.setCarId(param.getCarId());

			List<Long> checkDriver = carApplyService.checkDriverByTime(select);
			for (Long id : checkDriver) {
				if (id.equals(param.getDriverId())) {
					return 3;
				}
			}

			CarInfo carInfo = carInfoService.selectCarInfoById(param.getCarId());
			if (carInfo == null) {
				return 4;
			}

			carApply.setDriverId(param.getDriverId());

			carApply.setApproveStatus(param.getAuditStatus().toString());
			if (KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(param.getAuditStatus().toString())) {
				/*
		         *	List<SysUser> users = userService.selectUserLitByRoleKey("clgly");
		         *	users.forEach(u -> {
		         *		sendAppMsg(u.getLoginName(), carApply.getCarApplyId(),"车辆申请" ,"您有新的公务车预约申请待审批！");
		         *	});
		         */

				//通知申请人车辆申请已通过
				sendAppMsg(sysUserService.selectUserById(carApply.getUserId()).getLoginName(),carApply.getCarApplyId(),"公车预约","您的公务车预约已通过，进入APP查看车辆信息。");

				//通知司机有新的出车任务
				SysUser driver = sysUserService.selectUserById(param.getDriverId());
				if (driver != null) {
					try {
						String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(carApply.getStartTime());
						user = sysUserService.selectUserById(carApply.getUserId());
						CarInfo car = carInfoService.selectCarInfoById(param.getCarId());
						MapLocation endPoint = mapLocationService.selectMapLocationById(carApply.getEndLocationId());
						MapLocation startPoint = mapLocationService.selectMapLocationById(carApply.getStartLocationId());

						String smsContent = "【公车预约】您有新的公务车出车任务，请您在" + startTime +
								"前到" + startPoint.getLocationName() +
								"等候，车牌号：" + car.getLicensePlateNumber() +
								" 用车人：" + user.getUserName() +
								" 手机号：" + carApply.getTelphone() +
								" 目的地：" + endPoint.getLocationName() + "。";
						YxtSmsConfig.getYxtSmsService().sendMsg(driver.getPhonenumber(), smsContent);
					} catch (YxtSmsErrorException e) {
						e.printStackTrace();
						return 6;
					}
				}
			}
		}
		return carApplyMapper.updateCarApply(carApply);
	}

	@Override
	public List<EventAuditRecord> selectAuditList(String applyType, Long applyId) {

		List<EventAuditRecord> eventAuditRecords = eventAuditRecordService.selectAuditEventLit(applyType, applyId.intValue());
		CarApply carApply = this.selectCarApplyById(applyId);
		EventAuditRecord eventAuditRecord = new EventAuditRecord();
		eventAuditRecord.setApprovalObjectName("提交申请");
		eventAuditRecord.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
		eventAuditRecord.setApprovalOrder(KeyConstant.EVENT_ZERO);
		eventAuditRecord.setApprovalUserName(userService.selectUserById(Long.parseLong(carApply.getCreateBy())).getUserName());
		eventAuditRecord.setUpdateTime(carApply.getCreateTime());
		eventAuditRecords.add(0, eventAuditRecord);
		return eventAuditRecords;
	}


	public static void sendAppMsg(String target, Long cId, String title, String content) {
		AppMsgContent msgContent = new AppMsgContent();
		msgContent.setTitle(title);
		msgContent.setContent(content);

		Map<String, String> params = new HashMap<>();
		params.put("bizKey", cId.toString());
		params.put("bizType", BusinessTypeEnum.UMAP_CAR_MANAGE.getValue());
		msgContent.setParams(params);
		MsgPushUtils.push(msgContent, cId.toString(), BusinessTypeEnum.UMAP_CAR_MANAGE.getValue(), target);
		MsgPushUtils.getMsgPushTask().execute();
	}
}
