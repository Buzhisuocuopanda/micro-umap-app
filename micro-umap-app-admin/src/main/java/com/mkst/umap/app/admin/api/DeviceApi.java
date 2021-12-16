package com.mkst.umap.app.admin.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.umap.app.admin.api.bean.param.device.MeetingDeviceParam;
import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.controller.ArraignRoomController;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.RoomEquipment;
import com.mkst.umap.app.admin.dto.device.DRoomInfoDto;
import com.mkst.umap.app.admin.dto.device.DeviceInfoDto;
import com.mkst.umap.app.admin.dto.device.MeetingDeviceInfoDto;
import com.mkst.umap.app.admin.dto.device.RoomDeviceDto;
import com.mkst.umap.app.admin.dto.reception.RoomInfoDto;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IMeetingService;
import com.mkst.umap.app.admin.service.IRoomEquipmentService;
import com.mkst.umap.app.common.enums.RoomTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DeviceApi
 * @Description 设备相关api
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-04 16:05
 */
@Slf4j
@Api("远程视频提审服务接口")
@RestController
@RequestMapping("/api/device")
public class DeviceApi extends BaseApi {

    @Autowired
    private IMeetingService meetingService;

    @Autowired
    private IArraignRoomService roomService;

    @Autowired
    private IRoomEquipmentService equipmentService;

    @Autowired
    private ISysConfigService configService;

    /** 拿来测试api的
     * @see ArraignRoomController#screenList()
     * */
    /*@ApiLog(title = "查询会议室大屏列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/screenList")
    @ApiOperation("查询会议室大屏列表")
    @ResponseBody
    public List<Equipment> screenList() {

        Equipment selectEquipment = new Equipment();
        selectEquipment.setDevType("9");
        List<Equipment> list = equipmentService.selectRemoteEquipmentList(selectEquipment);
        return list;
    }*/

    @PostMapping("/directSoftware")
    @ApiOperation("智能投屏软件指向")
    @ResponseBody
    public Result directSoftware(){
        String package_name_android = configService.selectConfigByKey("device_cast_package_android");
        String package_name_ios = configService.selectConfigByKey("device_cast_package_ios");
        Map<String, String> map = new HashMap();
        map.put("pname",package_name_android);
        map.put("scheme",package_name_ios);
        return ResultGenerator.genSuccessResult("成功",map);
    }

