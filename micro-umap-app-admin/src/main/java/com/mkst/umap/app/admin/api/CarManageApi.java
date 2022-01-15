package com.mkst.umap.app.admin.api;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IEventAuditRecordService;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.ApproveParam;
import com.mkst.umap.app.admin.api.bean.param.CarApproveParam;
import com.mkst.umap.app.admin.api.bean.param.car.CarApplyParam;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import com.mkst.umap.app.admin.api.bean.result.car.CarApplyResult;
import com.mkst.umap.app.admin.api.bean.result.car.CarDetailResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.CarApply;
import com.mkst.umap.app.admin.domain.CarInfo;
import com.mkst.umap.app.admin.domain.MapLocation;
import com.mkst.umap.app.admin.dto.carApply.CarDetailDto;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.ICarApplyService;
import com.mkst.umap.app.admin.service.ICarInfoService;
import com.mkst.umap.app.admin.service.IMapLocationService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api("车辆管理服务接口")
@RestController
@RequestMapping("/api/carManage")
public class CarManageApi extends BaseApi {

	@Autowired
	private ICarApplyService carApplyService;
	@Autowired
	private IMapLocationService mapLocationService;
	@Autowired
	private IAuditRecordService auditRecordService;
	@Autowired
	private ICarInfoService carInfoService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IWfEventDetailService eventDetailService;
	@Autowired
	private IEventAuditRecordService eventAuditRecordService;

