package com.mkst.umap.app.admin.api;

import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionDetailResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import com.mkst.umap.app.admin.domain.ReceptionAppointment;
import com.mkst.umap.app.admin.dto.arraign.DayStatusDto;
import com.mkst.umap.app.admin.dto.reception.ReceptionScheduleInfoDto;
import com.mkst.umap.app.admin.dto.reception.RoomInfoDto;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IReceptionAppointmentService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ReceptionApi
 * @Description 接待服务接口
 * @Author wangyong
 * @Date 2020-07-08 17:56
 */
@Slf4j
@Api("接待服务接口")
@RestController
@RequestMapping("/api/reception")
public class ReceptionApi extends BaseApi {

    @Autowired
    private IReceptionAppointmentService receptionAppointmentService;
    @Autowired
    private IArraignRoomService roomService;

    @ApiLog(title = "添加接待预约申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/addSave")
    @ApiOperation("添加接待预约申请")
    @Login
    public Result addSave(HttpServletRequest request, @RequestBody @ApiParam(name = "receptionParam", value = "接待申请信息", required = true) ReceptionParam receptionParam) {
        try {
            if (receptionAppointmentService.checkIsOccupied(receptionParam)) {
                return ResultGenerator.genSuccessResult("您选择的时间已被其他人抢占，请刷新并重新选择时间！");
            }
            ReceptionAppointment receptionAppointment = new ReceptionAppointment();
            //装载roomId、startTime、endTime、deptId、type
            BeanUtils.copyProperties(receptionParam, receptionAppointment);
            //装载createBy
            receptionAppointment.setCreateBy(getLoginName(request));

            int row = receptionAppointmentService.insertReceptionAppointment(receptionAppointment);

            return row > 0 ? ResultGenerator.genSuccessResult("新增接待申请成功") : ResultGenerator.genFailResult("插入失败，请联系管理员或稍后重试！！！");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("插入失败，请联系管理员或稍后重试！！！");
        }
    }

