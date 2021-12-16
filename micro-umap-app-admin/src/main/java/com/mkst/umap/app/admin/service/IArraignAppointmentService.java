package com.mkst.umap.app.admin.service;


import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.api.bean.param.ArraignAppointmentParam;
import com.mkst.umap.app.admin.api.bean.param.SelectProcustorParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.ArraignAppointment;
import com.mkst.umap.app.admin.domain.ArraignSyncData;
import com.mkst.umap.app.admin.dto.arraign.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 提审预约 服务层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface IArraignAppointmentService {
    /**
     * 查询提审预约信息
     *
     * @param id 提审预约ID
     * @return 提审预约信息
     */
    public ArraignAppointment selectArraignAppointmentById(Long id);


    /**提审预约当日统计 通过roomId*/
    List<ArraignAppointmentTotalDto> selectTotalByRoomId(ArraignAppointmentTotalDto dto);

    /**提审预约 当天 通过犯罪类型*/
    List<ArraignAppointmentTotalDto> selectTotalByCriminalType(ArraignAppointmentTotalDto dto);

    List<CountDto> selectDeptFive30Day();

    Integer selectTotalByTime(ArraignAppointmentTotalDto dto);

    List<ReportDto> selectReport(ReportDto dto);

    List<CriminalTypeResultDto> selectGroupByCriminalType(ReportDto dto);

    List<CriminalTypeResultDto> select30Day(ReportDto dto);

    List<String> selectActiveUsers(String date);

    List<CriminalTypeResultDto> selectSeasonGroupByCriminalType(ReportDto dto);

    List<ReportDto> selectSeasonReport(ReportDto dto);
    /**
     * 查询提审预约列表
     *
     * @param arraignAppointment 提审预约信息
     * @return 提审预约集合
     */
    public List<ArraignAppointment> selectArraignAppointmentList(ArraignAppointment arraignAppointment);

    public Integer selectArraignAppointmentAuditCount();

    List<ArraignAppointment> selectArraignAppointmentListByDto(ArraignAppointmentTotalDto dto);
    /**
     * 新增提审预约
     *
     * @param arraignAppointment 提审预约信息
     * @return 结果
     */
    public int insertArraignAppointment(ArraignAppointment arraignAppointment);

    /**
     * 修改提审预约
     *
     * @param arraignAppointment 提审预约信息
     * @return 结果
     */
    public int updateArraignAppointment(ArraignAppointment arraignAppointment);

    public int updateArraignAppointmentStatus(ArraignAppointment arraignAppointment);

    public int updateAppointment(ArraignAppointment arraignAppointment);

    /**
     * 删除提审预约信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignAppointmentByIds(String ids);

    /**
     * 批量更新提审申请状态，也可单个更新
     *
     * @param ids 需要改变的数据ID
     * @return 结果
     */
    public int updateStatusByIds(String ids, String applyType, String status, String reason, String nowOperator);

    List<AppointmentDetailDto> getAppointmentDetailList(AppointmentDetailDto appointmentDetailDto);

    AppointmentDetailDto getAppointmentDetailById(Long id);

    List<ArraignAuditDto> selectArraignAppointmentAuditList(ArraignAuditDto ArraignAuditDto);

    int audit(Long id, String status, String reason);

    List<NameCountResult> selectDayCount(ArraignAppointmentParam param);

    List<NameCountResult> getAppointmentCalendar(Long userId);

    Map<String, List<TimeApplyResult>> selectTimeApplyList(ArraignAppointmentParam param);

    Map<String,List<TimeApplyResult>> selectDayApplyList(ArraignAppointmentParam param);

    void resetApplyOrder(long id , Integer numOrder ,  Date checkDate , String timePie);

    String importLog();

    //判断以前是否同步过 （根据数据表记录）
    boolean haveSyncArraign();

    //将数据同步到库里
    String syncArraign(Set<ArraignSyncData> arraignSyncDataList);

    int finishArraignAppoint(Long id , String finishBy);

    List<ArraignAppointment> selectNotFinishArraignAppointment(Long prosecutorUserId);

    List<SysUser> selectProsecutorList(SelectProcustorParam param);

    int modifyFinishStatus();

    int isArraign(ArraignAppointment appointment);
}
