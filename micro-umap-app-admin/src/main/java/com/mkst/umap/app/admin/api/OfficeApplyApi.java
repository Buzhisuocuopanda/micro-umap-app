package com.mkst.umap.app.admin.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.officeapply.OfficeApplyParam;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyDetailResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.OfficeApply;
import com.mkst.umap.app.admin.domain.OfficeApplyDevice;
import com.mkst.umap.app.admin.dto.officeapply.MyApplyDto;
import com.mkst.umap.app.admin.dto.officeapply.MyAuditDto;
import com.mkst.umap.app.admin.dto.officeapply.OfficeApplyDetailDto;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.service.IOfficeApplyDeviceService;
import com.mkst.umap.app.admin.service.IOfficeApplyService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OfficeApplyApi
 * @Description 办公申请的APi
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-07 14:27
 */
@Api(value = "办公申请接口")
@Slf4j
@RestController
@RequestMapping(value = "/api/officeApply")
public class OfficeApplyApi extends BaseApi {

    @Autowired
    private IOfficeApplyService officeApplyService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private AuditRecordMapper auditRecordMapper;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IOfficeApplyDeviceService officeApplyDeviceService;

    @Autowired
    private IWfEventDetailService eventDetailService;

    @Login
    @ApiOperation(value = "新增一条办公申请")
    @PostMapping(value = "/addSave")
    @Log(title = "新增一条办公申请", businessType = BusinessType.INSERT)
    public Result addSave(HttpServletRequest request, @ApiParam @RequestBody OfficeApplyParam officeApplyParam) {

        OfficeApply insertApply = transObject(officeApplyParam, OfficeApply.class);
        String loginName = getLoginName(request);
        insertApply.setCreateBy(loginName);
        insertApply.setUpdateBy(loginName);
        int row = officeApplyService.insertOfficeApply(insertApply);

        // 有设备的操作
        List<OfficeApplyDevice> deviceList = officeApplyParam.getDeviceList();
        if (CollectionUtil.isNotEmpty(deviceList)){
            Long applyId = insertApply.getId();
            deviceList.stream().forEach(d -> {
                d.setParentId(applyId);
                d.setCreateBy(loginName);
                officeApplyDeviceService.insertOfficeApplyDevice(d);
            });
        }

        // 初始化审核表的操作
        AuditRecord auditRecord = new AuditRecord(insertApply.getId(),AuditRecordTypeEnum.OfficeApplyAudit.getValue(),KeyConstant.EVENT_AUDIT_STATUS_WAIT,"");
        SysUser user = sysUserService.selectUserById(officeApplyParam.getTargetId());
        if (BeanUtil.isEmpty(user)){
            return ResultGenerator.genFailResult("当前系统中不存在该审核员，请选择其他人或联系管理员！！！");
        }
        WfEventDetail wfEventDetail = eventDetailService.selectFirstEventDetail(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
        if (BeanUtil.isNotEmpty(wfEventDetail)){
            auditRecord.setProgress(wfEventDetail.getApprovalOrder());
        }
        auditRecord.setCreateBy(user.getLoginName());
        auditRecordMapper.insertAuditRecord(auditRecord);

        // 推送审核app内部消息
        if (row > 0){
            new Thread(() ->{
                sendAppMsg(user.getLoginName(),insertApply.getId(),insertApply.getType());
            }).start();
        }
        return row > 0 ? ResultGenerator.genSuccessResult("提交成功，请等待审核") : ResultGenerator.genSuccessResult("提交失败，请联系管理员！");
    }

    private void sendAppMsg(String target, Long meetingId,String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("办公申请");
        msgContent.setContent("一个办公申请待审核，请及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", meetingId.toString());
        params.put("bizType",  BusinessTypeEnum.UMAP_OFFICE_APPLY + type);
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_OFFICE_APPLY.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }

    private void sendAppMsg(Long id, String type, Long approveToId) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("办公申请");
        msgContent.setContent("一条办公申请待审核，请您及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizType", BusinessTypeEnum.UMAP_OFFICE_APPLY + type);
        params.put("bizKey", id.toString());
        msgContent.setParams(params);

        /*SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_BGSQGLY.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_OFFICE_APPLY + type, user.getLoginName());
        });*/
        SysUser approveToUser = sysUserService.selectUserById(approveToId);
        if (BeanUtil.isNotEmpty(approveToUser)){
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_OFFICE_APPLY + type, approveToUser.getLoginName());
        }
        MsgPushUtils.getMsgPushTask().execute();
    }

    @Login
    @ApiOperation(value = "获取我的申请列表")
    @PostMapping(value = "/myApplyList")
    public Result myApplyList(HttpServletRequest request, @ApiParam @RequestBody(required = false) OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        // officeApplyParam.setApprovalToLoginName(sysUserService.selectUserById(officeApplyParam.getApprovalTo()).getLoginName());
        startPage();
        List<MyApplyDto> myApplyDtos = officeApplyService.selectMyApplyByParam(officeApplyParam);

        return ResultGenerator.genSuccessResult("查询成功", myApplyDtos);
    }

    @Login
    @ApiOperation(value = "获取我的审核列表")
    @PostMapping(value = "/myAuditList")
    public Result myAuditList(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {

        // officeApplyParam.setApprovalTo(getUserId(request));
        officeApplyParam.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        officeApplyParam.setApprovalToLoginName(getLoginName(request));
        startPage();
        List<MyAuditDto> myAuditDtos = officeApplyService.selectApplyAuditListByParam(officeApplyParam);

        return ResultGenerator.genSuccessResult("查询成功", myAuditDtos);
    }

    @Login
    @ApiOperation(value = "审核申请")
    @PostMapping(value = "/updateApply")
    public Result updateApply(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        int row = officeApplyService.updateApplyByParam(officeApplyParam);
        return row > 0 ? ResultGenerator.genSuccessResult("操作成功") : ResultGenerator.genSuccessResult("操作失败，请联系管理员");
    }

    @Login
    @ApiOperation(value = "取消申请")
    @GetMapping(value = "/cancelApply")
    @Transactional(rollbackFor = Exception.class)
    public Result cancelApply(@RequestParam @ApiParam Long appLyId){

        OfficeApply officeApply = new OfficeApply();
        officeApply.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
        officeApply.setId(appLyId);
        int i = officeApplyService.updateOfficeApply(officeApply);
        return i > 0 ? ResultGenerator.genSuccessResult("取消成功") : ResultGenerator.genFailResult("当前网络慢，请稍后再试或联系管理员！！！");
    }

    @Login
    @ApiOperation(value = "批量审批")
    @PostMapping(value = "/batchAudit")
    public Result batchAudit(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        Long[] idArr = officeApplyParam.getIdArr();
        Arrays.asList(idArr).stream().forEach(id -> {
            officeApplyParam.setId(id);
            officeApplyService.updateApplyByParam(officeApplyParam);
            if (BeanUtil.isNotEmpty(officeApplyParam.getTargetId())){
                SysUser user = sysUserService.selectUserById(officeApplyParam.getTargetId());
                // 推送审核app内部消息
                    new Thread(() ->{
                        sendAppMsg(user.getLoginName(),officeApplyParam.getId(), officeApplyService.selectOfficeApplyById(id).getType());
                    }).start();

            }
        });
        return ResultGenerator.genSuccessResult("操作成功");
    }

    @Login
    @ApiOperation(value = "获取详情")
    @PostMapping(value = "/detail")
    public Result detail(HttpServletRequest request,@RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        OfficeApply officeApply = officeApplyService.selectOfficeApplyById(officeApplyParam.getId());
        OfficeApplyDetailResult detailResult = officeApplyService.selectDetail(officeApplyParam.getId());
        if (detailResult == null) {
            return ResultGenerator.genFailResult("无查询结果");
        }

        String canAudit = "0";
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setProgress(String.valueOf(detailResult.getProgress()));
        auditRecord.setApplyType(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
        auditRecord.setApplyId(officeApplyParam.getId());
        auditRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordList(auditRecord);
        if (CollectionUtil.isNotEmpty(auditRecords) && StringUtils.equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT,officeApply.getAuditStatus())){
            AuditRecord auditRecord1 = auditRecords.get(0);
            if (getLoginName(request).equals(auditRecord1.getCreateBy())){
                canAudit = "1";
            }
        }

        OfficeApplyDetailDto officeApplyDetailDto = transObject(detailResult, OfficeApplyDetailDto.class);
        officeApplyDetailDto.setDeviceList(detailResult.getDeviceList());
        officeApplyDetailDto.setCanAudit(canAudit);
        officeApplyDetailDto.setUseById(sysUserService.selectUserByLoginName(officeApply.getCreateBy()).getUserId());
        officeApplyDetailDto.setAuditProgress(officeApply.getProgress());

        WfEventDetail wfEventDetail = eventDetailService.selectNextEventDetail(AuditRecordTypeEnum.OfficeApplyAudit.getValue(), detailResult.getProgress());
        if (BeanUtil.isNotEmpty(wfEventDetail)){
            officeApplyDetailDto.setNextRole(roleService.selectRoleById(Long.valueOf(wfEventDetail.getApprovalObjectId())).getRoleKey());
        }

        return ResultGenerator.genSuccessResult("查询成功",officeApplyDetailDto);
    }

    @Login
    @ApiOperation(value = "获取审核记录信息")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@RequestParam(required = true) @ApiParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordVo(selectRecord);

        return ResultGenerator.genSuccessResult("查询成功",auditRecords);
    }

    @Login
    @ApiOperation(value = "获取审核记录列表")
    @GetMapping(value = "/auditList")
    public Result auditList(@RequestParam @ApiParam Long applyId){
        return ResultGenerator.genSuccessResult("查询成功",officeApplyService.selectAuditList(applyId));
    }

}
