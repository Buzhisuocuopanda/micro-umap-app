package com.mkst.umap.app.admin.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo;
import com.mkst.umap.app.admin.dto.arraign.DayStatusDto;
import com.mkst.umap.app.admin.dto.meeting.*;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IAuditProgressService;
import com.mkst.umap.app.admin.service.IMeetingAttendeeService;
import com.mkst.umap.app.admin.service.IMeetingService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.MeetingAuditProgressEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName MeetingApi
 * @Description MeetingApi
 * @Author wangyong
 * @Modified By:
 * @Date 2020-07-31 17:46
 */
@Api(value = "会议室相关接口")
@Slf4j
@RestController
@RequestMapping(value = "/api/meeting")
public class MeetingApi extends BaseApi {

    @Autowired
    private IMeetingService meetingService;
    @Autowired
    private IMeetingAttendeeService meetingAttendeeService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IArraignRoomService roomService;
    @Autowired
    private AuditRecordMapper auditRecordMapper;
    @Autowired
    private IAuditProgressService progressService;

    @Login
    @Log(title = "新增会议申请", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addSave")
    @ApiOperation(value = "新增一条会议申请")
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(required = true) MeetingParam meetingParam) {

        String loginName = getLoginName(request);

        if (reqMeetingExisted(meetingParam)) {
            return ResultGenerator.genFailResult("您选择的时间点已被占用，请刷新页面重新尝试");
        }
        // 插入会议申请表
        Meeting insertMeeting = transObject(meetingParam, Meeting.class);
        insertMeeting.setCreateBy(loginName);
        insertMeeting.setUpdateBy(loginName);
        meetingService.insertMeeting(insertMeeting);

        Long meetingId = insertMeeting.getId();

        // 插入参与人员
        Long[] attendeeArr = meetingParam.getAttendeeArr();
        MeetingAttendee insertAttendee = new MeetingAttendee();
        insertAttendee.setMeetingId(meetingId);
        insertAttendee.setCreateBy(loginName);
        insertAttendee.setUpdateBy(loginName);

        // 默认申请人就是参会人

        insertAttendee.setUserId(meetingParam.getUseBy());
        int row = meetingAttendeeService.insertMeetingAttendee(insertAttendee);

        // 插入参会人
        for (Long attendeeId : attendeeArr) {
            insertAttendee.setMeetingId(meetingId);
            insertAttendee.setUserId(attendeeId);
            meetingAttendeeService.insertMeetingAttendee(insertAttendee);
        }

        // 插入流程
        String target = sysUserService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName();
        AuditProgress insertProgress = new AuditProgress();
        insertProgress.setBusinessType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
        insertProgress.setBusinessId(meetingId);
        insertProgress.setTargetType(KeyConstant.LOGIN_NAME);
        insertProgress.setTarget(target);
        insertProgress.setProgress(MeetingAuditProgressEnum.DEFAULT.getProgressCode());
        insertProgress.setCreateBy(loginName);
        progressService.insertAuditProgress(insertProgress);

        insertDefaultProgress(insertProgress);


        // 推送审核app内部消息
        if (row > 0){
            new Thread(() ->{
                sendAppMsg(target,meetingId);
            }).start();
        }

        return ResultGenerator.genSuccessResult("提交成功");
    }

    private void insertDefaultProgress(AuditProgress insertProgress) {

        AuditProgress hereProgress = new AuditProgress();
        hereProgress.setBusinessType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
        hereProgress.setBusinessId(insertProgress.getBusinessId());
        hereProgress.setTargetType(KeyConstant.LOGIN_NAME);
        hereProgress.setProgress(MeetingAuditProgressEnum.DEFAULT.getProgressCode());
        Integer progress = insertProgress.getProgress() + 1;
        while (StringUtils.isNotEmpty(MeetingAuditProgressEnum.getRoleCodeByProgressCode(progress))){
            if(progress == 1){
                hereProgress.setCreateBy(insertProgress.getTarget());
            }else {
                hereProgress.setCreateBy(null);
            }
            hereProgress.setProgress(progress);
            progressService.insertAuditProgress(hereProgress);
            progress++;
        }
    }

