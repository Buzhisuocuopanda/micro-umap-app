package com.mkst.umap.app.admin.api;

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
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysDictDataMapper;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.admin.api.bean.param.petition.MatterParam;
import com.mkst.umap.app.admin.api.bean.param.petition.PersonnelParam;
import com.mkst.umap.app.admin.api.bean.param.petition.PetitionParam;
import com.mkst.umap.app.admin.api.bean.param.reply.ReplyParam;
import com.mkst.umap.app.admin.domain.Petition;
import com.mkst.umap.app.admin.domain.PetitionMatter;
import com.mkst.umap.app.admin.domain.PetitionPersonnel;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.dto.ReplyDto;
import com.mkst.umap.app.admin.dto.petition.PetitionDetailDto;
import com.mkst.umap.app.admin.dto.petition.PetitionInfoDto;
import com.mkst.umap.app.admin.service.*;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import com.mkst.umap.app.common.enums.RoleKeyEnum;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PetitionApi
 * @Description 信访相关api---快速开发，有时间会进行优化
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-25 20:07
 */
@Api(value = "信访相关接口")
@RestController
@RequestMapping(value = "/api/petition")
public class PetitionApi extends BaseApi {

    @Autowired
    private IPetitionPersonnelService personnelService;

    @Autowired
    private IPetitionPersonnelBindService personnelBindService;

    @Autowired
    private IPetitionMatterService matterService;

    @Autowired
    private IPetitionMatterBindService matterBindService;

    @Autowired
    private IPetitionService petitionService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IReplyService replyService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Login
    @ApiOperation(value = "新增一个人", response = PetitionPersonnel.class)
    @PostMapping(value = "/addPersonnel")
    @Log(title = "新增一个信访相关人员", businessType = BusinessType.INSERT)
    public Result addPersonnel(HttpServletRequest request, @RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel insertPersonnel = transObject(personnelParam, PetitionPersonnel.class);
        insertPersonnel.setCreateBy(getUserId(request));
        int row = personnelService.insertPetitionPersonnel(insertPersonnel);
        return row > 0 ? ResultGenerator.genSuccessResult("保存成功", insertPersonnel) : ResultGenerator.genFailResult("保存失败，请联系管理员或刷新重试");
    }

    @Login
    @ApiOperation(value = "修改一个人", response = PetitionPersonnel.class)
    @PostMapping(value = "/updatePersonnel")
    @Log(title = "修改一个信访相关人员", businessType = BusinessType.UPDATE)
    public Result updatePersonnel(HttpServletRequest request, @RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel updatePersonnel = transObject(personnelParam, PetitionPersonnel.class);
        updatePersonnel.setUpdateBy(getUserId(request));
        int row = personnelService.updatePetitionPersonnel(updatePersonnel);
        return row > 0 ? ResultGenerator.genSuccessResult("保存成功", updatePersonnel) : ResultGenerator.genFailResult("保存失败，请联系管理员或刷新重试");
    }

    @Login
    @ApiOperation(value = "获取人员详情", response = PetitionPersonnel.class)
    @PostMapping(value = "/getPersonnelDetail")
    public Result getPersonnelDetail(@RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel petitionPersonnel = personnelService.selectPetitionPersonnelById(personnelParam.getId());
        if (petitionPersonnel == null) {
            petitionPersonnel = new PetitionPersonnel();
        }
        return ResultGenerator.genSuccessResult("查询成功", petitionPersonnel);
    }

    @Login
    @ApiOperation(value = "新增一个民行申诉事项", response = PetitionMatter.class)
    @PostMapping(value = "/addMatter")
    @Log(title = "新增一个民行申诉事项", businessType = BusinessType.INSERT)
    public Result addMatter(HttpServletRequest request, @ApiParam @RequestBody MatterParam matterParam) {
        PetitionMatter insertMatter = transObject(matterParam, PetitionMatter.class);
        insertMatter.setCreateBy(getUserId(request));
        int row = matterService.insertPetitionMatter(insertMatter);
        return row > 0 ? ResultGenerator.genSuccessResult("保存成功", insertMatter) : ResultGenerator.genFailResult("保存失败，请联系管理员或刷新重试");
    }

    @Login
    @ApiOperation(value = "修改一个民行申诉事项", response = PetitionMatter.class)
    @PostMapping(value = "/updateMatter")
    @Log(title = "修改一个民行申诉事项", businessType = BusinessType.UPDATE)
    public Result updateMatter(HttpServletRequest request, @ApiParam @RequestBody(required = true) MatterParam matterParam) {
        PetitionMatter updateMatter = transObject(matterParam, PetitionMatter.class);
        updateMatter.setUpdateBy(getUserId(request));
        int row = matterService.updatePetitionMatter(updateMatter);
        return row > 0 ? ResultGenerator.genSuccessResult("保存成功", updateMatter) : ResultGenerator.genFailResult("保存失败，请联系管理员或刷新重试");
    }