	@PostMapping("/addSave")
	@ApiOperation("添加车辆管理申请")
	@Login
	public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(name = "carApplyParam", value = "车辆管理信息", required = true) CarApplyParam carApplyParam) {

		CarApply carApply = new CarApply();
		//装载车辆申请数据
		BeanUtils.copyProperties(carApplyParam, carApply);
		//装载 起点和终点数据
		MapLocation startPoint = new MapLocation();
		MapLocation endPoint = new MapLocation();
		BeanUtils.copyProperties(carApplyParam.getStartPoint(), startPoint);
		BeanUtils.copyProperties(carApplyParam.getEndPoint(), endPoint);

		carApply.setApproveStatus(ApproveStatusEnum.Pending.getValue().toString());//设置为待审批状态
		carApply.setCreateBy(getUserId(request) + "");
		carApply.setProgress(1);

		//是否需要司机
		if (carApply.getDriverWhether()) {
			carApply.setDriverStatus(DriverStatusEnum.DRIVER_PENDING.getValue());
		}

		//是否需要额外审批
		Boolean needsExtraApproval = carApplyParam.getAreaSelect().equals("市外") || carApplyParam.isTimeOver1Day();
		carApply.setNeedExtraApproval(needsExtraApproval);

		//1.初始化申请
		int row = carApplyService.addSave(carApply, getSysUser(request), startPoint, endPoint);

		//2.初始化审核表
		EventAuditRecord eventAuditRecord = new EventAuditRecord();
		//判断审批人是否存在
		SysUser sysUser = sysUserService.selectUserById(carApplyParam.getApprovalUserId().longValue());
		if (BeanUtil.isEmpty(sysUser)) {
			return ResultGenerator.genFailResult("当前系统中不存在该审核员，请选择其他人或联系管理员！！！");
		}

		//查询审核工作流第一个审批角色
		WfEventDetail wfEventDetail;
		if (needsExtraApproval) {
			wfEventDetail = eventDetailService.selectFirstEventDetail(ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue());
			eventAuditRecord.setApplyType(ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue());
		} else {
			wfEventDetail = eventDetailService.selectFirstEventDetail(ApproveTypeEnum.APPROVE_CAR_SIMPLE.getValue());
			eventAuditRecord.setApplyType(ApproveTypeEnum.APPROVE_CAR_SIMPLE.getValue());
		}

		if (BeanUtil.isEmpty(wfEventDetail)) {
			return ResultGenerator.genFailResult("未发现审核流程，请重试或联系管理员！！！");
		}

		//新增审批记录
		eventAuditRecord.setApplyId(Math.toIntExact((carApply.getCarApplyId())));
//		eventAuditRecord.setApplyType(ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue());
		eventAuditRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);//状态（0待审核 1已审核 2未通过）
		eventAuditRecord.setApprovalUserId(Math.toIntExact(sysUser.getUserId()));
		eventAuditRecord.setApprovalOrder(wfEventDetail.getApprovalOrder()); //审批顺序1
		eventAuditRecord.setCreateBy(sysUser.getLoginName());
		eventAuditRecord.setUpdateBy(sysUser.getLoginName());
		eventAuditRecordService.insertEventAuditRecord(eventAuditRecord);

		sendAppMsg(sysUser.getLoginName(), carApply.getCarApplyId(),"车辆申请" ,"您有新的公务车预约申请待审批" );
		return row > 0 ? ResultGenerator.genSuccessResult("新增车辆申请成功") : ResultGenerator.genFailResult("新增车辆申请失败，请联系管理员或稍后重试！");
	}

	@GetMapping("/getApplyList")
	@ApiOperation("我的申请")
	@ResponseBody
	@Login
	public Result getApplyList(HttpServletRequest request,
	                           @RequestParam(value = "approveStatus", required = false) @ApiParam(name = "approveStatus", value = "审核状态", required = true) String approveStatus,
	                           @RequestParam(value = "startTime", required = false) @ApiParam(name = "startTime", value = "开始时间") String startTime,
	                           @RequestParam(value = "endTime", required = false) @ApiParam(name = "endTime", value = "结束时间") String endTime) {
		try {
			SysUser sysUser = getSysUser(request);
			if (sysUser == null) {
				return ResultGenerator.genFailResult("用户未登录，请登录");
			}

			CarApply carApply = new CarApply();
			carApply.setUserId(sysUser.getUserId());
			carApply.setApproveStatus(approveStatus);//状态 0待审批  1已通过  2未通过  3已取消
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StrUtil.isNotBlank(startTime)) {
				carApply.setStartTime(simpleDateFormat.parse(startTime));
			}
			if (StrUtil.isNotBlank(endTime)) {
				carApply.setEndTime(simpleDateFormat.parse(endTime));
			}

			startPage();
			List<CarApplyResult> resultList = carApplyService.selectResultList(carApply);
			return ResultGenerator.genSuccessResult("获取成功", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}

	@Login
	@GetMapping("/detail")
	@ApiOperation("预约详情")
	@ResponseBody
	public Result selectById(HttpServletRequest request, @RequestParam(value = "carApplyId") @ApiParam(name = "carApplyId", value = "车辆预约id", required = true) Long carApplyId) {
		Long userId = getUserId(request);
		try {
			CarDetailResult carDetailResult = carApplyService.selectCarDetailById(carApplyId);
			//通过司机id查司机名
			Long driverId = carDetailResult.getDriverId();
			if (driverId != null) {
				SysUser driver = sysUserService.selectUserById(driverId);
				carDetailResult.setDriverName(driver.getUserName());
				carDetailResult.setDriverPhoneNumber(driver.getPhonenumber());
			}
			MapLocation startPoint = mapLocationService.selectMapLocationById(carDetailResult.getStartLocationId());
			MapLocation endPoint = mapLocationService.selectMapLocationById(carDetailResult.getEndLocationId());
			List<AuditParam> auditParamList = new LinkedList<>();
			carDetailResult.setStartPoint(startPoint);
			carDetailResult.setEndPoint(endPoint);
			carDetailResult.setAuditParamList(auditParamList);

			//是否能审核
			if (userId.toString().equals(carDetailResult.getApprovalUserId().toString())) {
				carDetailResult.setCanAudit(KeyConstant.IS_CAN_AUDIT);
			}

			//下一个审核流程
			WfEventDetail wfEventDetail = eventDetailService.selectNextEventDetail(
					carDetailResult.getNeedExtraApproval() ? ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue() : ApproveTypeEnum.APPROVE_CAR_SIMPLE.getValue(),
					carDetailResult.getProgress()
			);
			CarDetailDto carDetailDto = transObject(carDetailResult, CarDetailDto.class);
			carDetailDto.setIsCarApproval(wfEventDetail == null);
			carDetailDto.setWfEventDetail(wfEventDetail);
			if (null != carDetailDto.getLicensePlateNumber()) {
				carDetailDto.setLicensePlateNumber(carDetailResult.getLicensePlateNumber());
			}
			if (null != carDetailDto.getDriverName()) {
				carDetailDto.setDriverName(carDetailResult.getDriverName());
			}
			if (null != carDetailDto.getDriverPhoneNumber()) {
				carDetailDto.setDriverPhoneNumber(carDetailResult.getDriverPhoneNumber());
			}
			return ResultGenerator.genSuccessResult("获取成功", carDetailDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}

	@ApiOperation(value = "审批进度")
	@GetMapping(value = "/auditList")
	public Result auditList(@RequestParam @ApiParam String applyId) {
		CarApply apply = carApplyService.selectCarApplyById(Long.parseLong(applyId));
		return ResultGenerator.genSuccessResult("查询成功",
				carApplyService.selectAuditList(
//						ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue(),
						apply.getNeedExtraApproval() ? ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue() : ApproveTypeEnum.APPROVE_CAR_SIMPLE.getValue(),
						Long.parseLong(applyId)
				)
		);
	}

	@GetMapping("/getApproveList")
	@ApiOperation("我的审批")
	@ResponseBody
	@Login
	public Result getApproveList(HttpServletRequest request,
	                             @RequestParam(value = "approveStatus", required = false) @ApiParam(name = "approveStatus", value = "审核状态") String approveStatus,
	                             @RequestParam(value = "date", required = false) @ApiParam(name = "date", value = "开始时间") String date) {

		try {
			Long userId = getUserId(request);
			CarApply carApply = new CarApply();
			carApply.setApproveStatus(approveStatus);
			carApply.setDelFlag("0");
			carApply.setApprovalUserId(userId.intValue());

			//判断是否需要通过时间刷选
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotBlank(date)) {
				carApply.setStartTime(simpleDateFormat.parse(date));
			}
			List<CarApply> carApplyList = carApplyService.selectCarApplyList(carApply);
			startPage();
			List<CarApply> list = new LinkedList();
			if (carApplyList != null) {
				for (CarApply c : carApplyList) {
					if (!"3".equals(c.getApproveStatus())) {
						list.add(c);
					}
				}
			}
			return ResultGenerator.genSuccessResult("获取成功", list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("异常问题，请联系管理员");
		}
	}

	@PostMapping(value = "/dealAudit")
	@ApiOperation("多级审批的审核接口")
	public Result dealAudit(HttpServletRequest request, @RequestBody @ApiParam CarApproveParam param) {

		//param.setCreateBy(getLoginName(request));
		int row = carApplyService.auditByParam(param);
		switch (row) {
			case 1:
				return ResultGenerator.genSuccessResult("审核成功");
			case 2:
				return ResultGenerator.genFailResult("该车辆不可用");
			case 3:
				return ResultGenerator.genFailResult("该司机已被预约");
			case 4:
				return ResultGenerator.genFailResult("该车辆已不存在");
			case 6:
				return ResultGenerator.genFailResult("短信发送失败");
			default:
				return ResultGenerator.genFailResult("审核失败");
		}
	}

	@ApiOperation(value = "获取支付类型及费用")
	@GetMapping(value = "/getFee")
	public Result getFee(@RequestParam @ApiParam Long carApplyId) {
		return ResultGenerator.genSuccessResult("查询成功",
				carApplyService.selectCarApplyById(carApplyId));
	}

	@ApiOperation(value = "新增支付类型及费用")
	@GetMapping(value = "/addFee")
	public Result addFee(@RequestParam @ApiParam Long carApplyId,
	                     @RequestParam(value = "feeType", required = false) @ApiParam(name = "feeType", value = "消费类型") String feeType,
	                     @RequestParam(value = "feeTotal", required = false) @ApiParam(name = "feeTotal", value = "消费金额") String feeTotal) {
		CarApply carApply = new CarApply();
		carApply.setCarApplyId(carApplyId);
		carApply.setFeeType(feeType);
		carApply.setFeeTotal(feeTotal);
		carApply.setDriverStatus(DriverStatusEnum.DRIVER_REIMBURSEMENT.getValue());
		return ResultGenerator.genSuccessResult("添加成功",
				carApplyService.updateCarApplyFee(carApply));
	}

	@GetMapping("/cancelAudit")
	@ApiOperation("取消申请")
	@ResponseBody
	@Login
	public Result cancelAudit(HttpServletRequest request,
	                          @RequestParam(value = "applyId", required = false) @ApiParam(name = "applyId", value = "订单id") Long applyId) {
		CarApply carApply = carApplyService.selectCarApplyById(applyId);
		carApply.setApproveStatus("3");
		carApplyService.updateCarApply(carApply);
		return ResultGenerator.genSuccessResult("取消成功");
	}


	@PostMapping("/getCarList")
	@ApiOperation("获取车辆集合")
	@Login
	public Result getCarList(@RequestBody Map<String, String> map) {
		try {

			String carApplyId = map.get("carApplyId");
			if (StringUtil.isBlank(carApplyId)) {
				return ResultGenerator.genFailResult("服务异常");
			}
			CarApply carApply = carApplyService.selectCarApplyById(Long.parseLong(carApplyId));
			//通过 开始时间和结束时间判断  是否可预约
			Integer peopleNum = carApply.getPeopleNumber();

			CarApply select = new CarApply();

			select.setStartTime(carApply.getStartTime());
			select.setEndTime(carApply.getEndTime());
			//
			List<Long> checkList = carApplyService.checkCarUseByTime(select);
			startPage();
			//查车辆信息
			List<CarInfo> allCar = carInfoService.selectCarInfoList(new CarInfo());

			if (null != allCar && allCar.size() != 0) {
				for (CarInfo c : allCar) {
					for (Long id : checkList) {
						if (id.equals(c.getCarId())) {
							//改为不可用
							c.setStatus("1");
						}
					}
				}
			}

			if (peopleNum != null) {
				List<CarInfo> list = new ArrayList();
				for (CarInfo c : allCar) {
					if (c.getMaxCarrying() >= peopleNum) {
						list.add(c);
					}
				}
				return list.isEmpty() ? ResultGenerator.genSuccessResult("暂无车辆，请联系管理员或稍后重试", new ArrayList<>()) :
						ResultGenerator.genSuccessResult("查询成功", list);
			}
			return allCar.isEmpty() ? ResultGenerator.genSuccessResult("暂无车辆，请联系管理员或稍后重试", new ArrayList<>()) :
					ResultGenerator.genSuccessResult("查询成功", allCar);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("异常问题，请联系管理员");
		}
	}


	@GetMapping("/driverComplete")
	@ApiOperation("司机完成接单接口")
	@ResponseBody
	@Login
	public Result driverOver(HttpServletRequest request,
	                         @RequestParam(value = "applyId") @ApiParam(name = "applyId", value = "申请id", required = true) Long applyId) {
		try {
			CarApply carApply = carApplyService.selectCarApplyById(applyId);
			carApply.setDriverStatus(DriverStatusEnum.DRIVER_COMPLETE.getValue());
			int row = carApplyService.updateCarApply(carApply);
			if (row > 0) {
				List<SysUser> users = sysUserService.selectUserLitByRoleKey("clgly");
				users.forEach(u -> {
					sendAppMsg(u.getLoginName(), carApply.getCarApplyId(), "公车预约", "车辆 " + carInfoService.selectCarInfoById(carApply.getCarId()).getLicensePlateNumber() + " 已归库");
				});
				return ResultGenerator.genSuccessResult("完单成功");
			} else {
				return ResultGenerator.genFailResult("完单失败，请联系管理员或稍后重试！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}


	@GetMapping("/getDriverListState")
	@ApiOperation("司机接单列表")
	@ResponseBody
	@Login
	public Result getDriverListState(HttpServletRequest request,
	                                 @RequestParam(value = "driverStatus", required = false) @ApiParam(name = "driverStatus", value = "接单状态", required = true) Integer driverStatus,
	                                 @RequestParam(value = "startTime", required = false) @ApiParam(name = "startTime", value = "开始时间") String startTime,
	                                 @RequestParam(value = "endTime", required = false) @ApiParam(name = "endTime", value = "开始时间") String endTime) {

		try {
			//获取当前登录用户
			SysUser sysUser = getSysUser(request);
			CarApply carApply = new CarApply();
			carApply.setDriverWhether(true);
			carApply.setApproveStatus(ApproveStatusEnum.SUCCESS.getValue().toString());

			startPage();

			//如果是待处理
			if (DriverStatusEnum.DRIVER_PENDING.getValue().equals(driverStatus)) {
				carApply.setDriverId(sysUser.getUserId());
				carApply.setDriverStatus(driverStatus);
				List<CarApply> list = carApplyService.selectCarApplyByDriver(carApply);
				for (CarApply c : list) {
					MapLocation startPoint = mapLocationService.selectMapLocationById(c.getStartLocationId());
					MapLocation endPoint = mapLocationService.selectMapLocationById(c.getEndLocationId());
					c.setStartPoint(startPoint);
					c.setEndPoint(endPoint);
					CarInfo carInfo = carInfoService.selectCarInfoById(c.getCarId());
					c.setLicensePlateNumber(carInfo.getLicensePlateNumber());
				}
				return ResultGenerator.genSuccessResult("获取成功", list);

			} else {
				carApply.setDriverId(sysUser.getUserId());
			}
			//判断是否需要通过时间刷选
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StrUtil.isNotBlank(startTime)) {
				carApply.setStartTime(simpleDateFormat.parse(startTime));
			}
			if (StrUtil.isNotBlank(endTime)) {
				carApply.setEndTime(simpleDateFormat.parse(endTime));
			}

			List<CarApply> list = carApplyService.selectCarApplyByDriver(carApply);
			for (CarApply c : list) {
				MapLocation startPoint = mapLocationService.selectMapLocationById(c.getStartLocationId());
				MapLocation endPoint = mapLocationService.selectMapLocationById(c.getEndLocationId());
				c.setStartPoint(startPoint);
				c.setEndPoint(endPoint);
				CarInfo carInfo = carInfoService.selectCarInfoById(c.getCarId());
				c.setLicensePlateNumber(carInfo.getLicensePlateNumber());
			}
			startPage();
			List<CarApply> list1 = new ArrayList();
			for (CarApply c : list) {
				if (!c.getDriverStatus().equals(DriverStatusEnum.DRIVER_PENDING.getValue())) {
					list1.add(c);
				}
			}
			return ResultGenerator.genSuccessResult("获取成功", list1);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}


	@GetMapping("/carAudit")
	@ApiOperation("审核列表")
	@ResponseBody
	@Login
	public Result carAudit(HttpServletRequest request,
	                       @RequestParam(value = "approveStatus", required = false) @ApiParam(name = "approveStatus", value = "审核状态") String approveStatus) {
		try {
			CarApply carApply = new CarApply();
			carApply.setApproveStatus(approveStatus);
			carApply.setDelFlag("0");

			startPage();
			List carApplyList = carApplyService.selectCarApplyList1(carApply);

			return ResultGenerator.genSuccessResult("获取成功", carApplyList);

		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}


	@GetMapping("/getDriverList")
	@ApiOperation("司机接单列表")
	@ResponseBody
	@Login
	public Result getDriverList(HttpServletRequest request,
	                            @RequestParam(value = "driverStatus", required = false) @ApiParam(name = "driverStatus", value = "接单状态", required = true) Integer driverStatus,
	                            @RequestParam(value = "startTime", required = false) @ApiParam(name = "startTime", value = "开始时间") String startTime,
	                            @RequestParam(value = "endTime", required = false) @ApiParam(name = "endTime", value = "开始时间") String endTime,
	                            @RequestParam(value = "keyword", required = false) @ApiParam(name = "keyword", value = "搜索关键字") String keyword) {
		try {
			//获取当前登录用户
			SysUser sysUser = getSysUser(request);
			//查询 需要司机 且审批通过的状态为1的列表
			CarApply carApply = new CarApply();
			carApply.setDriverWhether(true);
			carApply.setApproveStatus(ApproveStatusEnum.SUCCESS.getValue().toString());

			startPage();
			//如果是未接单 则司机id为空  否则搜索司机id为当前用户id的列表
			if (DriverStatusEnum.DRIVER_NOT_ORDER.getValue().equals(driverStatus)) {
				carApply.setDriverId(null);
				List<CarApply> list = carApplyService.selectCarApplyByDriver(carApply);
				list = list.stream().filter(o -> o.getEndTime().after(new Date())).collect(Collectors.toList());
				List<CarApplyResult> resultList = carApplyService.getResultList(list);
				return ResultGenerator.genSuccessResult("获取成功", resultList);
			} else {
				carApply.setDriverStatus(driverStatus);
				carApply.setDriverId(sysUser.getUserId());
			}
			//判断是否需要通过时间刷选
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (StrUtil.isNotBlank(startTime)) {
				carApply.setStartTime(simpleDateFormat.parse(startTime));
			}
			if (StrUtil.isNotBlank(endTime)) {
				carApply.setEndTime(simpleDateFormat.parse(endTime));
			}

			List<CarApply> list = carApplyService.selectCarApplyByDriver(carApply);
			List<CarApplyResult> resultList = carApplyService.getResultList(list);
			//做最后的刷选
			if (StrUtil.isNotBlank(keyword)) {
				resultList = resultList.stream().filter(o -> o.getEndPoint().getLocationName().contains(keyword) ||
						o.getStartPoint().getLocationName().contains(keyword) ||
						o.getLicensePlateNumber().contains(keyword)).collect(Collectors.toList());
			}

			return ResultGenerator.genSuccessResult("获取成功", resultList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}


	@GetMapping("/driverCancel")
	@ApiOperation("司机取消接单接口")
	@ResponseBody
	@Login
	public Result driverCancel(HttpServletRequest request,
	                           @RequestParam(value = "carApplyId", required = true) @ApiParam(name = "applyId", value = "申请id", required = true) Long applyId) {
		try {
			CarApply carApply = carApplyService.selectCarApplyById(applyId);
			carApply.setDriverStatus(DriverStatusEnum.DRIVER_NOT_ORDER.getValue());
			carApply.setDriverId(null);
			int row = carApplyService.updateCarApply(carApply);
			return row > 0 ? ResultGenerator.genSuccessResult("完单成功") : ResultGenerator.genFailResult("完单失败，请联系管理员或稍后重试！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
		}
	}

	private void sendAppMsg(String target, Long cId, String title, String content) {
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

	@PostMapping("/audit")
	@ApiOperation("预约申请审核 支持批量")
	@Login
	public Result audit(HttpServletRequest request,
	                    @RequestBody @ApiParam(name = "param", value = "预约申请信息") ApproveParam param) {
		try {
			if (CollUtil.isEmpty(param.getApplyIds()) || StrUtil.isBlank(param.getApproveType())) {
				return ResultGenerator.genFailResult("传入参数存在空值，请检查参数！");
			}
			List<Long> applyIds = param.getApplyIds();
			int row = 0;
			for (Long applyId : applyIds) {
				switch (param.getApproveType()) {
					//通过
					case "1":
						row = auditUpdateStatus(request, applyId, ApproveStatusEnum.SUCCESS.getValue().toString());
						break;
					//拒绝
					case "2": {
						row = auditUpdateStatus(request, applyId, ApproveStatusEnum.FAIL.getValue().toString());
						SysUser approvalUser = sysUserService.selectUserById(Long.valueOf(carApplyService.selectCarApplyById(applyId).getApprovalUserId()));
						sendAppMsg(approvalUser.getLoginName(), applyId, "车辆申请", "您的公务车预约申请被拒绝，进入APP查看拒绝原因");
						break;
					}
					//取消
					case "3":
						CarApply carApply = carApplyService.selectCarApplyById(applyId);
						if (carApply == null) {
							return ResultGenerator.genFailResult("审核失败，该车辆申请不存在，请联系管理员！");
						}
						carApply.setApproveStatus(ApproveStatusEnum.CANCEL.getValue().toString());
						row = carApplyService.updateCarApply(carApply);
				}
			}
			return row > 0 ? ResultGenerator.genSuccessResult("审核成功") : ResultGenerator.genFailResult("审核失败，请联系管理员或稍后重试！");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("审核失败，请联系管理员或稍后重试！");
		}
	}

	private Integer auditUpdateStatus(HttpServletRequest request, Long applyId, String approveStatus) {
		int row;
		CarApply carApply = carApplyService.selectCarApplyById(applyId);
		if (carApply == null) {
			return 0;
		}
		carApply.setApproveStatus(approveStatus);
		row = carApplyService.updateCarApply(carApply);
		if (row <= 0) {
			return row;
		}
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(applyId);
		auditRecord.setApplyType(AuditRecordTypeEnum.CarAudit.getValue());
		auditRecord.setStatus(approveStatus + "");
		auditRecord.setCreateBy(getLoginName(request));
		auditRecord.setCreateTime(new Date());
		auditRecord.setUpdateBy(getLoginName(request));
		auditRecordService.insertAuditRecord(auditRecord);
		return row;
	}


}
