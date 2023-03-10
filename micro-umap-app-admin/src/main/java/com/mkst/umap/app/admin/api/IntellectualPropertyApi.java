package com.mkst.umap.app.admin.api;

import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.api.bean.param.intellectualproperty.IntelProParam;
import com.mkst.umap.app.admin.domain.IntellectualProperty;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.dto.ReplyDto;
import com.mkst.umap.app.admin.dto.intellectualproperty.IntelProInfoDto;
import com.mkst.umap.app.admin.service.IIntellectualPropertyService;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
 * @ClassName IntellectualPropertyApi
 * @Description ????????????api
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-12 15:29
 */
@Slf4j
@Api(value = "??????????????????api")
@RestController
@RequestMapping(value = "/api/intelProperty")
public class IntellectualPropertyApi extends BaseApi {

    @Autowired
    private IIntellectualPropertyService intelProService;

    @Autowired
    private IReplyService replyService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;


    @Login
    @ApiOperation(value = "??????????????????????????????")
    @PostMapping(value = "/addSave")
    @ApiLog(title = "??????????????????????????????", businessType = BusinessType.INSERT)
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam IntelProParam intelProParam) {

        IntellectualProperty insertIntelPro = transObject(intelProParam, IntellectualProperty.class);
        insertIntelPro.setCreateBy(getLoginName(request));
        insertIntelPro.setUpdateBy(getLoginName(request));

        intelProService.insertIntellectualProperty(insertIntelPro);

        if (StringUtils.isNotEmpty(intelProParam.getFileEntityIds())) {
            FileUploadExtendUtils.saveFileUpload(insertIntelPro.getId().toString(), BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue(), intelProParam.getFileEntityIds());
        }

        // ????????????app????????????

        new Thread(() -> {
            sendAppMsg(insertIntelPro.getId());
        }).start();

        return ResultGenerator.genSuccessResult("??????????????????????????????");
    }

    private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("??????????????????");
        msgContent.setContent("??????????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue());
        msgContent.setParams(params);

        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_ZSCQHF.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = userService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue(), user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }

    @Login
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/list")
    public Result list(HttpServletRequest request, @RequestBody @ApiParam IntelProParam intelProParam) {
        IntellectualProperty selectIntel = transObject(intelProParam, IntellectualProperty.class);
        startPage();
        List<IntellectualProperty> infos = intelProService.selectIntellectualPropertyList(selectIntel);

        if (infos == null || infos.isEmpty()) {
            infos = new ArrayList<>();
        }
        List<IntelProInfoDto> intelProInfos = transList(infos, IntelProInfoDto.class);

        return ResultGenerator.genSuccessResult("????????????", intelProInfos);
    }

    @Login
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/myReqList")
    public Result myReqList(HttpServletRequest request, @RequestBody @ApiParam IntelProParam intelProParam) {

        IntellectualProperty selectIntel = transObject(intelProParam, IntellectualProperty.class);
        selectIntel.setCreateBy(getLoginName(request));
        startPage();
        List<IntellectualProperty> infos = intelProService.selectIntellectualPropertyList(selectIntel);

        if (infos == null || infos.isEmpty()) {
            infos = new ArrayList<>();
        }

        List<IntelProInfoDto> intelProInfos = transList(infos, IntelProInfoDto.class);

        return ResultGenerator.genSuccessResult("????????????", intelProInfos);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/detail")
    public Result getDetail(@RequestBody @ApiParam IntelProParam intelProParam){
        return ResultGenerator.genSuccessResult("????????????",intelProService.selectIntelProDetailById(intelProParam.getId()));
    }

    @Login
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/reply")
    @Transactional(rollbackFor = Exception.class)
    public Result reply(HttpServletRequest request,@RequestBody @ApiParam IntelProParam intelProParam){

        IntellectualProperty updateIntel = transObject(intelProParam, IntellectualProperty.class);
        updateIntel.setHasReplied("1");
        updateIntel.setUpdateBy(getLoginName(request));

        intelProService.updateIntellectualProperty(updateIntel);

        Reply insertReply = new Reply();
        insertReply.setContent(intelProParam.getReplyContent());
        insertReply.setCreateBy(getLoginName(request));
        insertReply.setBusinessType(BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue());
        insertReply.setObjectId(intelProParam.getId().toString());
        int row = replyService.insertReply(insertReply);

        String msg = row > 0 ? "????????????" : "???????????????????????????????????????????????????";

        return ResultGenerator.genSuccessResult(msg);
    }


    @Login
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/replyList")
    public Result replyList(HttpServletRequest request,@RequestBody @ApiParam IntelProParam intelProParam){

        Reply selectReply = new Reply();
        selectReply.setObjectId(intelProParam.getId().toString());
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_INTELLECTUAL_PROPERTY.getValue());

        // ????????????????????????????????????????????????????????????????????????sql?????????
        String loginName = getLoginName(request);
        ArrayList<ReplyDto> intelReplyDtos = new ArrayList<>();
        startPage();
        List<Reply> replyList = replyService.selectReplyList(selectReply);
        replyList.stream().forEach(reply -> {
            // id content createTime
            ReplyDto intelReplyDto = transObject(reply, ReplyDto.class);
            String userName = "";
            SysUser user = userService.selectUserByLoginName(reply.getCreateBy());
            intelReplyDto.setAvatar(user.getAvatar());
            if (reply.getCreateBy().equals(loginName)){
                userName = "???";
            }else {
                userName = user == null ? "" : user.getUserName();
            }

            intelReplyDto.setResponder(userName);

            intelReplyDtos.add(intelReplyDto);

        });

        return ResultGenerator.genSuccessResult("????????????",intelReplyDtos);
    }
}
