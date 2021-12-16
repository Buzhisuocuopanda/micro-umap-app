package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysDeptMapper;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.AuditInfoParam;
import com.mkst.umap.app.admin.api.bean.param.SpecialCaseAuditParam;
import com.mkst.umap.app.admin.api.bean.param.SpecialCaseListParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.AuditInfoResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.SpecialCaseAppointment;
import com.mkst.umap.app.admin.domain.vo.SpecialCaseAppointmentVo;
import com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto;
import com.mkst.umap.app.admin.mapper.ArraignRoomScheduleMapper;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.mapper.SpecialCaseAppointmentMapper;
import com.mkst.umap.app.admin.service.ISpecialCaseAppointmentService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.RoomColorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 专案预约 服务层实现
 *
 * @author wangyong
 * @date 2020-07-01
 */
@Service
public class SpecialCaseAppointmentServiceImpl implements ISpecialCaseAppointmentService {
    @Autowired
    private SpecialCaseAppointmentMapper specialCaseAppointmentMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ArraignRoomScheduleMapper scheduleMapper;
    @Autowired
    private AuditRecordMapper auditRecordMapper;

    /**
     * 查询专案预约信息
     *
     * @param id 专案预约ID
     * @return 专案预约信息
     */
    @Override
    public SpecialCaseAppointment selectSpecialCaseAppointmentById(Long id) {
        return specialCaseAppointmentMapper.selectSpecialCaseAppointmentById(id);
    }

    /**
     * 查询专案预约列表
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 专案预约集合
     */
    @Override
    public List<SpecialCaseAppointment> selectSpecialCaseAppointmentList(SpecialCaseAppointment specialCaseAppointment) {
        return specialCaseAppointmentMapper.selectSpecialCaseAppointmentList(specialCaseAppointment);
    }

    @Override
    public List<SpecialCaseAppointmentVo> selectSpecialCaseAppointmentWebList(SpecialCaseAppointment specialCaseAppointment) {
        List<SpecialCaseAppointmentVo> specialCaseAppointmentVos = specialCaseAppointmentMapper.selectSpecialCaseWebVo(specialCaseAppointment);

        for (SpecialCaseAppointmentVo vo : specialCaseAppointmentVos) {
            SysUser user = userService.selectUserByLoginName(vo.getCreateBy());
            if (BeanUtil.isEmpty(user)){
                continue;
            }
            vo.setCreateBy(user.getUserName());
        }

        return specialCaseAppointmentVos;
    }

    /**
     * 新增专案预约
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 结果
     */
    @Override
    public int insertSpecialCaseAppointment(SpecialCaseAppointment specialCaseAppointment) {
        return specialCaseAppointmentMapper.insertSpecialCaseAppointment(specialCaseAppointment);
    }

    /**
     * 修改专案预约
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 结果
     */
    @Override
    public int updateSpecialCaseAppointment(SpecialCaseAppointment specialCaseAppointment) {
        return specialCaseAppointmentMapper.updateSpecialCaseAppointment(specialCaseAppointment);
    }

    /**
     * 删除专案预约对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSpecialCaseAppointmentByIds(String ids) {
        return specialCaseAppointmentMapper.deleteSpecialCaseAppointmentByIds(Convert.toStrArray(ids));
    }

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetialDto>
     * @Author wangyong
     * @Description 获取专案预约详细信息
     * @Date 17:49 2020-07-03
     * @Param [specialCaseDetialDto]
     */
    @Override
    public List<SpecialCaseDetailDto> selectSpecialCaseDetailList(SpecialCaseDetailDto specialCaseDetailDto) {
        return specialCaseAppointmentMapper.selectSpecialCaseDetailList(specialCaseDetailDto);
    }

