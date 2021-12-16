package com.mkst.umap.app.admin.service;


import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.ArraignRoomPlus;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.dto.specialcase.ScheduleInfoDto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 资源排程 服务层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface IArraignRoomScheduleService {
    /**
     * 查询资源排程信息
     *
     * @param id 资源排程ID
     * @return 资源排程信息
     */
    public ArraignRoomSchedule selectArraignRoomScheduleById(Long id);

    /**
     * 查询资源排程列表
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 资源排程集合
     */
    public List<ArraignRoomSchedule> selectArraignRoomScheduleList(ArraignRoomSchedule arraignRoomSchedule);

    /**
     * 新增资源排程
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 结果
     */
    public int insertArraignRoomSchedule(ArraignRoomSchedule arraignRoomSchedule);

    /**
     * 新增七天资源排程
     *
     * @param arraignRoom 需要排班的提审房
     * @return 结果
     */
    public int insert7DayRoomSchedule(ArraignRoom arraignRoom);

    /**
     * 修改资源排程
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 结果
     */
    public int updateArraignRoomSchedule(ArraignRoomSchedule arraignRoomSchedule);

    /**
     * 删除资源排程信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomScheduleByIds(String ids);

    /**
     * 删除资源排程信息
     *
     * @param ids 需要删除的数据的房间ID
     * @return 结果
     */
    int deleteArraignRoomScheduleByrRoomId(String ids);

    LinkedList<LinkedList<Date[]>> getScheduleIntervalList(ArraignRoom arraignRoom, String date);

    /**
     * @return java.util.LinkedList<java.util.Date [ ]>
     * @Author wangyong
     * @Description
     * @Date 16:54 2020-07-06
     * @Param [roomScheduleParam]
     */
    LinkedList<ScheduleInfoDto> getScheduleForSpecialCaseList(RoomScheduleParam roomScheduleParam);

    /**
     * @return java.util.Date
     * @Author wangyong
     * @Description 选择开始时间后，获取下一个开始时间用以限制本次预约的结束时间
     * @Date 18:28 2020-07-06
     * @Param [roomScheduleParam]
     */
    Date getNextStartTime(RoomScheduleParam roomScheduleParam);

    List<ArraignRoomSchedule> roomUse(ArraignRoomSchedule arraignRoomSchedule);

    List<ArraignRoomPlus> selectRoomPlus();
}
