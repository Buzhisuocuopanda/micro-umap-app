package com.mkst.umap.app.admin.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.common.constant.Constants;
import com.mkst.mini.systemplus.common.exception.BusinessException;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysRole;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.mini.systemplus.ws.server.MicroWebsocketServer;
import com.mkst.mini.systemplus.ws.spring.SpringContextTool;
import com.mkst.umap.app.admin.api.bean.param.*;
import com.mkst.umap.app.admin.api.bean.param.shift.ArraignShiftApproveParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.dto.arraign.*;
import com.mkst.umap.app.admin.service.*;
import com.mkst.umap.app.admin.util.ArraignAppointmentExcel;
import com.mkst.umap.app.admin.util.MyDateUtil;
import com.mkst.umap.app.admin.util.PrintExcel;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.*;
import com.mkst.umap.base.core.util.UmapDateUtils;
import com.spire.xls.Workbook;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.PrinterException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName ArraignApi
 * @Description ????????????????????????
 * @Author wangyong
 * @Date 2020-06-15 20:09
 */
@Slf4j
@Api("??????????????????????????????")
@RestController
@RequestMapping("/api/arraign")
public class ArraignApi extends BaseApi {

    @Autowired
    private IArraignRoomService arraignRoomService;
    @Autowired
    private IArraignRoomScheduleService arraignRoomScheduleService;
    @Autowired
    private IArraignAppointmentService arraignAppointmentService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private IArraignAppointmentAddLogService arraignAppointmentAddLogService;
    @Autowired
    private IArraignOperateRecordService arraignOperateRecordService;
    @Autowired
    private IArraignShiftService arraignShiftService;
    @Autowired
    private ISysConfigService configService;

    @Value("${systemplus.profile}")
    private String profileDir;

    @ApiLog(title = "?????????????????????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/arraignRoomInfo")
    @ApiOperation("????????????????????????????????????")
    @ResponseBody
    @Login
    public Result arraignRoomInfo() {
        try {
            ArraignRoom arraignRoom = new ArraignRoom();
            //????????????????????????
            arraignRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
            arraignRoom.setType(KeyConstant.ARENA_TYPE_ARRAIGN_ROOM);
            List<ArraignRoom> listArraignRoom = arraignRoomService.selectArraignRoomList(arraignRoom);

            if (listArraignRoom.isEmpty()) {
                return ResultGenerator.genFailResult("???????????????????????????????????????");
            }

            startPage();
            ArrayList<ArraignRoomInfoDto> listResult = new ArrayList<>();

            ArraignRoomSchedule arraignRoomSchedule = new ArraignRoomSchedule();
            ArraignAppointment arraignAppointment = new ArraignAppointment();

            String nowTime = DateUtils.getTime();

            for (ArraignRoom room : listArraignRoom) {
                //???????????????
                ArraignRoomInfoDto arraignRoomInfoDto = new ArraignRoomInfoDto();
                arraignRoomInfoDto.setRoomName(room.getName());
                //???????????????
                arraignRoomSchedule.setRoomId(room.getId());
                //????????????
                arraignRoomSchedule.setAppointmentTime(nowTime);
                //?????????????????????????????????????????????
                List<ArraignRoomSchedule> listRoomSchedule = arraignRoomScheduleService.selectArraignRoomScheduleList(arraignRoomSchedule);

                if (listRoomSchedule.isEmpty()) {
                    //?????????
                    arraignRoomInfoDto.setStatus(KeyConstant.RESOURCES_STATUS_NOW_FREE);
                    listResult.add(arraignRoomInfoDto);
                    continue;
                }

                //???????????????????????????
                ArraignRoomSchedule schedule = listRoomSchedule.get(0);
                arraignAppointment.setScheduleId(schedule.getId());
                arraignAppointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
                List<ArraignAppointment> listAppointment = arraignAppointmentService.selectArraignAppointmentList(arraignAppointment);

                //?????????????????????
                if (listAppointment.isEmpty()) {
                    //?????????
                    arraignRoomInfoDto.setStatus(KeyConstant.RESOURCES_STATUS_NOW_FREE);
                    listResult.add(arraignRoomInfoDto);
                    continue;
                }
                //??????info.status
                arraignRoomInfoDto.setStatus(KeyConstant.RESOURCES_STATUS_NOW_BUSY);
                //??????info.startTime
                arraignRoomInfoDto.setStartTime(schedule.getStartTime());
                arraignRoomInfoDto.setEndTime(schedule.getEndTime());

                //??????info.useBy
                ArraignAppointment appointment = listAppointment.get(0);
                SysUser user = sysUserService.selectUserById(appointment.getProsecutorUserId());
                arraignRoomInfoDto.setUseBy(user.getUserName());
                //??????info.appointmentId
                arraignRoomInfoDto.setAppointmentId(appointment.getId());
                //?????????????????????
                arraignRoomInfoDto.setDeptName(user.getDept().getDeptName());

                //????????????
                listResult.add(arraignRoomInfoDto);
            }

            return ResultGenerator.genSuccessResult("????????????", listResult);

        } catch (Exception e) {
            e.getStackTrace();
            return ResultGenerator.genFailResult("????????????????????????????????????????????????");
        }
    }


    @ApiLog(title = "?????????5?????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/dayStatusList")
    @ApiOperation("?????????5???????????????")
    @Login
    public Result dateList() {

        List<DayStatusDto> listDayStatus = new ArrayList<>(3);
        //??????????????????????????????????????????
        ArrayList<Date> listDate = UmapDateUtils.getFutureDaysList(0, 5);
        int index = 0;
        for (Date date : listDate) {
            DayStatusDto dayStatusDto = new DayStatusDto();
            dayStatusDto.setDate(date);
            dayStatusDto.setIndex(index);
            dayStatusDto.setStatus(KeyConstant.ARRAIGN_ROOM_FULL_NO);
            String today = UmapDateUtils.isToday(date) ? "??????" : UmapDateUtils.getWeekOfDate(date);
            dayStatusDto.setWeekDay(today);
            listDayStatus.add(dayStatusDto);
            index++;
        }
        return ResultGenerator.genSuccessResult("????????????", listDayStatus);
    }

