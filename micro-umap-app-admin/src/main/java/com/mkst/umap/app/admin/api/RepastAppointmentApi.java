package com.mkst.umap.app.admin.api;


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
import com.mkst.umap.app.admin.api.bean.param.RepastAppointmentParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.param.report.ReportParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.domain.RepastAppointment;
import com.mkst.umap.app.admin.service.IRepastAppointmentService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Api(value = "就餐预约")
@RestController
@RequestMapping(value = "/api/repast")
public class RepastAppointmentApi extends BaseApi {

    @Autowired
    private IRepastAppointmentService repastAppointmentService;

    @Autowired
    ISysUserService sysUserService;

    @Login
    @PostMapping(value = "/addRepast")
    @ApiOperation(value = "新增就餐预约")
    @Log(title = "新增就餐预约", businessType = BusinessType.INSERT)
    public Result addRepast(HttpServletRequest request, @ApiParam @RequestBody RepastAppointmentParam repastAppointmentParam) {
        Long userId = getUserId(request);
        RepastAppointment repastAppointment = transObject(repastAppointmentParam, RepastAppointment.class);

        RepastAppointment repast = new RepastAppointment();
        repast.setUserId(userId.intValue());
        repast.setType(repastAppointmentParam.getType());
        repast.setRepastDate(repastAppointmentParam.getRepastDate());
        List<RepastAppointment> repastAppointments = repastAppointmentService.selectRepastAppointmentList(repast);
        if(!CollectionUtils.isEmpty(repastAppointments)){
            return ResultGenerator.genFailResult("请勿重复提交");
        }
        repastAppointment.setUserId(userId.intValue());
        repastAppointment.setCreateBy(userId.toString());
        repastAppointment.setCreateTime(new Date());
        repastAppointmentService.insertRepastAppointment(repastAppointment);
        SysUser sysUser = sysUserService.selectUserById(repastAppointmentParam.getMessageUserId());
        SysUser user = sysUserService.selectUserById(userId);
        sendAppMsg(sysUser.getLoginName(),repastAppointment.getId(),user.getUserName());
        return ResultGenerator.genSuccessResult("预约成功");
    }



    @Login
    @ApiOperation(value = "依据日期获取就餐申请")
    @PostMapping(value = "/dayCount")
    public Result dayCount(@RequestBody @ApiParam RepastAppointmentParam param){
        List<NameCountResult> nameCountResults = repastAppointmentService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("查询成功",nameCountResults);
    }


    @Login
    @ApiOperation(value = "我的就餐申请")
    @PostMapping(value = "/myListRepast")
    public Result myListRepast(HttpServletRequest request, @RequestBody @ApiParam RepastAppointmentParam param){
        Long userId = getUserId(request);
        RepastAppointment repastAppointment = transObject(param, RepastAppointment.class);
        repastAppointment.setUserId(userId.intValue());
        startPage();
        List<RepastAppointment> repastAppointments = repastAppointmentService.selectRepastAppointmentList(repastAppointment);
        return ResultGenerator.genSuccessResult("查询成功",repastAppointments);
    }


    @Login
    @ApiOperation(value = "就餐申请")
    @PostMapping(value = "/listRepast")
    public Result listRepast(HttpServletRequest request, @RequestBody @ApiParam RepastAppointmentParam param){
        RepastAppointment repastAppointment = transObject(param, RepastAppointment.class);
        startPage();
        List<RepastAppointment> repastAppointments = repastAppointmentService.selectRepastAppointmentList(repastAppointment);
        return ResultGenerator.genSuccessResult("查询成功",repastAppointments);
    }

    @Login
    @ApiOperation(value = "就餐申请总计")
    @PostMapping(value = "/countRepast")
    public Result countRepast(HttpServletRequest request, @RequestBody @ApiParam RepastAppointmentParam param){
        RepastAppointment repastAppointment = transObject(param, RepastAppointment.class);
        List<RepastAppointment> repastAppointments = repastAppointmentService.countRepast(repastAppointment);
        return ResultGenerator.genSuccessResult("查询成功",repastAppointments);
    }


    private void sendAppMsg(String target, Integer id,String userName) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("就餐申请");
        msgContent.setContent(userName + "预约了就餐申请！");
        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType",  BusinessTypeEnum.UMAP_REPAST_APPOINTMENT.getValue());
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_REPAST_APPOINTMENT.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }


}