    @Override
    public SpecialCaseDetailDto selectSpecialCaseDetailById(Long id) {
        SpecialCaseDetailDto specialCaseDetailDto = new SpecialCaseDetailDto();
        specialCaseDetailDto.setId(id);
        List<SpecialCaseDetailDto> specialCaseDetailDtos = this.selectSpecialCaseDetailList(specialCaseDetailDto);
        if (specialCaseDetailDtos.isEmpty()) {
            return null;
        }
        SpecialCaseDetailDto specialCaseDetailResult = specialCaseDetailDtos.get(0);
        specialCaseDetailResult.setDept(sysDeptMapper.selectDeptById(specialCaseDetailResult.getDeptId()).getDeptName());

        return specialCaseDetailResult;
    }


    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto>
     * @Author wangyong
     * @Description 获取专案列表
     * @Date 10:54 2020-07-06
     * @Param [specialCaseListParam]
     */
    @Override
    public List<SpecialCaseDetailDto> selectCaseList(SpecialCaseListParam specialCaseListParam) {

        // 20200730：要求除了“全部”外，其他都只显示已通过的申请
        // 20200911: 删除原有逻辑
        /*String nowStatus = specialCaseListParam.getNowStatus() == null ? "3" : specialCaseListParam.getNowStatus();
        if (!nowStatus.equals(KeyConstant.EVENT_ALL)) {
            ArrayList<String> statusList = new ArrayList<>();
            statusList.add(KeyConstant.EVENT_AUDIT_STATUS_PASS);
            specialCaseListParam.setStatusList(statusList);
        }*/

        List<SpecialCaseDetailDto> specialCaseDetailDtos = specialCaseAppointmentMapper.selectCaseList(specialCaseListParam);
        List<SpecialCaseDetailDto> resultList = new LinkedList<>();

        for (SpecialCaseDetailDto specialCaseDetailDto : specialCaseDetailDtos) {
            Date startTime = specialCaseDetailDto.getStartTime();
            Date endTime = specialCaseDetailDto.getEndTime();

            Date nowTime = DateUtils.getNowDate();

            String eventStatus = "";

            String paramNowStatus = specialCaseListParam.getNowStatus();

            specialCaseDetailDto.setAuditName(AuditStatusEnum.getName(Integer.parseInt(specialCaseDetailDto.getAuditStatus())));

            if (paramNowStatus.equals(KeyConstant.EVENT_ALL)) {
                resultList.add(specialCaseDetailDto);
                continue;
            }

            // 未要求当前状态
            if (paramNowStatus == null || paramNowStatus.isEmpty() || paramNowStatus.equals(KeyConstant.EVENT_ALL)) {
                resultList.add(specialCaseDetailDto);
            } else {

                //判断当前状态
                if (startTime.after(nowTime)) {
                    eventStatus = KeyConstant.EVENT_PROGRESS_STATUS_WAITING;
                } else if (endTime.before(nowTime)) {
                    eventStatus = KeyConstant.EVENT_PROGRESS_STATUS_FINISHED;
                } else {
                    eventStatus = KeyConstant.EVENT_PROGRESS_STATUS_ONGOING;
                }

                // 20200911：所有未通过的申请，放在已结束
                if (specialCaseDetailDto.getStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
                    eventStatus = KeyConstant.EVENT_PROGRESS_STATUS_FINISHED;
                }

                if (eventStatus.equals(paramNowStatus)) {
                    resultList.add(specialCaseDetailDto);
                }
            }
        }
        return resultList;
    }


