package com.mkst.umap.app.admin.api;

import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.cms.domain.Article;
import com.mkst.mini.systemplus.cms.service.IArticleService;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysNoticeService;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.*;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.AuditInfoResult;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.SpecialCaseAppointment;
import com.mkst.umap.app.admin.dto.arraign.ArraignRoomListDto;
import com.mkst.umap.app.admin.dto.room.RoomAppointmentDto;
import com.mkst.umap.app.admin.dto.specialcase.ScheduleInfoDto;
import com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto;
import com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDto;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.service.IArraignRoomScheduleService;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.ISpecialCaseAppointmentService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
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
 * @ClassName SpecialCaseApi
 * @Description
 * @Author wangyong
 * @Date 2020-07-01 19:37
 */
@Slf4j
@Api("专案预约相关接口")
@RestController
@RequestMapping("/api/specialCase")
public class SpecialCaseApi extends BaseApi {

    @Autowired
    private IArraignRoomScheduleService scheduleService;
    @Autowired
    private ISpecialCaseAppointmentService specialService;
    @Autowired
    private IArraignRoomService arraignRoomService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private AuditRecordMapper auditRecordMapper;
    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private IArticleService articleService;

    @ApiLog(title = "添加专案预约申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/addSave")
    @ApiOperation("添加专案预约申请")
    @Login
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(name = "specialCaseDto", value = "专案申请信息", required = true) SpecialCaseDto specialCaseDto) {

        try {
            //前端传的是id，这里处理一下
            String realLoginName = sysUserService.selectUserById(Long.valueOf(specialCaseDto.getUseBy())).getLoginName();
            specialCaseDto.setUseBy(realLoginName);

            //装载roomId、tittle、deptId、startTime、endTime
            SpecialCaseAppointment specialCaseAppointment = transObject(specialCaseDto, SpecialCaseAppointment.class);
            ArraignRoomSchedule roomSchedule = transObject(specialCaseDto, ArraignRoomSchedule.class);

            //API用户一定要手动添加CreateBy
            specialCaseAppointment.setCreateBy(getSysUser(request).getLoginName());
            specialCaseAppointment.setScheduleId(roomSchedule.getId());
            if (specialService.selectIsOccupied(specialCaseAppointment)) {
                return ResultGenerator.genSuccessResult("您选择的时间已被其他人抢占，请刷新并重新选择时间！");
            }

            int row = specialService.insertSpecialCaseAppointment(specialCaseAppointment);
            //API用户一定要手动添加CreateBy
            roomSchedule.setCreateBy(getLoginName(request));
            scheduleService.insertArraignRoomSchedule(roomSchedule);

            // 推送审核app内部消息
            if (row > 0){
                new Thread(() ->{
                    sendAppMsg(specialCaseAppointment.getId());
                }).start();
            }

            return ResultGenerator.genSuccessResult("专案预约申请提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("提交失败，请稍后再试");
        }
    }

    private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("专案预约");
        msgContent.setContent("一个专案申请待审核，请及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_SPECIAL_CASE.getValue());
        msgContent.setParams(params);

        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_ZAGLY.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_SPECIAL_CASE.getValue(), user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }

    @ApiLog(title = "获取提审室使用情况", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/roomList")
    @ApiOperation("获取提审室使用情况")
    @Login
    public Result roomList(HttpServletRequest request, @RequestBody @ApiParam(name = "roomListParam", value = "提审室信息的请求参数", required = true) RoomListParam roomListParam) {

        ArraignRoomListDto arraignRoomListDto = transObject(roomListParam, ArraignRoomListDto.class);

        //这里基本上已经获取到想要的信息了，具体实现请进service查看
        startPage();
        List<ArraignRoomListDto> roomListResult = arraignRoomService.getRoomListResult(arraignRoomListDto);

        if (roomListResult.isEmpty()) {
            return ResultGenerator.genFailResult("无可用的提审室，请联系管理员");
        }
        return ResultGenerator.genSuccessResult("查询成功", roomListResult);
    }

    @ApiLog(title = "获取提审室正在进行的提审、专案信息", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "roomAppointmentDetial")
    @ApiOperation(value = "获取提审室正在进行的提审、专案信息")
    @Login
    public Result roomAppointmentDetail(HttpServletRequest request, @RequestBody @ApiParam(name = "roomAppointmentDto", value = "提审室") RoomDetailParam roomDetailParam) {
        RoomAppointmentDto roomAppointment = arraignRoomService.getRoomAppointment(roomDetailParam);
        return ResultGenerator.genSuccessResult("查询成功", roomAppointment);
    }

    @ApiLog(title = "获取专案详情", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "specialCaseDetial")
    @ApiOperation(value = "获取专案详情")
    @Login
    public Result specialCaseDetail(HttpServletRequest request, @RequestBody @ApiParam(name = "roomAppointmentDto", value = "提审室") RoomDetailParam roomDetailParam) {
        roomDetailParam.setType(AuditRecordTypeEnum.SpecialCaseAudit.getValue());
        RoomAppointmentDto roomAppointment = arraignRoomService.getRoomAppointment(roomDetailParam);
        return ResultGenerator.genSuccessResult("查询成功", roomAppointment);
    }

    @ApiLog(title = "获取专案列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "specialeCaseList")
    @ApiOperation(value = "获取专案列表")
    @Login
    public Result specialeCaseList(HttpServletRequest request, @RequestBody @ApiParam(name = "specialCaseListParam", value = "专案列表申请") SpecialCaseListParam specialCaseListParam) {

        ArrayList<String> statusList = new ArrayList<>();
        statusList.add("1");
        statusList.add("0");
        specialCaseListParam.setStatusList(statusList);
        specialCaseListParam.setNowStatus(KeyConstant.EVENT_ALL);
        /*startPage();*/
        List<SpecialCaseDetailDto> specialCaseDetailDtos = specialService.selectCaseList(specialCaseListParam);
        return ResultGenerator.genSuccessResult("查询成功", specialCaseDetailDtos);
    }

    @ApiLog(title = "获取所有专案-日期", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "appointmentList")
    @ApiOperation(value = "获取所有专案-日期")
    @Login
    public Result appointmentList(HttpServletRequest request, @RequestBody @ApiParam(name = "specialCaseListParam", value = "专案列表申请") SpecialCaseListParam specialCaseListParam) {
        /*startPage();*/
        List<SpecialCaseDetailDto> specialCaseDetailDtos = specialService.selectCaseList(specialCaseListParam);
        return ResultGenerator.genSuccessResult("查询成功", specialCaseDetailDtos);
    }

    @ApiLog(title = "获取我的专案列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "mySpecialeCaseList")
    @ApiOperation(value = "获取我的专案列表")
    @Login
    public Result mySpecialeCaseList(HttpServletRequest request, @RequestBody @ApiParam(name = "specialCaseListParam", value = "专案列表申请") SpecialCaseListParam specialCaseListParam) {

        specialCaseListParam.setCreateBy(getLoginName(request));
        specialCaseListParam.setUseBy(getLoginName(request));
        /*startPage();*/
        List<SpecialCaseDetailDto> specialCaseDetailDtos = specialService.selectCaseList(specialCaseListParam);
        return ResultGenerator.genSuccessResult("查询成功", specialCaseDetailDtos);
    }

    @ApiLog(title = "获取房间排班", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "roomSchedule")
    @ApiOperation(value = "获取房间排班")
    @Login
    public Result roomSchedule(HttpServletRequest request, @RequestBody @ApiParam(name = "roomScheduleParam", value = "获取房间排班的参数") RoomScheduleParam roomScheduleParam) {
        LinkedList<ScheduleInfoDto> scheduleForSpecialCaseList = scheduleService.getScheduleForSpecialCaseList(roomScheduleParam);
        return ResultGenerator.genSuccessResult("查询成功", scheduleForSpecialCaseList);
    }

    @ApiLog(title = "获取可选的结束时间", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "getEndTime")
    @ApiOperation(value = "获取可选的结束时间")
    @Login
    public Result getEndTime(@RequestBody @ApiParam(name = "roomScheduleParam", value = "获取房间排班的参数") RoomScheduleParam roomScheduleParam) {
        Date nextStartTime = scheduleService.getNextStartTime(roomScheduleParam);
        if (nextStartTime == null) {
            return ResultGenerator.genSuccessResult("查询成功");
        }
        return ResultGenerator.genSuccessResult("查询成功", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nextStartTime));
    }

    @ApiLog(title = "获取审核列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "/auditList")
    @ApiOperation(value = "获取审核列表")
    @Login
    public Result auditList(@RequestBody @ApiParam(name = "auditInfoParam", value = "专案列表申请") AuditInfoParam auditInfoParam) {
        startPage();
        List<AuditInfoResult> auditInfoResults = specialService.selectCaseAuditList(auditInfoParam);
        return ResultGenerator.genSuccessResult("查询成功", auditInfoResults);
    }

    @ApiLog(title = "审核专案申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping(value = "/audit")
    @ApiOperation(value = "审核专案申请")
    @Login
    public Result audit(HttpServletRequest request, @RequestBody @ApiParam(name = "auditParam", value = "审核专案申请") SpecialCaseAuditParam auditParam) {
        auditParam.setUpdateBy(getLoginName(request));
        int result = specialService.auditCaseList(auditParam);
        return result > 0 ? ResultGenerator.genSuccessResult("审核成功") : ResultGenerator.genFailResult("审核失败，请重新尝试！");
    }

    @Login
    @ApiOperation(value = "获取审核记录信息")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@RequestParam(required = true) @ApiParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.SpecialCaseAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordVo(selectRecord);

        return ResultGenerator.genSuccessResult("查询成功",auditRecords);
    }

    @ApiLog(title = "依据日期获取专案申请数量",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "依据日期获取专案申请数量")
    @PostMapping("/dayCount")
    @Login
    public Result dayCount(@RequestBody @ApiParam SpecialCaseListParam param){
        ArrayList<String> statusList = new ArrayList<>();
        statusList.add(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        statusList.add(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        param.setStatusList(statusList);
        List<NameCountResult> nameCountResults = specialService.selectDayCount(param);
        nameCountResults.stream().forEach(o -> o.setStatus(false));
        return ResultGenerator.genSuccessResult("查询成功",nameCountResults);
    }

    @ApiLog(title = "时间排序-申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-申请info")
    @PostMapping("/timeApplyList")
    @Login
    public Result timeApplyList(@RequestBody @ApiParam SpecialCaseListParam param){
        // 只查询未取消的，且属于未审核或者审核通过的会议
        ArrayList<String> statusList = new ArrayList<>();
        statusList.add(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        statusList.add(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        param.setStatusList(statusList);
        return ResultGenerator.genSuccessResult("success",specialService.selectTimeApplyList(param));
    }

    @ApiLog(title = "时间排序-我的申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-我的申请info")
    @PostMapping("/myTimeApplyList")
    @Login
    public Result myTimeApplyList(HttpServletRequest request,@RequestBody @ApiParam SpecialCaseListParam param){
        // 查询我申请的或者使用人是我的会议
        param.setNowUserLoginName(getLoginName(request));
        return ResultGenerator.genSuccessResult("success",specialService.selectTimeApplyList(param));
    }


    @ApiLog(title = "弹出公告内容",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "弹出公告内容")
    @PostMapping("/popUpAnnouncement")
    public Result popUpAnnouncement(HttpServletRequest request){

        //查专案预约信息
        Map<Object, Object> map = new HashMap<>();
        List<SpecialCaseAppointment> appointmentList = specialService.selectAppointmentByDay();
        map.put("appointmentList",appointmentList);

        //查公告信息
        Article article = new Article();
        article.setCategoryId("226");
        List<Article> articles = articleService.selectArticleList(article);
        List<Article> list = new ArrayList();
        for(Article article1:articles){
            Article article2 = articleService.selectArticleById(article1.getId());
            article1.setContent(article2.getContent());
            list.add(article1);
        }
        map.put("articles",list);
        return ResultGenerator.genSuccessResult("success",map);
    }

}
