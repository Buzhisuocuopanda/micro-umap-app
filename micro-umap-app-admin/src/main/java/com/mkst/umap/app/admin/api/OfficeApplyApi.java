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
 * @Description ???????????????APi
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-07 14:27
 */
@Api(value = "??????????????????")
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
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/addSave")
    @Log(title = "????????????????????????", businessType = BusinessType.INSERT)
    public Result addSave(HttpServletRequest request, @ApiParam @RequestBody OfficeApplyParam officeApplyParam) {

        OfficeApply insertApply = transObject(officeApplyParam, OfficeApply.class);
        String loginName = getLoginName(request);
        insertApply.setCreateBy(loginName);
        insertApply.setUpdateBy(loginName);
        int row = officeApplyService.insertOfficeApply(insertApply);

        // ??????????????????
        List<OfficeApplyDevice> deviceList = officeApplyParam.getDeviceList();
        if (CollectionUtil.isNotEmpty(deviceList)){
            Long applyId = insertApply.getId();
            deviceList.stream().forEach(d -> {
                d.setParentId(applyId);
                d.setCreateBy(loginName);
                officeApplyDeviceService.insertOfficeApplyDevice(d);
            });
        }

        // ???????????????????????????
        AuditRecord auditRecord = new AuditRecord(insertApply.getId(),AuditRecordTypeEnum.OfficeApplyAudit.getValue(),KeyConstant.EVENT_AUDIT_STATUS_WAIT,"");
        SysUser user = sysUserService.selectUserById(officeApplyParam.getTargetId());
        if (BeanUtil.isEmpty(user)){
            return ResultGenerator.genFailResult("????????????????????????????????????????????????????????????????????????????????????");
        }
        WfEventDetail wfEventDetail = eventDetailService.selectFirstEventDetail(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
        if (BeanUtil.isNotEmpty(wfEventDetail)){
            auditRecord.setProgress(wfEventDetail.getApprovalOrder());
        }
        auditRecord.setCreateBy(user.getLoginName());
        auditRecordMapper.insertAuditRecord(auditRecord);

        // ????????????app????????????
        if (row > 0){
            new Thread(() ->{
                sendAppMsg(user.getLoginName(),insertApply.getId(),insertApply.getType());
            }).start();
        }
        return row > 0 ? ResultGenerator.genSuccessResult("??????????????????????????????") : ResultGenerator.genSuccessResult("????????????????????????????????????");
    }

    private void sendAppMsg(String target, Long meetingId,String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("????????????");
        msgContent.setContent("????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", meetingId.toString());
        params.put("bizType",  BusinessTypeEnum.UMAP_OFFICE_APPLY + type);
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_OFFICE_APPLY.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }

    private void sendAppMsg(Long id, String type, Long approveToId) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("????????????");
        msgContent.setContent("???????????????????????????????????????????????????");

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
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/myApplyList")
    public Result myApplyList(HttpServletRequest request, @ApiParam @RequestBody(required = false) OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        // officeApplyParam.setApprovalToLoginName(sysUserService.selectUserById(officeApplyParam.getApprovalTo()).getLoginName());
        startPage();
        List<MyApplyDto> myApplyDtos = officeApplyService.selectMyApplyByParam(officeApplyParam);

        return ResultGenerator.genSuccessResult("????????????", myApplyDtos);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/myAuditList")
    public Result myAuditList(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {

        // officeApplyParam.setApprovalTo(getUserId(request));
        officeApplyParam.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        officeApplyParam.setApprovalToLoginName(getLoginName(request));
        startPage();
        List<MyAuditDto> myAuditDtos = officeApplyService.selectApplyAuditListByParam(officeApplyParam);

        return ResultGenerator.genSuccessResult("????????????", myAuditDtos);
    }

    @Login
    @ApiOperation(value = "????????????")
    @PostMapping(value = "/updateApply")
    public Result updateApply(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        int row = officeApplyService.updateApplyByParam(officeApplyParam);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genSuccessResult("?????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "????????????")
    @GetMapping(value = "/cancelApply")
    @Transactional(rollbackFor = Exception.class)
    public Result cancelApply(@RequestParam @ApiParam Long appLyId){

        OfficeApply officeApply = new OfficeApply();
        officeApply.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
        officeApply.setId(appLyId);
        int i = officeApplyService.updateOfficeApply(officeApply);
        return i > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genFailResult("????????????????????????????????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "????????????")
    @PostMapping(value = "/batchAudit")
    public Result batchAudit(HttpServletRequest request, @RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        officeApplyParam.setCreateBy(getLoginName(request));
        Long[] idArr = officeApplyParam.getIdArr();
        Arrays.asList(idArr).stream().forEach(id -> {
            officeApplyParam.setId(id);
            officeApplyService.updateApplyByParam(officeApplyParam);
            if (BeanUtil.isNotEmpty(officeApplyParam.getTargetId())){
                SysUser user = sysUserService.selectUserById(officeApplyParam.getTargetId());
                // ????????????app????????????
                    new Thread(() ->{
                        sendAppMsg(user.getLoginName(),officeApplyParam.getId(), officeApplyService.selectOfficeApplyById(id).getType());
                    }).start();

            }
        });
        return ResultGenerator.genSuccessResult("????????????");
    }

    @Login
    @ApiOperation(value = "????????????")
    @PostMapping(value = "/detail")
    public Result detail(HttpServletRequest request,@RequestBody @ApiParam OfficeApplyParam officeApplyParam) {
        OfficeApply officeApply = officeApplyService.selectOfficeApplyById(officeApplyParam.getId());
        OfficeApplyDetailResult detailResult = officeApplyService.selectDetail(officeApplyParam.getId());
        if (detailResult == null) {
            return ResultGenerator.genFailResult("???????????????");
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

        return ResultGenerator.genSuccessResult("????????????",officeApplyDetailDto);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@RequestParam(required = true) @ApiParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.OfficeApplyAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordMapper.selectAuditRecordVo(selectRecord);

        return ResultGenerator.genSuccessResult("????????????",auditRecords);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @GetMapping(value = "/auditList")
    public Result auditList(@RequestParam @ApiParam Long applyId){
        return ResultGenerator.genSuccessResult("????????????",officeApplyService.selectAuditList(applyId));
    }

}
