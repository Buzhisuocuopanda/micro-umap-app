package com.mkst.umap.app.admin.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.domain.SysDictData;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysRoleMapper;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.HolidayUtil;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.backup.BackUpParam;
import com.mkst.umap.app.admin.api.bean.result.BackUpApplyCount;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.domain.vo.ApplyInfoVo;
import com.mkst.umap.app.admin.domain.vo.BackUpRoomVo;
import com.mkst.umap.app.admin.dto.apply.ApplyInfoDto;
import com.mkst.umap.app.admin.dto.apply.ApplyNumberDto;
import com.mkst.umap.app.admin.dto.apply.BackUpRoomDto;
import com.mkst.umap.app.admin.dto.apply.DoorLockDeviceDto;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IBackUpRoomService;
import com.mkst.umap.app.admin.util.MyDateUtil;
import com.mkst.umap.app.admin.util.WebcardApiUtil;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.ApplyStatusEnum;
import com.mkst.umap.app.common.enums.ApproveStatusEnum;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @ClassName ApplyInfoApi
 * @Description 备勤间申请服务接口
 * @Author lcq
 */
@Api("备勤间申请服务接口")
@RestController
@RequestMapping("/api/backUp")
public class BackUpApplyInfoApi extends BaseApi {

    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private IBackUpRoomService backUpRoomService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAuditRecordService auditRecordService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysDictDataService sysDictDataService;

    @PostMapping("/applyInfo")
    @ApiOperation("获取备勤间申请表信息集合")
    @Login
    public Result applyInfo(HttpServletRequest request,@RequestBody  @ApiParam(name = "applyInfoDto", value = "申请表数据传递对象", required = true)ApplyInfoDto applyInfoDto) {
        try {
            List<ApplyInfoVo> applyInfoVos;
            Long applicantId;
//            Long approverId;
            SysUser sysUser = getSysUser(request);
            if(StrUtil.isBlank(applyInfoDto.getSelectType())) {
                applyInfoDto.setSelectType("0");
            }
            if("0".equals(applyInfoDto.getSelectType())){
                applicantId = sysUser.getUserId();
                applyInfoDto.setApplicantId(applicantId);
            }/*else{
                approverId = sysUser.getUserId();
                applyInfoDto.setApproverId(approverId);
            }*/
            //查询数据库 返回满足要求的vo集合
            startPage();
            applyInfoVos = applyInfoService.selectApplyVo(applyInfoDto);

            return ResultGenerator.genSuccessResult("查询申请信息成功",applyInfoVos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("获取申请表信息失败，请稍后再试！");
        }
    }

    @GetMapping("/detail")
    @ApiOperation("备勤间预约详情")
    @ResponseBody
    public Result selectById(Integer applyId) {
        Map<String , Object> params = new HashMap<>();
        params.put("applyId",applyId);
        List<ApplyInfo> list = applyInfoService.selectApplyInfoListByMap(params);
        if(list.size() != 1){
            return ResultGenerator.genFailResult("申请ID错误");
        }
        return ResultGenerator.genSuccessResult("获取成功",list.get(0));
        /*try{
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
            List<AuditParam> auditParamList = new LinkedList<>();
            ApplyInfoVo vo = applyInfoService.selectApplyVoById(applyId);
            List<BackUpGuestVo> guests = backUpGuestService.selectGuestVoList(vo.getApplyId());
            vo.setBackUpGuests(guests);
            vo.setPeopleNum(guests.size());
            List<String> days = DateUtils.getDays(sdf.format(vo.getStartTime()),sdf.format(vo.getEndTime()));
            vo.setDayNum((long) days.size() - 1);
            if(ApplyStatusEnum.Pending.getValue().equals(vo.getApplyStatus())){
                AuditParam param = new AuditParam();
                param.setApproveStatus(ApproveStatusEnum.Pending.getValue());
                param.setApproveType(ApproveTypeEnum.APPROVE_BACKUP.getValue());
                param.setDate(vo.getCreateTime());
                auditParamList.add(param);
            }else {
                AuditRecord auditRecord = new AuditRecord();
                auditRecord.setApplyId(applyId);
                auditRecord.setApplyType(AuditRecordTypeEnum.BackUpAudit.getValue());
                List<AuditRecord> auditRecordList = auditRecordService.selectAuditRecordList(auditRecord);
                for(AuditRecord a : auditRecordList){
                    AuditParam param = new AuditParam();
                    SysUser sysUser = sysUserService.selectUserByLoginName(a.getCreateBy());
                    param.setUrl(sysUser.getAvatar());
                    param.setUserName(a.getCreateBy());
                    param.setApproveStatus(Integer.valueOf(a.getStatus()));
                    param.setApproveType(ApproveTypeEnum.APPROVE_CAR.getValue());
                    param.setDate(vo.getCreateTime());
                    auditParamList.add(param);
                }
            }
            vo.setAuditParamList(auditParamList);
            return ResultGenerator.genSuccessResult("获取成功",vo);
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
        }*/
    }

    @GetMapping("/lockDetail")
    @ApiOperation("备勤间蓝牙锁详情")
    @ResponseBody
    public Result lockDetail(Integer roomId) {
        // 获取锁设备ID
        BackUpRoom backUpRoom = backUpRoomService.selectBackUpRoomById(roomId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", backUpRoom.getDoorLockId());

        String url = "/api/device/getDeviceDetail";
		String responseData = WebcardApiUtil.sendPost(url, paramMap);
		DoorLockDeviceDto doorLock = JSON.parseObject(responseData, DoorLockDeviceDto.class);
        if(doorLock == null){
            return ResultGenerator.genFailResult("备勤间尚未关联门锁，请联系管理员。");
        }
        return ResultGenerator.genSuccessResult(doorLock);
    }
    
    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date(),"yyyy年MM月dd日"));
        System.out.println(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
	}
    
