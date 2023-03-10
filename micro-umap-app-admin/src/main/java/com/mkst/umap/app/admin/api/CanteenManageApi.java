package com.mkst.umap.app.admin.api;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysRoleMapper;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.mini.systemplus.workflow.domain.WfEventDetail;
import com.mkst.mini.systemplus.workflow.service.IEventAuditRecordService;
import com.mkst.mini.systemplus.workflow.service.IWfEventDetailService;
import com.mkst.umap.app.admin.api.bean.param.ApproveParam;
import com.mkst.umap.app.admin.api.bean.param.canteen.CanteenManageParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenDetailResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.CanteenResult;
import com.mkst.umap.app.admin.api.bean.result.canteen.MealTypeCountResult;
import com.mkst.umap.app.admin.api.bean.result.car.AuditParam;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.dto.arraign.CanteenManageDto;
import com.mkst.umap.app.admin.dto.arraign.DayStatusDto;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.umap.app.admin.service.ICanteenManageService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.*;
import com.mkst.umap.base.core.util.UmapDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Api("??????????????????????????????")
@RestController
@RequestMapping("/api/canteen")
public class CanteenManageApi extends BaseApi {

    @Autowired
    private ICanteenManageService canteenManageService;
    @Autowired
    private IArraignRoomService roomService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAuditRecordService auditRecordService;
    @Autowired
    private IBoxMealService boxMealService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IWfEventDetailService eventDetailService;
    @Autowired
    private IEventAuditRecordService eventAuditRecordService;

