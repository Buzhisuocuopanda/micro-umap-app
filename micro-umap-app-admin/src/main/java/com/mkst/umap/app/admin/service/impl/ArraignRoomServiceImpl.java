package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysDictDataService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.RoomDetailParam;
import com.mkst.umap.app.admin.api.bean.param.canteen.BoxMealParam;
import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.api.bean.result.arraign.OngoingAppointmentResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.dto.arraign.*;
import com.mkst.umap.app.admin.dto.reception.RoomInfoDto;
import com.mkst.umap.app.admin.dto.room.RoomAppointmentDto;
import com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto;
import com.mkst.umap.app.admin.mapper.ArraignAppointmentAddLogMapper;
import com.mkst.umap.app.admin.mapper.ArraignRoomMapper;
import com.mkst.umap.app.admin.mapper.ReceptionAppointmentMapper;
import com.mkst.umap.app.admin.service.*;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.RoomTypeEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提审室 服务层实现
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Service
public class ArraignRoomServiceImpl implements IArraignRoomService {
    @Autowired
    private ArraignRoomMapper arraignRoomMapper;
    @Autowired
    private IArraignRoomScheduleService arraignRoomScheduleService;
    @Autowired
    private ReceptionAppointmentMapper receptionAppointmentMapper;
    @Autowired
    private IArraignAppointmentService arraignAppointmentService;
    @Autowired
    private ISpecialCaseAppointmentService specialCaseAppointmentService;
    @Autowired
    private ICanteenManageService canteenManageService;
    @Autowired
    private IBoxMealService boxMealService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IRoomEquipmentService equipmentService;
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ArraignAppointmentAddLogMapper arraignAppointmentAddLogMapper;


    private static String MEAL_TYPE = "meal_type";

    /**
     * 查询提审室信息
     *
     * @param id 提审室ID
     * @return 提审室信息
     */
    @Override
    public ArraignRoom selectArraignRoomById(String id) {
        return arraignRoomMapper.selectArraignRoomById(id);
    }

    @Override
    public List<CountDto> selectRoomType0And30Day(){
        return arraignRoomMapper.selectRoomType0And30Day();
    }

    /**
     * 查询提审室列表
     *
     * @param arraignRoom 提审室信息
     * @return 提审室集合
     */
    @Override
    public List<ArraignRoom> selectArraignRoomList(ArraignRoom arraignRoom) {
        return arraignRoomMapper.selectArraignRoomList(arraignRoom);
    }


    @Override
    public List<ArraignRoom> selectRoomByType(String type) {

        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(type);

        return this.selectArraignRoomList(selectRoom);
    }