    private void sendAppMsg(String target, Long meetingId) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("会议申请");
        msgContent.setContent("一个会议申请待审核，请及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", meetingId.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_MEETING.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }

    /*private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("会议申请");
        msgContent.setContent("一个会议申请待审核，请及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
        msgContent.setParams(params);

        // 向所有的会议管理员发送消息
        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_HYSGLY.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_MEETING.getValue(), user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }*/

    private boolean reqMeetingExisted(MeetingParam meetingParam) {

        Meeting selectMeeting = new Meeting();
        selectMeeting.setReqStartTime(meetingParam.getStartTime());
        selectMeeting.setReqEndTime(meetingParam.getEndTime());
        selectMeeting.setRoomId(meetingParam.getRoomId());
        selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        selectMeeting.setAuditStatusArr(new String[]{"0","1"});
        List<Meeting> meetings = meetingService.selectMeetingList(selectMeeting);
        return meetings != null && meetings.size() != 0;
    }

    @Login
    @PostMapping(value = "getEndTime")
    @ApiOperation(value = "获取可选的下一个时间点")
    public Result getEndTime(@ApiParam @RequestBody MeetingParam meetingParam) {
        Date nextStartTime = meetingService.getNextStartTime(meetingParam);
        if (nextStartTime == null) {
            return ResultGenerator.genSuccessResult("查询成功");
        }
        return ResultGenerator.genSuccessResult("查询成功", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nextStartTime));
    }

    @ApiLog(title = "会议室日期接口", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/dayStatusList")
    @ApiOperation("接待室日期接口")
    @Login
    public Result dayStatusList() {

        List<DayStatusDto> listDayStatus = new ArrayList<>();
        ArrayList<Date> futureDaysList = UmapDateUtils.getFutureDaysList(0, 5);
        for (Date date : futureDaysList) {
            DayStatusDto dayStatusDto = new DayStatusDto();
            dayStatusDto.setDate(date);
            dayStatusDto.setStatus("0");
            String today = UmapDateUtils.isToday(date) ? "今天" : UmapDateUtils.getWeekOfDate(date);
            dayStatusDto.setWeekDay(today);

            listDayStatus.add(dayStatusDto);
        }
        return ResultGenerator.genSuccessResult("查询成功", listDayStatus);
    }

    @Login
    @PostMapping("roomSchedule")
    @ApiOperation(value = "获取会议室排班信息")
    public Result roomSchedule(@RequestBody @ApiParam RoomScheduleParam roomScheduleParam) {
        List<MeetingRoomScheduleDto> scheduleDtos = meetingService.getRoomSchedule(roomScheduleParam);
        return ResultGenerator.genSuccessResult("查询成功", scheduleDtos);
    }

    @Login
    @PostMapping("/roomInfoList")
    @ApiOperation("房间使用情况")
    public Result roomInfoList(@RequestBody @ApiParam MeetingParam meetingParam) {

        startPage();
        List<MeetingRoomInfoDto> roomInfoList = meetingService.getRoomInfoList(meetingParam);
        if (roomInfoList == null || roomInfoList.size() == 0) {
            return ResultGenerator.genSuccessResult("当前无可用的会议室，请联系管理员或稍后重试！");
        }
        return ResultGenerator.genSuccessResult("查询成功", roomInfoList);
    }

    @Login
    @PostMapping("/myMeetingReq")
    @Deprecated
    @ApiOperation("获取我的会议申请")
    public Result myMeetingReq(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam) {
        meetingParam.setUseBy(getUserId(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.selectMyMeetingList(meetingParam);
        return ResultGenerator.genSuccessResult("查询成功", meetingInfoResults);
    }

    @Login
    @PostMapping("/myApply")
    @ApiOperation("我的申请")
    public Result myApply(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam) {

        meetingParam.setNowUserId(getUserId(request));
        meetingParam.setNowUserLoginName(getLoginName(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.selectApplyList(meetingParam);
        meetingInfoResults.stream().forEach(result -> {
            result.setAuditStatusName(AuditStatusEnum.getName(Integer.parseInt(result.getAuditStatus())));
        });
        return ResultGenerator.genSuccessResult("查询成功", meetingInfoResults);
    }

    @Login
    @PostMapping("/myAttended")
    @ApiOperation("我参加的会议")
    public Result myAttended(HttpServletRequest request,@RequestBody @ApiParam MeetingParam meetingParam){
        meetingParam.setNowUserId(getUserId(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.getMyAttendedMeetingInfo(meetingParam);
        return ResultGenerator.genSuccessResult("查询成功", meetingInfoResults);
    }

    @Login
    @PostMapping(value = "/myAudit")
    @ApiOperation(value = "我的审批")
    public Result myAudit(HttpServletRequest request,@RequestBody @ApiParam MeetingParam meetingParam){

        // auditStatus、checkDate
        Meeting selectMeeting = transObject(meetingParam, Meeting.class);
        // selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        selectMeeting.setTarget(getLoginName(request));
        startPage();
        List<MeetingWebInfoVo> meetingWebInfoVos = meetingService.selectMeetingAuditList(selectMeeting);
        List<MeetingAuditInfoDto> resultList = transList(meetingWebInfoVos, MeetingAuditInfoDto.class);
        return ResultGenerator.genSuccessResult("查询成功",resultList);
    }

    @Login
    @PostMapping(value = "/audit")
    @ApiOperation(value = "进行审核")
    public Result audit(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam){
        Long[] ids = meetingParam.getIds();
        String loginName = getLoginName(request);
        for (Long id : ids){
            int row = meetingService.auditMeeting(id,meetingParam.getAuditStatus(),meetingParam.getReason(),loginName,meetingParam);
        }
        String target = meetingParam.getTarget();
        if (BeanUtil.isNotEmpty(target)){

            // 推送审核app内部消息

                new Thread(() ->{
                    sendAppMsg(target,meetingParam.getId());
                }).start();
        }
        return ResultGenerator.genSuccessResult("审核成功");
    }

    @Login
    @GetMapping("/meetingDetail")
    @ApiOperation("/获取会议详情")
    public Result meetingDetail(HttpServletRequest request,@RequestParam @ApiParam Long meetingId) {
        return ResultGenerator.genSuccessResult("查询成功", meetingService.getDetailById(meetingId,getLoginName(request)));
    }

    @Login
    @GetMapping("/auditList")
    @ApiOperation("/获取会议审核列表")
    public Result auditList(@RequestParam @ApiParam Long meetingId) {
        return ResultGenerator.genSuccessResult("查询成功", meetingService.selectAuditList(meetingId));
    }

    @Login
    @GetMapping("/cancelMeeting")
    @ApiOperation("/取消会议")
    public Result cancelMeeting(@RequestParam @ApiParam Long meetingId,@RequestParam(required = false) @ApiParam String remark) {
        // 会议开始前一小时内或会议开始后无法取消会议
        Meeting meeting = meetingService.selectMeetingById(meetingId);
        long timeGap = UmapDateUtils.getTimeGap(new Date(), meeting.getStartTime());
        if (timeGap < DateUtils.MILLIS_PER_HOUR){
            ResultGenerator.genSuccessResult("取消失败，会议开始前一小时内或会议开始后无法取消会议！！！");
        }
        int row = meetingService.cancelMeeting(meetingId,remark);
        return row > 0 ? ResultGenerator.genSuccessResult("取消成功") : ResultGenerator.genSuccessResult("取消失败，请联系管理员");
    }

    @Login
    @GetMapping("/cancelMeetingMsg")
    @ApiOperation("/取消会议通知")
    public Result cancelMeetingMsg(@RequestParam @ApiParam Long meetingId,@RequestParam(required = false) @ApiParam String remark) {
        int row = meetingService.cancelMeetingMsg(meetingId,remark);
        return row > 0 ? ResultGenerator.genSuccessResult("取消成功") : ResultGenerator.genSuccessResult("取消失败，请联系管理员");
    }

    @Login
    @PostMapping("/changeMeeting")
    @ApiOperation("变更会议")
    @ApiLog(title = "变更会议前检查",businessType = BusinessType.OTHER,ApiOperatorType = ApiOperatorType.POST)
    public Result changeMeeting(HttpServletRequest request, @RequestBody @ApiParam Meeting meeting){
        new Thread(() ->{
            meetingService.changeMeeting(meeting);
        }).start();
        return ResultGenerator.genSuccessResult("通知成功");
    }

    @Login
    @PostMapping("/beforeChange")
    @ApiOperation("变更会议前检查")
    public Result beforeChange(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam){

        MeetingChangeCheckDto result = new MeetingChangeCheckDto();

        Meeting currentMeeting = meetingService.selectMeetingById(meetingParam.getId());
        if (BeanUtil.isEmpty(currentMeeting)){
            return ResultGenerator.genFailResult("会议id错误");
        }
        result.setCurrentMeeting(currentMeeting);
        result.setId(meetingParam.getId());

        List<MeetingDetailDto> occupyMeeting = meetingService.getOccupyMeeting(meetingParam);


        if (CollectionUtil.isEmpty(occupyMeeting)) {
            result.setConflict(false);
            return ResultGenerator.genSuccessResult("未占用",result);
        }

        result.setConflict(true);
        result.setConflictMeetingList(occupyMeeting);
        return ResultGenerator.genSuccessResult("占用",result);
    }

    @Login
    @PostMapping("/meetingRoomList")
    @ApiOperation("会议室列表")
    public Result meetingRoomList(){
        List<ArraignRoom> arraignRooms = roomService.selectRoomByType(KeyConstant.ARENA_TYPE_MEETING_ROOM);
        return ResultGenerator.genSuccessResult(arraignRooms);
    }

    @Login
    @ApiOperation(value = "获取审核记录信息")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@RequestParam(required = true) @ApiParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordVo(selectRecord);

        return ResultGenerator.genSuccessResult("查询成功",auditRecords);
    }

    @Login
    @ApiOperation(value = "依据日期获取会议申请数量")
    @PostMapping(value = "/dayCount")
    public Result dayCount(@RequestBody @ApiParam MeetingParam param){
        startPage();
        List<NameCountResult> nameCountResults = meetingService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("查询成功",nameCountResults);
    }

    @ApiLog(title = "时间排序-申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-申请info")
    @PostMapping("/timeApplyList")
    @Login
    public Result timeApplyList(@RequestBody @ApiParam MeetingParam param){
        // 只查询未取消的，且属于未审核或者审核通过的会议
        param.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        param.setAuditStatusArr(new String[]{KeyConstant.EVENT_AUDIT_STATUS_WAIT,KeyConstant.EVENT_AUDIT_STATUS_PASS});
        return ResultGenerator.genSuccessResult("success",meetingService.selectTimeApplyList(param));
    }

    @ApiLog(title = "时间排序-我的申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-我的申请info")
    @PostMapping("/myTimeApplyList")
    @Login
    public Result myTimeApplyList(HttpServletRequest request,@RequestBody @ApiParam MeetingParam param){
        // 查询我申请的或者使用人是我的会议
        param.setNowUserId(getUserId(request));
        param.setNowUserLoginName(getLoginName(request));
        return ResultGenerator.genSuccessResult("success",meetingService.selectTimeApplyList(param));
    }
}
