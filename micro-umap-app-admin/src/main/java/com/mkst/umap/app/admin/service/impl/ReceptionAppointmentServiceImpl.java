package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.umap.app.admin.api.bean.param.reception.ReceptionParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionDetailResult;
import com.mkst.umap.app.admin.api.bean.result.reception.ReceptionInfoResult;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.ReceptionAppointment;
import com.mkst.umap.app.admin.dto.reception.ReceptionScheduleInfoDto;
import com.mkst.umap.app.admin.mapper.ArraignRoomMapper;
import com.mkst.umap.app.admin.mapper.ReceptionAppointmentMapper;
import com.mkst.umap.app.admin.service.IReceptionAppointmentService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.RoomColorEnum;
import com.mkst.umap.app.common.enums.RoomTypeEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 接待预约 服务层实现
 *
 * @author wangyong
 * @date 2020-07-08
 */
@Service
public class ReceptionAppointmentServiceImpl implements IReceptionAppointmentService {
    @Autowired
    private ReceptionAppointmentMapper receptionAppointmentMapper;
    @Autowired
    private ArraignRoomMapper arraignRoomMapper;

    /**
     * 查询接待预约信息
     *
     * @param id 接待预约ID
     * @return 接待预约信息
     */
    @Override
    public ReceptionAppointment selectReceptionAppointmentById(Integer id) {
        return receptionAppointmentMapper.selectReceptionAppointmentById(id);
    }

    /**
     * 查询接待预约列表
     *
     * @param receptionAppointment 接待预约信息
     * @return 接待预约集合
     */
    @Override
    public List<ReceptionAppointment> selectReceptionAppointmentList(ReceptionAppointment receptionAppointment) {
        return receptionAppointmentMapper.selectReceptionAppointmentList(receptionAppointment);
    }

    /**
     * 新增接待预约
     *
     * @param receptionAppointment 接待预约信息
     * @return 结果
     */
    @Override
    public int insertReceptionAppointment(ReceptionAppointment receptionAppointment) {
        return receptionAppointmentMapper.insertReceptionAppointment(receptionAppointment);
    }

