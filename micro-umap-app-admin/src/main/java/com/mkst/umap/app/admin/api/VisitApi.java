package com.mkst.umap.app.admin.api;


import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.SmsMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.common.constant.UserConstants;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.VisitParam;
import com.mkst.umap.app.admin.domain.Invitation;
import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.service.IInvitationService;
import com.mkst.umap.app.admin.service.IVisitApplyService;
import com.mkst.umap.app.common.enums.ApplyStatusEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.InvitationStatusEnum;
import com.mkst.umap.base.core.util.QrcodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Api(value = "来访登记接口")
@Slf4j
@RestController
@RequestMapping(value = "/api/visit")
public class VisitApi extends BaseApi {

    @Autowired
    IInvitationService iInvitationService;

    @Autowired
    IVisitApplyService visitApplyService;

    @Autowired
    ISysUserService sysUserService;


    @PostMapping("addVisit")
    @ApiOperation("添加预约")
    @ApiLog(title = "添加预约", ApiOperatorType = ApiOperatorType.POST)
    @Login
    @Transactional(rollbackFor = Exception.class)
    public Result addVisit(@RequestBody VisitParam visitParam, HttpServletRequest request) {

        SysUser sysUser = new SysUser();
        sysUser.setUserType(UserConstants.SYSTEM_USER_TYPE);
        sysUser.setPhonenumber(visitParam.getVisitPhone());
        List<SysUser> sysUsers = sysUserService.selectUserList(sysUser);
        if(CollectionUtils.isEmpty(sysUsers)){
            return ResultGenerator.genFailResult("请检查手机号是否正确");
        }
        Long userId = getUserId(request);
        VisitApply visitApply = transObject(visitParam, VisitApply.class);
        visitApply.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        visitApply.setInvitationUser(sysUsers.get(0).getUserId().intValue());
        visitApply.setCreateBy(userId.toString());
        visitApply.setCreateTime(new Date());
        visitApplyService.insertVisitApply(visitApply);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("auditList")
    @ApiOperation("审核列表")
    @ApiLog(title = "审核列表", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result auditList(@RequestParam(value = "status",required = false) String status,@RequestParam(value="type",required = false) String type,@RequestParam(value="name",required = false) String name,HttpServletRequest request) {
        Long userId = getUserId(request);
        VisitApply visitApply = new VisitApply();
        visitApply.setInvitationUser(userId.intValue());

        if(StringUtils.isNotEmpty(type)){
            visitApply.setType(type);
        }
        if(StringUtils.isNotEmpty(status)){
            if(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString().equals(status)){
                visitApply.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
            }else {
                visitApply.setAuditStatus(ApplyStatusEnum.Approval.getValue().toString());
            }
        }
        startPage();
        visitApply.setName(name);
        List<VisitApply> visitApplies = visitApplyService.selectVisitApplyList(visitApply);
        return ResultGenerator.genSuccessResult(visitApplies);
    }


    @PostMapping("visitList")
    @ApiOperation("预约列表")
    @ApiLog(title = "预约列表", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result visitList(@RequestParam("status") String status,HttpServletRequest request) {
        Long userId = getUserId(request);
        VisitApply visitApply = new VisitApply();
        visitApply.setCreateBy(userId.toString());
        visitApply.setStatus(status);

        startPage();
        List<VisitApply> visitApplies = visitApplyService.selectVisitApplyList(visitApply);
        return ResultGenerator.genSuccessResult(visitApplies);
    }

    @PostMapping("visitInfo")
    @ApiOperation("预约详情")
    @ApiLog(title = "预约详情", ApiOperatorType = ApiOperatorType.POST)
    public Result visitInfo(@RequestParam("id") Integer id) {
        VisitApply visitApply = visitApplyService.selectVisitApplyById(id);
        SysUser sysUser = sysUserService.selectUserById(visitApply.getInvitationUser().longValue());
        visitApply.setVisitPhone(sysUser.getPhonenumber());
        return ResultGenerator.genSuccessResult(visitApply);
    }


    @PostMapping("visitQrcode")
    @ApiOperation("预约二维码")
    @ApiLog(title = "预约二维码", ApiOperatorType = ApiOperatorType.POST)
    public Result visitQrcode(@RequestParam("id") Integer id) {
        VisitApply visitApply = visitApplyService.selectVisitApplyById(id);
        String qrcodeUrl = UUID.randomUUID()+".png" ;
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("event","visit");
        json.put("type",visitApply.getType());
        QrcodeUtil.createQrcode(json.toJSONString(), Global.getProfile() +qrcodeUrl);
        return ResultGenerator.genSuccessResult("获取二维码成功",qrcodeUrl);
    }


    @PostMapping("updateStatus")
    @ApiOperation("预约审核")
    @ApiLog(title = "预约审核", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result updateStatus(@RequestParam("id") Integer id,@RequestParam("status") String status) {
        VisitApply visitApply = new VisitApply();
        visitApply.setId(id);
        visitApply.setStatus(status);
        visitApplyService.updateVisitApply(visitApply);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("invitation")
    @ApiOperation("邀请")
    @ApiLog(title = "邀请", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result updateStatus(@RequestBody VisitParam visitParam,HttpServletRequest request) throws YxtSmsErrorException {
        Long userId = getUserId(request);
        VisitApply visitApply = transObject(visitParam, VisitApply.class);
        visitApply.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString());
        visitApply.setInvitationUser(userId.intValue());
        visitApplyService.insertVisitApply(visitApply);
        SmsMsgContent msgContent = new SmsMsgContent();
        msgContent.setTitle("来访邀请");
        msgContent.setContent("您有一个新的邀请，请到访时打开短信链接，出示拜访二维码："+SysConfigUtil.getKey("image_url")+"/open/visit/"+visitApply.getId());
        MsgPushUtils.push(msgContent, visitApply.getId().toString(), "VISIT", "[CODE]"+visitParam.getPhone());
        MsgPushUtils.getMsgPushTask().execute();
        return ResultGenerator.genSuccessResult();
    }

}