    @ApiLog(title = "获取当前时间点的会议信息", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getMeetingInfo")
    @ApiOperation("所有提审室当前时间点信息")
    @ResponseBody
    public Result getMeetingInfo(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        MeetingDeviceInfoDto meetingDeviceInfo = meetingService.getMeetingInfo(meetingDeviceParam);
        return ResultGenerator.genSuccessResult("success", meetingDeviceInfo);
    }

    @ApiLog(title = "新增一个设备", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/deviceAddSave")
    @ApiOperation("新增一个设备")
    @ResponseBody
    public Result deviceAddSave(HttpServletRequest request, @RequestBody Map<String, String> map) {

        String url = "/api/device/deviceAddSave";
        String responseStr = sendPost(url, map);
        JSONObject oldData = JSON.parseObject(responseStr).getJSONObject("data");

        return ResultGenerator.genSuccessResult("success", transObject(oldData, DeviceInfoDto.class));
    }


    @ApiLog(title = "获取会议室列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getMeetingRoomList")
    @ApiOperation("获取会议室列表")
    @ResponseBody
    public Result getMeetingRoomList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        List<DRoomInfoDto> result = new ArrayList<>();

        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());

        List<ArraignRoom> arraignRooms = roomService.selectArraignRoomList(selectRoom);
        if (CollectionUtil.isNotEmpty(arraignRooms)) {
            result = transList(arraignRooms, DRoomInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("success", result);
    }

    @ApiLog(title = "获取房间列表", ApiOperatorType = ApiOperatorType.POST)
    @GetMapping("/getRoomList")
    @ApiOperation("获取房间列表")
    @ResponseBody
    public Result getRoomList(@RequestParam(required = true,value = "type") String type) {

        List<DRoomInfoDto> result = new ArrayList<>();

        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(type);

        List<ArraignRoom> arraignRooms = roomService.selectArraignRoomList(selectRoom);
        if (CollectionUtil.isNotEmpty(arraignRooms)) {
            result = transList(arraignRooms, DRoomInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("success", result);
    }

    /*@ApiLog(title = "获取会议室列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getRoomList")
    @ApiOperation("获取会议室列表")
    @ResponseBody
    public Result getRoomList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        List<DRoomInfoDto> result = new ArrayList<>();

        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());

        List<ArraignRoom> arraignRooms = roomService.selectArraignRoomList(selectRoom);
        if (CollectionUtil.isNotEmpty(arraignRooms)) {
            result = transList(arraignRooms, DRoomInfoDto.class);
        }

        return ResultGenerator.genSuccessResult("success", result);
    }*/

    @ApiLog(title = "获取房间设备列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getRoomDeviceList")
    @ApiOperation("获取房间设备列表")
    @ResponseBody
    public Result getRoomDeviceList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        String roomId = meetingDeviceParam.getRoomId();
        RoomEquipment selectRoomEquipment = new RoomEquipment();
        selectRoomEquipment.setRoomId(roomId);
        List<RoomEquipment> roomList = equipmentService.selectRoomEquipmentList(selectRoomEquipment);

        if (CollectionUtil.isNotEmpty(roomList)) {
            List<RoomDeviceDto> roomDeviceDtos = transList(roomList, RoomDeviceDto.class);
            return ResultGenerator.genSuccessResult("success", roomDeviceDtos);
        }

        return ResultGenerator.genSuccessResult("success");
    }

    @ApiLog(title = "获取设备所在房间", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getDeviceRoomList")
    @ApiOperation("获取设备所在房间")
    @ResponseBody
    public Result getDeviceRoomList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        // 设备传来的是自己的唯一标识
        String equipmentId = meetingDeviceParam.getEquipmentId();
        RoomEquipment selectRoomEquipment = new RoomEquipment();
        selectRoomEquipment.setUniqueId(equipmentId);
        List<RoomEquipment> roomList = equipmentService.selectRoomEquipmentList(selectRoomEquipment);

        if (CollectionUtil.isNotEmpty(roomList)) {
            List<RoomDeviceDto> roomDeviceDtos = transList(roomList, RoomDeviceDto.class);
            return ResultGenerator.genSuccessResult("success", roomDeviceDtos);
        }

        return ResultGenerator.genSuccessResult("success");
    }

    @ApiLog(title = "获取设备分组列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getDeviceGroupList")
    @ApiOperation("获取设备分组列表")
    @ResponseBody
    public Result getDeviceGroupList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("parentGroupName",meetingDeviceParam.getParentGroupName());
        map.put("parentId",meetingDeviceParam.getParentId());

        String url = "/api/device/getDeviceGroupList";
        String responseStr = sendPost(url, map);
        String oldData = JSON.parseObject(responseStr).getString("data");

        return ResultGenerator.genSuccessResult("success", JSON.parseArray(oldData));
    }

    @ApiLog(title = "获取设备列表", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getRemoteDeviceList")
    @ApiOperation("获取设备列表")
    @ResponseBody
    public Result getRemoteDeviceList(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("groupId",meetingDeviceParam.getGroupId());
        map.put("devType",meetingDeviceParam.getDevType());

        String url = "/api/device/getDeviceList";
        String responseStr = sendPost(url, map);
        String oldData = JSON.parseObject(responseStr).getString("data");

        return ResultGenerator.genSuccessResult("success", JSON.parseArray(oldData));
    }

    @ApiLog(title = "获取设备状态", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getRemoteDeviceStatus")
    @ApiOperation("获取设备状态")
    @ResponseBody
    public Result getRemoteDeviceStatus(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam) {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("groupId",meetingDeviceParam.getGroupId());
        map.put("devType",meetingDeviceParam.getDevType());

        HashMap<Object, Object> headMap = new HashMap<>();

        String url = "/api/device/getDeviceList";
        String responseStr = sendPost(url, map);
        String oldData = JSON.parseObject(responseStr).getString("data");

        return ResultGenerator.genSuccessResult("success", JSON.parseArray(oldData));
    }

    @ApiLog(title = "获取空调状态", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/getConditionStatus")
    @ApiOperation("获取空调状态")
    public Result getConditionStatus(@RequestBody @ApiParam JSONObject dataJson){

        String url = "/device/propertie";
        String responseStr = sendPostToJinZheng(url, dataJson.toJSONString());
        JSONObject result = JSON.parseObject(responseStr);
        if(!"0".equals(result.getString("code"))){
            return ResultGenerator.genFailResult("当前设备网络不稳定，请稍后重新尝试");
        }
        JSONArray propertyList = result.getJSONObject("body").getJSONArray("propertyModelList");
        HashMap<String, String> resultMap = new HashMap<>();
        propertyList.stream().forEach(pro ->{
            if (!(pro instanceof JSON)){
                return;
            }
            resultMap.put(((JSONObject) pro).getString("identifier"),((JSONObject) pro).getString("value"));
        });
        return ResultGenerator.genSuccessResult("success",resultMap);
    }

    @ApiLog(title = "设备控制", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/deviceControl")
    @ApiOperation("设备控制")
    @ResponseBody
    public Result deviceControl(@RequestBody @ApiParam JSONArray jsonObject) {

        String url = "/device/mqCtrlDevice";
        String responseStr = sendPostToJinZheng(url, jsonObject.toJSONString());
        JSONObject result = JSON.parseObject(responseStr);
        if(result.getString("code").equals("0")){
            return ResultGenerator.genSuccessResult("success");
        }
        return ResultGenerator.genFailResult("当前设备网络不稳定，请稍后重新尝试");
    }

    @ApiLog(title = "改变房间灯光模式",ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/changeLightMode")
    @ApiOperation("改变房间灯光模式")
    public Result changeLightMode(@RequestBody @ApiParam MeetingDeviceParam meetingDeviceParam){

        HashMap<Object, Object> map = new HashMap<>();
        map.put("groupId",meetingDeviceParam.getGroupId());
        map.put("devType","light");

        String url = "/api/device/getDeviceList";
        String responseStr = sendPost(url, map);
        JSONArray data = JSON.parseObject(responseStr).getJSONArray("data");
        if (data == null ||data.size() == 0){
            return ResultGenerator.genSuccessResult("success");
        }

        String mode = meetingDeviceParam.getMode();
        if (StringUtils.isEmpty(mode)){
            return ResultGenerator.genFailResult("灯光模式选择错误");
        }

        JSONArray sendData = new JSONArray();
        data.stream().forEach(devObj ->{
            if (!(devObj instanceof JSON)){
                return;
            }
            JSONObject devJson = (JSONObject) devObj;
            Integer lightStatus = new Integer(1);
            if (StringUtils.equals(mode,"warm") && devJson.getString("devName").contains("筒灯")){
                lightStatus = 0;
            }
            JSONObject nodeJsonData = initNodeJsonData(devJson.getString("serialNo"),lightStatus);
            sendData.add(nodeJsonData);
        });

        String ctrlUrl = "/device/mqCtrlDevice";
        String ctrResponseStr = sendPostToJinZheng(ctrlUrl, sendData.toJSONString());
        System.out.println(sendData.toJSONString());
        JSONObject result = JSON.parseObject(ctrResponseStr);
        if(result.getString("code").equals("0")){
            return ResultGenerator.genSuccessResult("success");
        }
        return ResultGenerator.genFailResult("当前设备网络不稳定，请稍后重新尝试");
    }

    private JSONObject initNodeJsonData(String serialNo, Integer lightStatus) {

        JSONObject result = new JSONObject();
        result.put("deviceId",serialNo);

        JSONArray properties = new JSONArray();
        JSONObject pro = new JSONObject();
        pro.put("identifier","CtrlChStatus");
        pro.put("value",lightStatus);

        properties.add(pro);
        result.put("properties",properties);

        return result;
    }


    @ApiLog(title = "获取接待室使用情况", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/receptionRoomList")
    @ApiOperation("接待室使用情况")
    public Result receptionRoomList(@RequestBody @ApiParam(name = "receptionParam", value = "接待室的请求参数", required = true) ReceptionParam receptionParam) {

        startPage();
        LinkedList<RoomInfoDto> roomInfoDtos = roomService.getReceptionRoomList(receptionParam);
        if (roomInfoDtos == null || roomInfoDtos.isEmpty()) {
            ResultGenerator.genSuccessResult("当前无可用的接待室，请联系管理员或稍后重试！");
        }
        return ResultGenerator.genSuccessResult("查询成功", roomInfoDtos);
    }

    /**
     * @return java.lang.String
     * @Author wangyong
     * @Description 封装sign发送申请
     * @Date 18:07 2020-09-09
     * @Param [url, reqParamMap]
     */
    private String sendPost(String url, Map reqParamMap) {
        String webCardUrl = configService.selectConfigByKey("webcard_url");
        String webCardAppId = configService.selectConfigByKey("webcard_appid");
        String webCardSecret = configService.selectConfigByKey("webcard_secret");
        url = webCardUrl + url;
        long t = System.currentTimeMillis();
        StringBuilder query = new StringBuilder();
        query.append("appId").append(webCardAppId);
        query.append("t").append(t);
        query.append(webCardSecret);
        String sign = SecureUtil.md5(query.toString());
        url += "?appId=" + webCardAppId + "&t=" + t + "&sign=" + sign;
        String responseStr = HttpUtil.createPost(url).body(JSON.toJSONString(reqParamMap), "application/json").execute().body();
        return responseStr;
    }

    private String sendPostToJinZheng(String url, String content){

        String jinZhengCardUrl = configService.selectConfigByKey("jinzheng_url");
        String jinZhengSign = configService.selectConfigByKey("jinzheng_sign");
        String jinZhengAppKey = configService.selectConfigByKey("jinzheng_appKey");

        url = jinZhengCardUrl + url;

        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("_sign",jinZhengSign);
        headMap.put("appKey",jinZhengAppKey);
        log.info(content);
        return HttpUtil.createPost(url).body(content, "application/json")
                .header("_sign",jinZhengSign).header("appKey",jinZhengAppKey)
                .execute().body();
    }
}