    /**
     * 新增提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertArraignRoom(ArraignRoom arraignRoom) {
        if (arraignRoom.getCreateBy() == null || arraignRoom.getCreateBy().isEmpty()) {
            arraignRoom.setCreateBy(ShiroUtils.getLoginName());
        }

        int row = arraignRoomMapper.insertArraignRoom(arraignRoom);

        if (StringUtil.isNotEmpty(arraignRoom.getScreenId())){
            Equipment selectEquipment = new Equipment();
            selectEquipment.setId(arraignRoom.getScreenId());
            List<Equipment> remoteEquipmentList = equipmentService.selectRemoteEquipmentList(selectEquipment);
            if (CollUtil.isNotEmpty(remoteEquipmentList)){
                Equipment screen = remoteEquipmentList.get(0);
                RoomEquipment insertRoomEquipment = new RoomEquipment();
                insertRoomEquipment.setRoomType(RoomTypeEnum.MEETING_ROOM.getValue());
                insertRoomEquipment.setUniqueId(screen.getSerialNo());
                insertRoomEquipment.setRoomId(arraignRoom.getId());
                insertRoomEquipment.setEquipmentId(arraignRoom.getScreenId());
                equipmentService.insertRoomEquipment(insertRoomEquipment);
            }
        }
        return row;
    }

    /**
     * 修改提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateArraignRoom(ArraignRoom arraignRoom) {
        if (arraignRoom.getUpdateBy() == null || arraignRoom.getUpdateBy().isEmpty()) {
            arraignRoom.setUpdateBy(ShiroUtils.getLoginName());
        }

        // 清空数据
        equipmentService.deleteRoomEquipmentByRoomId(arraignRoom.getId());
        if (StringUtil.isNotEmpty(arraignRoom.getScreenId())){
            Equipment selectEquipment = new Equipment();
            selectEquipment.setId(arraignRoom.getScreenId());
            List<Equipment> remoteEquipmentList = equipmentService.selectRemoteEquipmentList(selectEquipment);
            if (CollUtil.isNotEmpty(remoteEquipmentList)){
                Equipment screen = remoteEquipmentList.get(0);

                // 删除所有这个设备已绑定的房间信息，只能绑定一个
                RoomEquipment deleteEquipment = new RoomEquipment();
                deleteEquipment.setUniqueId(screen.getSerialNo());
                List<RoomEquipment> deleteList = equipmentService.selectRoomEquipmentList(deleteEquipment);
                deleteList.stream().forEach(e -> equipmentService.deleteRoomEquipmentByIds(e.getId().toString()));

                RoomEquipment insertRoomEquipment = new RoomEquipment();
                insertRoomEquipment.setRoomType(RoomTypeEnum.MEETING_ROOM.getValue());
                insertRoomEquipment.setUniqueId(screen.getSerialNo());
                insertRoomEquipment.setRoomId(arraignRoom.getId());
                insertRoomEquipment.setEquipmentId(arraignRoom.getScreenId());
                equipmentService.insertRoomEquipment(insertRoomEquipment);
            }
        }
        return arraignRoomMapper.updateArraignRoom(arraignRoom);
    }

    /**
     * 删除提审室对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteArraignRoomByIds(String ids) {

        arraignRoomScheduleService.deleteArraignRoomScheduleByrRoomId(ids);
        Arrays.asList(ids.split(",")).stream().forEach(roomId ->{
            equipmentService.deleteRoomEquipmentByRoomId(roomId);
        });
        return arraignRoomMapper.deleteArraignRoomByIds(Convert.toStrArray(ids));
    }

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.arraign.ArraignRoomListDto>
     * @Author wangyong
     * @Description 获取提审室列表及某的时间点占用提审室的预约信息
     * @Date 11:14 2020-07-03
     * @Param [arraignRoomListDto]
     */
    @Override
    public List<ArraignRoomListDto> getRoomListResult(ArraignRoomListDto arraignRoomListDto) {

        //获取结果骨架，这里装载了id,name
        ArraignRoom selectArraignRoom = new ArraignRoom();
        selectArraignRoom.setType(KeyConstant.ARENA_TYPE_ARRAIGN_ROOM);
        List<ArraignRoomListDto> resultList = transList(arraignRoomMapper.selectArraignRoomList(selectArraignRoom), ArraignRoomListDto.class);

        for (ArraignRoomListDto result : resultList) {
            // 不传时间点，选择当前时间点
            Date checkDatetime = arraignRoomListDto.getCheckDatetime() == null ? DateUtils.getNowDate() : arraignRoomListDto.getCheckDatetime();

            //结果装载时间点，返回时可以不装载，这里统一装载
            result.setCheckDatetime(checkDatetime);
            //查询条件限制时间点和房间
            arraignRoomListDto.setCheckDatetime(checkDatetime);
            arraignRoomListDto.setId(result.getId());

            // 获取结果
            List<ArraignRoomListDto> arraignRoomListDtos = arraignRoomMapper.selectRoomScheduleResult(arraignRoomListDto);

            //装载房间nowStatus和OngoingAppointmentResult
            if (arraignRoomListDtos.isEmpty()) {

                //该房间时间点下没有正在进行的提审时，OngoingAppointmentResult是空的
                result.setNowStatus(KeyConstant.RESOURCES_STATUS_NOW_FREE);
                result.setOngoingAppointmentResult(new OngoingAppointmentResult());
            } else {
                result.setNowStatus(KeyConstant.RESOURCES_STATUS_NOW_BUSY);
                OngoingAppointmentResult ongoingAppointmentResult = arraignRoomListDtos.get(0).getOngoingAppointmentResult();
                result.setOngoingAppointmentResult(ongoingAppointmentResult);
            }
        }
        return resultList;
    }

