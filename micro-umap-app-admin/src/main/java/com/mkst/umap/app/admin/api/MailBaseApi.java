package com.mkst.umap.app.admin.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.api.bean.param.MailBaseParam;
import com.mkst.umap.app.admin.api.bean.param.reply.ReplyParam;
import com.mkst.umap.app.admin.domain.MailBase;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.dto.ReplyDto;
import com.mkst.umap.app.admin.dto.common.MailBaseDetailDto;
import com.mkst.umap.app.admin.dto.common.MailBaseInfoDto;
import com.mkst.umap.app.admin.service.IMailBaseService;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SuggestApi
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-24 12:22
 */
@Api
@RestController
@RequestMapping(value = "/api/mailBase")
public class MailBaseApi extends BaseApi {

    @Autowired
    private IMailBaseService mailBaseService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IReplyService replyService;

    @Login
    @PostMapping(value = "/addSave")
    @ApiOperation(value = "新增一条")
    @Log(title = "在邮箱基本信息中新增一条",businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request, @ApiParam @RequestBody MailBaseParam param){

        MailBase insertMail = transObject(param, MailBase.class);
        insertMail.setCreateBy(getUserId(request));
        int row = mailBaseService.insertMailBase(insertMail);

        // 绑定文件
        if (StringUtils.isNotEmpty(param.getFileEntityIds())) {
            FileUploadExtendUtils.saveFileUpload(insertMail.getId().toString(), BusinessTypeEnum.UMAP_MAIL_BASE.getValue(), param.getFileEntityIds());
        }

        return row > 0 ? ResultGenerator.genSuccessResult("提交成功") : ResultGenerator.genFailResult("提交失败，请联系管理员");
    }


    @Login
    @PostMapping(value = "/myMailList")
    @ApiOperation(value = "我的提交列表", response = MailBaseInfoDto.class)
    public Result myMailList(HttpServletRequest request,@ApiParam @RequestBody MailBaseParam param) {

        List<MailBaseInfoDto> result = new ArrayList<>();

        // hasReplied type
        MailBase selectMail = transObject(param, MailBase.class);
        selectMail.setCreateBy(getUserId(request));

        startPage();
        List<MailBase> mailList = mailBaseService.selectMailBaseList(selectMail);

        if (CollUtil.isNotEmpty(mailList)){
            result = transList(mailList,MailBaseInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("查询成功",result);
    }

    @Login
    @PostMapping(value = "/mailList")
    @ApiOperation(value = "列表", response = MailBaseInfoDto.class)
    public Result mailList(HttpServletRequest request,@ApiParam @RequestBody MailBaseParam param) {

        List<MailBaseInfoDto> result = new ArrayList<>();

        // hasReplied type checkDate
        MailBase selectMail = transObject(param, MailBase.class);

        startPage();
        List<MailBase> mailList = mailBaseService.selectMailBaseList(selectMail);

        if (CollUtil.isNotEmpty(mailList)){
            result = transList(mailList,MailBaseInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("查询成功",result);
    }

    @Login
    @ApiOperation(value = "获取详情",response = MailBase.class)
    @PostMapping(value = "/detail")
    public Result detail(HttpServletRequest request,@ApiParam @RequestBody MailBaseParam param){
        // id
        MailBase mailBase = mailBaseService.selectMailBaseById(param.getId());

        if (BeanUtil.isEmpty(mailBase)){
            return ResultGenerator.genFailResult("查询结果不存在");
        }
        return ResultGenerator.genSuccessResult("查询成功", transObject(mailBase, MailBaseDetailDto.class));
    }

    @Login
    @ApiOperation(value = "获取附件列表",response = MailBase.class)
    @PostMapping(value = "/fileList")
    public Result fileList(@ApiParam @RequestBody MailBaseParam param){

        Long mailId = param.getId();
        List<String> resultList = new ArrayList<>();

        MailBase mailBase = mailBaseService.selectMailBaseById(mailId);
        if (mailBase == null){
            return ResultGenerator.genSuccessResult("查询成功",resultList);
        }

        List<SysFileUpload> uploadList = FileUploadExtendUtils.findFileUpload(mailId.toString(), BusinessTypeEnum.UMAP_MAIL_BASE.getValue());

        if (CollectionUtil.isEmpty(uploadList)){
            return ResultGenerator.genSuccessResult("查询成功",resultList);
        }

        uploadList.stream().forEach(fileUpload ->{
            fileUpload.getFileEntityList().stream().forEach(file -> {
                resultList.add(file.getFilePath());
            });
        });
        return ResultGenerator.genSuccessResult("查询成功",resultList);
    }

    @Login
    @ApiOperation(value = "回复")
    @PostMapping(value = "/reply")
    @Transactional(rollbackFor = Exception.class)
    public Result reply(HttpServletRequest request, @RequestBody @ApiParam ReplyParam replyParam) {

        MailBase updateMail = new MailBase();
        updateMail.setId(Long.valueOf(replyParam.getObjectId()));
        updateMail.setHasReplied("1");
        updateMail.setCreateBy(getUserId(request));
        mailBaseService.updateMailBase(updateMail);

        // content businessType objectId
        Reply insertReply = transObject(replyParam, Reply.class);
        insertReply.setCreateBy(getLoginName(request));
        insertReply.setBusinessType(BusinessTypeEnum.UMAP_MAIL_BASE.getValue());
        int row = replyService.insertReply(insertReply);

        String msg = row > 0 ? "回复成功" : "回复失败，请刷新重试或联系管理员！";

        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation(value = "获取回复列表")
    @PostMapping(value = "/replyList")
    public Result replyList(HttpServletRequest request, @RequestBody @ApiParam ReplyParam replyParam) {
        Reply selectReply = new Reply();
        selectReply.setObjectId(replyParam.getObjectId());
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_MAIL_BASE.getValue());

        ArrayList<ReplyDto> replyDtos = new ArrayList<>();
        startPage();
        List<Reply> replyList = replyService.selectReplyList(selectReply);

        replyList.stream().forEach(reply -> {
            ReplyDto replyDto = transObject(reply, ReplyDto.class);
            SysUser user = userService.selectUserByLoginName(reply.getCreateBy());
            replyDto.setResponder(user.getUserName());
            replyDto.setAvatar(user.getAvatar());
            replyDtos.add(replyDto);
        });
        return ResultGenerator.genSuccessResult("查询成功", replyDtos);
    }


}
