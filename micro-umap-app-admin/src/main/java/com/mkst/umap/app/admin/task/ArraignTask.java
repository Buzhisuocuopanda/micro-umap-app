package com.mkst.umap.app.admin.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.ArraignAppointment;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;
import com.mkst.umap.app.admin.service.IArraignRoomScheduleService;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * @ClassName ArraignTask
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-10-10 16:21
 */
@Component("ArraignTask")
public class ArraignTask {

    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IArraignRoomService roomService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IArraignRoomScheduleService scheduleService;
    @Autowired
    private IArraignAppointmentService arraignService;

    public void doubleBoxArraign(){
        String findUrl = configService.selectConfigByKey("doublebox_arraign_find_url");
        JSONObject arraignJson = sendPost(findUrl, new JSONObject());
        // 请求失败或请求异常
        if (arraignJson.size() == 0 || arraignJson.getIntValue("code") != 0){
            return;
        }

        // 无更新数据
        JSONArray dataArr = arraignJson.getJSONArray("data");
        if (dataArr.size() == 0){
            return;
        }

        dataArr.subList(0,dataArr.size()).stream().forEach(obj ->{
            if (!(obj instanceof JSONObject)){
                return;
            }
            dealArraign(((JSONObject) obj));
        });
    }

    public void dealArraign(JSONObject obj) {
        String callbackUrl = configService.selectConfigByKey("doublebox_arraign_callback_url");
        SysUser user = userService.selectUserById(obj.getLong("promoter"));
        if (BeanUtil.isEmpty(user)){
            return;
        }
        // 初始化数据
        ArraignAppointment baseArraign = new ArraignAppointment();
        baseArraign.setCreateBy(user.getLoginName());
        baseArraign.setProsecutorName(user.getUserName());
        baseArraign.setProsecutorUserId(user.getUserId());

        List<ArraignRoom> roomList = roomService.selectRoomByType(KeyConstant.ARENA_TYPE_ARRAIGN_ROOM);

        for (ArraignRoom room : roomList){
            baseArraign.setRoomId(room.getId());
            Date futureForthDate = UmapDateUtils.getFutureDate(4);
            LinkedList<LinkedList<Date[]>> scheduleIntervalList = scheduleService.getScheduleIntervalList(room, UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD, futureForthDate));
            for (LinkedList<Date[]> sc : scheduleIntervalList){
                for (Date[] dates : sc){
                    ArraignAppointment hereArraign = new ArraignAppointment();
                    BeanUtil.copyProperties(baseArraign,hereArraign);
                    hereArraign.setStartTime(dates[0]);
                    hereArraign.setEndTime(dates[1]);
                    int i = addSaveArraign(hereArraign);
                    if (i > 0){
                        JSONObject callbackJson = new JSONObject();
                        callbackJson.put("id",obj.getString("id"));
                        sendPost(callbackUrl,callbackJson);
                        return;
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int addSaveArraign(ArraignAppointment hereArraign) {
        //装载criminalName、criminalType、criminalBirth、prosecutorId、prosecutorName
        ArraignAppointment appointment = transObject(hereArraign, ArraignAppointment.class);
        ArraignRoomSchedule schedule = transObject(appointment, ArraignRoomSchedule.class);
        //若排班已存在
        if (!checkScheduleNotExist(schedule)) {
            return 0;
        }
        schedule.setCreateBy(hereArraign.getCreateBy());
        scheduleService.insertArraignRoomSchedule(schedule);
        appointment.setScheduleId(schedule.getId());
        appointment.setCreateBy(hereArraign.getCreateBy());
        appointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
        return arraignService.insertArraignAppointment(appointment);
    }

    private boolean checkScheduleNotExist(ArraignRoomSchedule schedule) {
        ArraignRoomSchedule selectSchedule = new ArraignRoomSchedule();
        selectSchedule.setRoomId(schedule.getRoomId());
        selectSchedule.setStartTime(schedule.getStartTime());
        List<ArraignRoomSchedule> arraignRoomSchedules = scheduleService.selectArraignRoomScheduleList(selectSchedule);
        //为空就是不存在
        return arraignRoomSchedules.isEmpty();
    }

    /**
     * @return java.lang.String
     * @Author wangyong
     * @Description 封装sign发送申请
     * @Date 18:07 2020-09-09
     * @Param [url, reqParamMap]
     */
    private JSONObject sendPost(String url, JSONObject reqParamJson) {
        String doubleBoxAppId = configService.selectConfigByKey("doublebox_appid");
        String doubleBoxSecret = configService.selectConfigByKey("doublebox_secret");
        long t = System.currentTimeMillis();
        StringBuilder query = new StringBuilder();
        query.append("appId").append(doubleBoxAppId);
        query.append("t").append(t);
        query.append(doubleBoxSecret);
        String sign = SecureUtil.md5(query.toString());
        url += "?appId=" + doubleBoxAppId + "&t=" + t + "&sign=" + sign;
        String responseStr = HttpUtil.createPost(url).body(reqParamJson.toJSONString(), "application/json").execute().body();
        if (StringUtils.isEmpty(responseStr)){
            return new JSONObject();
        }
        return JSON.parseObject(responseStr);
    }

    public <T> T transObject(Object ob, Class<T> clazz) {
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

    /**
     * 跳过结束
     */
    public void modifyFinishStatus(){

        arraignService.modifyFinishStatus();

    }

}