    @Override
    public List<RoomScheduleDto> getRoomScheduleArraign(String scheduleDate, String index , Long userId ){
        return getRoomScheduleArraign( scheduleDate,  index ,  userId , false);
    }

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.arraign.MeetingRoomScheduleDto>
     * @Author wangyong
     * @Description 获取提审时的可用时间点
     * @Date 9:30 2020-07-08
     * @Param [scheduleDate]
     */
    @Override
    public List<RoomScheduleDto> getRoomScheduleArraign(String scheduleDate, String index , Long userId , boolean isAdd) {

        List<RoomScheduleDto> listScheduleDto = new LinkedList<>();

        ArraignRoomSchedule selectRoomSchedule = new ArraignRoomSchedule();
        ArraignRoom selectRoom = new ArraignRoom();
        //排班查询条件：限定日期
        selectRoomSchedule.setScheduleDate(scheduleDate);
        //查询条件：房间可用
        selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
        selectRoom.setType(KeyConstant.ARENA_TYPE_ARRAIGN_ROOM);
        //获取所有可用的提审室
        List<ArraignRoom> listRoom = selectArraignRoomList(selectRoom);

        for (ArraignRoom room : listRoom) {

            RoomScheduleDto roomScheduleDto = new RoomScheduleDto();

            //获取提审室的时间段分布
            LinkedList<LinkedList<Date[]>> listRoomSchedule = arraignRoomScheduleService.getScheduleIntervalList(room, scheduleDate);
            LinkedList<Date[]> listScheduleAm = listRoomSchedule.get(0);
            LinkedList<Date[]> listSchedulePm = listRoomSchedule.get(1);

            selectRoomSchedule.setRoomId(room.getId());
            selectRoomSchedule.setScheduleDate(scheduleDate);
            //获取已占用的排班
            List<ArraignRoomSchedule> arraignRoomSchedules = arraignRoomScheduleService.selectArraignRoomScheduleList(selectRoomSchedule);
            LinkedHashMap<String, Boolean> mapScheduleAm = getRoomSchedule(listScheduleAm, arraignRoomSchedules, scheduleDate, room.getId(),isAdd);
            LinkedHashMap<String, Boolean> mapSchedulePm = getRoomSchedule(listSchedulePm, arraignRoomSchedules, scheduleDate, room.getId(),isAdd);
            List<ArraignAppointmentAddLog> logList = findLog(userId , DateUtil.parse(scheduleDate,"yyyy-MM-dd"),room.getId() );
            boolean amFlag = false;
            boolean pmFlag = false;
            if(logList!=null){
                for(ArraignAppointmentAddLog log:logList){
                    if("1".equals(log.getTimePie())){
                        amFlag = true;
                    }
                    if("2".equals(log.getTimePie())){
                        pmFlag = true;
                    }
                }
            }
            if(amFlag ){
                mapScheduleAm.put("+",true);
                mapScheduleAm.put("roomAvailable",true);
            }
            if(pmFlag){
                mapSchedulePm.put("+",true);
                mapSchedulePm.put("roomAvailable",true);
            }
            boolean roomVaailable = mapScheduleAm.get("roomAvailable") || mapSchedulePm.get("roomAvailable");
            mapScheduleAm.remove("roomAvailable");
            mapSchedulePm.remove("roomAvailable");
            roomScheduleDto.setId(room.getId());
            roomScheduleDto.setName(room.getName());
            roomScheduleDto.setScheduleAm(mapScheduleAm);
            roomScheduleDto.setSchedulePm(mapSchedulePm);
            roomScheduleDto.setRoomAvailable(roomVaailable);
            listScheduleDto.add(roomScheduleDto);
            // 第四天的处理办法
            /*if (StringUtils.equals(index,"4")){
                removeMapItem(mapScheduleAm);
                removeMapItem(mapSchedulePm);
            }*/
        }
        return listScheduleDto;
    }

    private List<ArraignAppointmentAddLog> findLog(Long userId,Date scheduleDate , String roomId){
        ArraignAppointmentAddLog query = new ArraignAppointmentAddLog();
        query.setUseStatus("0");
        query.setAppointmentDate(scheduleDate);
        query.setRoomId(roomId);
        query.setAppointmentUserId(userId);
        return arraignAppointmentAddLogMapper.selectArraignAppointmentAddLogList(query);
    }