    @Login
    @ApiOperation(value = "获取民行申诉事项详情", response = PetitionMatter.class)
    @PostMapping(value = "/getMatterDetail")
    public Result getMatterDetail(@ApiParam @RequestBody MatterParam matterParam) {
        PetitionMatter petitionMatter = matterService.selectPetitionMatterById(matterParam.getId());
        if (petitionMatter == null) {
            petitionMatter = new PetitionMatter();
        }
        return ResultGenerator.genSuccessResult("查询成功", petitionMatter);
    }

    @Login
    @ApiOperation(value = "新增一个信访")
    @PostMapping(value = "/addPetition")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "新增一个信访", businessType = BusinessType.INSERT)
    public Result addPetition(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {
        // type title content
        Petition insertPetition = transObject(petitionParam, Petition.class);
        insertPetition.setCreateBy(getUserId(request));
        petitionService.insertPetition(insertPetition);

        // 绑定人员
        if (StringUtils.isNotEmpty(petitionParam.getPersonnelIds())) {
            personnelBindService.insert(insertPetition.getId(), petitionParam.getPersonnelIds(), getUserId(request));
        }

        //绑定事项
        if (StringUtils.isNotEmpty(petitionParam.getMatterIds())) {
            matterBindService.insert(insertPetition.getId(), petitionParam.getMatterIds(), getUserId(request));
        }

        // 绑定文件
        if (StringUtils.isNotEmpty(petitionParam.getFileEntityIds())) {
            FileUploadExtendUtils.saveFileUpload(insertPetition.getId().toString(), BusinessTypeEnum.UMAP_PETITION.getValue(), petitionParam.getFileEntityIds());
        }

        if (!isLegal(petitionParam.getType())){
            new Thread(() -> {
                sendAppMsg(insertPetition.getId(), insertPetition.getType());
            }).start();
        }

        return ResultGenerator.genSuccessResult("提交成功，请等待回复");
    }

    private void sendAppMsg(Long id, String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("信访待回复");
        msgContent.setContent("有人提交信访，请您及时回复！");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", id.toString());
        params.put("bizType", type);
        msgContent.setParams(params);

        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_XFHF.getValue());
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
    @ApiOperation(value = "获取我提交的信访", response = PetitionInfoDto.class)
    @PostMapping(value = "myPetitionList")
    public Result myPetitionList(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {
        // hasReplied
        Petition selectPetition = transObject(petitionParam, Petition.class);
        selectPetition.setCreateBy(getUserId(request));
        startPage();
        List<Petition> petitionsList = petitionService.selectPetitionList(selectPetition);
        List<Petition>  petitions = new ArrayList();

        //过滤掉建言献策
        for(Petition petition:petitionsList){
            if (!"umap_petition_party_building".equals(petition.getType())) {
                petitions.add(petition);
            }
        }
        // 快速开发过程中选择的非最优处理方式，如果有时间优化会替换成在数据库中操作
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
        }
        petitions.stream().forEach(petition -> {
            // id type createTime
            PetitionInfoDto petitionInfoDto = transObject(petition, PetitionInfoDto.class);

            petitionInfoDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

            petitionInfoDto.setLegal(isLegal(petition.getType()));

            petitionInfoDtos.add(petitionInfoDto);
        });

        return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
    }

    @Login
    @ApiOperation(value = "工作台-信访列表")
    @PostMapping(value = "/list")
    public Result list(HttpServletRequest request,@ApiParam @RequestBody PetitionParam petitionParam){

        // hasReplied type createTime
        Petition selectPetition = transObject(petitionParam, Petition.class);
        startPage();
        List<Petition> petitions = petitionService.selectPetitionList(selectPetition);

        // 快速开发过程中选择的非最优处理方式，如果有时间优化会替换成在数据库中操作
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
        }

        // 查询我的回复
        Reply selectReply = new Reply();
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_PETITION.getValue());
        selectReply.setCreateBy(getLoginName(request));

        petitions.stream().forEach(petition -> {
            // 禅道3769:只能看到自己的回复
            if (petitionParam.getHasReplied() != null && "1".equals(petitionParam.getHasReplied())){
                selectReply.setObjectId(petition.getId().toString());
                List<Reply> bindReply = replyService.selectReplyList(selectReply);
                if (CollectionUtil.isEmpty(bindReply)){
                    return;
                }
            }

            // id type createTime
            PetitionInfoDto petitionInfoDto = transObject(petition, PetitionInfoDto.class);
            petitionInfoDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

            petitionInfoDto.setLegal(isLegal(petition.getType()));

            petitionInfoDtos.add(petitionInfoDto);
        });

        return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
    }

    @Login
    @ApiOperation(value = "获取信访详情", response = PetitionDetailDto.class)
    @PostMapping(value = "/detail")
    public Result detail(@ApiParam @RequestBody PetitionParam petitionParam) {

        Long petitionId = petitionParam.getId();

        Petition petition = petitionService.selectPetitionById(petitionId);
        if (petition == null) {
            return ResultGenerator.genFailResult("查询结果不存在");
        }
        // id type createTime title content
        PetitionDetailDto petitionDetailDto = transObject(petition, PetitionDetailDto.class);
        petitionDetailDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

        petitionDetailDto.setFileList(petitionService.getFileBind(petitionId));

        petitionDetailDto.setPersonnelInfo(petitionService.getPersonnelBind(petitionId));

        petitionDetailDto.setMatterInfo(petitionService.getMatterInfo(petitionId));

        petitionDetailDto.setLegal(isLegal(petition.getType()));

        return ResultGenerator.genSuccessResult("查询成功", petitionDetailDto);
    }


    @Login
    @ApiOperation(value = "回复信访")
    @PostMapping(value = "/reply")
    @Transactional(rollbackFor = Exception.class)
    public Result reply(HttpServletRequest request, @RequestBody @ApiParam ReplyParam replyParam) {

        Petition updatePetition = new Petition();
        updatePetition.setId(Long.valueOf(replyParam.getObjectId()));
        updatePetition.setHasReplied("1");
        updatePetition.setUpdateBy(getUserId(request));
        petitionService.updatePetition(updatePetition);

        // content businessType objectId
        Reply insertReply = transObject(replyParam, Reply.class);
        insertReply.setCreateBy(getLoginName(request));
        insertReply.setBusinessType(BusinessTypeEnum.UMAP_PETITION.getValue());
        int row = replyService.insertReply(insertReply);

        String msg = row > 0 ? "回复成功" : "回复失败，请刷新重试或联系管理员！";

        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation(value = "修改信访")
    @PostMapping(value = "/updateReply")
    @Transactional(rollbackFor = Exception.class)
    public Result updateReply(HttpServletRequest request, @RequestBody @ApiParam ReplyParam replyParam) {

        // id content
        Reply updateReply = transObject(replyParam, Reply.class);
        updateReply.setUpdateBy(getLoginName(request));
        int row = replyService.updateReply(updateReply);

        String msg = row > 0 ? "修改成功" : "修改失败，请刷新重试或联系管理员！";

        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation(value = "获取回复列表")
    @PostMapping(value = "/replyList")
    public Result replyList(HttpServletRequest request, @ApiParam @RequestBody PetitionParam petitionParam) {
        Reply selectReply = new Reply();
        selectReply.setObjectId(petitionParam.getId().toString());
        /*if ("umap_petition_party_building".equals(petitionParam.getType())) {
            selectReply.setBusinessType("umap_petition_party_building");
        } else {

            selectReply.setBusinessType(BusinessTypeEnum.UMAP_PETITION.getValue());
        }*/
        String loginName = getLoginName(request);
        ArrayList<ReplyDto> replyDtos = new ArrayList<>();
        startPage();
        List<Reply> replyList = replyService.selectReplyList(selectReply);

        replyList.stream().forEach(reply -> {
            ReplyDto replyDto = transObject(reply, ReplyDto.class);
            String userName = "";
            boolean modify ;
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
        return ResultGenerator.genSuccessResult("查询成功",replyDtos);
    }


    private boolean isLegal(String type){
        String result = dictDataMapper.selectDictLabel("legal_advice", type);
        return com.mkst.mini.systemplus.common.utils.StringUtils.isNotEmpty(result);
    }

    @Login
    @ApiOperation(value = "获取建言献策提交列表", response = PetitionInfoDto.class)
    @PostMapping(value = "suggestionList")
    public Result suggestionList(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {

        // hasReplied
        Petition selectPetition = transObject(petitionParam, Petition.class);

        List<SysUser> users =  sysUserService.selectUserLitByRoleKey("djgly");
        String str = "";
        for (SysUser user : users) {
            if (getUserId(request).equals(user.getUserId())) {
                str="党建管理员";
            }
        }
        List<Petition> petitions = null;
        if("党建管理员".equals(str)){
            startPage();
            petitions = petitionService.selectPetitionList(selectPetition);
        } else {
            selectPetition.setCreateBy(getUserId(request));
            startPage();
            petitions = petitionService.selectPetitionList(selectPetition);
        }

        // 快速开发过程中选择的非最优处理方式，如果有时间优化会替换成在数据库中操作
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
        }
        petitions.stream().forEach(petition -> {
            // id type createTime
            PetitionInfoDto petitionInfoDto = transObject(petition, PetitionInfoDto.class);

            petitionInfoDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

            petitionInfoDto.setLegal(isLegal(petition.getType()));

            petitionInfoDtos.add(petitionInfoDto);
        });

        return ResultGenerator.genSuccessResult("查询成功", petitionInfoDtos);
    }

}
