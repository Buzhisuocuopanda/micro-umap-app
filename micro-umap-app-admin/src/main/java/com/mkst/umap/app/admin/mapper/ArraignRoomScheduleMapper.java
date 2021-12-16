package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.api.bean.result.arraign.ScheduleResult;
import com.mkst.umap.app.admin.domain.ArraignRoomPlus;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;

import java.util.List;

/**
 * 资源排程 数据层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface ArraignRoomScheduleMapper {
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
     * 修改资源排程
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 结果
     */
    public int updateArraignRoomSchedule(ArraignRoomSchedule arraignRoomSchedule);

    /**
     * 删除资源排程
     *
     * @param id 资源排程ID
     * @return 结果
     */
    public int deleteArraignRoomScheduleById(Long id);

    /**
     * 删除资源排程
     *
     * @param roomIds 资源排程的roomID
     * @return 结果
     */
    public int deleteScheduleByRoomId(String[] roomIds);

    /**
     * 批量删除资源排程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignRoomScheduleByIds(String[] ids);


    /**
     * 按预约id批量删除资源排程
     *
     * @param ids 需要删除的数据预约ID
     * @return 结果
     */
    int deleteScheduleByAppointmentIds(String[] ids);

    /**
     * @return
     * @Author wangyong
     * @Description 专案预约的时候展示已预约的时间段
     * @Date 16:08 2020-07-06
     * @Param
     */
    List<ScheduleResult> getRoomScheduleList(RoomScheduleParam roomScheduleParam);

    List<ArraignRoomSchedule> roomUse(ArraignRoomSchedule arraignRoomSchedule);

    List<ArraignRoomPlus> selectRoomPlus();
}