    private void removeMapItem(LinkedHashMap<String, Boolean> mapSchedule) {
        Iterator<Map.Entry<String, Boolean>> it = mapSchedule.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Boolean> entry = it.next();
            if(entry.getValue()){
                it.remove();//使用迭代器的remove()方法删除元素
            }
        }
    }

    private LinkedHashMap<String, Boolean> getRoomSchedule(LinkedList<Date[]> listSchedule, List<ArraignRoomSchedule> arraignRoomSchedules, String scheduleDate, String roomId){
        return getRoomSchedule(listSchedule, arraignRoomSchedules, scheduleDate, roomId , false);
    }

    private LinkedHashMap<String, Boolean> getRoomSchedule(LinkedList<Date[]> listSchedule, List<ArraignRoomSchedule> arraignRoomSchedules, String scheduleDate, String roomId , boolean isAdd) {

        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

        LinkedHashMap<String, Boolean> resultMap = new LinkedHashMap();
        //遍历可用的时间段
        boolean roomAvailable = false;
//        // 重新查询房间使用情况
//        ArraignRoomSchedule room = new ArraignRoomSchedule();
//        room.setRoomId(roomId);
//        room.setScheduleDate(scheduleDate);
//        List<ArraignRoomSchedule> newRooms = arraignRoomScheduleService.roomUse(room);
        for (Date[] dates : listSchedule) {
            //yyyy-MM-dd HH:mm:ss
            Date startTime = dates[0];
            Date endTime = dates[1];
            //map的key
            String mapKey = UmapDateUtils.combine2Str(dates[0], dates[1]);
            //如果是加号则不用验证
//            if(isAdd){
//                boolean flag = true;
//                for(ArraignRoomSchedule e : newRooms){
//                    if(!("plus".equals(e.getRemark())))
//                        continue;
//                    if(startTime.equals(e.getStartTime())){
//                        resultMap.put(mapKey, false);
//                        flag = false;
//                        break;
//                    }
//                }
//                if(flag)
//                resultMap.put(mapKey, true);
//                roomAvailable = true;
//                continue;
//            }
            //roomId+date(yyyy-MM-dd)+time(HH=mm)
            String redisKey = roomId + scheduleDate + UmapDateUtils.parseDateToStr(UmapDateUtils.HH_MM, dates[0]);
            boolean redisNotLock = !redisTemplate.hasKey(redisKey);

            if (arraignRoomSchedules == null || arraignRoomSchedules.isEmpty()) {
                //没有排班，那只要别被redis锁就好
                resultMap.put(mapKey, redisNotLock);
                roomAvailable = redisNotLock;
                continue;
            }

            boolean itemAvailable = true;
            //遍历已预约的时间段
            for (ArraignRoomSchedule schedule : arraignRoomSchedules) {
                //yyyy-MM-dd HH:mm:ss
                Date scheduleStartTime = schedule.getStartTime();
                Date scheduleEndTime = schedule.getEndTime();


                //检查时间段的开始时间在这个已经预约的时间段的结束时间之后，那么他俩不冲突
                if (startTime.equals(scheduleEndTime) || startTime.after(scheduleEndTime)) {
                    continue;
                }
                //同上
                if (endTime.equals(scheduleStartTime) || endTime.before(scheduleStartTime)) {
                    continue;
                }

                //排除以上两种情况，两个时间段必然相交
                itemAvailable = false;
                break;
            }

            //只要有一次为true，那房间可用为true
            if (itemAvailable & redisNotLock) {
                roomAvailable = true;
            }
            resultMap.put(mapKey, itemAvailable & redisNotLock);
        }
        resultMap.put("roomAvailable", roomAvailable);
        return resultMap;
    }


    @Override
    public RoomAppointmentDto getRoomAppointment(RoomDetailParam roomDetailParam) {

        //RoomAppointmentDto roomAppointmentDto = new RoomAppointmentDto();

        RoomAppointmentDto roomAppointmentDto = transObject(roomDetailParam, RoomAppointmentDto.class);

        String type = roomDetailParam.getType();
        Long id = roomDetailParam.getId();

        Object eventDetailDto = null;

        if (type.equals(AuditRecordTypeEnum.ArraignAudit.getValue())) {
            AppointmentDetailDto appointmentDetail = arraignAppointmentService.getAppointmentDetailById(id);

            SysUser user = userService.selectUserById(appointmentDetail.getProsecutorUserId());
            if (BeanUtil.isNotEmpty(user)) {
                appointmentDetail.setDept(user.getDept().getDeptName());
            }

            eventDetailDto = appointmentDetail;

        } else if (type.equals(AuditRecordTypeEnum.SpecialCaseAudit.getValue())) {
            eventDetailDto = specialCaseAppointmentService.selectSpecialCaseDetailById(id);
            ((SpecialCaseDetailDto) eventDetailDto).setProsecutorName(((SpecialCaseDetailDto) eventDetailDto).getUndertaker());
        }

        roomAppointmentDto.setEventDetailDto(eventDetailDto);
        return roomAppointmentDto;
    }

    /**
     * @param receptionParam
     * @return java.util.LinkedList<com.mkst.umap.app.admin.dto.reception.RoomInfoDto>
     * @Author wangyong
     * @Description 获取接待室的相关信息
     * @Date 19:13 2020-07-08
     * @Param [roomListParam]
     */
    @Override
    public LinkedList<RoomInfoDto> getReceptionRoomList(ReceptionParam receptionParam) {

        receptionParam.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);

        //result
        LinkedList<RoomInfoDto> roomInfoDtos = new LinkedList<>();

        //查询所有接待室
        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(KeyConstant.ARENA_TYPE_RECEPTION_RECEPTION_ROOM);
        selectRoom.setId(receptionParam.getRoomId());
        List<ArraignRoom> arraignRooms = arraignRoomMapper.selectArraignRoomList(selectRoom);

        //遍历接待室
        for (ArraignRoom arraignRoom : arraignRooms) {


            RoomInfoDto roomInfoDto = new RoomInfoDto();
            roomInfoDto.setName(arraignRoom.getName());
            roomInfoDto.setAddress(arraignRoom.getAddress());

            receptionParam.setRoomId(arraignRoom.getId());

            LinkedList<ReceptionInfoResult> receptionInfoResultList = receptionAppointmentMapper.getReceptionInfoResultList(receptionParam);

            setNowStatus(receptionInfoResultList);

            if (receptionInfoResultList == null) {
                receptionInfoResultList = new LinkedList<>();
            }
            receptionInfoResultList.stream().forEach(info ->{
                info.setTypeName(dictDataService.selectDictLabel("reception_type", info.getType()));
            });
            roomInfoDto.setOngoingAppointmentResult(receptionInfoResultList);

            roomInfoDtos.add(roomInfoDto);
        }
        return roomInfoDtos;
    }

    private void setNowStatus(LinkedList<ReceptionInfoResult> receptionInfoResultList) {

        for (ReceptionInfoResult receptionInfoResult : receptionInfoResultList) {

            Date nowDate = DateUtils.getNowDate();

            Date startTime = receptionInfoResult.getStartTime();
            Date endTime = receptionInfoResult.getEndTime();

            if (nowDate.after(endTime) || nowDate.equals(endTime)) {
                receptionInfoResult.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
                continue;
            }

            if (nowDate.before(startTime)) {
                receptionInfoResult.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
                continue;
            }

            receptionInfoResult.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
        }
    }


    private <T> List<T> transList(List<?> list, Class<T> clazz) {
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

    public <T> T transObject(Object ob, Class<T> clazz) {
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

    @Override
    public List<CanteenManageDto> getRoomCanteen(String date){
        List<CanteenManageDto> dtoList = new LinkedList<>();
        //获取所有可用状态的包厢
        ArraignRoom selectRoom = new ArraignRoom();
        //查询条件：房间可用
        selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
        selectRoom.setType(RoomTypeEnum.CANTEEN_ROOM.getValue());

        List<ArraignRoom> roomList = selectArraignRoomList(selectRoom);

        //通过传入日期搜索当天包厢申请情况
        for(ArraignRoom room : roomList){
            CanteenManageDto dto = new CanteenManageDto();

            //获取所有餐次
            BoxMeal boxMeal = new BoxMeal();
            boxMeal.setBoxId(room.getId());
            List<BoxMeal> boxMeals = boxMealService.selectBoxMealList(boxMeal);

            Map<String,Object> selectMap = new HashMap<>();
            selectMap.put("dateTime",UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date));
            selectMap.put("boxId",room.getId());
            List<CanteenManage> list = canteenManageService.selectCanteenListByBoxId(selectMap);

            //LinkedHashMap<Object, Object> map = getMealRoom(list, boxMeals, date, room.getId());

            List<Map<String,Object>> resultList = getBoxMeal(list,boxMeals,date);
            dto.setId(room.getId());
            dto.setName(room.getName());
            dto.setTotal(room.getGalleryful());
            dto.setUseStatus(resultList);
            dto.setAddress(room.getAddress());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private List<Map<String,Object>> getBoxMeal(List<CanteenManage> list,List<BoxMeal> boxMeals,String date){
        try{
            List<Map<String,Object>> resultList = new LinkedList<>();
            //转换提日期输出格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //如果list为空，则所有餐次都可选
            if(CollUtil.isEmpty(new ArrayList<>())){
                for(BoxMeal boxMeal : boxMeals){
                    //如果当前时间超过餐次结束时间 则不能预约
                    Date now = new Date();
                    String nowDay = dateFormat.format(now);
                    if(date.equals(nowDay.split(" ")[0])){
                        String endTime = date+" "+boxMeal.getEndTime();
                        Date end = dateFormat.parse(endTime);
                        if(now.after(end)){
                            continue;
                        }
                    }

                    Map<String,Object> map = new HashMap<>();
                    map.put("value",boxMeal.getId());
                    map.put("label",boxMeal.getTypeName());
                    resultList.add(map);
                }
            }else {

                cc: for(BoxMeal boxMeal : boxMeals) {
                    Long id = 0L;
                    for (CanteenManage canteenManage : list){
                        //防止同一天 同一个餐次的错误数据
                        if(id.equals(canteenManage.getBoxMealId())) {
                            break;
                        } else {
                            id = canteenManage.getBoxMealId();
                        }
                        //因为每天同一个餐次同房间只能预约一次，所以匹配到一次直接跳出不用再遍历
                        /*if (boxMeal.getId().equals(canteenManage.getBoxMealId())) {
                            continue cc;
                        }*/
                    }
                    Date now = new Date();
                    String nowDay = dateFormat.format(now);
                    if(date.equals(nowDay.split(" ")[0])){
                        String endTime = date+" "+boxMeal.getEndTime();
                        Date end = dateFormat.parse(endTime);
                        if(now.after(end)){
                            continue;
                        }
                    }
                    Map<String,Object> map = new HashMap<>();
                    map.put("value",boxMeal.getId());
                    map.put("label",boxMeal.getTypeName());
                    resultList.add(map);
                }
            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    private LinkedHashMap<Object, Object> getMealRoom(List<CanteenManage> list,List<BoxMeal> boxMeals,String roomId, String date){
        RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

        LinkedHashMap<Object, Object> resultMap = new LinkedHashMap();
        //遍历可用的时间段
        boolean roomAvailable = false;
        for (BoxMeal boxMeal : boxMeals) {
            //map的key
            String key = boxMeal.getId().toString();

            //roomId+date(yyyy-MM-dd)+meal
            String redisKey = roomId + date + key;
            boolean redisNotLock = !redisTemplate.hasKey(redisKey);

            if (CollUtil.isEmpty(list)) {
                //没有排班，那只要别被redis锁就好
                BoxMealParam param = new BoxMealParam();
                param.setBoxMeal(boxMeal);
                param.setStatus(redisNotLock);
                resultMap.put(key, param);
                roomAvailable = redisNotLock;
                continue;
            }

            boolean itemAvailable = true;
            //遍历已有申请
            for (CanteenManage canteenManage : list) {
                //因为每天同一个餐次同房间只能预约一次，所以匹配到一次直接跳出不用再遍历
                if (key.equals(canteenManage.getBoxMealId().toString())){
                    itemAvailable = false;
                    break;
                }
            }

            //只要有一次为true，那房间可用为true
            if (itemAvailable & redisNotLock) {
                roomAvailable = true;
            }
            BoxMealParam param = new BoxMealParam();
            param.setBoxMeal(boxMeal);
            param.setStatus(itemAvailable & redisNotLock);
            resultMap.put(key, param);
        }
        resultMap.put("roomAvailable", roomAvailable);
        return resultMap;
    }
}
