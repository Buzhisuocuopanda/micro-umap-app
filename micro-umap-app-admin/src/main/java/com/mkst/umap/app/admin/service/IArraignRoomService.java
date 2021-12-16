package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.RoomDetailParam;
import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.dto.arraign.ArraignRoomListDto;
import com.mkst.umap.app.admin.dto.arraign.CanteenManageDto;
import com.mkst.umap.app.admin.dto.arraign.CountDto;
import com.mkst.umap.app.admin.dto.arraign.RoomScheduleDto;
import com.mkst.umap.app.admin.dto.reception.RoomInfoDto;
import com.mkst.umap.app.admin.dto.room.RoomAppointmentDto;

import java.util.LinkedList;
import java.util.List;

/**
 * 提审室 服务层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface IArraignRoomService {
    /**
     * 查询提审室信息
     *
     * @param id 提审室ID
     * @return 提审室信息
     */
    public ArraignRoom selectArraignRoomById(String id);

    /**
     * 查询近30天提审室预约情况
     * @return
     */
    List<CountDto> selectRoomType0And30Day();
    /**
     * 查询提审室列表
     *
     * @param arraignRoom 提审室信息
     * @return 提审室集合
     */
    public List<ArraignRoom> selectArraignRoomList(ArraignRoom arraignRoom);

    List<ArraignRoom> selectRoomByType(String type);

    /**
     * 新增提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    public int insertArraignRoom(ArraignRoom arraignRoom);

    /**
     * 新增提审室并且新增提审室七天排班
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     *//*
    public int insertRoomAnd7DaySchedule(ArraignRoom arraignRoom);*/

    /**
     * 修改提审室
     *
     * @param arraignRoom 提审室信息
     * @return 结果
     */
    public int updateArraignRoom(ArraignRoom arraignRoom);

    /**
     * 删除提审室信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomByIds(String ids);

    List<ArraignRoomListDto> getRoomListResult(ArraignRoomListDto arraignRoomListDto);

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.arraign.MeetingRoomScheduleDto>
     * @Author wangyong
     * @Description 获取提审时的可用时间点
     * @Date 9:30 2020-07-08
     * @Param [scheduleDate]
     */
    List<RoomScheduleDto> getRoomScheduleArraign(String scheduleDate, String index , Long userId);

    List<RoomScheduleDto> getRoomScheduleArraign(String scheduleDate, String index , Long userId , boolean isAdd);

    List<CanteenManageDto> getRoomCanteen(String date);

    RoomAppointmentDto getRoomAppointment(RoomDetailParam roomDetailParam);

    /**
     * @param roomListParam
     * @return java.util.LinkedList<com.mkst.umap.app.admin.dto.reception.RoomInfoDto>
     * @Author wangyong
     * @Description 获取接待室的相关信息
     * @Date 19:13 2020-07-08
     * @Param [roomListParam]
     */
    LinkedList<RoomInfoDto> getReceptionRoomList(ReceptionParam roomListParam);
}