    @ApiLog(title = "???????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiImplicitParam(name = "date", value = "????????????", required = true, dataType = "string", paramType = "query")
    @GetMapping("/roomSchedule")
    @ApiOperation("?????????????????????")
    @Login
    public Result roomSchedule(HttpServletRequest request , @RequestParam(name = "date", required = true) String date
            ,@RequestParam(name = "index",required = true) String index
            ,@RequestParam(name= "plus", required = false) Integer plus) {
        log.info("index :{} , date :{}" ,index , date);
        boolean isAdd = plus != null && 1==plus;
        log.info("plus :{} , isAdd :{}" ,plus , isAdd);
        List<RoomScheduleDto> roomScheduleArraign = arraignRoomService.getRoomScheduleArraign(date,index , getUserId(request) , isAdd);
        // ??????
        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

        Pattern p = Pattern.compile("^\\d{2}:\\d{2}");
        Pattern p2 = Pattern.compile("\\d{2}:\\d{2}");
        for(RoomScheduleDto e : roomScheduleArraign){
            // ??????????????????????????????????????????
            Set<String> keys = redisTemplate.keys(e.getId() + "--" + date + "*");

            List<String> list = new ArrayList<>();
            for(String key : keys) {

                Matcher m2 = p2.matcher(key);
                while (m2.find()) {
                    list.add(m2.group());
                }
            }
            LinkedHashMap<String, Boolean> tempRoom = e.getScheduleAm();
            Set<String> tempKeys = tempRoom.keySet();
            String res = "";
            for(String tempKey : tempKeys){
                Matcher m = p.matcher(tempKey);
                while (m.find()){
                    res = m.group();
                }
                for(String e2 : list){
                    if(res.equals(e2)){
                        tempRoom.put(tempKey, false);
                        break;
                    }
                }
            }

            tempRoom = e.getSchedulePm();
            tempKeys = tempRoom.keySet();
            for(String tempKey : tempKeys){
                Matcher m = p.matcher(tempKey);
                while (m.find()){
                    res = m.group();
                }
                for(String e2 : list){
                    if(res.equals(e2)){
                        tempRoom.put(tempKey, false);
                        break;
                    }
                }
            }

        }

        log.info("-----------------------------------------");
        log.info(JSONObject.toJSONString(roomScheduleArraign));
        if(isAdd)
            createNewSchedule(roomScheduleArraign, date);
        return roomScheduleArraign == null || roomScheduleArraign.isEmpty() ? ResultGenerator.genSuccessResult("?????????????????????????????????????????????????????????") :
                ResultGenerator.genSuccessResult("????????????", roomScheduleArraign);
    }

    private void createNewSchedule(List<RoomScheduleDto> allRoom, String date) {
        try{
            DateTimeFormatter dateParse = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeParse = DateTimeFormatter.ofPattern("HH:mm:ss");

            Integer timeSpan = Integer.parseInt(configService.selectConfigByKey("time_span"));
            String[] amStr = configService.selectConfigByKey("am_plus_time").split(",");
            LocalTime startAm = LocalTime.parse(amStr[0].trim(), timeParse);
            LocalTime endAm = LocalTime.parse(amStr[1].trim(), timeParse);
            String[] pmStr = configService.selectConfigByKey("pm_plus_time").split(",");
            LocalTime startPm = LocalTime.parse(pmStr[0].trim(), timeParse);
            LocalTime endPm = LocalTime.parse(pmStr[1].trim(), timeParse);
            LocalDate day = LocalDate.parse(date, dateParse);

            for(RoomScheduleDto e : allRoom){
                // am
                LinkedHashMap<String, Boolean> map = e.getScheduleAm();
                Set<String> keys = map.keySet();
                // ?????????????????????????????????
                for(String key : keys){
                    map.put(key, false);
                }
                Date start = Date.from(LocalDateTime.of(day, startAm).atZone(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(LocalDateTime.of(day, endAm).atZone(ZoneId.systemDefault()).toInstant());
                LinkedHashMap<String, Boolean> temp = plusTime(start, end, e.getId(), date, timeSpan);
                map.putAll(temp);
                keys = map.keySet();
                for (String key : keys){
                    // ???????????????????????????, ????????????
                    Boolean flag = map.get(key);
                    if(flag){
                        e.setRoomAvailable(true);
                        break;
                    }
                }
                // pm
                map = e.getSchedulePm();
                keys = map.keySet();
                for(String key : keys){
                    map.put(key, false);
                }
                start = Date.from(LocalDateTime.of(day, startPm).atZone(ZoneId.systemDefault()).toInstant());
                end = Date.from(LocalDateTime.of(day, endPm).atZone(ZoneId.systemDefault()).toInstant());
                temp = plusTime(start, end, e.getId(), date, timeSpan);
                map.putAll(temp);
                keys = map.keySet();
                for (String key : keys){
                    Boolean flag = map.get(key);
                    if(flag){
                        e.setRoomAvailable(true);
                        break;
                    }
                }
            }
        } catch (RuntimeException err){
            log.error("????????????????????????");
        }
    }

    public LinkedHashMap<String, Boolean> plusTime(Date start, Date end, String roomId, String date, Integer timeSpan){

        ArraignRoomSchedule room = new ArraignRoomSchedule();
        room.setRoomId(roomId);
        room.setScheduleDate(date);
        List<ArraignRoomSchedule> rs = arraignRoomScheduleService.roomUse(room);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");
        Set<String> keys = redisTemplate.keys(roomId + "--" + date + "*");
        Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

        LinkedHashMap<String, Boolean> list = new LinkedHashMap<>();
        while(start.getTime() < end.getTime()) {
            Date head = new Date(start.getTime() + timeSpan * 60 * 1000L);
            String s = UmapDateUtils.combine2Str(start, head);
            boolean flag = true;
            for(ArraignRoomSchedule r : rs){
                if(r.getStartTime().equals(start)){
                    flag = false;
                    break;
                }
            }
            for(String key : keys){
                String o = (String) redisTemplate.opsForValue().get(key);
                Matcher m = p.matcher(o);
                while (m.find()){
                    String res = m.group();
                    Date redisDate = Date.from(LocalDateTime.parse(res, dtf).atZone(ZoneId.systemDefault()).toInstant());
                    if(redisDate.equals(start)){
                        flag = false;
                        break;
                    }

                }
            }
            if(flag)
                list.put(s, true);
            else
                list.put(s, false);
            start = head;
        }
        return list;
    }



    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/addArraignNumber")
    @ApiOperation("??????????????????")
    @Login
    public Result addArraignNumber(HttpServletRequest request , ArraignAppointmentAddLog log){
        SysUser sysUser = getSysUser(request);
        log.setCreateBy(sysUser.getLoginName());
        log.setCreateTime(new Date());
        log.setUseStatus("0");
        arraignAppointmentAddLogService.insertArraignAppointmentAddLog(log);
        return ResultGenerator.genSuccessResult("????????????");
    }

    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/sendAppointment")
    @ApiOperation("??????????????????")
    @Login
    @Transactional(rollbackFor = Exception.class)
    public Result sendAppointment(@RequestBody ArraignAppointmentParam appointmentParam, HttpServletRequest request) {
        //??????criminalName???criminalType???criminalBirth???prosecutorId???prosecutorName
        ArraignAppointment appointment = transObject(appointmentParam, ArraignAppointment.class);
        ArraignRoomSchedule schedule = transObject(appointment, ArraignRoomSchedule.class);
        //??????????????????
        if(!"1".equals(appointmentParam.getPlus())){
            //????????????????????????????????????
            if (!checkScheduleNotExist(schedule,getUserId(request))) {
                return ResultGenerator.genFailResult("??????????????????????????????????????????????????????");
            }
            appointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        }else{
            //??????
            appointment.setStatus("5");
            appointment.setRemark("plus");
        }

        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String stringDate = dtf.format(LocalDateTime.ofInstant(appointment.getStartTime().toInstant(), ZoneId.systemDefault()));
        String redisKey = appointment.getRoomId() + "--" +  stringDate;
        // ?????????, ????????????
        redisTemplate.delete(redisKey);
        Boolean redisFlag = redisTemplate.opsForValue().setIfAbsent(redisKey, appointment.getRoomId());
        if(redisFlag){
            int flag = arraignAppointmentService.isArraign(appointment);
            if(flag > 0){
                // ?????????????????????, ??????????????????
                redisTemplate.delete(redisKey);
                return ResultGenerator.genFailResult("??????????????????????????????????????????????????????");
            }
            schedule.setCreateBy(getSysUser(request).getLoginName());
            arraignRoomScheduleService.insertArraignRoomSchedule(schedule);
            appointment.setScheduleId(schedule.getId());
            appointment.setCreateBy(getSysUser(request).getLoginName());
            appointment.setTimePie(DateUtil.isAM(schedule.getStartTime()) ? "1" : "2");
            // ??? 12 -- 14 ????????????????????????????????????
            appointment.setTimePie(isAM(schedule.getStartTime()));
            int row = arraignAppointmentService.insertArraignAppointment(appointment);
            // ????????????app????????????
            if (row > 0){
                new Thread(() ->{
                    sendAppMsg(appointment.getId(),"1".equals(appointmentParam.getPlus()));
                }).start();
            }
            redisTemplate.delete(redisKey);
            return ResultGenerator.genSuccessResult("??????????????????");
        }

        return ResultGenerator.genFailResult("??????????????????????????????????????????????????????");


    }

    private String isAM(Date d){
        try{
            LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
            int hour = ldt.getHour();
            if(hour < 14)
                return "1";
        }catch (RuntimeException err){
            log.error("?????????????????????");
        }
        return "2";
    }

    private void sendAppMsg(Long id , boolean isAdd) {
        AppMsgContent msgContent = new AppMsgContent();
        if(isAdd){
            msgContent.setTitle("????????????????????????");
            msgContent.setContent("????????????????????????????????????????????????????????????");
        }else{
            msgContent.setTitle("??????????????????");
            msgContent.setContent("????????????????????????????????????????????????????????????");
        }

        Map<String, String> params = new HashMap<>();
        params.put("bizType", BusinessTypeEnum.UMAP_ARRAIGN_APPOINTMENT.getValue());
        params.put("bizKey", id.toString());
        msgContent.setParams(params);

        SysRole selectRole = new SysRole();
        selectRole.setRoleKey(RoleKeyEnum.ROLE_SPTSGLY.getValue());
        List<SysRole> sysRoles = roleService.selectRoleList(selectRole);
        if (CollUtil.isEmpty(sysRoles)) {
            return;
        }
        Long roleId = sysRoles.get(0).getRoleId();
        SysUser selectUser = new SysUser();
        selectUser.setRoleIds(new Long[]{roleId});
        List<SysUser> sysUsers = sysUserService.selectUserListByRoleIds(selectUser);
        sysUsers.stream().forEach(user -> {
            MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_ARRAIGN_APPOINTMENT.getValue(), user.getLoginName());
        });
        MsgPushUtils.getMsgPushTask().execute();
    }


    @ApiLog(title = "arraignNotice", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/arraignNotice")
    @ApiOperation("??????????????????")
    @Login
    @Transactional(rollbackFor = Exception.class)
    public Result arraignNotice(ArraignNoticeParam param) throws YxtSmsErrorException {
        ArraignAppointment arraignAppointment = arraignAppointmentService.selectArraignAppointmentById(param.getId());
        if(arraignAppointment == null){
            return ResultGenerator.genFailResult("??????ID??????");
        }
        SysUser sysUser = sysUserService.selectUserById(arraignAppointment.getProsecutorUserId());
        String content = StrUtil.format("?????????{}???????????????{}?????????????????????????????????{}??????????????????????????????"
                , DateUtil.format(arraignAppointment.getStartTime(),"yyyy???MM???dd???")
                , arraignAppointment.getCriminalName()
                , arraignAppointment.getNumOrder());
        //????????????
        YxtSmsConfig.getYxtSmsService().sendMsg(sysUser.getPhonenumber(),content);
        //
        // ????????????app????????????
        new Thread(() ->{
            sendAppNoticeMsg(arraignAppointment.getId(),sysUser,"????????????",content);
        }).start();
        return ResultGenerator.genSuccessResult("??????????????????");
    }

    @ApiLog(title = "sendArraignScheduleNotice", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/sendArraignScheduleNotice")
    @ApiOperation("?????????????????????")
    @Login
    public Result sendArraignScheduleNotice(ArraignNoticeParam param) throws YxtSmsErrorException {
        ArraignAppointmentParam aap = new ArraignAppointmentParam();
        Date checkDate = param.getCheckDate();
        if(checkDate == null){
            checkDate = new Date();
        }
        aap.setCheckDate(checkDate);
        Map<String, List<TimeApplyResult>> map = arraignAppointmentService.selectDayApplyList(aap);
        List<TimeApplyResult> result = new ArrayList();
        for(String key:map.keySet()){
            result.addAll(map.get(key));
        }
        for(TimeApplyResult r: result){
            ArraignAppointment arraignAppointment = arraignAppointmentService.selectArraignAppointmentById(r.getId());
            if(arraignAppointment == null){
                continue;
            }
            SysUser sysUser = sysUserService.selectUserById(arraignAppointment.getProsecutorUserId());
            String content = StrUtil.format("?????????{}???????????????{}?????????????????????????????????{}??????????????????????????????"
                    , DateUtil.format(arraignAppointment.getStartTime(),"yyyy???MM???dd???")
                    , arraignAppointment.getCriminalName()
                    , arraignAppointment.getNumOrder());
            //????????????
            try{
                YxtSmsConfig.getYxtSmsService().sendMsg(sysUser.getPhonenumber(),content);
            }catch (Exception e){}
            //??????app??????
            try{
                sendAppNoticeMsg(arraignAppointment.getId(),sysUser,"????????????",content);
            }catch (Exception e){}
        }
        return ResultGenerator.genSuccessResult("??????????????????");
    }

    private void sendAppNoticeMsg(Long id , SysUser sysUser , String title , String content) {
        AppMsgContent msgContent = new AppMsgContent();
        msgContent.setTitle(title);
        msgContent.setContent(content);
        Map<String, String> params = new HashMap<>();
        params.put("bizType", BusinessTypeEnum.UMAP_ARRAIGN_APPOINTMENT.getValue());
        params.put("bizKey", id.toString());
        msgContent.setParams(params);
        MsgPushUtils.push(msgContent, id.toString(), BusinessTypeEnum.UMAP_ARRAIGN_APPOINTMENT.getValue(), sysUser.getLoginName());
        MsgPushUtils.getMsgPushTask().execute();
    }

    private boolean checkScheduleNotExist(ArraignRoomSchedule schedule , Long userId) {
        ArraignRoomSchedule selectSchedule = new ArraignRoomSchedule();
        selectSchedule.setRoomId(schedule.getRoomId());
        selectSchedule.setStartTime(schedule.getStartTime());
        List<ArraignRoomSchedule> arraignRoomSchedules = arraignRoomScheduleService.selectArraignRoomScheduleList(selectSchedule);
        // ?????????????????????
        boolean flag =  arraignRoomSchedules.isEmpty();
        if(flag == false){
            //?????????
            ArraignAppointmentAddLog query = new ArraignAppointmentAddLog();
            query.setRoomId(schedule.getRoomId());
            query.setAppointmentUserId(userId);
            query.setUseStatus("0");
            //query.setTimePie();
            boolean isAm = DateUtil.isAM(schedule.getStartTime());
            query.setTimePie(isAm?"1":"2");
            query.setAppointmentDate(DateUtil.parse(DateUtil.format(schedule.getStartTime(),"yyyy-MM-dd"),"yyyy-MM-dd"));
            List<ArraignAppointmentAddLog> list = arraignAppointmentAddLogService.selectArraignAppointmentAddLogList(query);
            ArraignAppointmentAddLog log =  (list != null && list.size() > 0 )?list.get(0):null;
            if(log != null){
                flag = true;
                log.setUseStatus("1");
                log.setUseTime(new Date());
                arraignAppointmentAddLogService.updateArraignAppointmentAddLog(log);
            }
        }
        return flag;
    }

    @ApiLog(title = "??????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/getArraignAppointmentTel")
    @ApiOperation("??????????????????????????????")
    @Login
    public Result getArraignAppointmentTel(){
        String tel = SysConfigUtil.getKey("arraign_appointment_tel");
        return ResultGenerator.genSuccessResult("????????????",tel);
    }

    @ApiLog(title = "?????????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiImplicitParam(name = "appointmentId", value = "????????????????????????appointmentId", required = true, dataType = "string", paramType = "query")
    @GetMapping("/roomDetail")
    @ApiOperation("???????????????????????????")
    @Login
    public Result roomDetail(HttpServletRequest request,
                             @RequestParam(value = "appointmentId", required = true) String appointmentId) {
        try {
            ArraignAppointment appointment = arraignAppointmentService.selectArraignAppointmentById(Long.valueOf(appointmentId));

            //??????????????????????????????????????????????????????
            ArraignRoomDetailDto appointmentDetailDto = transObject(appointment, ArraignRoomDetailDto.class);

            SysUser user = sysUserService.selectUserById(appointment.getProsecutorUserId());
            appointmentDetailDto.setUseBy(user.getUserName());
            appointmentDetailDto.setDeptName(user.getDept().getDeptName());

            return ResultGenerator.genSuccessResult("????????????", appointmentDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("?????????????????????????????????????????????");
        }
    }


    @ApiLog(title = "??????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiImplicitParam(name = "checkDate", value = "????????????", required = true, dataType = "string", paramType = "query")
    @GetMapping("/appointmentList")
    @ApiOperation("????????????????????????")
    @Login
    public Result appointmentList(@RequestParam(value = "checkDate", required = true) String checkDate) {


        List<AppointmentInfoDto> listAppointmentDto = new ArrayList<>();

        ArraignAppointment selectAppointment = new ArraignAppointment();
        selectAppointment.setAppointmentDate(checkDate);
        selectAppointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        /*startPage();*/
        List<ArraignAppointment> listAppointment = arraignAppointmentService.selectArraignAppointmentList(selectAppointment);

        for (ArraignAppointment appointment : listAppointment) {
            //??????id???startTime???endTime???criminalName???criminalType
            AppointmentInfoDto appointmentInfoDto = transObject(appointment, AppointmentInfoDto.class);
            String roomName = arraignRoomService.selectArraignRoomById(appointment.getRoomId()).getName();
            appointmentInfoDto.setRoomName(roomName);

            appointmentInfoDto.setUndertaker(sysUserService.selectUserByLoginName(appointment.getCreateBy()).getUserName());

            listAppointmentDto.add(appointmentInfoDto);
        }

        return ResultGenerator.genSuccessResult("????????????", listAppointmentDto);
    }

    @ApiLog(title = "??????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiImplicitParam(name = "appointmentId", value = "??????id", required = true, dataType = "string", paramType = "query")
    @GetMapping("/appointmentDetail")
    @ApiOperation("????????????????????????")
    @Login
    public Result appointmentDetail(@RequestParam(value = "appointmentId", required = true) String appointmentId) {

        try {
            ArraignAppointment appointment = arraignAppointmentService.selectArraignAppointmentById(Long.valueOf(appointmentId));
            //???????????????id???startTime???endTime???criminalName???criminalBirth???criminalType???prosecutorName???remark???status
            AppointmentDetailDto appointmentDetailDto = transObject(appointment, AppointmentDetailDto.class);

            SysUser user = sysUserService.selectUserById(appointment.getProsecutorUserId());
            appointmentDetailDto.setUndertaker(user.getUserName());
            appointmentDetailDto.setDept(user.getDept().getDeptName());
            appointmentDetailDto.setRoomName(arraignRoomService.selectArraignRoomById(appointment.getRoomId()).getName());

            return ResultGenerator.genSuccessResult(appointmentDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("?????????????????????????????????????????????");
        }
    }


    @ApiLog(title = "????????????????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/myAppointment")
    @ApiOperation("??????????????????")
    @Login
    public Result myAppointment(@RequestBody MyAppointmentParam myAppointmentParam, HttpServletRequest request) {

        SysUser sysUser = getSysUser(request);
        String loginName = sysUser.getLoginName();



        List<MyAppointmentDto> listResult = new ArrayList<>();

        ArraignAppointment selectAppointment = new ArraignAppointment();
        selectAppointment.setCheckCreateBy(loginName);
        selectAppointment.setCheckProsecutorUserId(getUserId(request));
        selectAppointment.setCheckDate(myAppointmentParam.getCheckDate());
        selectAppointment.setStatus(myAppointmentParam.getStatus());
        selectAppointment.setAuditStatus(myAppointmentParam.getAuditStatus());

        // 20200730?????????????????????????????????????????????????????????????????????
        // 20200907?????????????????????????????????????????????????????????
        /*if (myAppointmentParam.getStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_WAITING)
                || myAppointmentParam.getStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING)
                || myAppointmentParam.getStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED)) {
            selectAppointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        }*/
        startPage();
        List<ArraignAppointment> listAppointment = arraignAppointmentService.selectArraignAppointmentList(selectAppointment);

        /*//??????appointmentId???criminalName???criminalType
        List<MyAppointmentDto> listAppointmentDtos = transList(listAppointment, MyAppointmentDto.class);*/
        for (ArraignAppointment appointment : listAppointment) {
            //??????id???startTime???endTime???criminalName???criminalType
            MyAppointmentDto myAppointmentDto = transObject(appointment, MyAppointmentDto.class);

            //????????????
            Date nowDate = DateUtils.getNowDate();
            if (nowDate.before(appointment.getStartTime())) {
                myAppointmentDto.setStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
            } else if (nowDate.after(appointment.getEndTime())) {
                myAppointmentDto.setStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
            } else {
                myAppointmentDto.setStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
            }

            //????????????????????????????????????????????? ---??????????????????????????????
            //20200911?????????????????????????????????????????????
            if (appointment.getStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
                myAppointmentDto.setStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
            }

            // ??????????????????
            myAppointmentDto.setAuditStatus(appointment.getStatus());
            myAppointmentDto.setAuditName(AuditStatusEnum.getName(Integer.parseInt(appointment.getStatus())));

            //?????????????????????????????????
            // ??????????????????  && !myAppointmentParam.getStatus().equals(KeyConstant.EVENT_ALL)
            if (!myAppointmentParam.getNowStatus().isEmpty()
                    && !myAppointmentDto.getStatus().equals(myAppointmentParam.getNowStatus())) {
                //continue;
            }

            SysUser user = sysUserService.selectUserById(appointment.getProsecutorUserId());

            if(user != null){
                myAppointmentDto.setUseBy(user.getUserName());
                myAppointmentDto.setDeptName(user.getDept().getDeptName());
            }

            ArraignRoom room = arraignRoomService.selectArraignRoomById(appointment.getRoomId());
            // ???????????????????????????????????????room????????????
            myAppointmentDto.setRoomName(room == null ? "???????????????" : room.getName());

            listResult.add(myAppointmentDto);
        }
        return ResultGenerator.genSuccessResult("????????????", listResult);
    }

    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiOperation("????????????")
    @GetMapping("/auditBadge")
    @Login
    public Result auditBadge(){
        Integer count = arraignAppointmentService.selectArraignAppointmentAuditCount();
        return ResultGenerator.genSuccessResult("????????????", count == null?0:count);
    }


    /*@RequiresRoles("15")*/
    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiOperation("????????????")
    @ApiImplicitParam(name = "needDeal", value = "??????????????????0:????????? 1:????????????", required = true, dataType = "string", paramType = "query")
    @GetMapping("/auditList")
    @Login
    public Result auditList(@RequestParam(required = false) String status,
                            @RequestParam(required = false) String needDeal,
                            @RequestParam(required = false) String auditStatus,
                            @RequestParam(required = false) String appointmentDate,
                            HttpServletRequest request) {

        log.info("status:{},needDeal:{},auditStatus:{},appointmentDate:{}",status,needDeal,auditStatus,appointmentDate);
        SysUser sysUser = getSysUser(request);
        ArraignAppointment selectAppointment = new ArraignAppointment();
        selectAppointment.setNeedDeal(needDeal);
        selectAppointment.setAuditStatus(auditStatus);
        selectAppointment.setAppointmentDate(appointmentDate);
        selectAppointment.setStatus(status);
        startPage();
        List<ArraignAppointment> listArraignAppointment = arraignAppointmentService.selectArraignAppointmentList(selectAppointment);
        //??????createBy???createTime???id???startTime???endTime???status
        List<AuditInfoDto> auditInfoDtos = transList(listArraignAppointment, AuditInfoDto.class);
        System.out.println(auditInfoDtos);
        //????????????
        auditInfoDtos.stream().forEach(auditInfoDto -> {
            auditInfoDto.setUseByName(auditInfoDto.getProsecutorName());
            SysUser user = sysUserService.selectUserByLoginName(auditInfoDto.getCreateBy());
            if(user != null){
                auditInfoDto.setCreateBy(user.getUserName());
                auditInfoDto.setDept((user.getDept() !=null && user.getDept().getDeptName()!=null)? user.getDept().getDeptName():"");
            }
        });
        //???????????????????????????
        ArrayList<AuditInfoDto> list = new ArrayList<>();
        for(AuditInfoDto auditInfoDto:auditInfoDtos){
            list.add(auditInfoDto);
            /*if (!AuditStatusEnum.EVENT_AUDIT_STATUS_CANCEL.getValue().toString().equals(auditInfoDto.getStatus())) {
            }*/
        }
        return ResultGenerator.genSuccessResult("????????????", list);
    }

    @ApiLog(title = "??????/????????????????????????", ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation("??????/????????????")
    @PostMapping("/audit")
    @Login
    public Result audit(HttpServletRequest request, @RequestBody AppointmentAuditParam auditParam) {
        int row = arraignAppointmentService.updateStatusByIds(auditParam.getIds(), AuditRecordTypeEnum.ArraignAudit.getValue(), auditParam.getStatus(), auditParam.getReason(), getSysUser(request).getLoginName());
        return row > 0 ? ResultGenerator.genSuccessResult("????????????") : ResultGenerator.genFailResult("????????????");
    }

    @ApiLog(title = "????????????????????????????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????????????????????????????")
    @PostMapping("/dayCount")
    @Login
    public Result dayCount(@RequestBody @ApiParam ArraignAppointmentParam param){
        param.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        startPage();
        List<NameCountResult> nameCountResults = arraignAppointmentService.selectDayCount(param);
        nameCountResults.stream().forEach(o -> o.setStatus(false));
        return ResultGenerator.genSuccessResult("????????????",nameCountResults);
    }

    @ApiLog(title = "????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????")
    @PostMapping("/appointmentCalendar")
    @Login
    public Result appointmentCalendar(HttpServletRequest request){
        //??????????????????????????????
        //?????????????????? ??????????????????
        List<NameCountResult> nameCountResults = arraignAppointmentService.getAppointmentCalendar(getUserId(request));
        return ResultGenerator.genSuccessResult("????????????",nameCountResults);
    }

    @ApiLog(title = "??????????????????????????????????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "??????????????????????????????????????????")
    @PostMapping("/myDayCount")
    @Login
    public Result myDayCount(HttpServletRequest request,@RequestBody @ApiParam ArraignAppointmentParam param){
        Long userId = getUserId(request);
        String loginName = getLoginName(request);
        param.setCreateBy(loginName);
        param.setProsecutorUserId(userId);
        startPage();
        List<NameCountResult> nameCountResults = arraignAppointmentService.selectDayCount(param);
        nameCountResults.stream().forEach(o -> o.setStatus(false));
        return ResultGenerator.genSuccessResult("????????????",nameCountResults);
    }

    @ApiLog(title = "????????????-??????info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????-??????info")
    @PostMapping("/timeApplyList")
    @Login

    public Result timeApplyList(@RequestBody @ApiParam ArraignAppointmentParam param){
        param.setPassStatus(KeyConstant.EVENT_AUDIT_STATUS_FAIL);
        Map<String, List<TimeApplyResult>> map = arraignAppointmentService.selectTimeApplyList(param);
        int i = 1;
        for(String key:map.keySet()){
            List<TimeApplyResult> list = map.get(key);
            if(list != null && list.size() > 0){
                for(TimeApplyResult result:list){
                    result.setNumOrder(i++);
                }
            }
        }
        return ResultGenerator.genSuccessResult("success",map);
    }

    @ApiLog(title = "??????????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "??????????????????")
    @PostMapping("/dayApplyList")
    @Login
    public Result dayApplyList(@RequestBody @ApiParam ArraignAppointmentParam param){
        Map<String,List<TimeApplyResult>> map = arraignAppointmentService.selectDayApplyList(param);
        return ResultGenerator.genSuccessResult("success",map);
    }

    @ApiLog(title = "????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????")
    @PostMapping("/resetApplyOrder")
    @Login
    public Result resetApplyOrder(AppointmentArraignSortParam param){
        log.info("?????????????????????{}",param);
        arraignAppointmentService.resetApplyOrder(param.getId(),param.getNumOrder() , param.getCheckDate() , param.getTimePie());
        return ResultGenerator.genSuccessResult("success");
    }

    @ApiLog(title = "????????????-????????????info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "????????????-????????????info")
    @PostMapping("/myTimeApplyList")
    @Login
    public Result myTimeApplyList(HttpServletRequest request,@RequestBody @ApiParam ArraignAppointmentParam param){

        param.setProsecutorUserId(getUserId(request));
        param.setCreateBy(getLoginName(request));

        Map<String, List<TimeApplyResult>> map = arraignAppointmentService.selectTimeApplyList(param);
        int i = 1;
        for(String key:map.keySet()){
            List<TimeApplyResult> list = map.get(key);
            if(list != null && list.size() > 0){
                for(TimeApplyResult result:list){
                    result.setNumOrder(i++);
                }
            }
        }
        return ResultGenerator.genSuccessResult("success",map);
    }

    @ApiLog(title = "???????????????????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "???????????????????????????")
    @PostMapping("/getChangeProsecutorList")
    @Login
    public Result getChangeProsecutorList(HttpServletRequest request,@RequestBody @ApiParam SelectProcustorParam param){
        param.setProsecutorUserId(getUserId(request));
        param.setCreateBy(getLoginName(request));
        param.setLimitDate(DateUtil.beginOfDay(new Date()));
        List<SysUser> list =  arraignAppointmentService.selectProsecutorList(param);
        return ResultGenerator.genSuccessResult("????????????",transList(list,ProsecutorDto.class));
    }

    @ApiLog(title = "??????????????????",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "??????????????????")
    @PostMapping("/myApplyList")
    @Login
    public Result myApplyList(HttpServletRequest request,@RequestBody @ApiParam ArraignAppointmentParam param){
        param.setProsecutorUserId(getUserId(request));
        param.setCreateBy(getLoginName(request));
        Map<String, List<TimeApplyResult>> map = arraignAppointmentService.selectTimeApplyList(param);
        List<TimeApplyResult> result = new ArrayList();
        for(String key:map.keySet()){
            result.addAll(map.get(key));
        }
        Collections.sort(result, new Comparator<TimeApplyResult>() {
            @Override
            public int compare(TimeApplyResult o1, TimeApplyResult o2) {
                if(o1.getNumOrder() == null || o2.getNumOrder() == null){
                    return 0;
                }
                return o1.getNumOrder() - o2.getNumOrder();
            }
        });
        Map<String,List<TimeApplyResult>> mapResult = new HashMap<>();
        List<TimeApplyResult> morningList = new ArrayList<>();
        List<TimeApplyResult> afternoonList = new ArrayList<>();
        List<TimeApplyResult> noPassList = new ArrayList<>();
        if(result != null && result.size() > 0 ){
            for(TimeApplyResult bean : result){
                if(KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(bean.getAuditStatus())){
                    if("1".equals(bean.getTimePie())){
                        morningList.add(bean);
                    }else{
                        afternoonList.add(bean);
                    }
                }else{
                    noPassList.add(bean);
                }
            }
        }
        mapResult.put("morning",morningList);
        mapResult.put("afternoon",afternoonList);
        mapResult.put("noPass",noPassList);
        return ResultGenerator.genSuccessResult("success",mapResult);
    }

    @ApiLog(title = "????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @ApiOperation(value = "????????????????????????")
    @GetMapping(value = "/importTrigger")
    public Result importTrigger(){
        String msg = arraignAppointmentService.importLog();
        return ResultGenerator.genSuccessResult(msg);
    }

    @Login
    @ApiOperation("??????????????????")
    @PostMapping(value = "/exportData")
    public Result exportData(@RequestBody @ApiParam ArraignAppointment arraignAppointment) throws FileNotFoundException, PrinterException {
        String filename = encodingFilename("????????????????????????");
        HSSFWorkbook workbook = null;
        arraignAppointment.setDelFlag("0");
        arraignAppointment.setStatus("1");
        arraignAppointment.setOrderBy(" timePie,numOrder ");
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentList(arraignAppointment);
        workbook = AssembleExcel(list,arraignAppointment.getAppointmentDate());

        File file = writeFile(filename, workbook);
        PrintExcel.ExcelPrint(file,arraignAppointment.getPrinterName());
        return ResultGenerator.genSuccessResult("?????????????????????????????????");
    }

    @Login
    @ApiOperation("????????????excel")
    @PostMapping(value = "/getExportFileUrl")
    public Result getExportFileUrl(@RequestBody @ApiParam ArraignAppointment arraignAppointment) throws IOException, PrinterException {
        String fileUrlName =  "/arraign/" + UUID.randomUUID().toString().replaceAll("-","");
        String filename = profileDir + fileUrlName + ".xls";
        //???????????????
        File printFile = new File(filename);
        File parentFile = printFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        printFile.createNewFile();
        HSSFWorkbook workbook = null;
        arraignAppointment.setDelFlag("0");
        arraignAppointment.setStatus("1");
        arraignAppointment.setOrderBy(" time_pie,num_order ");
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentList(arraignAppointment);
        workbook = AssembleExcel(list,arraignAppointment.getAppointmentDate());
        File file = writeFile(filename, workbook);
        System.out.println(file.getPath());
        String url = Constants.RESOURCE_PREFIX + fileUrlName + ".xls";
        System.out.println(url);
        return ResultGenerator.genSuccessResult("????????????",url);
    }

    public String encodingFilename(String filename) {
        filename = profileDir + filename + ".xls";
        return filename;
    }

    /**
     * ??????list?????? ??????excel
     *
     * @param list
     * @param appointmentDate
     * @return
     */
    public HSSFWorkbook AssembleExcel(List<ArraignAppointment> list, String appointmentDate) {
        try {
            List<Map> afternoon = new ArrayList<Map>();
            List<Map> morning = new ArrayList<Map>();
            Map<String, List<Map>> mapList = new HashMap<>();
            int morningIndex = 1;

            String[] headNames = {"??????", "???????????????", "????????????", "??????", "?????????", "??????????????????", "??????", "??????","????????????"};
            String[] keys = {"id", "criminalName", "criminalBirth", "criminalType", "prosecutorName", "prosecutorId", "stage", "arraignType", "criminalAdmit"};
            for (ArraignAppointment appointment : list) {
                LinkedHashMap<String, Object> e = new LinkedHashMap<String, Object>();
                SimpleDateFormat df = new SimpleDateFormat("HH");

                e.put("criminalName", appointment.getCriminalName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String criminalBirth = null;
                if (appointment.getCriminalBirth() != null) {
                    criminalBirth = format.format(appointment.getCriminalBirth());
                }
                e.put("criminalBirth", criminalBirth);


                e.put("criminalType", dictDataService.selectDictLabel("criminal_type", appointment.getCriminalType()));
                e.put("prosecutorName", appointment.getProsecutorName());
                e.put("prosecutorId", appointment.getProsecutorId());

                String stage = dictDataService.selectDictLabel("case_stage", appointment.getStage());
                e.put("stage", stage);

                e.put("arraignType", dictDataService.selectDictLabel("arraign_type", appointment.getArraignType()));

                String criminalAdmit = dictDataService.selectDictLabel("sys_yes_no", appointment.getCriminalAdmit());
                e.put("criminalAdmit", "???".equals(criminalAdmit)?"":criminalAdmit);
                //int time = Integer.parseInt(df.format(appointment.getStartTime()));
                if ("1".equals(appointment.getTimePie())) {
                    e.put("id", morningIndex++);
                    morning.add(e);
                } else {
                    afternoon.add(e);
                }
            }
            //int afternoonIndex = morning.size() + 1;
            int afternoonIndex = 1;
            for (int j = 0; j < afternoon.size(); j++) {
                afternoon.get(j).put("id", afternoonIndex);
                afternoonIndex++;
            }
            mapList.put("morning", morning);
            mapList.put("afternoon", afternoon);
            int colWidths[] = {40, 100, 100, 150, 120, 150, 90, 90, 80};

            //????????????????????????
            ArraignAppointmentExcel arraignAppointmentExcel = new ArraignAppointmentExcel();
            return arraignAppointmentExcel.getExcelFile(mapList, "????????????????????????", headNames, keys, colWidths, appointmentDate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????Excel??????{}", e.getMessage());
            throw new BusinessException("??????Excel????????????????????????????????????");
        }
    }

    /**
     * ??????response??????
     * ???excel??????????????????
     *
     * @param fileName
     * @param workbook
     */
    public File writeFile(String fileName, HSSFWorkbook workbook) {
        try {
            Workbook wb = new Workbook();
            wb.loadFromFile(fileName);
            File outFile = new File(fileName);
            OutputStream outputStream = new FileOutputStream(outFile);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            return outFile;
        } catch (IOException e) {
            log.error("??????Excel??????{}", e.getMessage());
            throw new BusinessException("??????Excel????????????????????????????????????");
        }
    }

    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.POST)
    @GetMapping("/cancelArraignAppointment")
    @ApiOperation("??????????????????")
    @Login
    public Result cancelArraignAppointment(HttpServletRequest request,@RequestParam(value = "id", required = true) Long id,String cancelReason){

        //???????????????????????????????????????????????????
        ArraignAppointment appointment = arraignAppointmentService.selectArraignAppointmentById(id);

        Date startTime = appointment.getStartTime();
        String cancelConfig = SysConfigUtil.getKey("arraign_appointment_cancel_limit");
        if(StrUtil.isBlank(cancelConfig)){
            cancelConfig = "1-15";
        }
        boolean b = MyDateUtil.canCancelOrUpdate(startTime,cancelConfig);
        if(b){
            //????????????????????????
            ArraignAppointment arraignAppointment = new ArraignAppointment();
            arraignAppointment.setId(id);
            arraignAppointment.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_CANCEL.getValue().toString());
            arraignAppointment.setCancelReason(cancelReason);
            arraignAppointment.setUpdateBy(getLoginName(request));
            arraignAppointmentService.updateArraignAppointmentStatus(arraignAppointment);
            //?????????????????????
            ArraignRoomSchedule arraignRoomSchedule = new ArraignRoomSchedule();
            arraignRoomSchedule.setId(appointment.getScheduleId());
            arraignRoomSchedule.setDelFlag("1");
            arraignRoomScheduleService.updateArraignRoomSchedule(arraignRoomSchedule);

            //????????????
            /*SysUser sysUser = sysUserService.selectUserById(appointment.getProsecutorUserId());*/

            //????????????
            String content = StrUtil.format("{}-{}?????????????????????"
                    , DateUtil.format(appointment.getStartTime(),"yyyy???MM???dd??? HH:mm:ss"),
                    DateUtil.format(appointment.getEndTime(),"HH:mm:ss"));

            List<SysUser> users =  sysUserService.selectUserLitByRoleKey("sptsgly");
            if(users != null && users.size()!=0){
                for(SysUser user:users){

                    try{
                        YxtSmsConfig.getYxtSmsService().sendMsg(user.getPhonenumber(),content);
                    }catch (Exception e){}
                }
            }
            //??????????????????
            ArraignOperateRecord arraignOperateRecord = new ArraignOperateRecord();
            arraignOperateRecord.setOperateFunction(ArraignOperateTypeEnum.ARRAIGN_CASE_CANCEL.getValue());
            arraignOperateRecord.setCreateTime(new Date());
            arraignOperateRecord.setCreateBy(getLoginName(request));
            arraignOperateRecord.setCriminalName(appointment.getCriminalName());
            arraignOperateRecord.setCrimeType(appointment.getCriminalType());
            String start = DateUtil.format(appointment.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(appointment.getEndTime(), "HH:mm:ss");
            String appointmentTime = start+"-"+end;
            arraignOperateRecord.setAppointmentTime(appointmentTime);
            arraignOperateRecordService.insertArraignOperateRecord(arraignOperateRecord);


            return ResultGenerator.genSuccessResult("????????????");
        }else {
            return ResultGenerator.genFailResult("????????????????????????????????????");
        }
    }

    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.POST)
    @GetMapping("/updateArraignAppointment")
    @ApiOperation("??????????????????")
    @Login
    public Result updateArraignAppointment(HttpServletRequest request, ArraignAppointment arraignAppointment){

        ArraignAppointment appointment = arraignAppointmentService.selectArraignAppointmentById(arraignAppointment.getId());
        Date startTime = arraignAppointment.getStartTime();
        arraignAppointment.setUpdateBy(getLoginName(request));
        arraignAppointment.setUpdateTime(new Date());

        String cancelConfig = SysConfigUtil.getKey("arraign_appointment_update_limit");
        if(StrUtil.isBlank(cancelConfig)){
            cancelConfig = "1-15";
        }
        boolean b = MyDateUtil.canCancelOrUpdate(startTime,cancelConfig);
        if(b){
            arraignAppointmentService.updateAppointment(arraignAppointment);
            //????????????
            String content = StrUtil.format("{}???????????????????????????????????????app?????????",appointment.getProsecutorName());

            List<SysUser> users =  sysUserService.selectUserLitByRoleKey("sptsgly");
            if(users != null && users.size()!=0){
                for(SysUser user:users){

                    try{
                        YxtSmsConfig.getYxtSmsService().sendMsg(user.getPhonenumber(),content);
                    }catch (Exception e){}
                }
            }
            //??????????????????
            ArraignOperateRecord arraignOperateRecord = new ArraignOperateRecord();
            arraignOperateRecord.setOperateFunction(ArraignOperateTypeEnum.ARRAIGN_CASE_UPDATE.getValue());
            arraignOperateRecord.setCreateTime(new Date());
            arraignOperateRecord.setCreateBy(getLoginName(request));
            arraignOperateRecord.setCriminalName(appointment.getCriminalName());
            arraignOperateRecord.setCrimeType(appointment.getCriminalType());
            String start = DateUtil.format(appointment.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(appointment.getEndTime(), "HH:mm:ss");
            String appointmentTime = start+"-"+end;
            arraignOperateRecord.setAppointmentTime(appointmentTime);
            arraignOperateRecordService.insertArraignOperateRecord(arraignOperateRecord);
            return ResultGenerator.genSuccessResult("????????????");
        } else {
            return ResultGenerator.genFailResult("????????????????????????????????????");
        }

    }

    @ApiLog(title = "??????????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/getNotFinishArraignAppointment")
    @ApiOperation("??????????????????????????????")
    @Login
    public Result getNotFinishArraignAppointment(Long prosecutorUserId){
        if(prosecutorUserId == null){
            return ResultGenerator.genFailResult("???????????????ID????????????");
        }
        List<ArraignAppointment> list = arraignAppointmentService.selectNotFinishArraignAppointment(prosecutorUserId);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiLog(title = "????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/finishArraignAppointment")
    @ApiOperation("????????????")
    @Login
    public Result finishArraignAppointment(HttpServletRequest request , @RequestParam(value = "id", required = true) Long id){
        SysUser sysUser = getSysUser(request);
        ArraignAppointment appointment = arraignAppointmentService.selectArraignAppointmentById(id);
        if(appointment == null){
            return ResultGenerator.genFailResult("????????????????????????");
        }
        //????????????????????????????????????????????????????????????????????????
        if(!sysUser.getLoginName().equals(appointment.getCreateBy())
                && !sysUser.getUserId().equals(appointment.getProsecutorUserId())){
            return ResultGenerator.genFailResult("?????????????????????????????????");
        }
        int row = arraignAppointmentService.finishArraignAppoint(id, sysUser.getLoginName());
        if(row == 1){

            //??????????????????
            ArraignOperateRecord arraignOperateRecord = new ArraignOperateRecord();
            arraignOperateRecord.setOperateFunction(ArraignOperateTypeEnum.ARRAIGN_END_ARRAIGNMENT.getValue());
            arraignOperateRecord.setCreateTime(new Date());
            arraignOperateRecord.setCreateBy(getLoginName(request));
            arraignOperateRecord.setCriminalName(appointment.getCriminalName());
            arraignOperateRecord.setCrimeType(appointment.getCriminalType());
            String start = DateUtil.format(appointment.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(appointment.getEndTime(), "HH:mm:ss");
            String appointmentTime = start+"-"+end;
            arraignOperateRecord.setAppointmentTime(appointmentTime);
            arraignOperateRecordService.insertArraignOperateRecord(arraignOperateRecord);

            return ResultGenerator.genSuccessResult("??????");
        }else{
            return ResultGenerator.genFailResult("??????");
        }
    }

    @ApiLog(title = "??????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/shiftBadge")
    @ApiOperation("??????????????????")
    @Login
    //????????????????????????
    public Result shiftBadge(HttpServletRequest request){
        SysUser sysUser = getSysUser(request);
        Integer badge = arraignShiftService.selectArraignShiftCountByTargetUserId(sysUser
                , KeyConstant.EVENT_AUDIT_STATUS_WAIT);
        sysUser.getRoles();
        return ResultGenerator.genSuccessResult("????????????",badge);
    }

    @ApiLog(title = "????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/shiftApply")
    @ApiOperation("????????????")
    @Login
    public Result shiftApply(@RequestBody ArraignShift arraignShift , HttpServletRequest request){
        //?????????????????????
        log.info("arraignShift:"+arraignShift);
        ArraignAppointment applyExist = checkExistAppointment(arraignShift.getApplyUserId() , arraignShift.getApplySourceDate()
                , arraignShift.getApplyTimePie() , arraignShift.getApplyNumOrder() );
        //?????????????????????
        if(applyExist == null){
            return ResultGenerator.genFailResult("??????????????????");
        }else{
            arraignShift.setApplyArraignAppointmentId(applyExist.getId());
        }
        ArraignAppointment targetExist = checkExistAppointment(arraignShift.getTargetUserId() , arraignShift.getTargetDate()
                , arraignShift.getTargetTimePie() , arraignShift.getTargetNumOrder() );
        if(targetExist == null){
            return ResultGenerator.genFailResult("????????????????????????????????????");
        }else{
            arraignShift.setTargetArraignAppointmentId(targetExist.getId());
        }
        arraignShift.setCreateBy(getLoginName(request));
        arraignShift.setCreateTime(new Date());
        arraignShift.setStatus(ArraignShift.STATUS_INIT);
        int r = arraignShiftService.insertArraignShift(arraignShift);
        if(r == 1){
            return ResultGenerator.genSuccessResult("????????????");
        }else{
            return ResultGenerator.genFailResult("????????????");
        }
    }

    @ApiLog(title = "????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/shiftApplyList")
    @ApiOperation("????????????????????????")
    @Login
    public Result shiftApplyList(HttpServletRequest request ,
                                 @RequestParam(required = false) @ApiParam(name = "checkDate", value = "??????", required = false ) String checkDate,
                                 @RequestParam(required = false) @ApiParam(name = "status", value = "?????????0????????????1?????????2??????", required = false ) String status,
                                 @RequestParam(required = false) @ApiParam(name = "needDeal", value = "??????????????????0:????????? 1:????????????", required = false ) String needDeal){
        startPage();
        SysUser sysUser = getSysUser(request);
        ArraignShift arraignShift = new ArraignShift();
        arraignShift.setCreateBy(getSysUser(request).getLoginName());
        arraignShift.setApplyUserId(getUserId(request));
        arraignShift.setStatus(status);
        Map<String,Object> queryMap = new HashMap<>();
        if(StrUtil.isNotBlank(needDeal)){
            queryMap.put("needDeal",needDeal);
        }
        arraignShift.setParams(queryMap);
        if(StrUtil.isNotBlank(checkDate)){
            arraignShift.setCheckDate(DateUtil.parse(checkDate,"yyyy-MM-dd"));
        }
        List<ArraignShift> list = arraignShiftService.selectArraignShiftList(arraignShift);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiLog(title = "????????????????????????", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/shiftApproveList")
    @ApiOperation("????????????????????????")
    @Login
    public Result shiftApproveList(HttpServletRequest request, @RequestParam(required = false) @ApiParam(name = "checkDate", value = "??????", required = false ) String checkDate ,
                                   @RequestParam(required = false) @ApiParam(name = "status", value = "?????????0????????????1?????????2??????", required = false ) String status ,
                                   @RequestParam(required = false) @ApiParam(name = "needDeal", value = "??????????????????0:????????? 1:????????????", required = false ) String needDeal ){
        startPage();
        SysUser sysUser = getSysUser(request);
        ArraignShift arraignShift = new ArraignShift();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("approveUserId",sysUser.getUserId());
        queryMap.put("approveUserLoginName",sysUser.getLoginName());
        if(StrUtil.isNotBlank(checkDate)){
            arraignShift.setCheckDate(DateUtil.parse(checkDate,"yyyy-MM-dd"));
        }
        arraignShift.setStatus(status);
        if(StrUtil.isNotBlank(needDeal)){
            queryMap.put("needDeal",needDeal);
        }
        arraignShift.setParams(queryMap);
        List<ArraignShift> list = arraignShiftService.selectArraignShiftList(arraignShift);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiLog(title = "????????????", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/shiftApprove")
    @ApiOperation("????????????")
    @Login
    public Result shiftApprove(@RequestBody ArraignShiftApproveParam param , HttpServletRequest request){
        ArraignShift shift = arraignShiftService.selectArraignShiftById(param.getShiftId());
        if(shift == null){
            return ResultGenerator.genFailResult("ID??????");
        }
        if(!ArraignShift.STATUS_INIT.equals(shift.getStatus())){
            return ResultGenerator.genFailResult("??????????????????");
        }
        if(ArraignShift.STATUS_AGREE.equals(param.getStatus()) || ArraignShift.STATUS_REJECT.equals(param.getStatus())){
            //???????????????????????????
            arraignShiftService.changeShift(param.getShiftId());
            //??????????????????
            arraignShiftService.auditArraignShift(param.getShiftId(),param.getStatus());
        }else{
            return ResultGenerator.genFailResult("????????????");
        }
        return ResultGenerator.genSuccessResult("????????????");
    }

    /**
     * ????????????????????????
     * @param prosecutorUserId ???????????????ID
     * @param appointmentDate ????????????
     * @param timePie ?????????
     * @param numOrder ??????
     * @return
     */
    private ArraignAppointment checkExistAppointment(Long prosecutorUserId , String appointmentDate ,  String timePie , Integer numOrder ){
        if(prosecutorUserId == null || appointmentDate == null || StrUtil.isBlank(timePie) || numOrder == null){
            return null;
        }
        ArraignAppointment param = new ArraignAppointment();
        param.setProsecutorUserId(prosecutorUserId);
        param.setAppointmentDate(appointmentDate);
        param.setTimePie(timePie);
        param.setNumOrder(numOrder);
        List<ArraignAppointment> list = arraignAppointmentService.selectArraignAppointmentList(param);
        return list == null?null:list.get(0);
    }

}