    /**
     * @return int
     * @Author wangyong
     * @Description 批量审核
     * @Date 17:16 2020-07-07
     * @Param [auditParam]
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int auditCaseList(SpecialCaseAuditParam auditParam) {
        Long[] ids = auditParam.getIds();
        SpecialCaseAppointment specialCaseAppointment = new SpecialCaseAppointment();
        specialCaseAppointment.setStatus(auditParam.getStatus());
        specialCaseAppointment.setUpdateBy(auditParam.getUpdateBy());

        for (Long id : ids) {
            specialCaseAppointment.setId(id);
            dealForRelated(specialCaseAppointment, auditParam);
            this.updateSpecialCaseAppointment(specialCaseAppointment);
        }
        return ids.length;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(Long id, String status, String reason) {

        SpecialCaseAppointment appointment = selectSpecialCaseAppointmentById(id);

        //新增审核记录
        AuditRecord auditRecord = new AuditRecord(appointment.getId(), AuditRecordTypeEnum.SpecialCaseAudit.getValue(), status, "");
        auditRecord.setCreateBy(ShiroUtils.getLoginName());
        auditRecord.setUpdateBy(ShiroUtils.getLoginName());

        if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
            auditRecord.setReason(reason);
        }

        auditRecordMapper.insertAuditRecord(auditRecord);

        if (!appointment.getStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT)) {
            return 0;
        }

        appointment.setStatus(status);

        int row = this.updateSpecialCaseAppointment(appointment);

        if (status.equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
            scheduleMapper.deleteArraignRoomScheduleById(appointment.getScheduleId());
        }
        return row;
    }

    /**
     * @return void
     * @Author wangyong
     * @Description 处理连带的排班表和审核表信息
     * @Date 17:03 2020-07-07
     * @Param [caseAppointment]
     */
    private void dealForRelated(SpecialCaseAppointment caseAppointment, SpecialCaseAuditParam auditParam) {

        SpecialCaseAppointment specialCaseAppointment = specialCaseAppointmentMapper.selectSpecialCaseAppointmentById(caseAppointment.getId());

        //插入记录表
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setApplyId(specialCaseAppointment.getId());
        auditRecord.setApplyType(AuditRecordTypeEnum.SpecialCaseAudit.getValue());
        auditRecord.setStatus(caseAppointment.getStatus());
        auditRecord.setReason(auditParam.getReason());
        //审核者对于记录表来说是创建者
        auditRecord.setCreateBy(auditParam.getUpdateBy());
        auditRecordMapper.insertAuditRecord(auditRecord);

        //如果审核结果是不通过，删除排班表
        if (caseAppointment.getStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_FAIL)) {
            scheduleMapper.deleteArraignRoomScheduleById(specialCaseAppointment.getScheduleId());
        }
    }

    /**
     * 批量审核专案预约
     *
     * @param auditParam 包括审核id、状态、理由
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int auditCaseBatch(SpecialCaseAuditParam auditParam) {
        return specialCaseAppointmentMapper.auditCaseBatch(auditParam);
    }


    @Override
    public List<AuditInfoResult> selectCaseAuditList(AuditInfoParam auditInfoParam) {
        return specialCaseAppointmentMapper.selectAuditList(auditInfoParam);
    }

    @Override
    public List<SpecialCaseAppointmentVo> selectCaseWebAuditList(SpecialCaseAppointment specialCaseAppointment) {
        return specialCaseAppointmentMapper.selectSpecialCaseAuditList(specialCaseAppointment);
    }

    @Override
    public boolean selectIsOccupied(SpecialCaseAppointment specialCaseAppointment) {
        return specialCaseAppointmentMapper.selectIsOccupy(specialCaseAppointment) > 0;
    }

    @Override
    public List<NameCountResult> selectDayCount(SpecialCaseListParam param) {
        return specialCaseAppointmentMapper.selectDayCount(param);
    }

    @Override
    public Map<String, List<TimeApplyResult>> selectTimeApplyList(SpecialCaseListParam param) {

        // 一个Key升序排序的treeMap
        Map<String, List<TimeApplyResult>> result = new TreeMap<>(Comparator.reverseOrder());
        ArrayList<TimeApplyResult> container = new ArrayList<>();
        HashMap<String, String> colorContainer = new HashMap<>();

        // 查出各种时间状态的申请
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
        container.addAll(specialCaseAppointmentMapper.selectTimeApplyList(param));
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
        container.addAll(specialCaseAppointmentMapper.selectTimeApplyList(param));
        param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
        container.addAll(specialCaseAppointmentMapper.selectTimeApplyList(param));

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

    @Override
    public List<SpecialCaseAppointment> selectAppointmentByDay(){
        return specialCaseAppointmentMapper.selectAppointmentByDay();
    }
}
