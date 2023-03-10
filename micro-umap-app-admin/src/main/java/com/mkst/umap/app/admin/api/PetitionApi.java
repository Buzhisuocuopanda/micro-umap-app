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
 * @Description ????????????api---???????????????????????????????????????
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-25 20:07
 */
@Api(value = "??????????????????")
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
    @ApiOperation(value = "???????????????", response = PetitionPersonnel.class)
    @PostMapping(value = "/addPersonnel")
    @Log(title = "??????????????????????????????", businessType = BusinessType.INSERT)
    public Result addPersonnel(HttpServletRequest request, @RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel insertPersonnel = transObject(personnelParam, PetitionPersonnel.class);
        insertPersonnel.setCreateBy(getUserId(request));
        int row = personnelService.insertPetitionPersonnel(insertPersonnel);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????", insertPersonnel) : ResultGenerator.genFailResult("????????????????????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "???????????????", response = PetitionPersonnel.class)
    @PostMapping(value = "/updatePersonnel")
    @Log(title = "??????????????????????????????", businessType = BusinessType.UPDATE)
    public Result updatePersonnel(HttpServletRequest request, @RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel updatePersonnel = transObject(personnelParam, PetitionPersonnel.class);
        updatePersonnel.setUpdateBy(getUserId(request));
        int row = personnelService.updatePetitionPersonnel(updatePersonnel);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????", updatePersonnel) : ResultGenerator.genFailResult("????????????????????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "??????????????????", response = PetitionPersonnel.class)
    @PostMapping(value = "/getPersonnelDetail")
    public Result getPersonnelDetail(@RequestBody @ApiParam PersonnelParam personnelParam) {
        PetitionPersonnel petitionPersonnel = personnelService.selectPetitionPersonnelById(personnelParam.getId());
        if (petitionPersonnel == null) {
            petitionPersonnel = new PetitionPersonnel();
        }
        return ResultGenerator.genSuccessResult("????????????", petitionPersonnel);
    }

    @Login
    @ApiOperation(value = "??????????????????????????????", response = PetitionMatter.class)
    @PostMapping(value = "/addMatter")
    @Log(title = "??????????????????????????????", businessType = BusinessType.INSERT)
    public Result addMatter(HttpServletRequest request, @ApiParam @RequestBody MatterParam matterParam) {
        PetitionMatter insertMatter = transObject(matterParam, PetitionMatter.class);
        insertMatter.setCreateBy(getUserId(request));
        int row = matterService.insertPetitionMatter(insertMatter);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????", insertMatter) : ResultGenerator.genFailResult("????????????????????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "??????????????????????????????", response = PetitionMatter.class)
    @PostMapping(value = "/updateMatter")
    @Log(title = "??????????????????????????????", businessType = BusinessType.UPDATE)
    public Result updateMatter(HttpServletRequest request, @ApiParam @RequestBody(required = true) MatterParam matterParam) {
        PetitionMatter updateMatter = transObject(matterParam, PetitionMatter.class);
        updateMatter.setUpdateBy(getUserId(request));
        int row = matterService.updatePetitionMatter(updateMatter);
        return row > 0 ? ResultGenerator.genSuccessResult("????????????", updateMatter) : ResultGenerator.genFailResult("????????????????????????????????????????????????");
    }

    @Login
    @ApiOperation(value = "??????????????????????????????", response = PetitionMatter.class)
    @PostMapping(value = "/getMatterDetail")
    public Result getMatterDetail(@ApiParam @RequestBody MatterParam matterParam) {
        PetitionMatter petitionMatter = matterService.selectPetitionMatterById(matterParam.getId());
        if (petitionMatter == null) {
            petitionMatter = new PetitionMatter();
        }
        return ResultGenerator.genSuccessResult("????????????", petitionMatter);
    }

    @Login
    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/addPetition")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "??????????????????", businessType = BusinessType.INSERT)
    public Result addPetition(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {
        // type title content
        Petition insertPetition = transObject(petitionParam, Petition.class);
        insertPetition.setCreateBy(getUserId(request));
        petitionService.insertPetition(insertPetition);

        // ????????????
        if (StringUtils.isNotEmpty(petitionParam.getPersonnelIds())) {
            personnelBindService.insert(insertPetition.getId(), petitionParam.getPersonnelIds(), getUserId(request));
        }

        //????????????
        if (StringUtils.isNotEmpty(petitionParam.getMatterIds())) {
            matterBindService.insert(insertPetition.getId(), petitionParam.getMatterIds(), getUserId(request));
        }

        // ????????????
        if (StringUtils.isNotEmpty(petitionParam.getFileEntityIds())) {
            FileUploadExtendUtils.saveFileUpload(insertPetition.getId().toString(), BusinessTypeEnum.UMAP_PETITION.getValue(), petitionParam.getFileEntityIds());
        }

        if (!isLegal(petitionParam.getType())){
            new Thread(() -> {
                sendAppMsg(insertPetition.getId(), insertPetition.getType());
            }).start();
        }

        return ResultGenerator.genSuccessResult("??????????????????????????????");
    }

    private void sendAppMsg(Long id, String type) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("???????????????");
        msgContent.setContent("??????????????????????????????????????????");

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
    @ApiOperation(value = "????????????????????????", response = PetitionInfoDto.class)
    @PostMapping(value = "myPetitionList")
    public Result myPetitionList(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {
        // hasReplied
        Petition selectPetition = transObject(petitionParam, Petition.class);
        selectPetition.setCreateBy(getUserId(request));
        startPage();
        List<Petition> petitionsList = petitionService.selectPetitionList(selectPetition);
        List<Petition>  petitions = new ArrayList();

        //?????????????????????
        for(Petition petition:petitionsList){
            if (!"umap_petition_party_building".equals(petition.getType())) {
                petitions.add(petition);
            }
        }
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
        }
        petitions.stream().forEach(petition -> {
            // id type createTime
            PetitionInfoDto petitionInfoDto = transObject(petition, PetitionInfoDto.class);

            petitionInfoDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

            petitionInfoDto.setLegal(isLegal(petition.getType()));

            petitionInfoDtos.add(petitionInfoDto);
        });

        return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
    }

    @Login
    @ApiOperation(value = "?????????-????????????")
    @PostMapping(value = "/list")
    public Result list(HttpServletRequest request,@ApiParam @RequestBody PetitionParam petitionParam){

        // hasReplied type createTime
        Petition selectPetition = transObject(petitionParam, Petition.class);
        startPage();
        List<Petition> petitions = petitionService.selectPetitionList(selectPetition);

        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
        }

        // ??????????????????
        Reply selectReply = new Reply();
        selectReply.setBusinessType(BusinessTypeEnum.UMAP_PETITION.getValue());
        selectReply.setCreateBy(getLoginName(request));

        petitions.stream().forEach(petition -> {
            // ??????3769:???????????????????????????
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

        return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
    }

    @Login
    @ApiOperation(value = "??????????????????", response = PetitionDetailDto.class)
    @PostMapping(value = "/detail")
    public Result detail(@ApiParam @RequestBody PetitionParam petitionParam) {

        Long petitionId = petitionParam.getId();

        Petition petition = petitionService.selectPetitionById(petitionId);
        if (petition == null) {
            return ResultGenerator.genFailResult("?????????????????????");
        }
        // id type createTime title content
        PetitionDetailDto petitionDetailDto = transObject(petition, PetitionDetailDto.class);
        petitionDetailDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

        petitionDetailDto.setFileList(petitionService.getFileBind(petitionId));

        petitionDetailDto.setPersonnelInfo(petitionService.getPersonnelBind(petitionId));

        petitionDetailDto.setMatterInfo(petitionService.getMatterInfo(petitionId));

        petitionDetailDto.setLegal(isLegal(petition.getType()));

        return ResultGenerator.genSuccessResult("????????????", petitionDetailDto);
    }


    @Login
    @ApiOperation(value = "????????????")
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

        String msg = row > 0 ? "????????????" : "???????????????????????????????????????????????????";

        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation(value = "????????????")
    @PostMapping(value = "/updateReply")
    @Transactional(rollbackFor = Exception.class)
    public Result updateReply(HttpServletRequest request, @RequestBody @ApiParam ReplyParam replyParam) {

        // id content
        Reply updateReply = transObject(replyParam, Reply.class);
        updateReply.setUpdateBy(getLoginName(request));
        int row = replyService.updateReply(updateReply);

        String msg = row > 0 ? "????????????" : "???????????????????????????????????????????????????";

        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation(value = "??????????????????")
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
                userName = "???";
                modify = true;
            } else {
                modify = false;
                userName = user == null ? "" : user.getUserName();
            }

            replyDto.setResponder(userName);
            replyDto.setModify(modify);

            replyDtos.add(replyDto);
        });
        return ResultGenerator.genSuccessResult("????????????",replyDtos);
    }


    private boolean isLegal(String type){
        String result = dictDataMapper.selectDictLabel("legal_advice", type);
        return com.mkst.mini.systemplus.common.utils.StringUtils.isNotEmpty(result);
    }

    @Login
    @ApiOperation(value = "??????????????????????????????", response = PetitionInfoDto.class)
    @PostMapping(value = "suggestionList")
    public Result suggestionList(HttpServletRequest request, @RequestBody @ApiParam PetitionParam petitionParam) {

        // hasReplied
        Petition selectPetition = transObject(petitionParam, Petition.class);

        List<SysUser> users =  sysUserService.selectUserLitByRoleKey("djgly");
        String str = "";
        for (SysUser user : users) {
            if (getUserId(request).equals(user.getUserId())) {
                str="???????????????";
            }
        }
        List<Petition> petitions = null;
        if("???????????????".equals(str)){
            startPage();
            petitions = petitionService.selectPetitionList(selectPetition);
        } else {
            selectPetition.setCreateBy(getUserId(request));
            startPage();
            petitions = petitionService.selectPetitionList(selectPetition);
        }

        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        List<PetitionInfoDto> petitionInfoDtos = new ArrayList<>();
        if (CollectionUtil.isEmpty(petitions)) {
            return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
        }
        petitions.stream().forEach(petition -> {
            // id type createTime
            PetitionInfoDto petitionInfoDto = transObject(petition, PetitionInfoDto.class);

            petitionInfoDto.setCreateByName(userService.selectUserById(petition.getCreateBy()).getUserName());

            petitionInfoDto.setLegal(isLegal(petition.getType()));

            petitionInfoDtos.add(petitionInfoDto);
        });

        return ResultGenerator.genSuccessResult("????????????", petitionInfoDtos);
    }

}
