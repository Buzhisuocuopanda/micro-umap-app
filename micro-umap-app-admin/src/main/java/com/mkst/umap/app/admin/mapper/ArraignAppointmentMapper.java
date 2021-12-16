package com.mkst.umap.app.admin.mapper;

import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.api.bean.param.ArraignAppointmentParam;
import com.mkst.umap.app.admin.api.bean.param.SelectProcustorParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.ArraignAppointment;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.domain.ArraignShift;
import com.mkst.umap.app.admin.dto.arraign.*;
import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 提审预约 数据层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Repository
public interface ArraignAppointmentMapper {
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

    Integer selectTotalByDay(@Param("startTime") String startTime,@Param("endTime") String endTime);

    Integer selectTotal();

    List<AnalysisCountResult> analysisList(@Param("startDay") String startDay, @Param("endDay") String endDay);

    List<CriminalTypeResultDto> selectGroupByCriminalType(ReportDto dto);

    List<CriminalTypeResultDto> select30Day(ReportDto dto);

    List<String> selectActiveUsers(String date);

    List<CriminalTypeResultDto> selectSeasonGroupByCriminalType(ReportDto dto);

    List<ReportDto> selectReport(ReportDto dto);

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

    public Integer selectMaxNumOrderByDay(@Param("checkDay") Date checkDay , @Param("status") String status  , @Param("timePie") String timePie );

    /**
     * 修改提审预约
     *
     * @param arraignAppointment 提审预约信息
     * @return 结果
     */
    public int updateArraignAppointment(ArraignAppointment arraignAppointment);

    /**
     * 删除提审预约
     *
     * @param id 提审预约ID
     * @return 结果
     */
    public int deleteArraignAppointmentById(Integer id);

    /**
     * 批量删除提审预约
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteArraignAppointmentByIds(String[] ids);

    public List<AppointmentDetailDto> selectAppointmentDetail(AppointmentDetailDto appointmentDetailDto);

    List<ArraignAuditDto> getArraignAuditDtoList(ArraignAuditDto arraignAuditDto);

    List<NameCountResult> selectDayCount(ArraignAppointmentParam param);

    List<TimeApplyResult> selectTimeApplyList(ArraignAppointmentParam param);

    public int updateNumOrder(@Param("id") Long id , @Param("numOrder") Integer numOrder , @Param("timePie") String timePie);

    public int finish(@Param("id") Long id , @Param("finishStatus") String finishStatus , @Param("finishBy") String finishBy , @Param("finishTime") Date finishTime);

    public List<ArraignAppointment> selectNotFinishAppointment(@Param("prosecutorUserId") Long prosecutorUserId
            , @Param("endDate") String endDate ,  @Param("finishStatus") String finishStatus
            ,  @Param("status") String status);

    public List<SysUser> selectProsecutorList(SelectProcustorParam param);

    int modifyFinishStatus();

    List<ArraignShift> selectDayShift(ArraignShift shift);

    List<ArraignAppointment> selectAppoint(Long[] ids);

    int changeTime(ArraignAppointment appointment);

    int changeRoomTime(ArraignRoomSchedule roomSchedule);

    int isArraign(ArraignAppointment appointment);
}