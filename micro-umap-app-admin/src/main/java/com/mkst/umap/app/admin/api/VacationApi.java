package com.mkst.umap.app.admin.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.vacation.VacationParam;
import com.mkst.umap.app.admin.api.bean.result.vacation.VacationResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.Vacation;
import com.mkst.umap.app.admin.dto.vacation.MyVacationAuditInfoDto;
import com.mkst.umap.app.admin.dto.vacation.MyVacationInfoDto;
import com.mkst.umap.app.admin.dto.vacation.VacationDetailDto;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IVacationService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName VacationApi
 * @Description 请假接口
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 17:08
 */
@Api(value = "请假接口")
@RestController
@RequestMapping(value = "/api/vacation")
public class VacationApi extends BaseApi {

    @Autowired
    private IVacationService vacationService;

    @Autowired
    private IAuditRecordService auditRecordService;

    @Autowired
    private ISysUserService sysUserService;

    @Login
    @ApiOperation(value = "新增一条请假申请")
    @PostMapping(value = "/addSave")
    @Log(title = "新增一条请假申请", businessType = BusinessType.INSERT)
    public Result addSave(HttpServletRequest request, @ApiParam @RequestBody VacationParam vacationParam) {
        //type、startTime、endTime、content、approveTo
        Vacation insertVacation = transObject(vacationParam, Vacation.class);
        Long userId = getUserId(request);
        insertVacation.setCreateBy(userId);
        insertVacation.setUpdateBy(userId);
        int row = vacationService.insertVacation(insertVacation);

        if(row > 0){
            new Thread(() ->{
                sendAppMsg(insertVacation.getId(), insertVacation.getApproveTo(), insertVacation.getType());
            }).start();
        }

        return row > 0 ? ResultGenerator.genSuccessResult("申请提交成功") : ResultGenerator.genFailResult("申请提交失败");
    }

    private void sendAppMsg(Long id, Long approveToId, String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("请假申请");
        msgContent.setContent("一条请假申请待审核，请及时处理！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_VACATION.getValue() + type);
        msgContent.setParams(params);

        SysUser user = sysUserService.selectUserById(approveToId);
        if (user != null) {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_VACATION.getValue() + type, user.getLoginName());
        }
        MsgPushUtils.getMsgPushTask().execute();
    }

    @Login
    @ApiOperation(value = "查询我的申请列表")
    @PostMapping(value = "/myApplyList")
    public Result myApplyList(HttpServletRequest request, @ApiParam @RequestBody VacationParam vacationParam) {
        //type、auditStatus、checkDate
        Vacation selectVacation = transObject(vacationParam, Vacation.class);
        selectVacation.setCreateBy(getUserId(request));

        startPage();
        List<VacationResult> vacationResults = vacationService.selectVacationInfoList(selectVacation);

        if (CollUtil.isEmpty(vacationResults)) {
            vacationResults = new ArrayList<>();
        }

        return ResultGenerator.genSuccessResult("查询成功", transList(vacationResults, MyVacationInfoDto.class));
    }

    @Login
    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/detail")
    public Result detail(@ApiParam @RequestParam Long id) {

        Vacation selectVacation = new Vacation();
        selectVacation.setId(id);

        // 按id查询
        List<VacationResult> vacationResults = vacationService.selectVacationInfoList(selectVacation);

        VacationDetailDto result = new VacationDetailDto();

        if (CollectionUtil.isNotEmpty(vacationResults)) {
            result = transObject(vacationResults.get(0), VacationDetailDto.class);
            result.setDuration(UmapDateUtils.differentDaysByMillisecond(result.getStartTime(), result.getEndTime()) + 1);
        }

        return ResultGenerator.genSuccessResult("查询成功", result);
    }

    @Login
    @ApiOperation(value = "查询审核人信息")
    @GetMapping(value = "/auditor")
    public Result auditor(@ApiParam @RequestParam Long id) {

        String result = "";
        Vacation selectVacation = new Vacation();
        selectVacation.setId(id);

        // 按id查询
        Vacation vacation = vacationService.selectVacationById(id);

        if (BeanUtil.isNotEmpty(vacation)) {
            SysUser user = sysUserService.selectUserById(vacation.getApproveTo());
            result = user.getUserName();
        }

        return ResultGenerator.genSuccessResult("查询成功", result);
    }


    @Login
    @ApiOperation(value = "查询我的审核列表")
    @PostMapping(value = "/myAuditList")
    public Result myAuditList(HttpServletRequest request, @ApiParam @RequestBody VacationParam vacationParam) {
        //type、auditStatus、checkDate
        Vacation selectVacation = transObject(vacationParam, Vacation.class);
        selectVacation.setApproveTo(getUserId(request));
        selectVacation.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);

        startPage();
        List<VacationResult> vacationResults = vacationService.selectVacationInfoList(selectVacation);

        if (CollUtil.isEmpty(vacationResults)) {
            vacationResults = new ArrayList<>();
        }

        return ResultGenerator.genSuccessResult("查询成功", transList(vacationResults, MyVacationAuditInfoDto.class));
    }

    @Login
    @ApiOperation(value = "取消申请")
    @PostMapping(value = "/cancel")
    @Log(title = "取消请假申请",businessType = BusinessType.UPDATE)
    public Result cancel(HttpServletRequest request,@ApiParam @RequestBody VacationParam vacationParam){
        // id
        Vacation updateVacation = transObject(vacationParam, Vacation.class);
        updateVacation.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
        updateVacation.setUpdateBy(getUserId(request));
        int row = vacationService.updateVacation(updateVacation);
        return row > 0 ? ResultGenerator.genSuccessResult("取消成功") : ResultGenerator.genFailResult("取消失败，请联系管理员");
    }

    @Login
    @ApiOperation(value = "审核")
    @PostMapping(value = "/audit")
    @Log(title = "审核请假申请",businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public Result audit(HttpServletRequest request,@ApiParam @RequestBody VacationParam vacationParam){

        int row = vacationService.audit(getLoginName(request), getUserId(request), vacationParam);

        return row > 0 ? ResultGenerator.genSuccessResult("操作成功") : ResultGenerator.genFailResult("审核失败，请联系管理员");
    }

    @Login
    @ApiOperation(value = "获取审核列表")
    @GetMapping(value = "/auditRecord")
    public Result auditRecord(@ApiParam @RequestParam Long id){
        AuditRecord selectRecord = new AuditRecord();
        selectRecord.setApplyType(AuditRecordTypeEnum.VacationApplyAudit.getValue());
        selectRecord.setApplyId(id);

        startPage();
        List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordVo(selectRecord);

        if (CollectionUtil.isEmpty(auditRecords)){
            return ResultGenerator.genFailResult("审核记录不存在");
        }

        return ResultGenerator.genSuccessResult("查询成功",auditRecords);
    }

}
