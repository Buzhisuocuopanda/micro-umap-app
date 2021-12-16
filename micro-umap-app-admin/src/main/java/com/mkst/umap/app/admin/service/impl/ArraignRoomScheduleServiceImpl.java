package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.api.bean.result.arraign.ScheduleResult;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.ArraignRoomPlus;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.dto.specialcase.ScheduleInfoDto;
import com.mkst.umap.app.admin.mapper.ArraignRoomMapper;
import com.mkst.umap.app.admin.mapper.ArraignRoomScheduleMapper;
import com.mkst.umap.app.admin.service.IArraignRoomScheduleService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 资源排程 服务层实现
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Service
public class ArraignRoomScheduleServiceImpl implements IArraignRoomScheduleService {
    @Autowired
    private ArraignRoomScheduleMapper arraignRoomScheduleMapper;

    @Autowired
    private ArraignRoomMapper arraignRoomMapper;

    /**
     * 查询资源排程信息
     *
     * @param id 资源排程ID
     * @return 资源排程信息
     */
    @Override
    public ArraignRoomSchedule selectArraignRoomScheduleById(Long id) {
        return arraignRoomScheduleMapper.selectArraignRoomScheduleById(id);
    }

    /**
     * 查询资源排程列表
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 资源排程集合
     */
    @Override
    public List<ArraignRoomSchedule> selectArraignRoomScheduleList(ArraignRoomSchedule arraignRoomSchedule) {
        return arraignRoomScheduleMapper.selectArraignRoomScheduleList(arraignRoomSchedule);
    }

    /**
     * 新增资源排程
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 结果
     */
    @Override
    public int insertArraignRoomSchedule(ArraignRoomSchedule arraignRoomSchedule) {
        //API用户会自己添加CreateBy
        if (arraignRoomSchedule.getCreateBy() == null || arraignRoomSchedule.getCreateBy().isEmpty()) {
            arraignRoomSchedule.setCreateBy(ShiroUtils.getLoginName());
        }
        return arraignRoomScheduleMapper.insertArraignRoomSchedule(arraignRoomSchedule);
    }

    /**
     * 新增七天资源排程
     *
     * @param arraignRoom 需要排班的提审房
     * @return 结果
     */
    @Override
    public int insert7DayRoomSchedule(ArraignRoom arraignRoom) {

        try {
            ArraignRoomSchedule schedule = new ArraignRoomSchedule();
            schedule.setCreateBy(ShiroUtils.getLoginName());
            schedule.setRoomId(arraignRoom.getId());

            ArrayList<Date> listFeture5Days = UmapDateUtils.getFutureDaysList(5);

            long perDurationMin = Long.valueOf(arraignRoom.getDuration());
            Date workStartTime = arraignRoom.getWorkStartTime();
            Date workEndTime = arraignRoom.getWorkEndTime();
            Date restStartTime = arraignRoom.getRestStartTime();
            Date restEndTime = arraignRoom.getRestEndTime();

            Date scheduleStartTime = new Date();
            Date scheduleEndTime;
            for (Date date : listFeture5Days) {
                scheduleStartTime.setTime(workStartTime.getTime());
                scheduleEndTime = new Date();
                //上班到下班时间之间排班
                while (scheduleStartTime.before(workEndTime)) {
                    //如果排班开始时间已经等于下班时间了，那说明这一天的排班已经完成
                    if (scheduleStartTime.equals(workEndTime)) {
                        continue;
                    }
                    //如果排班开始的时间等于开始休息的时间，那就可以从下午上班时间继续排班了
                    if (scheduleStartTime.equals(restStartTime)) {
                        scheduleStartTime.setTime(restEndTime.getTime());
                        continue;
                    }
                    //计算班次结束时间：开始时间加上持续时间
                    scheduleEndTime.setTime(scheduleStartTime.getTime() + perDurationMin * 60L * 1000L);

                    //组合日期与时间
                    schedule.setStartTime(UmapDateUtils.combineDateTime(date, scheduleStartTime));
                    schedule.setEndTime(UmapDateUtils.combineDateTime(date, scheduleEndTime));

                    arraignRoomScheduleMapper.insertArraignRoomSchedule(schedule);

                    //插入之后，上一班的结束时间变成下一班的开始时间
                    scheduleStartTime.setTime(scheduleEndTime.getTime());
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改资源排程
     *
     * @param arraignRoomSchedule 资源排程信息
     * @return 结果
     */
    @Override
    public int updateArraignRoomSchedule(ArraignRoomSchedule arraignRoomSchedule) {
        /*arraignRoomSchedule.setUpdateBy(ShiroUtils.getLoginName());*/
        return arraignRoomScheduleMapper.updateArraignRoomSchedule(arraignRoomSchedule);
    }

    /**
     * 删除资源排程对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteArraignRoomScheduleByIds(String ids) {
        return arraignRoomScheduleMapper.deleteArraignRoomScheduleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资源排程对象
     *
     * @param roomIds 需要删除的数据的房间ID
     * @return 结果
     */
    @Override
    public int deleteArraignRoomScheduleByrRoomId(String roomIds) {
        return arraignRoomScheduleMapper.deleteScheduleByRoomId(Convert.toStrArray(roomIds));
    }


    /**
     * @return
     * @描述 获取某个房间的时间安排
     * @方法名 getScheduleIntervalList
     * @参数 [arraignRoom, date]
     * @返回值 java.util.LinkedList<java.util.Date [ ]>
     * @创建人 Baldwin
     * @创建时间 2020-06-18
     * @修改人和其它信息
     */
    @Override
    public LinkedList<LinkedList<Date[]>> getScheduleIntervalList(ArraignRoom arraignRoom, String date) {

        LinkedList<LinkedList<Date[]>> listScheduleInterval = new LinkedList<>();
        LinkedList<Date[]> scheduleAm = new LinkedList<>();
        LinkedList<Date[]> schedulePm = new LinkedList<>();

        long perDurationMin = Long.valueOf(arraignRoom.getDuration());
        Date workStartTime = UmapDateUtils.combineDateTime(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date), arraignRoom.getWorkStartTime());
        Date workEndTime = UmapDateUtils.combineDateTime(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date), arraignRoom.getWorkEndTime());
        Date restStartTime = UmapDateUtils.combineDateTime(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date), arraignRoom.getRestStartTime());
        Date restEndTime = UmapDateUtils.combineDateTime(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date), arraignRoom.getRestEndTime());

        Date scheduleStartTime = UmapDateUtils.combineDateTime(UmapDateUtils.dateTime(UmapDateUtils.YYYY_MM_DD, date), workStartTime);
        Date scheduleEndTime = new Date();

        LinkedList<Date[]> fakeList = scheduleAm;
        while (scheduleStartTime.before(workEndTime)) {
            //如果排班开始时间已经等于下班时间了，那说明这一天的排班已经完成
            if (scheduleStartTime.equals(workEndTime)) {
                continue;
            }
            //如果排班开始的时间等于开始休息的时间，那就可以从下午上班时间继续排班了
            if (scheduleStartTime.equals(restStartTime)) {
                scheduleStartTime.setTime(restEndTime.getTime());
                fakeList = schedulePm;
                continue;
            }

            Date[] dates = new Date[2];
            dates[0] = new Date(scheduleStartTime.getTime());

            //计算班次结束时间：开始时间加上持续时间
            scheduleEndTime.setTime(scheduleStartTime.getTime() + perDurationMin * 60L * 1000L);

            dates[1] = new Date(scheduleEndTime.getTime());

            fakeList.add(dates);

            //插入之后，上一班的结束时间变成下一班的开始时间
            scheduleStartTime.setTime(scheduleEndTime.getTime());

        }
        listScheduleInterval.add(scheduleAm);
        listScheduleInterval.add(schedulePm);
        return listScheduleInterval;
    }

