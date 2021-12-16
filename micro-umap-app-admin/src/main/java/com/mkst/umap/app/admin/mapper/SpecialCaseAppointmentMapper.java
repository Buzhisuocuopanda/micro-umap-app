package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.AuditInfoParam;
import com.mkst.umap.app.admin.api.bean.param.SpecialCaseAuditParam;
import com.mkst.umap.app.admin.api.bean.param.SpecialCaseListParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.AuditInfoResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.domain.SpecialCaseAppointment;
import com.mkst.umap.app.admin.domain.vo.SpecialCaseAppointmentVo;
import com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto;

import java.util.List;

/**
 * 专案预约 数据层
 *
 * @author wangyong
 * @date 2020-07-01
 */
public interface SpecialCaseAppointmentMapper {
    /**
     * 查询专案预约信息
     *
     * @param id 专案预约ID
     * @return 专案预约信息
     */
    public SpecialCaseAppointment selectSpecialCaseAppointmentById(Long id);

    /**
     * 查询专案预约列表
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 专案预约集合
     */
    List<SpecialCaseAppointment> selectSpecialCaseAppointmentList(SpecialCaseAppointment specialCaseAppointment);

    List<SpecialCaseAppointmentVo> selectSpecialCaseWebVo(SpecialCaseAppointment specialCaseAppointment);

    /**
     * 新增专案预约
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 结果
     */
    public int insertSpecialCaseAppointment(SpecialCaseAppointment specialCaseAppointment);

    /**
     * 修改专案预约
     *
     * @param specialCaseAppointment 专案预约信息
     * @return 结果
     */
    public int updateSpecialCaseAppointment(SpecialCaseAppointment specialCaseAppointment);

    /**
     * 删除专案预约
     *
     * @param id 专案预约ID
     * @return 结果
     */
    public int deleteSpecialCaseAppointmentById(Long id);

    /**
     * 批量删除专案预约
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSpecialCaseAppointmentByIds(String[] ids);

    /**
     * @return int
     * @Author hsw
     * @Description 批量审核
     * @Date 9:57 2020-07-17
     * @Param [auditParam]
     */
    int auditCaseBatch(SpecialCaseAuditParam auditParam);

    List<SpecialCaseDetailDto> selectSpecialCaseDetailList(SpecialCaseDetailDto specialCaseDetailDto);

    List<SpecialCaseDetailDto> selectCaseList(SpecialCaseListParam specialCaseListParam);

    List<AuditInfoResult> selectAuditList(AuditInfoParam auditInfoParam);

    List<SpecialCaseAppointmentVo> selectSpecialCaseAuditList(SpecialCaseAppointment specialCaseAppointment);

    int selectIsOccupy(SpecialCaseAppointment caseAppointment);

    List<NameCountResult> selectDayCount(SpecialCaseListParam param);

    List<TimeApplyResult> selectTimeApplyList(SpecialCaseListParam param);

    List<SpecialCaseAppointment> selectAppointmentByDay();
}