    @ApiLog(title = "接待室日期接口", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/dayStatusList")
    @ApiOperation("接待室日期接口")
    public Result dayStatusList() {

        List<DayStatusDto> listDayStatus = new ArrayList<>();
        ArrayList<Date> fetureDaysList = UmapDateUtils.getFutureDaysList(0, 5);

        for (Date date : fetureDaysList) {
            DayStatusDto dayStatusDto = new DayStatusDto();
            dayStatusDto.setDate(date);
            dayStatusDto.setStatus(KeyConstant.ARRAIGN_ROOM_FULL_NO);
            String today = UmapDateUtils.isToday(date) ? "今天" : UmapDateUtils.getWeekOfDate(date);
            dayStatusDto.setWeekDay(today);

            listDayStatus.add(dayStatusDto);
        }

        return ResultGenerator.genSuccessResult("查询成功", listDayStatus);
    }

    @ApiLog(title = "获取接待室使用情况", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/roomList")
    @ApiOperation("接待室使用情况")
    @Login
    public Result roomList(@RequestBody @ApiParam(name = "receptionParam", value = "接待室的请求参数", required = true) ReceptionParam receptionParam) {

        startPage();
        LinkedList<RoomInfoDto> roomInfoDtos = roomService.getReceptionRoomList(receptionParam);
        if (roomInfoDtos == null || roomInfoDtos.isEmpty()) {
            ResultGenerator.genSuccessResult("当前无可用的接待室，请联系管理员或稍后重试！");
        }
        return ResultGenerator.genSuccessResult("查询成功", roomInfoDtos);
    }

    @ApiLog(title = "获取接待申请详情", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/receptionDetail")
    @ApiOperation("接待申请详情")
    @Login
    public Result receptionDetail(@RequestBody @ApiParam(name = "receptionParam", value = "提审信息的请求参数", required = true) ReceptionParam receptionParam) {
        ReceptionDetailResult receptionDetail = receptionAppointmentService.getReceptionDetailById(receptionParam.getId());
        return ResultGenerator.genSuccessResult("查询成功", receptionDetail);
    }

    @ApiLog(title = "接待申请-我的申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/myReceptionList")
    @ApiOperation("接待申请-我的申请")
    @Login
    public Result myReceptionList(HttpServletRequest request, @RequestBody @ApiParam(name = "receptionParam", value = "提审信息的请求参数", required = true) ReceptionParam receptionParam) {

        //nowStatus   +useBy、status
        receptionParam.setCreateBy(getLoginName(request));
        receptionParam.setUseBy(getUserId(request));
        // startPage(); 
        LinkedList<ReceptionInfoResult> receptionInfoResults = receptionAppointmentService.getMyReceptionList(receptionParam);
        return ResultGenerator.genSuccessResult("查询成功", receptionInfoResults);
    }

    @ApiLog(title = "取消接待申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/cancelReception")
    @ApiOperation("接待申请-我的申请-取消申请")
    @Login
    public Result cancelReception(HttpServletRequest request, @RequestBody @ApiParam(name = "receptionParam", value = "取消接待申请的参数", required = true) ReceptionParam receptionParam) {

        ReceptionAppointment receptionAppointment = transObject(receptionParam, ReceptionAppointment.class);
        receptionAppointment.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
        receptionAppointment.setUpdateBy(getLoginName(request));
        int row = receptionAppointmentService.updateReceptionAppointment(receptionAppointment);

        return row > 0 ? ResultGenerator.genSuccessResult("取消成功") : ResultGenerator.genFailResult("取消失败，请联系管理员或稍后重试。");
    }

    @ApiLog(title = "添加接待申请", ApiOperatorType = ApiOperatorType.POST)
    @PostMapping("/roomSchedule")
    @ApiOperation("接待申请-添加申请")
    @Login
    public Result roomSchedule(HttpServletRequest request, @RequestBody @ApiParam(name = "receptionParam", value = "取消接待申请的参数", required = true) ReceptionParam receptionParam) {

        LinkedList<ReceptionScheduleInfoDto> scheduleInfoDtos = receptionAppointmentService.getSchedule(receptionParam);
        return ResultGenerator.genSuccessResult("查询成功", scheduleInfoDtos);
    }

    @PostMapping("/getEndTime")
    @ApiOperation("点击开始时间后，获取结束时间")
    @Login
    public Result getEndTime(HttpServletRequest request, @RequestBody @ApiParam(name = "receptionParam", value = "取消接待申请的参数", required = true) ReceptionParam receptionParam) {
        Date nextStartTime = receptionAppointmentService.getNextStartTime(receptionParam);
        if (nextStartTime == null) {
            return ResultGenerator.genSuccessResult("查询成功");
        }
        return ResultGenerator.genSuccessResult("查询成功", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, nextStartTime));
    }

    @Login
    @ApiOperation(value = "依据日期获取会议申请数量")
    @PostMapping(value = "/dayCount")
    public Result dayCount(@RequestBody @ApiParam ReceptionParam param){
        startPage();
        List<NameCountResult> nameCountResults = receptionAppointmentService.selectDayCount(param);
        return ResultGenerator.genSuccessResult("查询成功",nameCountResults);
    }

    @ApiLog(title = "时间排序-申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-申请info")
    @PostMapping("/timeApplyList")
    @Login
    public Result timeApplyList(@RequestBody @ApiParam ReceptionParam param){
        // 只查询未取消的
        param.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        return ResultGenerator.genSuccessResult("success",receptionAppointmentService.selectTimeApplyList(param));
    }

    @ApiLog(title = "时间排序-我的申请info",ApiOperatorType = ApiOperatorType.POST)
    @ApiOperation(value = "时间排序-我的申请info")
    @PostMapping("/myTimeApplyList")
    @Login
    public Result myTimeApplyList(HttpServletRequest request,@RequestBody @ApiParam ReceptionParam param){
        // 查询我申请的或者使用人是我的会议
        param.setNowUserId(getUserId(request));
        param.setNowUserLoginName(getLoginName(request));
        return ResultGenerator.genSuccessResult("success",receptionAppointmentService.selectTimeApplyList(param));
    }

}
