package com.mkst.umap.app.admin.api;

import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.api.bean.param.report.ReportParam;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.domain.Report;
import com.mkst.umap.app.admin.dto.ReplyDto;
import com.mkst.umap.app.admin.dto.report.ReportDetailDto;
import com.mkst.umap.app.admin.dto.report.ReportInfoDto;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.admin.service.IReportService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportApi
 * @Description 举报相关的接口
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-27 11:50
 */
@Api(value = "举报、随手拍")
@RestController
@RequestMapping(value = "/api/report")
public class ReportApi extends BaseApi {

    @Autowired
    private IReportService reportService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IReplyService replyService;

    @Autowired
    private ISysRoleService roleService;

    @Login
    @PostMapping(value = "/addSave")
    @ApiOperation(value = "新增一条举报")
    @Log(title = "新增一条举报", businessType = BusinessType.INSERT)
    public Result addSave(HttpServletRequest request, @ApiParam @RequestBody ReportParam reportParam) {

        Report insertReport = transObject(reportParam, Report.class);
        insertReport.setCreateBy(getUserId(request));
        int row = reportService.insertReport(insertReport);

        // 绑定文件
        if (StringUtils.isNotEmpty(reportParam.getFileEntityIds())) {
            FileUploadExtendUtils.saveFileUpload(insertReport.getId().toString(), BusinessTypeEnum.UMAP_REPORT.getValue(), reportParam.getFileEntityIds());
        }

        // 20201028 禅道4263要求去除消息通知
        /*new Thread(() -> {
            sendAppMsg(insertReport.getId(), insertReport.getType());
        }).start();*/

        return row > 0 ? ResultGenerator.genSuccessResult("提交成功") : ResultGenerator.genFailResult("提交失败，请联系管理员");
    }

    private void sendAppMsg(Long id, String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("举报待回复");
        msgContent.setContent("有人提交一条举报，请您及时回复！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", type);
        msgContent.setParams(params);

        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_JBHF.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = userService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), type, user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }


    @Login
    @PostMapping(value = "/myReportList")
    @ApiOperation(value = "我的举报列表", response = ReportInfoDto.class)
    public Result myReportList(HttpServletRequest request) {

        List<ReportInfoDto> reportInfoDtos = new ArrayList<>();

        Report selectReport = new Report();
        selectReport.setCreateBy(getUserId(request));

        startPage();
        List<Report> reports = reportService.selectReportList(selectReport);

        if (CollUtil.isNotEmpty(reports)){
            reportInfoDtos = transList(reports,ReportInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("查询成功",reportInfoDtos);
    }

    @Login
    @ApiOperation(value = "获取举报详情",response = ReportDetailDto.class)
    @PostMapping(value = "/detail")
    public Result detail(HttpServletRequest request,@ApiParam @RequestBody ReportParam reportParam){

        // id
        Report report = reportService.selectReportById(reportParam.getId());
        if (report == null) {
            return ResultGenerator.genFailResult("查询结果不存在");
        }

        ReportDetailDto reportDetailDto = transObject(report, ReportDetailDto.class);
        reportDetailDto.setFileList(reportService.getFileBind(reportParam.getId()));

        return ResultGenerator.genSuccessResult("查询成功", reportDetailDto);
    }

    @Login
    @ApiOperation(value = "获取回复列表")
    @PostMapping(value = "/replyList")
    public Result replyList(HttpServletRequest request, @ApiParam @RequestBody ReportParam reportParam) {
        Reply selectReply = new Reply();
        selectReply.setObjectId(reportParam.getId().toString());
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_REPORT.getValue());

        String loginName = getLoginName(request);
        ArrayList<ReplyDto> replyDtos = new ArrayList<>();
        startPage();
        List<Reply> replyList = replyService.selectReplyList(selectReply);

        replyList.stream().forEach(reply -> {
            ReplyDto replyDto = transObject(reply, ReplyDto.class);
            String userName = "";
            boolean modify;
            SysUser user = userService.selectUserByLoginName(reply.getCreateBy());
            replyDto.setAvatar(user.getAvatar());
            if (reply.getCreateBy().equals(loginName)) {
                userName = "我";
                modify = true;
            } else {
                modify = false;
                userName = user == null ? "" : user.getUserName();
            }

            replyDto.setResponder(userName);
            replyDto.setModify(modify);

            replyDtos.add(replyDto);
        });
        return ResultGenerator.genSuccessResult("查询成功", replyDtos);
    }


}
