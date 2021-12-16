package com.mkst.umap.app.admin.api;


import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.system.domain.SysPost;
import com.mkst.mini.systemplus.system.domain.SysUserPost;
import com.mkst.mini.systemplus.system.mapper.SysUserPostMapper;
import com.mkst.mini.systemplus.system.service.ISysPostService;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.api.bean.param.IdentityParam;
import com.mkst.umap.app.admin.domain.Identity;
import com.mkst.umap.app.admin.service.IIdentityService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Api(value = "身份接口")
@Slf4j
@RestController
@RequestMapping(value = "/api/identity")
public class IdentityApi extends BaseApi {


    @Autowired
    private IIdentityService iIdentityService;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysPostService sysPostService;


    @PostMapping("postType")
    @ApiOperation("身份类型")
    @ApiLog(title = "身份类型", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result postType(HttpServletRequest request){
        Long userId = getUserId(request);
        List<SysPost> sysPosts = iIdentityService.selectPostList(userId);
        return ResultGenerator.genSuccessResult(sysPosts);
    }


    @PostMapping("addPost")
    @ApiOperation("申请身份")
    @ApiLog(title = "申请身份", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result addPost(@RequestBody IdentityParam identityParam, HttpServletRequest request){
        Long userId = getUserId(request);

        SysUserPost sysUserPost = iIdentityService.selectUserPost(userId, identityParam.getUserType().longValue());
        if(sysUserPost!=null){
            return ResultGenerator.genFailResult("该身份已存在,请勿重复申请");
        }

        Identity checkIdentity = new Identity();
        checkIdentity.setUserId(userId.intValue());
        checkIdentity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        checkIdentity.setUserType(identityParam.getUserType());
        List<Identity> identities = iIdentityService.selectIdentityList(checkIdentity);
        if(!CollectionUtils.isEmpty(identities)){
            return ResultGenerator.genFailResult("该身份申请已存在");
        }
        Identity identity = transObject(identityParam, Identity.class);
        identity.setUserId(userId.intValue());
        identity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());

        identity.setApplyTime(new Date());
        iIdentityService.insertIdentity(identity);
        FileUploadExtendUtils.saveFileUpload(identity.getId().toString(), BusinessTypeEnum.UMAP_IDENTITY.getValue(),identityParam.getFileIds());
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("auditPost")
    @ApiOperation("审核身份")
    @ApiLog(title = "审核身份", ApiOperatorType = ApiOperatorType.POST)
    public Result auditPost(@RequestParam("id") Integer id,@RequestParam("status") String status){
        Identity identity = iIdentityService.selectIdentityById(id);
        identity.setStatus(status);
        identity.setAduitTime(new Date());
        iIdentityService.updateIdentity(identity);
        if(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)){
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            SysUserPost up = new SysUserPost();
            up.setUserId(identity.getUserId().longValue());
            up.setPostId(identity.getUserType().longValue());
            list.add(up);
            userPostMapper.batchUserPost(list);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("identityDetail")
    @ApiOperation("申请详情")
    @ApiLog(title = "申请详情", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result identityDetail(@RequestParam("id") Integer id){
        Identity identitie = iIdentityService.selectIdentityById(id);
        List<SysFileUpload> fileUpload = FileUploadExtendUtils.findFileUpload(id.toString(), BusinessTypeEnum.UMAP_IDENTITY.getValue());
        identitie.setSysFileUploads(fileUpload);
        return ResultGenerator.genSuccessResult(identitie);
    }

    @PostMapping("identityList")
    @ApiOperation("申请列表")
    @ApiLog(title = "申请列表", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result identityList(HttpServletRequest request,@RequestParam("status") String status){
        Long userId = getUserId(request);
        Identity identity = new Identity();
        identity.setStatus(status);
        identity.setUserId(userId.intValue());

        startPage();
        List<Identity> identities = iIdentityService.selectIdentityList(identity);
        return ResultGenerator.genSuccessResult(identities);
    }

    @PostMapping("auditLawyerPost")
    @ApiOperation("审核律师身份")
    @ApiLog(title = "审核律师身份", ApiOperatorType = ApiOperatorType.POST)
    public Result auditLawyerPost(@RequestParam("userId") Integer userId,@RequestParam("status") String status){
        SysPost sysPost = new SysPost();
        sysPost.setPostCode("lawyer");
        List<SysPost> sysPosts = sysPostService.selectPostList(sysPost);
        if(CollectionUtils.isEmpty(sysPosts)){
            return ResultGenerator.genFailResult("未查询到律师身份");
        }
        Identity identity = new Identity();
        identity.setUserId(userId);
        identity.setUserType(sysPosts.get(0).getPostId().intValue());
        identity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        List<Identity> identities = iIdentityService.selectIdentityList(identity);
        if(CollectionUtils.isEmpty(identities)){
            return ResultGenerator.genFailResult("未查询到律师身份申请");
        }
        identities.stream().forEach( i->{
            i.setStatus(status);
            iIdentityService.updateIdentity(i);
        });
        if(AuditStatusEnum.EVENT_AUDIT_STATUS_FAIL.getValue().toString().equals(status)){
            return ResultGenerator.genSuccessResult();
        }
        SysUserPost sysUserPost = iIdentityService.selectUserPost(identity.getUserId().longValue(), identity.getUserType().longValue());
        if(sysUserPost == null && AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)) {
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            SysUserPost up = new SysUserPost();
            up.setUserId(userId.longValue());
            up.setPostId(sysPosts.get(0).getPostId());
            list.add(up);

            userPostMapper.batchUserPost(list);
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("addLawyerPost")
    @ApiOperation("申请律师身份")
    @ApiLog(title = "申请律师身份", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result addLawyerPost(@RequestBody IdentityParam identityParam, HttpServletRequest request){
        Identity identity = transObject(identityParam, Identity.class);
        SysPost sysPost = new SysPost();
        sysPost.setPostCode(KeyConstant.LAWYER_CODE);
        List<SysPost> sysPosts = sysPostService.selectPostList(sysPost);
        if(CollectionUtils.isEmpty(sysPosts)){
            return ResultGenerator.genFailResult("未查询到律师身份");
        }
        identity.setUserType(sysPosts.get(0).getPostId().intValue());
        identity.setApplyTime(new Date());
        identity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        iIdentityService.insertIdentity(identity);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("checkLawyer")
    @ApiOperation("申请律师身份")
    @ApiLog(title = "申请律师身份", ApiOperatorType = ApiOperatorType.POST)
    @Login
    public Result checkLawyer(HttpServletRequest request){
        Long userId = getUserId(request);
        SysPost sysPost = new SysPost();
        sysPost.setPostCode("lawyer");
        List<SysPost> sysPosts = sysPostService.selectPostList(sysPost);
        if(CollectionUtils.isEmpty(sysPosts)){
            return ResultGenerator.genFailResult("未查询到律师身份");
        }
        Identity identity = new Identity();
        identity.setUserId(userId.intValue());
        identity.setUserType(sysPosts.get(0).getPostId().intValue());
        identity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        if(!CollectionUtils.isEmpty(iIdentityService.selectIdentityList(identity))){
            return ResultGenerator.genSuccessResult("已提交律师申请，请勿重复提交",KeyConstant.RESOURCES_STATUS_DISAVAILABLE);
        }
        identity.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString());
        if(!CollectionUtils.isEmpty(iIdentityService.selectIdentityList(identity))){
            return ResultGenerator.genSuccessResult("已存在律师身份，请勿重复提交",KeyConstant.RESOURCES_STATUS_DISAVAILABLE);
        }
        return ResultGenerator.genSuccessResult("可以申请", KeyConstant.RESOURCES_STATUS_AVAILABLE);
    }

}
