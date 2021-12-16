package com.mkst.umap.app.admin.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysRoleMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.backup.BackUpParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.domain.vo.ApplyInfoVo;
import com.mkst.umap.app.admin.domain.vo.BackUpGuestVo;
import com.mkst.umap.app.admin.domain.vo.BackUpRoomVo;
import com.mkst.umap.app.admin.dto.apply.ApplyInfoDto;
import com.mkst.umap.app.admin.dto.apply.BackUpRoomDto;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IBackUpGuestService;
import com.mkst.umap.app.admin.service.IBackUpRoomService;
import com.mkst.umap.app.admin.util.MyDateUtil;
import com.mkst.umap.app.common.enums.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ApplyInfoApi
 * @Description 备勤间申请服务接口
 * @Author lcq
 */
@Slf4j
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
    private IBackUpGuestService backUpGuestService;

    @PostMapping("/applyInfo")
    @ApiOperation("获取备勤间申请表信息集合")
    @Login
    public Result applyInfo(HttpServletRequest request,@RequestBody  @ApiParam(name = "applyInfoDto", value = "申请表数据传递对象", required = true)ApplyInfoDto applyInfoDto) {
        try {
            List<ApplyInfoVo> applyInfoVos;
            Long applicantId;
            Long approverId;
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

    @ApiOperation("新增申请")
    @PostMapping("/addSave")
    @Login
    public Result addSave(HttpServletRequest request,@RequestBody  @ApiParam(name = "applyInfoDto", value = "申请表数据传递对象", required = true) ApplyInfoDto applyInfoDto)
    {
        ApplyInfo applyInfo = new ApplyInfo();
        //将dto转换为 申请表实体 并set当前用户id和 待审批状态码
        BeanUtils.copyProperties(applyInfoDto,applyInfo);

        //applyInfo.setBackUpGuests(transList(applyInfoDto.getGuests(),BackUpGuest.class));
        //applyInfo.setApplicantId(getUserId(request));
        applyInfo.setCreateBy(getLoginName(request));
        applyInfo.setApplyStatus(ApplyStatusEnum.Pending.getValue());
        applyInfo.setApproveStatus(0);
        //int rows = applyInfoService.insertApplyInfoWithGuests(applyInfo);

        int rows = applyInfoService.insertApply(applyInfo);

        // 推送app内部消息
        if (rows > 0){
            new Thread(() ->{
                sendAppMsg(applyInfo.getApplyId());
            }).start();
        }

        return rows>0 ? ResultGenerator.genSuccessResult("提交申请成功！") : ResultGenerator.genFailResult("提交申请失败，请联系管理员");
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

    private SysRole getSysRole(){
        return sysRoleMapper.checkRoleKeyUnique(RoleKeyEnum.ROLE_ADMIN.getValue());
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

        ai.setApplyStatus(1);
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
}