    /**
     * 修改接待预约
     *
     * @param receptionAppointment 接待预约信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateReceptionAppointment(ReceptionAppointment receptionAppointment) {
        return receptionAppointmentMapper.updateReceptionAppointment(receptionAppointment);
    }

    /**
     * 删除接待预约对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteReceptionAppointmentByIds(String ids) {
        return receptionAppointmentMapper.deleteReceptionAppointmentByIds(Convert.toStrArray(ids));
    }

    @Override
    public ReceptionDetailResult getReceptionDetailById(Long id) {
        return receptionAppointmentMapper.getReceptionDetailById(id);
    }

    @Override
    public LinkedList<ReceptionInfoResult> getMyReceptionList(ReceptionParam receptionParam) {

        LinkedList<ReceptionInfoResult> resultList = new LinkedList<>();

        // 20200730：要求除了“全部”外，其他都只显示已通过的申请
        if (receptionParam.getNowStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_WAITING)
                || receptionParam.getNowStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING)
                || receptionParam.getNowStatus().equals(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED)) {
            receptionParam.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        }
        LinkedList<ReceptionInfoResult> receptionInfoResultList = receptionAppointmentMapper.getReceptionInfoResultList(receptionParam);
        for (ReceptionInfoResult result : receptionInfoResultList) {

            Date startTime = result.getStartTime();
            Date endTime = result.getEndTime();
            Date nowDate = DateUtils.getNowDate();

            if (nowDate.after(endTime) || nowDate.equals(endTime)) {
                result.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
            } else if (nowDate.before(startTime)) {
                result.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
            } else {
                result.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
            }

            // 返回审核状态
            result.setAuditStatus(result.getAuditStatus());
            result.setAuditName(AuditStatusEnum.getName(Integer.parseInt(result.getAuditStatus())));

            if (receptionParam.getNowStatus().equals(KeyConstant.EVENT_ALL)
                    || result.getNowStatus().equals(receptionParam.getNowStatus())) {
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public LinkedList<ReceptionScheduleInfoDto> getSchedule(ReceptionParam receptionParam) {

        LinkedList<ReceptionScheduleInfoDto> scheduleInfoResult = new LinkedList<>();

        ArraignRoom selectRoom = new ArraignRoom();
        selectRoom.setType(RoomTypeEnum.RECEPTION_ROOM.getValue());

        List<ArraignRoom> arraignRooms = arraignRoomMapper.selectArraignRoomList(selectRoom);

        for (ArraignRoom arraignRoom : arraignRooms) {
            ReceptionScheduleInfoDto scheduleInfo = new ReceptionScheduleInfoDto();

            //id、name
            BeanUtils.copyProperties(arraignRoom, scheduleInfo);
            scheduleInfo.setRoomAvailable(true);

            receptionParam.setRoomId(arraignRoom.getId());
            receptionParam.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
            //roomId、date
            LinkedList<ReceptionInfoResult> receptionInfoList = receptionAppointmentMapper.getReceptionInfoResultList(receptionParam);

            LinkedList<String> dateList = getDateList(receptionInfoList);

            scheduleInfo.setDateList(dateList);
            scheduleInfoResult.add(scheduleInfo);

        }
        return scheduleInfoResult;
    }

    private LinkedList<String> getDateList(LinkedList<ReceptionInfoResult> receptionInfoList) {

        LinkedList<String> dateList = new LinkedList<>();

        for (ReceptionInfoResult receptionInfoResult : receptionInfoList) {
            Date startTime = receptionInfoResult.getStartTime();
            Date endTime = receptionInfoResult.getEndTime();

            String dateStr = UmapDateUtils.combine2Str(startTime, endTime);
            dateList.add(dateStr);
        }
        return dateList;
    }

    @Override
    public Date getNextStartTime(ReceptionParam receptionParam) {
        return receptionAppointmentMapper.getNextTime(receptionParam);
    }

    @Override
    public boolean checkIsOccupied(ReceptionParam receptionParam) {
        return receptionAppointmentMapper.selectIsOccupied(receptionParam) > 0;
    }

    @Override
    public List<NameCountResult> selectDayCount(ReceptionParam param) {
        param.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        List<NameCountResult> result = receptionAppointmentMapper.selectDayCount(param);
        result.stream().forEach(o -> o.setStatus(false));
        return result;
    }

    @Override
    public Map<String, List<TimeApplyResult>> selectTimeApplyList(ReceptionParam param) {

        // 一个Key升序排序的treeMap
        Map<String, List<TimeApplyResult>> result = new TreeMap<>(Comparator.reverseOrder());
        ArrayList<TimeApplyResult> container = new ArrayList<>();
        HashMap<String, String> colorContainer = new HashMap<>();

        // 查出各种时间状态的申请
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
        container.addAll(receptionAppointmentMapper.selectTimeApplyList(param));
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
        container.addAll(receptionAppointmentMapper.selectTimeApplyList(param));
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
        container.addAll(receptionAppointmentMapper.selectTimeApplyList(param));

        // 写入数据
        container.stream().forEach(timeApplyResult -> {
            if (!result.containsKey(timeApplyResult.getTimeCon())){
                List<TimeApplyResult> arrForTimeCon = new ArrayList<>();
                result.put(timeApplyResult.getTimeCon(),arrForTimeCon);
            }

            // 如果房间无初始数据，就初始一下
            if (!colorContainer.containsKey(timeApplyResult.getRoomName())){
                String color = new String();
                // 挑一个未被使用的颜色
                for (RoomColorEnum roomColorEnum : RoomColorEnum.values()) {
                    if (colorContainer.containsValue(roomColorEnum.getValue())){
                        continue;
                    }
                    color = roomColorEnum.getValue();
                    break;
                }
                colorContainer.put(timeApplyResult.getRoomName(),color);
            }

            timeApplyResult.setRoomBackColor(colorContainer.get(timeApplyResult.getRoomName()));
            result.get(timeApplyResult.getTimeCon()).add(timeApplyResult);
        });

        return result;
    }
}