    /**
     * @return java.util.LinkedList<com.mkst.umap.app.admin.dto.specialcase.ScheduleInfoDto>
     * @Author wangyong
     * @Description 获取当前提审室预约时间占用情况
     * @Date 18:03 2020-07-06
     * @Param [roomScheduleParam]
     */
    @Override
    public LinkedList<ScheduleInfoDto> getScheduleForSpecialCaseList(RoomScheduleParam roomScheduleParam) {

        LinkedList<ScheduleInfoDto> scheduleInfoDtos = new LinkedList<>();

        ArraignRoom selectArraignRoom = new ArraignRoom();
        selectArraignRoom.setType(KeyConstant.ARENA_TYPE_ARRAIGN_ROOM);
        List<ArraignRoom> arraignRooms = arraignRoomMapper.selectArraignRoomList(selectArraignRoom);

        for (ArraignRoom arraignRoom : arraignRooms) {
            ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto();

            roomScheduleParam.setRoomId(arraignRoom.getId());
            List<ScheduleResult> roomScheduleList = arraignRoomScheduleMapper.getRoomScheduleList(roomScheduleParam);

            LinkedList<String> datesList = new LinkedList<>();
            for (ScheduleResult scheduleResult : roomScheduleList) {
                String dates = UmapDateUtils.combine2Str(scheduleResult.getStartTime(), scheduleResult.getEndTime());
                datesList.add(dates);
            }
            scheduleInfoDto.setId(arraignRoom.getId());
            scheduleInfoDto.setName(arraignRoom.getName());
            scheduleInfoDto.setDateList(datesList);
            scheduleInfoDto.setRoomAvailable(true);

            scheduleInfoDtos.add(scheduleInfoDto);

        }
        return scheduleInfoDtos;
    }

    @Override
    public Date getNextStartTime(RoomScheduleParam roomScheduleParam) {
        return arraignRoomMapper.getNextStartTime(roomScheduleParam);
    }

    @Override
    public List<ArraignRoomSchedule> roomUse(ArraignRoomSchedule arraignRoomSchedule) {
        return arraignRoomScheduleMapper.roomUse(arraignRoomSchedule);
    }

    @Override
    public List<ArraignRoomPlus> selectRoomPlus() {
        return arraignRoomScheduleMapper.selectRoomPlus();
    }

}
