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
@Api(value = "?????????????????????")
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
    @Log(title = "??????????????????", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addSave")
    @ApiOperation(value = "????????????????????????")
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(required = true) MeetingParam meetingParam) {

        String loginName = getLoginName(request);

        if (reqMeetingExisted(meetingParam)) {
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????????????????");
        }
        // ?????????????????????
        Meeting insertMeeting = transObject(meetingParam, Meeting.class);
        insertMeeting.setCreateBy(loginName);
        insertMeeting.setUpdateBy(loginName);
        meetingService.insertMeeting(insertMeeting);

        Long meetingId = insertMeeting.getId();

        // ??????????????????
        Long[] attendeeArr = meetingParam.getAttendeeArr();
        MeetingAttendee insertAttendee = new MeetingAttendee();
        insertAttendee.setMeetingId(meetingId);
        insertAttendee.setCreateBy(loginName);
        insertAttendee.setUpdateBy(loginName);

        // ??????????????????????????????

        insertAttendee.setUserId(meetingParam.getUseBy());
        int row = meetingAttendeeService.insertMeetingAttendee(insertAttendee);

        // ???????????????
        for (Long attendeeId : attendeeArr) {
            insertAttendee.setMeetingId(meetingId);
            insertAttendee.setUserId(attendeeId);
            meetingAttendeeService.insertMeetingAttendee(insertAttendee);
        }

        // ????????????
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


        // ????????????app????????????
        if (row > 0){
            new Thread(() ->{
                sendAppMsg(target,meetingId);
            }).start();
        }

        return ResultGenerator.genSuccessResult("????????????");
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
        msgContent.setTitle("????????????");
        msgContent.setContent("????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", meetingId.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_MEETING.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }

    /*private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("????????????");
        msgContent.setContent("????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
        msgContent.setParams(params);

        // ???????????????????????????????????????
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
    @ApiOperation(value = "?????????????????????????????????")
    public Result getEndTime(@ApiParam @RequestBody MeetingParam meetingParam) {
        Date nextStartTime = meetingService.getNextStartTime(meetingParam);
        if (nextStartTime == null) {
            return ResultGenerator.genSuccessResult("????????????");
        }
        return ResultGenerator.genSuccessResult("????????????", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nextStartTime));
    }

    @ApiLog(title = "?????????????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/dayStatusList")
    @ApiOperation("?????????????????????")
    @Login
    public Result dayStatusList() {

        List<DayStatusDto> listDayStatus = new ArrayList<>();
        ArrayList<Date> futureDaysList = UmapDateUtils.getFutureDaysList(0, 5);
        for (Date date : futureDaysList) {
            DayStatusDto dayStatusDto = new DayStatusDto();
            dayStatusDto.setDate(date);
            dayStatusDto.setStatus("0");
            String today = UmapDateUtils.isToday(date) ? "??????" : UmapDateUtils.getWeekOfDate(date);
            dayStatusDto.setWeekDay(today);

            listDayStatus.add(dayStatusDto);
        }
        return ResultGenerator.genSuccessResult("????????????", listDayStatus);
    }

    @Login
    @PostMapping("roomSchedule")
    @ApiOperation(value = "???????????????????????????")
    public Result roomSchedule(@RequestBody @ApiParam RoomScheduleParam roomScheduleParam) {
        List<MeetingRoomScheduleDto> scheduleDtos = meetingService.getRoomSchedule(roomScheduleParam);
        return ResultGenerator.genSuccessResult("????????????", scheduleDtos);
    }

    @Login
    @PostMapping("/roomInfoList")
    @ApiOperation("??????????????????")
    public Result roomInfoList(@RequestBody @ApiParam MeetingParam meetingParam) {

        startPage();
        List<MeetingRoomInfoDto> roomInfoList = meetingService.getRoomInfoList(meetingParam);
        if (roomInfoList == null || roomInfoList.size() == 0) {
            return ResultGenerator.genSuccessResult("??????????????????????????????????????????????????????????????????");
        }
        return ResultGenerator.genSuccessResult("????????????", roomInfoList);
    }

    @Login
    @PostMapping("/myMeetingReq")
    @Deprecated
    @ApiOperation("????????????????????????")
    public Result myMeetingReq(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam) {
        meetingParam.setUseBy(getUserId(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.selectMyMeetingList(meetingParam);
        return ResultGenerator.genSuccessResult("????????????", meetingInfoResults);
    }

    @Login
    @PostMapping("/myApply")
    @ApiOperation("????????????")
    public Result myApply(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam) {

        meetingParam.setNowUserId(getUserId(request));
        meetingParam.setNowUserLoginName(getLoginName(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.selectApplyList(meetingParam);
        meetingInfoResults.stream().forEach(result -> {
            result.setAuditStatusName(AuditStatusEnum.getName(Integer.parseInt(result.getAuditStatus())));
        });
        return ResultGenerator.genSuccessResult("????????????", meetingInfoResults);
    }

    @Login
    @PostMapping("/myAttended")
    @ApiOperation("??????????????????")
    public Result myAttended(HttpServletRequest request,@RequestBody @ApiParam MeetingParam meetingParam){
        meetingParam.setNowUserId(getUserId(request));
        startPage();
        List<MeetingInfoResult> meetingInfoResults = meetingService.getMyAttendedMeetingInfo(meetingParam);
        return ResultGenerator.genSuccessResult("????????????", meetingInfoResults);
    }

    @Login
    @PostMapping(value = "/myAudit")
    @ApiOperation(value = "????????????")
    public Result myAudit(HttpServletRequest request,@RequestBody @ApiParam MeetingParam meetingParam){

        // auditStatus???checkDate
        Meeting selectMeeting = transObject(meetingParam, Meeting.class);
        // selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        selectMeeting.setTarget(getLoginName(request));
        startPage();
        List<MeetingWebInfoVo> meetingWebInfoVos = meetingService.selectMeetingAuditList(selectMeeting);
        List<MeetingAuditInfoDto> resultList = transList(meetingWebInfoVos, MeetingAuditInfoDto.class);
        return ResultGenerator.genSuccessResult("????????????",resultList);
    }

    @Login
    @PostMapping(value = "/audit")
    @ApiOperation(value = "????????????")
    public Result audit(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam){
        Long[] ids = meetingParam.getIds();
        String loginName = getLoginName(request);
        for (Long id : ids){
            int row = meetingService.auditMeeting(id,meetingParam.getAuditStatus(),meetingParam.getReason(),loginName,meetingParam);
        }
        String target = meetingParam.getTarget();
        if (BeanUtil.isNotEmpty(target)){

            // ????????????app????????????

                new Thread(() ->{
                    sendAppMsg(target,meetingParam.getId());
                }).start();
        }
        return ResultGenerator.genSuccessResult("????????????");
    }

    @Login
    @GetMapping("/meetingDetail")
    @ApiOperation("/??????????????????")
    public Result meetingDetail(HttpServletRequest request,@RequestParam @ApiParam Long meetingId) {
        return ResultGenerator.genSuccessResult("????????????", meetingService.getDetailById(meetingId,getLoginName(request)));
    }

    @Login
    @GetMapping("/auditList")
    @ApiOperation("/????????????????????????")
    public Result auditList(@RequestParam @ApiParam Long meetingId) {
        return ResultGenerator.genSuccessResult("????????????", meetingService.selectAuditList(meetingId));
    }

    @Login
    @GetMapping("/cancelMeeting")
    @ApiOperation("/????????????")
    public Result cancelMeeting(@RequestParam @ApiParam Long meetingId,@RequestParam(required = false) @ApiParam String remark) {
        // ???????????????????????????????????????????????????????????????
        Meeting meeting = meetingService.selectMeetingById(meetingId);
        long timeGap = UmapDateUtils.getTimeGap(new Date(), meeting.getStartTime());
        if (timeGap < DateUtils.MILLIS_PER_HOUR){
            ResultGenerator.genSuccessResult("???????????????????????????????????????????????????????????????????????????????????????");
        }
        int row = meetingService.cancelMeeting(meetingId,remark);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genSuccessResult("?????????????????????????????????");
    }

    @Login
    @GetMapping("/cancelMeetingMsg")
    @ApiOperation("/??????????????????")
    public Result cancelMeetingMsg(@RequestParam @ApiParam Long meetingId,@RequestParam(required = false) @ApiParam String remark) {
        int row = meetingService.cancelMeetingMsg(meetingId,remark);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genSuccessResult("?????????????????????????????????");
    }

    @Login
    @PostMapping("/changeMeeting")
    @ApiOperation("????????????")
    @ApiLog(title = "?????????????????????",businessType = BusinessType.OTHER,ApiOperatorType = ApiOperatorType.POST)
    public Result changeMeeting(HttpServletRequest request, @RequestBody @ApiParam Meeting meeting){
        new Thread(() ->{
            meetingService.changeMeeting(meeting);
        }).start();
        return ResultGenerator.genSuccessResult("????????????");
    }

    @Login
    @PostMapping("/beforeChange")
    @ApiOperation("?????????????????????")
    public Result beforeChange(HttpServletRequest request, @RequestBody @ApiParam MeetingParam meetingParam){

        MeetingChangeCheckDto result = new MeetingChangeCheckDto();

        Meeting currentMeeting = meetingService.selectMeetingById(meetingParam.getId());
        if (BeanUtil.isEmpty(currentMeeting)){
            return ResultGenerator.genFailResult("??????id??????");
        }
        result.setCurrentMeeting(currentMeeting);
        result.setId(meetingParam.getId());

        List<MeetingDetailDto> occupyMeeting = meetingService.getOccupyMeeting(meetingParam);


        if (CollectionUtil.isEmpty(occupyMeeting)) {
            result.setConflict(false);
            return ResultGenerator.genSuccessResult("?????????",result);
        }

        result.setConflict(true);
        result.setConflictMeetingList(occupyMeeting);
        return ResultGenerator.genSuccessResult("??????",result);
    }

    @Login
    @PostMapping("/meetingRoomList")
    @ApiOperation("???????????????")
    public Result meetingRoomList(){
        List<ArraignRoom> arraignRooms = roomService.selectRoomByType(KeyConstant.ARENA_TYPE_MEETING_ROOM);
        return ResultGenerator.genSuccessResult(arraignRooms);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@RequestParam(required = true) @ApiParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordVo(selectRecord);

        return ResultGenerator.genSuccessResult("????????????",auditRecords);
    }

    @Login
    @ApiOperation(value = "????????????????????????????????????")
    @PostMapping(value = "/dayCount")
    public Result dayCount(@RequestBody @ApiParam MeetingParam param){
        startPage();
        List<NameCountResult> nameCountResults = meetingService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("????????????",nameCountResults);
    }

    @ApiLog(title = "????????????-??????info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????-??????info")
    @PostMapping("/timeApplyList")
    @Login
    public Result timeApplyList(@RequestBody @ApiParam MeetingParam param){
        // ?????????????????????????????????????????????????????????????????????
        param.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        param.setAuditStatusArr(new String[]{KeyConstant.EVENT_AUDIT_STATUS_WAIT,KeyConstant.EVENT_AUDIT_STATUS_PASS});
        return ResultGenerator.genSuccessResult("success",meetingService.selectTimeApplyList(param));
    }

    @ApiLog(title = "????????????-????????????info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????-????????????info")
    @PostMapping("/myTimeApplyList")
    @Login
    public Result myTimeApplyList(HttpServletRequest request,@RequestBody @ApiParam MeetingParam param){
        // ????????????????????????????????????????????????
        param.setNowUserId(getUserId(request));
        param.setNowUserLoginName(getLoginName(request));
        return ResultGenerator.genSuccessResult("success",meetingService.selectTimeApplyList(param));
    }
}