    @Login
    @ApiOperation("新增申请")
    @PostMapping("/addSave")
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request,@RequestBody  @ApiParam(name = "applyInfoDto", value = "申请表数据传递对象", required = true) ApplyInfoDto applyInfoDto)
    {
        ApplyInfo applyInfo = new ApplyInfo();
        //将dto转换为 申请表实体 并set当前用户id和 待审批状态码
        BeanUtils.copyProperties(applyInfoDto,applyInfo);

        //applyInfo.setBackUpGuests(transList(applyInfoDto.getGuests(),BackUpGuest.class));
        //applyInfo.setApplicantId(getUserId(request));
        applyInfo.setCreateBy(getLoginName(request));
        applyInfo.setApplyStatus(ApplyStatusEnum.Pending.getValue());
        applyInfo.setApproveStatus(ApproveStatusEnum.Pending.getValue());
        //int rows = applyInfoService.insertApplyInfoWithGuests(applyInfo);

        
        // step 1 判断预约时间是否超过晚上10点，如果10以后系统自动审批通过
        int auditTime = Integer.valueOf(SysConfigUtil.getKey("umap_backupRoom_auditTime"));
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= auditTime) {
        	applyInfo.setApplyStatus(ApplyStatusEnum.Approval.getValue());
            applyInfo.setApproveStatus(ApproveStatusEnum.SUCCESS.getValue());
        }
        // step 2 锁定房间，房间锁定规则：双人间→三人间→单人间，需要避免有房间经常轮空的情况
        SysUser applyUser = getSysUser(request);
        BackUpRoom room = allotRoom(applyUser.getSex(), applyInfo.getStartTime());
        if(room == null) {
        	return ResultGenerator.genFailResult("房间已全部约满");
        }
        applyInfo.setRoomId(room.getRoomId());
        int rows = applyInfoService.insertApply(applyInfo);
        
        room.setUseCount(room.getUseCount()+1); // 使用次数+1
        backUpRoomService.updateBackUpRoom(room);
        // step 3 通知备勤间管理员审批

        // 推送app内部消息
        if (rows > 0){
            new Thread(() ->{
                sendAppMsg(applyInfo.getApplyId());
            }).start();
        }

        return rows>0 ? ResultGenerator.genSuccessResult("提交申请成功！") : ResultGenerator.genFailResult("提交申请失败，请联系管理员");
    }

    
	/**
	 * <p>分配房间
	 * 
	 * <p>房间锁定规则：双人间→三人间→单人间，需要避免有房间经常轮空的情况
	 */
	private BackUpRoom allotRoom(String userSex, Date date) {
		// 所有房间
		List<BackUpRoom> allRoomList = backUpRoomService.selectBackUpRoomList(new BackUpRoom());
		//根据房间类型来分组
		Map<String, List<BackUpRoom>> roomGroup = new HashMap<String, List<BackUpRoom>>();
		List<BackUpRoom> roomGroupList = null;
		for (BackUpRoom backUpRoom : allRoomList) {
			// roomType值应遵循规则，1：单人间，2：双人间，3：三人间，以此类推
			String roomType = backUpRoom.getRoomType().toString();
			if(roomGroup.containsKey(roomType)) {
				roomGroupList = roomGroup.get(roomType);
				roomGroupList.add(backUpRoom);
			}else {
				roomGroupList = new ArrayList<BackUpRoom>();
				roomGroupList.add(backUpRoom);
				roomGroup.put(roomType, roomGroupList);
			}
		}
		
		// 该时间每个房间的预约人数
		List<ApplyNumberDto> applyInfoList = applyInfoService.countGroupbyRoomIdByTime(date);
		Map<String, ApplyNumberDto> applyMap = new HashMap<String, ApplyNumberDto>();
		for (ApplyNumberDto map : applyInfoList) {
			applyMap.put(map.getRoomId(), map);
		}
		
		// 根据房间类型数组来遍历（排序需要遵循规则：双人间、三人间、N人间、最后单人间）
		List<SysDictData> listDictData = sysDictDataService.selectDictDataByType("back_up_room_type");
		for (SysDictData sysDictData : listDictData) {
			// 几人间
			Integer roomType = Integer.valueOf(sysDictData.getDictValue());
			roomGroupList = roomGroup.get(sysDictData.getDictValue());
			if(roomGroupList == null) {
				continue;
			}
			// 优先分配已住人，但未住满的房间，如果不存在没住满的房间，则优先安排入住次数最少的同类型房间
			for (BackUpRoom backUpRoom : roomGroupList) {
				// 该房间已约人数
				ApplyNumberDto applyNumberDto = applyMap.get(String.valueOf(backUpRoom.getRoomId()));
				if(applyNumberDto == null) {
					continue;
				}
				Integer applyNumber = applyNumberDto.getApplyNumber();
				// 如果该房间未约满，并且申请人性别跟已申请这个房间的人相同，则分配这个房间
				if(applyNumber != null && applyNumber.intValue() < roomType.intValue() && userSex.equals(applyNumberDto.getApplyUserSex())) {
					return backUpRoom;
				}
			}
			// 按入住次数升序排列
			Collections.sort(roomGroupList, new Comparator<BackUpRoom>() {
				@Override
				public int compare(BackUpRoom o1, BackUpRoom o2) {
					return o1.getUseCount() - o2.getUseCount();
				}
			});
			for (BackUpRoom backUpRoom : roomGroupList) {
				// 分配第一个预约次数最少的空房间
				ApplyNumberDto applyNumberDto = applyMap.get(String.valueOf(backUpRoom.getRoomId()));
				if(applyNumberDto == null || applyNumberDto.getApplyNumber() == null) {
					return backUpRoom;
				}
			}
		}
		
		return null;
	}

    // todo 信息有待完善
    private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("备勤间预约待审核");
        msgContent.setContent("有人提交新的备勤间预约请求，请您及时审批！");

        Map<String ,String> params =new HashMap<>();
        params.put("bizType",id.toString());
        params.put("bizKey",BusinessTypeEnum.UMAP_BACKUP_MANAGE.getValue());
        // todo 测试的处理地址
        params.put("pageUrl","http://localhost:18083/umap/index");
        msgContent.setParams(params);

        // 向所有的视频提审审核员发送消息 18-》视频提审审核员角色id
        SysUser selectUser = new SysUser();
        SysRole sysRole = getSysRole();
        selectUser.setRoleIds(new Long[]{sysRole.getRoleId()});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent,id.toString(), BusinessTypeEnum.UMAP_BACKUP_MANAGE.getValue(),user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }

    /**
     * 备勤间预约成功发送短信
     */
    private void sendReserveSuccessSmsMsg(ApplyInfo info) {
        SmsMsgContent msgContent = new SmsMsgContent();
        msgContent.setTitle("备勤间预约成功通知");
        msgContent.setContent(StrUtil.format("【龙华区人民检察院】【门禁预约】{}您好，您的门禁预约已通过，入住信息如下：入住时间：{}，房号【{}】,注意保管好个人物品，以免丢失。开门权限于明早九点自动失效，请您按时离开房间，多谢合作。"
                , info.getApplicant(), DateUtil.format(info.getStartTime(),"yyyy年MM月dd日"), info.getRoomNum()));
        MsgPushUtils.push(msgContent, info.getApplyId().toString(), "umap_backup_success", "[CODE]"+info.getApplicantPhoneNumber());
        MsgPushUtils.getMsgPushTask().execute();
    }

    private SysRole getSysRole(){
        return sysRoleMapper.checkRoleKeyUnique(RoleKeyEnum.ROLE_ADMIN.getValue());
    }
    
    @Login
    @ApiOperation("撤销申请")
    @PostMapping("/cancelApply")
    @Transactional(rollbackFor = Exception.class)
    public Result cancelApply(HttpServletRequest request, @ApiParam(name = "applyId", value = "申请ID", required = true) Long applyId)
    {
    	ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
    	Long userId = this.getUserId(request);
    	if(userId.longValue() != applyInfo.getApplicantId().longValue()) {
    		return ResultGenerator.genFailResult("非法操作");
    	}
    	applyInfo.setApplyStatus(ApplyStatusEnum.Cancel.getValue());
    	applyInfo.setApproveStatus(ApproveStatusEnum.CANCEL.getValue());
    	applyInfoService.updateApplyInfo(applyInfo);
    	return ResultGenerator.genSuccessResult("撤销成功");
    }

    @ApiOperation("申请表审核")
    @PostMapping("/audit")
    @Login
    public Result audit(HttpServletRequest request,@RequestBody ApplyInfoDto applyInfoDto)
    {

        Map<String , Object> params = new HashMap<>();
        params.put("applyId",applyInfoDto.getApplyId());
        List<ApplyInfo> list = applyInfoService.selectApplyInfoListByMap(params);
        if(list.size() != 1){
            return ResultGenerator.genFailResult("申请ID错误");
        }
        ApplyInfo ai = list.get(0);

        if(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(applyInfoDto.getApproverStatus())){
        	ai.setApplyStatus(ApplyStatusEnum.Approval.getValue());
            new Thread(() ->{
                sendReserveSuccessSmsMsg(ai);
            }).start();
		}else {
			ai.setApplyStatus(ApplyStatusEnum.Fail.getValue());
		}
        ai.setApproveStatus(Integer.parseInt(applyInfoDto.getApproverStatus()));
        ai.setApprovalUserId(getUserId(request));
        applyInfoService.auditNew(ai);
        return ResultGenerator.genSuccessResult("审核成功");
        /*try{
            *//*
             * 0 直接审批  1 提交领导审批  2 驳回
             * 如果为 0 审批成功
             * 如果为 1 提交领导审批 则将审核人id改为领导id 状态依旧为待审核
             * 如果为 2 驳回  则将状态修改为 驳回
             * 3 取消
             *//*

           *//*ApplyInfo applyInfo = transObject(applyInfoDto,ApplyInfo.class);
           applyInfo.setApplyStatus(applyInfoDto.getApplyStatusList().get(0));*//*
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyInfoDto.getApplyId());
            if(applyInfo == null) {
                return ResultGenerator.genFailResult("获取申请表信息为空，请联系管理员！");
            }

            int rows;
            rows = updateAudit(applyInfoDto.getApproverStatus(),applyInfo,request);
            return rows>0 ? ResultGenerator.genSuccessResult("审核成功") : ResultGenerator.genFailResult("审核失败，请联系管理员");
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genFailResult("获取申请表信息失败，请稍后再试！");
        }*/
    }

    //创建审核记录
    private void createAuditRecord(ApplyInfo applyInfo, Integer status,String loginName){
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setApplyId(applyInfo.getApplyId());
        auditRecord.setApplyType(AuditRecordTypeEnum.BackUpAudit.getValue());
        auditRecord.setStatus(status+"");
        auditRecord.setCreateBy(loginName);
        auditRecord.setUpdateBy(loginName);
        auditRecordService.insertAuditRecord(auditRecord);
    }
    @ApiOperation("审核获取房间使用详情")
    @PostMapping("/selectApplyRoomUse")
    public Result selectApplyRoomUse(@RequestBody  @ApiParam(name = "applyInfoDto", value = "申请表数据传递对象", required = true) ApplyInfoDto applyInfoDto){
        /*
         * 获取该申请单的开始和结束时间
         * 查出该时间区类已预订房间集合
         *
         * 获取全部房间集合
         * 将已预订房间set进全部集合再返回
         */
        startPage();
        List<BackUpRoom> useRoomList = new LinkedList<>();
        List<Long> useRoomIds = applyInfoService.selectApplyRoomByDates(applyInfoDto);
        if(CollUtil.isNotEmpty(useRoomIds)) {
            useRoomList = backUpRoomService.selectRoomListByIds(useRoomIds);
        }

        List<BackUpRoom> allRoomList = backUpRoomService.selectBackUpRoomList(new BackUpRoom());
        List<BackUpRoomVo> voList = null;
        voList = useRoomAddAll(allRoomList,useRoomList);
        return ResultGenerator.genSuccessResult("查询成功",getDataTable(voList));
    }

    @ApiOperation("获取选定日期备勤间详情")
    @PostMapping("/NowDateRoomInfo")
    public Result NowDateRoomInfo(@RequestBody @ApiParam(name = "backUpRoomDto", value = "备勤间数据传递对象", required = true)BackUpRoomDto backUpRoomDto)
    {
        try{
            List<BackUpRoom> allRoomList = backUpRoomService.selectBackUpRoomList(new BackUpRoom());
            List<BackUpRoomVo> voList = null;
            if(allRoomList == null) {
                return ResultGenerator.genFailResult("获取备勤间列表失败，请联系管理员");
            }
            List<BackUpRoom> useRooms;
            if(backUpRoomDto.getNowDate() != null){
                useRooms = backUpRoomService.selectBackUpRoomByDate(backUpRoomDto);
                voList=useRoomAddAll(allRoomList,useRooms);
            }
            return ResultGenerator.genSuccessResult(voList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genFailResult("获取备勤间列表失败，请联系管理员");
        }
    }

    //将已预订房间或已用房间修改状态为使用之后加入 所有房间列表
    public List<BackUpRoomVo> useRoomAddAll(List<BackUpRoom> allRoomList, List<BackUpRoom> useRoomList){
        for(BackUpRoom room : useRoomList){
            allRoomList = allRoomList.stream().filter(r -> !r.getRoomId().equals(room.getRoomId())).collect(Collectors.toList());
            room.setStatus("2");
            allRoomList.add(room);
        }
        allRoomList = allRoomList.stream().sorted(Comparator.comparingLong(BackUpRoom::getRoomId)).collect(Collectors.toList());
        return transList(allRoomList,BackUpRoomVo.class);
    }


    @ApiOperation("申请表状态修改")
    @GetMapping("/updateApplyStatus")
    @Login
    public Result updateApplyStatus(HttpServletRequest request,@RequestParam(value = "statusCode", required = true) String statusCode, @RequestParam(value = "applyIds", required = true) List<Long> applyIds)
    {
        if(statusCode == null || applyIds == null || applyIds.size() <=0) {
            return ResultGenerator.genFailResult("传入参数存在空值，请重新输入参数！");
        }
        int row = 0;
        for(Long applyId : applyIds){
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
            if(applyInfo == null) {
                return ResultGenerator.genFailResult("修改申请单状态失败,数据不存在，请联系管理员");
            }
            row = updateAudit(statusCode,applyInfo,request);
        }

        return row>0 ? ResultGenerator.genSuccessResult("修改申请单状态成功！") : ResultGenerator.genFailResult("修改申请单状态失败，请联系管理员");
    }

    public int updateAudit(String approverStatus,ApplyInfo applyInfo,HttpServletRequest request){
        int rows = 0;
        //0 审批通过，状态改为已通过   1： 提交领导 //暂时不用   2  已驳回  3 取消
        switch (approverStatus){
            case "0":
                applyInfo.setApplyStatus(ApproveStatusEnum.SUCCESS.getValue());
                rows = applyInfoService.updateApplyInfo(applyInfo);
                createAuditRecord(applyInfo,ApproveStatusEnum.SUCCESS.getValue(),getLoginName(request));
                return rows;
            case "1":
                return rows;
            case "2":
                applyInfo.setApplyStatus(ApproveStatusEnum.FAIL.getValue());
                rows = applyInfoService.updateApplyInfo(applyInfo);
                createAuditRecord(applyInfo,ApproveStatusEnum.FAIL.getValue(),getLoginName(request));
                return rows;
            case "3":
                applyInfo.setApplyStatus(ApproveStatusEnum.CANCEL.getValue());
                rows = applyInfoService.updateApplyInfo(applyInfo);
                return rows;
        }
        return rows;
    }

    @Login
    @ApiOperation(value = "依据日期获取备勤间申请数量")
    @PostMapping(value = "/dayCount")
    public Result dayCount(HttpServletRequest request,@RequestBody @ApiParam BackUpParam param){
        startPage();
        Long userId = getUserId(request);
        param.setApplicantId(userId);
        List<NameCountResult> nameCountResults = applyInfoService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("查询成功",nameCountResults);
    }

    @Login
    @ApiOperation(value = "获取指定日期的预约列表")
    @PostMapping(value = "/timeApplyList")
    public Result timeApplyList(HttpServletRequest request,@RequestBody @ApiParam BackUpParam param){
        Map<String , Object> params = new HashMap<>();
        params.put("checkDate",MyDateUtil.format(param.getCheckDate()));
        if(param.getApproveStatus() != null){
            params.put("approveStatus",param.getApproveStatus());
        }
        if(param.getApplyStatus() != null){
            params.put("applyStatus",param.getApplyStatus());
        }
        Long userId = getUserId(request);
        params.put("applicantId",userId);
        List<ApplyInfo> list = applyInfoService.selectApplyInfoListByMap(params);
        return ResultGenerator.genSuccessResult("查询成功",list);
    }
    
    @Login
    @ApiOperation(value = "获取预约总数和今日预约数量")
    @GetMapping(value = "/countApplyNumber")
    public Result countApplyNumber(HttpServletRequest request){
    	BackUpApplyCount count = new BackUpApplyCount();
    	count.setDayNumber(applyInfoService.countApplyNumberByDay(DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd")));
    	count.setTotalNumber(applyInfoService.countApplyNumber());
        return ResultGenerator.genSuccessResult(count);
    }
    
    @Login
    @ApiOperation(value = "获取预约统计列表")
    @GetMapping(value = "/countApplyList")
    public Result countApplyList(HttpServletRequest request, @RequestParam(value = "startDate", required = true) Date startDate, @RequestParam(value = "endDate", required = true) Date endDate){
    	if(startDate == null || endDate == null) {
    		return ResultGenerator.genSuccessResult();
    	}
    	ApplyInfo applyInfo = new ApplyInfo();
    	applyInfo.setStartTime(startDate);
    	applyInfo.setEndTime(endDate);
    	List<BackUpApplyCount> countList = applyInfoService.countApplyNumberByUserAndDate(applyInfo);
        return ResultGenerator.genSuccessResult(countList);
    }

    @Login
    @ApiLog(title = "获取备勤间预约日历", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/calendarList")
    @ApiOperation("获取备勤间预约日历")
    public Result calendarList() {
    	// 每个房间可住人数
    	BackUpRoom room = new BackUpRoom();
    	room.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
    	List<BackUpRoom> roomList = backUpRoomService.selectBackUpRoomList(room);
    	// 所有房间每天可预约人数
    	int totalApplyNum = 0;
    	for (BackUpRoom backUpRoom : roomList) {
    		totalApplyNum += backUpRoom.getRoomType();
		}
    	
    	int limitDayCount = Integer.parseInt(SysConfigUtil.getKey(KeyConstant.BACKUPROOM_APPOINTMENT_LIMIT_DAY));
		String openTime = SysConfigUtil.getKey(KeyConstant.BACKUPROOM_APPOINTMENT_OPEN_TIME_KEY);
		//是否忽略假日
		String ignoreHolidayStr = SysConfigUtil.getKey(KeyConstant.BACKUPROOM_APPOINTMENT_IGNORE_HOLIDAY_KEY);
		boolean ignoreHoliday = "1".equals(ignoreHolidayStr);
		List<NameCountResult> result  = new ArrayList<>();
        //第二天放号结束时间
        int yesterEndTime = Integer.parseInt(SysConfigUtil.getKey(KeyConstant.BACKUPROOM_APPOINTMENT_LIMIT_HOUR));
		Date date = new Date();
        if (DateUtil.hour(date, true) < yesterEndTime) {
            date = DateUtil.offsetDay(date, -1);
        }
		for(int i = 1 ; i <= limitDayCount ; date = DateUtil.offsetDay(date , 1)){
			//如果是最后一天 校验是否到了开放时间
			date = DateUtil.parse(DateUtil.format(date,"yyyy-MM-dd"),"yyyy-MM-dd");
			if(i == limitDayCount && !checkOpen(openTime)){
				break;
			}
			if(HolidayUtil.isHoliday(date)){
				//如果预约提前天数不用忽略假日 则累计
				if(!ignoreHoliday){
					i++;
				}
				continue;
			}
			
			boolean available = false;
			// 当前日期的预约人数
			int todayApplyNum = applyInfoService.countApplyNumberByDay(date);
			if(todayApplyNum < totalApplyNum) {
				available = true;
			}
			
			NameCountResult c = new NameCountResult();
			c.setName(DateUtil.format(date,"yyyy-MM-dd"));
			if(available){
				c.setInfo("可预约");
				c.setStatus(true);
			}else{
				c.setInfo("已约满");
				c.setStatus(false);
			}
			result.add(c);
			i++;
		}
        return ResultGenerator.genSuccessResult(result);
    }
    
    /**
	 * 校验放号时间点
	 * @param openTimeStr
	 * @return
	 */
	private boolean checkOpen(String openTimeStr){
		Date openTime = DateUtil.parse(DateUtil.format(new Date(),"yyyy-MM-dd ")+ openTimeStr , "yyyy-MM-dd HH:mm");
		return new Date().getTime() > openTime.getTime();
	}

}