    @PostMapping("/addSave")
    @ApiOperation("????????????????????????")
    @Login
    @Transactional(rollbackFor = Exception.class)
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(name = "canteenManageParam", value = "????????????????????????", required = true) CanteenManageParam canteenManageParam) {

        try {
            CanteenManage canteenManage = new CanteenManage();
            //1.??????????????????
            //????????????
            BeanUtils.copyProperties(canteenManageParam, canteenManage);
            canteenManage.setCreateBy(getUserId(request) + "");
            //?????????
            canteenManage.setDiningStatus(DiningStatusEnum.NotStart.getValue());
            //?????????
            canteenManage.setApplyStatus(ApplyStatusEnum.Pending.getValue());
            //??????
            canteenManage.setProgress(1);

            int row = canteenManageService.insertCanteenManage(canteenManage);

            //2.???????????????????????????
            EventAuditRecord insertRecord = new EventAuditRecord();
            //???????????????????????????
            SysUser targetUser = sysUserService.selectUserById(canteenManageParam.getTargetId());
            if (BeanUtil.isEmpty(targetUser)){
                return ResultGenerator.genFailResult("????????????????????????????????????????????????????????????????????????????????????");
            }
            //?????????????????????
            WfEventDetail wfEventDetail = eventDetailService.selectFirstEventDetail(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue());
            if (BeanUtil.isEmpty(wfEventDetail)){
                return ResultGenerator.genFailResult("????????????????????????????????????????????????????????????");
            }

            insertRecord.setApplyId(Math.toIntExact(canteenManage.getCanteenApplyId()));
            insertRecord.setApplyType(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue());
            insertRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
            insertRecord.setApprovalUserId(Math.toIntExact(targetUser.getUserId()));
            insertRecord.setApprovalOrder(wfEventDetail.getApprovalOrder());
            insertRecord.setCreateBy(targetUser.getLoginName());
            insertRecord.setUpdateBy(targetUser.getLoginName());
            eventAuditRecordService.insertEventAuditRecord(insertRecord);

            // ????????????app????????????
            if (row > 0){
                new Thread(() ->{
                    sendAppMsg(targetUser.getLoginName(),canteenManage.getCanteenApplyId());
                }).start();
            }
            return row > 0 ? ResultGenerator.genSuccessResult("??????????????????????????????") : ResultGenerator.genFailResult("???????????????????????????????????????????????????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        }
    }

    private void sendAppMsg(String target, Long cId) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("????????????");
        msgContent.setContent("????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizKey", cId.toString());
        params.put("bizType", BusinessTypeEnum.UMAP_CANTEEN_MANAGE.getValue());
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, cId.toString(), BusinessTypeEnum.UMAP_CANTEEN_MANAGE.getValue(), target);
        MsgPushUtils.getMsgPushTask().execute();
    }

    private void sendAppMsg(Long id) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle("?????????????????????");
        msgContent.setContent("????????????????????????????????????????????????????????????");

        Map<String, String> params = new HashMap<>();
        params.put("bizType", id.toString());
        params.put("bizKey", BusinessTypeEnum.UMAP_CANTEEN_MANAGE.getValue());
        msgContent.setParams(params);

        // ????????????????????????????????????????????? 18-??????????????????????????????id
        SysUser selectUser = new SysUser();
        SysRole lSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());

        SysRole kSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());

        selectUser.setRoleIds(new Long[]{lSysRole.getRoleId()});
        List<SysUser> lSysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        lSysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_CANTEEN_MANAGE.getValue(), user.getLoginName());
        });

        selectUser.setRoleIds(new Long[]{kSysRole.getRoleId()});
        List<SysUser> kSysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        kSysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_CANTEEN_MANAGE.getValue(), user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }


    @ApiLog(title = "?????????5?????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/dayStatusList")
    @ApiOperation("?????????5??????????????? ")
    @Login
    public Result dateList() {

        List<DayStatusDto> listDayStatus = new ArrayList<>(5);
        //???????????????????????????????????????
        ArrayList<Date> listDate = UmapDateUtils.getFutureDaysList(0, 5);

        BoxMeal boxMeal = new BoxMeal();
        boxMeal.setDelFlag("0");
        //??????????????????
        List<BoxMeal> list = boxMealService.selectBoxMealList(boxMeal);
        int total = list.size();
        for (Date date : listDate) {
            //??????????????????????????????????????????????????????
            //????????????????????????  ?????????????????? ??? ????????? ????????????  ????????????????????????
            Integer count = canteenManageService.selectCanteenManageCountByDateAndStatusEq01(UmapDateUtils.dateTime(date));

            DayStatusDto dayStatusDto = new DayStatusDto();
            dayStatusDto.setDate(date);
            if (count < total) {
                dayStatusDto.setStatus(KeyConstant.ARRAIGN_ROOM_FULL_NO);
            } else {
                dayStatusDto.setStatus(KeyConstant.ARRAIGN_ROOM_FULL_YES);
            }
            String today = UmapDateUtils.isToday(date) ? "??????" : UmapDateUtils.getWeekOfDate(date);
            dayStatusDto.setWeekDay(today);
            listDayStatus.add(dayStatusDto);
        }
        return ResultGenerator.genSuccessResult("????????????", listDayStatus);
    }


    @ApiImplicitParam(name = "date", value = "????????????", required = true, dataType = "string", paramType = "query")
    @GetMapping("/roomList")
    @ApiOperation("?????????????????????????????????")
    @Login
    public Result roomList(@RequestParam(value = "date") String date) {

        List<CanteenManageDto> list = roomService.getRoomCanteen(date);
        return list == null || list.isEmpty() ? ResultGenerator.genSuccessResult("??????????????????????????????????????????????????????") :
                ResultGenerator.genSuccessResult("????????????", list);
    }


    @ApiImplicitParam(name = "date", value = "????????????", required = true, dataType = "string", paramType = "query")
    @GetMapping("/usage")
    @ApiOperation("?????????????????????????????? ")
    @ResponseBody
    public Result usage(@RequestParam(value = "date") String date) {
        try {
            JSONArray array = new JSONArray();
            //?????????????????????????????????
            ArraignRoom selectRoom = new ArraignRoom();
            //???????????????????????????
            selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
            selectRoom.setType(RoomTypeEnum.CANTEEN_ROOM.getValue());

            List<ArraignRoom> roomList = roomService.selectArraignRoomList(selectRoom);

            //?????? ??????json??????
            for (ArraignRoom room : roomList) {
                List<CanteenResult> list = new LinkedList<>();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roomId", room.getId());
                jsonObject.put("roomName", room.getName());
                jsonObject.put("address", room.getAddress());

                CanteenManage canteenManage = new CanteenManage();
                canteenManage.setDateTime(date);
                canteenManage.setBoxId(room.getId());
                list = canteenManageService.selectCanteenResultByDate(canteenManage);

                CanteenManageParam canteenManageParam = new CanteenManageParam();
                canteenManageParam.setBoxId(room.getId());
                canteenManageParam.setDateTime(date);
                canteenManageParam.setApplyStatusArr(new String[]{KeyConstant.EVENT_AUDIT_STATUS_PASS,KeyConstant.EVENT_AUDIT_STATUS_WAIT});
                List<MealTypeCountResult> data = canteenManageService.countByMealType(canteenManageParam);

                jsonObject.put("data", data);
                array.add(jsonObject);
            }

            return ResultGenerator.genSuccessResult("????????????", array);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("????????????,??????????????????");
        }
    }

    @Login
    @GetMapping("/detail")
    @ApiOperation("??????????????????")
    public Result detail(HttpServletRequest request, @RequestParam(value = "canteenApplyId", required = true) @ApiParam(name = "canteenApplyId", value = "????????????id", required = true) Long canteenApplyId) {
        try {
            CanteenDetailResult detail = canteenManageService.selectCanteenManageAllById(canteenApplyId);
            List<AuditParam> auditParamList = new LinkedList<>();
           /* SysRole lSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());
            SysRole kSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());*/

            AuditRecord auditRecord = new AuditRecord();
            auditRecord.setApplyId(canteenApplyId);

            //?????????????????? ?????????????????????,?????? ???????????????????????????
            /*if (ApproveStatusEnum.Pending.getValue().equals(detail.getApplyStatus())) {

                auditParamListByRoleKey(lSysRole, detail.getCreateTime(), auditParamList, AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
                auditParamListByRoleKey(kSysRole, detail.getCreateTime(), auditParamList, AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
            } else if (ApproveStatusEnum.SUCCESS.getValue().equals(detail.getApplyStatus()) || ApproveStatusEnum.FAIL.getValue().equals(detail.getApplyStatus())) {
                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
                auditParamByAuditRecord(auditRecord, detail, auditParamList);
                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
                auditParamByAuditRecord(auditRecord, detail, auditParamList);
            } else if (ApproveStatusEnum.KITCHEN.getValue().equals(detail.getApplyStatus())) {
                auditParamListByRoleKey(lSysRole, detail.getCreateTime(), auditParamList, AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());

                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
                auditParamByAuditRecord(auditRecord, detail, auditParamList);
            } else if (ApproveStatusEnum.LOGISTICS.getValue().equals(detail.getApplyStatus())) {
                auditParamListByRoleKey(kSysRole, detail.getCreateTime(), auditParamList, AuditRecordTypeEnum.CanteenKitchenAudit.getValue());

                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
                auditParamByAuditRecord(auditRecord, detail, auditParamList);
            }
*/
            detail.setAuditParamList(auditParamList);

            String canAudit = "0";
            EventAuditRecord eventAuditRecord = new EventAuditRecord();
            eventAuditRecord.setApprovalOrder(StringUtil.toString(Integer.parseInt(detail.getProgress())));
            eventAuditRecord.setApplyType(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue());
            eventAuditRecord.setApplyId(Math.toIntExact(detail.getCanteenApplyId()));
            eventAuditRecord.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
            List<EventAuditRecord> auditRecords = eventAuditRecordService.selectEventAuditRecordList(eventAuditRecord);
            if (CollectionUtil.isNotEmpty(auditRecords) && StringUtils.equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT,detail.getApplyStatus().toString())){
                EventAuditRecord auditRecord1 = auditRecords.get(0);
                Long userId = getUserId(request);
                Integer approvalUserId = auditRecord1.getApprovalUserId();
                if (userId.longValue() == approvalUserId.longValue()){
                    canAudit = "1";
                }
            }
            detail.setCanAudit(canAudit);

            WfEventDetail eventDetail = eventDetailService.selectNextEventDetail(AuditRecordTypeEnum.CANTEEN_AUDIT.getValue(), Integer.parseInt(detail.getProgress()));
            if (BeanUtil.isNotEmpty(eventDetail)){
                detail.setNextRole(sysRoleMapper.selectRoleById(Long.valueOf(eventDetail.getApprovalObjectId())).getRoleKey());
            }

            return ResultGenerator.genSuccessResult("????????????", detail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        }
    }

    private List<AuditParam> auditParamByAuditRecord(AuditRecord auditRecord, CanteenDetailResult detail, List<AuditParam> auditParamList) {
        List<AuditRecord> auditRecordList = auditRecordService.selectAuditRecordList(auditRecord);
        for (AuditRecord a : auditRecordList) {
            AuditParam param = new AuditParam();
            SysUser sysUser = sysUserService.selectUserByLoginName(a.getCreateBy());
            param.setUserName(a.getCreateBy());
            param.setSex(sysUser.getSex());
            param.setApproveStatus(Integer.valueOf(a.getStatus()));
            param.setApproveType(a.getApplyType());
            param.setDate(a.getCreateTime());
            param.setUrl(sysUser.getAvatar());
            auditParamList.add(param);
        }
        return auditParamList;
    }

    //???????????????????????????
    private List<AuditParam> auditParamListByRoleKey(SysRole sysRole, Date date, List<AuditParam> auditParamList, String type) {
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{sysRole.getRoleId()});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        AuditParam param = new AuditParam();
        param.setApproveStatus(ApproveStatusEnum.Pending.getValue());
        param.setApproveType(type);
        param.setDate(date);
        auditParamList.add(param);
        return auditParamList;
    }

    @GetMapping("/getApplyList")
    @ApiOperation("??????????????????")
    @Login
    public Result getApplyList(HttpServletRequest request,
                               @RequestParam(value = "status") @ApiParam(name = "status", value = "????????????", required = false)
                                       String status,@RequestParam(value = "date") @ApiParam(name = "date", value = "????????????", required = true)
                                           String date) {
        try {
            //????????????????????????
            SysUser user = getSysUser(request);
            if (user == null) {
                return ResultGenerator.genFailResult("???????????????????????????");
            }
            CanteenManage canteenManage = new CanteenManage();
            canteenManage.setDateTime(date);
            canteenManage.setUserId(user.getUserId());
            List<CanteenResult> list = new LinkedList<>();
            List<Integer> applyStatusList = new LinkedList<>();

            startPage();
            // 0 ??????  1 ?????????(?????????????????????  ?????????  ????????? ?????????) ?????????????????? ????????? 2 ???????????????1 ??????????????? ??????????????????
            if ("0".equals(status)) {
                list = canteenManageService.selectCanteenResultByStatus(canteenManage);
            } else if ("1".equals(status)) {
                applyStatusList.add(ApproveStatusEnum.Pending.getValue());
                applyStatusList.add(ApproveStatusEnum.SUCCESS.getValue());
                applyStatusList.add(ApproveStatusEnum.KITCHEN.getValue());
                applyStatusList.add(ApproveStatusEnum.LOGISTICS.getValue());
                canteenManage.setDiningStatus(DiningStatusEnum.NotStart.getValue());
                canteenManage.setApplyStatusList(applyStatusList);
                list = canteenManageService.selectCanteenResultByStatus(canteenManage);
            } else if ("2".equals(status)) {
                canteenManage.setDiningStatus(DiningStatusEnum.Ongoing.getValue());
                list = canteenManageService.selectCanteenResultByStatus(canteenManage);
            } else if ("3".equals(status)) {
                applyStatusList.add(ApproveStatusEnum.CANCEL.getValue());
                applyStatusList.add(ApproveStatusEnum.FAIL.getValue());
                canteenManage.setApplyStatusList(applyStatusList);
                list = canteenManageService.selectCanteenResultByStatus(canteenManage);
            }

            return ResultGenerator.genSuccessResult("????????????", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        }
    }


    @GetMapping("/getApproveList")
    @ApiOperation("?????????????????? ")
    @Login
    public Result getApproveList(HttpServletRequest request,
                                 @RequestParam(value = "status", required = false) @ApiParam(name = "status", value = "????????????") Integer status,
                                 @RequestParam(value = "date", required = false) @ApiParam(name = "date", value = "????????????") String date) {
        try {
            //status  0 ??????????????????applyStatus??? 0 4 5?????? 1 ??????   2  ??????  3 ??????
            //????????????????????????
            SysUser user = getSysUser(request);
            if (user == null) {
                return ResultGenerator.genFailResult("???????????????????????????");
            }
            CanteenManage canteenManage = new CanteenManage();
            canteenManage.setDateTime(date);
            //???????????????
            //???????????????????????????????????????
            if (!user.isAdmin()) {
                SysRole logisticsSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());
                List<SysRole> lRoles = user.getRoles().stream().filter(o -> o.getRoleId().equals(logisticsSysRole.getRoleId())).collect(Collectors.toList());

                //???????????????
                SysRole kitchenSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());
                List<SysRole> kRoles = user.getRoles().stream().filter(o -> o.getRoleId().equals(kitchenSysRole.getRoleId())).collect(Collectors.toList());
                if (lRoles.size() <= 0 && kRoles.size() <= 0) {
                    return ResultGenerator.genFailResult("????????????????????????????????????");
                } else if (lRoles.size() <= 0 & kRoles.size() > 0) {
                    if (status != null) {
                        canteenManage.setApplyStatus(status);
                    } else {
                        List<Integer> list = new ArrayList<>();
                        list.add(ApproveStatusEnum.FAIL.getValue());
                        list.add(ApproveStatusEnum.SUCCESS.getValue());
                        list.add(ApproveStatusEnum.FINISH.getValue());
                        canteenManage.setApplyStatusList(list);

                        startPage();
                        List<CanteenResult> list1 = canteenManageService.selectCanteenResultByStatus(canteenManage);

                        list = new ArrayList<>();
                        list.add(ApproveStatusEnum.LOGISTICS.getValue());
                        canteenManage.setApplyStatusList(list);
                        List<CanteenResult> list2 = canteenManageService.selectCanteenResultByStatus(canteenManage);
                        list1.addAll(0, list2);
                        if (StrUtil.isNotBlank(date)) {
                            list1 = list1.stream().filter(o -> o.getDateTime().equals(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date))).collect(Collectors.toList());
                        }
                        return ResultGenerator.genSuccessResult("????????????", list1);
                    }
                } else {
                    if (ApproveStatusEnum.Pending.getValue().equals(status)) {
                        List<Integer> list = new ArrayList<>();
                        list.add(ApproveStatusEnum.Pending.getValue());
                        list.add(ApproveStatusEnum.KITCHEN.getValue());
                        list.add(ApproveStatusEnum.LOGISTICS.getValue());
                        canteenManage.setApplyStatusList(list);
                    } else {
                        canteenManage.setApplyStatus(status);
                    }
                }
            } else {
                if (ApproveStatusEnum.Pending.getValue().equals(status)) {
                    List<Integer> list = new ArrayList<>();
                    list.add(ApproveStatusEnum.Pending.getValue());
                    list.add(ApproveStatusEnum.KITCHEN.getValue());
                    list.add(ApproveStatusEnum.LOGISTICS.getValue());
                    canteenManage.setApplyStatusList(list);
                } else {
                    canteenManage.setApplyStatus(status);
                }
            }
            startPage();
            List<CanteenResult> list = canteenManageService.selectCanteenResultByStatus(canteenManage);
            if (StrUtil.isNotBlank(date)) {
                list = list.stream().filter(o -> o.getDateTime().equals(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date))).collect(Collectors.toList());
            }
            return ResultGenerator.genSuccessResult("????????????", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        }
    }

    private SysRole getSysRole(String roleKey) {
        return sysRoleMapper.checkRoleKeyUnique(roleKey);
    }

    @Login
    @PostMapping(value = "/dealAudit")
    @ApiOperation("???????????????????????????")
    public Result dealAudit(HttpServletRequest request,@RequestBody @ApiParam ApproveParam param){
        param.setCreateBy(getLoginName(request));
        int row = canteenManageService.auditByParam(param);
        if (BeanUtil.isNotEmpty(param.getTargetId())){
            SysUser user = sysUserService.selectUserById(param.getTargetId());
            // ????????????app????????????
            if (row > 0){
                new Thread(() ->{
                    sendAppMsg(user.getLoginName(),param.getId());
                }).start();
            }
        }
        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genFailResult("?????????????????????????????????????????????????????????");
    }

    @Login
    @PostMapping(value = "/myAuditList")
    @ApiOperation("??????????????????")
    public Result myAuditList(HttpServletRequest request,@RequestBody @ApiParam CanteenManageParam param){
        param.setNowUserId(getUserId(request));
        startPage();
        List<CanteenResult> myAuditDtos = canteenManageService.selectApplyAuditListByParam(param);
        return CollUtil.isEmpty(myAuditDtos) ? ResultGenerator.genSuccessResult("????????????",new ArrayList<CanteenResult>()) : ResultGenerator.genSuccessResult("????????????",myAuditDtos);
    }

    @Login
    @ApiOperation(value = "????????????????????????")
    @GetMapping(value = "/auditList")
    public Result auditList(@RequestParam @ApiParam Long applyId){
        return ResultGenerator.genSuccessResult("????????????",canteenManageService.selectAuditList(applyId));
    }


    @Deprecated
    @PostMapping("/audit")
    @ApiOperation("??????????????????")
    @Login
    public Result audit(HttpServletRequest request,
                        @RequestBody @ApiParam(name = "param", value = "??????????????????") ApproveParam param) {
        try {
            if (CollUtil.isEmpty(param.getApplyIds()) || StrUtil.isBlank(param.getApproveType())) {
                return ResultGenerator.genFailResult("?????????????????????????????????????????????");
            }
            SysUser sysUser = getSysUser(request);
            List<Long> applyIds = param.getApplyIds();
            CanteenManage canteenManage = new CanteenManage();
            int row = 0;
            for (Long applyId : applyIds) {
                switch (param.getApproveType()) {
                    case "1":
                        //????????? ????????????????????????????????????
                        String s = canteenManageService.auditUpdateSuccess(sysUser, applyId);
                        if (s.contains("??????")) {
                            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
                        } else {
                            return ResultGenerator.genSuccessResult(s);
                        }
                    case "2":
                        //?????????????????????????????????
                        canteenManage = canteenManageService.selectCanteenManageById(applyId);
                        canteenManage.setApplyStatus(ApproveStatusEnum.FAIL.getValue());
                        canteenManage.setDiningStatus(DiningStatusEnum.Finish.getValue());
                        row = canteenManageService.updateCanteenManage(canteenManage);

                        if (!sysUser.isAdmin()) {
                            //???????????????
                            SysRole logisticsSysRole = getSysRole(RoleKeyEnum.ROLE_LOGISTICS_ADMIN.getValue());
                            List<SysRole> lRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(logisticsSysRole.getRoleId())).collect(Collectors.toList());
                            if (CollUtil.isEmpty(lRoles)) {
                                AuditRecord auditRecord = new AuditRecord();
                                auditRecord.setApplyId(applyId);
                                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
                                auditRecord.setStatus(ApproveStatusEnum.FAIL.getValue() + "");
                                auditRecord.setCreateBy(getLoginName(request));
                                auditRecord.setUpdateBy(getLoginName(request));
                                auditRecord.setCreateTime(new Date());
                                auditRecordService.insertAuditRecord(auditRecord);
                            }
                            //???????????????
                            SysRole kitchenSysRole = getSysRole(RoleKeyEnum.ROLE_KITCHEN_ADMIN.getValue());
                            List<SysRole> kRoles = sysUser.getRoles().stream().filter(o -> o.getRoleId().equals(kitchenSysRole.getRoleId())).collect(Collectors.toList());
                            if (CollUtil.isEmpty(kRoles)) {
                                AuditRecord auditRecord = new AuditRecord();
                                auditRecord.setApplyId(applyId);
                                auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
                                auditRecord.setStatus(ApproveStatusEnum.FAIL.getValue() + "");
                                auditRecord.setCreateBy(getLoginName(request));
                                auditRecord.setUpdateBy(getLoginName(request));
                                auditRecordService.insertAuditRecord(auditRecord);
                            }
                            break;
                        } else {
                            AuditRecord auditRecord = new AuditRecord();
                            auditRecord.setApplyId(applyId);
                            auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
                            auditRecord.setStatus(ApproveStatusEnum.FAIL.getValue() + "");
                            auditRecord.setCreateBy(getLoginName(request));
                            auditRecord.setUpdateBy(getLoginName(request));
                            auditRecordService.insertAuditRecord(auditRecord);

                            AuditRecord newA = new AuditRecord();
                            newA.setApplyId(applyId);
                            newA.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
                            newA.setStatus(ApproveStatusEnum.FAIL.getValue() + "");
                            newA.setCreateBy(getLoginName(request));
                            newA.setUpdateBy(getLoginName(request));
                            auditRecordService.insertAuditRecord(newA);
                        }


                        //??????
                    case "3":
                        canteenManage = canteenManageService.selectCanteenManageById(applyId);
                        canteenManage.setApplyStatus(ApproveStatusEnum.CANCEL.getValue());
                        canteenManage.setDiningStatus(DiningStatusEnum.Finish.getValue());
                        row = canteenManageService.updateCanteenManage(canteenManage);
                        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genFailResult("???????????????????????????????????????????????????");
                }
            }

            return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("???????????????????????????????????????????????????");
        }
    }

    @Login
    @ApiOperation(value = "????????????????????????????????????")
    @PostMapping(value = "/dayCount")
    public Result dayCount(HttpServletRequest request,@RequestBody @ApiParam CanteenManageParam param){
        param.setUserId(getUserId(request));
        List<NameCountResult> results = canteenManageService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("????????????",results);
    }


}
