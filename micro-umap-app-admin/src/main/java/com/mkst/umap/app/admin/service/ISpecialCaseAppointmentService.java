package com.mkst.umap.app.admin.service;

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
import java.util.Map;

/**
 * 专案预约 服务层
 *
 * @author wangyong
 * @date 2020-07-02
 */
public interface ISpecialCaseAppointmentService {
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

    List<SpecialCaseAppointmentVo> selectSpecialCaseAppointmentWebList(SpecialCaseAppointment specialCaseAppointment);

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
     * 删除专案预约信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSpecialCaseAppointmentByIds(String ids);

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetialDto>
     * @Author wangyong
     * @Description 获取专案预约详细信息
     * @Date 17:49 2020-07-03
     * @Param [specialCaseDetialDto]
     */
    List<SpecialCaseDetailDto> selectSpecialCaseDetailList(SpecialCaseDetailDto specialCaseDetailDto);

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetialDto>
     * @Author wangyong
     * @Description 获取专案预约详细信息
     * @Date 17:49 2020-07-03
     * @Param [specialCaseDetialDto]
     */
    SpecialCaseDetailDto selectSpecialCaseDetailById(Long id);

    /**
     * @return java.util.List<com.mkst.umap.app.admin.dto.specialcase.SpecialCaseDetailDto>
     * @Author wangyong
     * @Description 获取专案列表
     * @Date 10:54 2020-07-06
     * @Param [specialCaseListParam]
     */
    List<SpecialCaseDetailDto> selectCaseList(SpecialCaseListParam specialCaseListParam);

    /**
     * 批量审核专案预约
     *
     * @param auditParam 需要审核的数据信息
     * @return 结果
     */
    int auditCaseList(SpecialCaseAuditParam auditParam);

    int audit(Long id, String status, String reason);

    /**
     * 批量审核专案预约
     *
     * @param auditParam 需要审核的数据信息
     * @return 结果
     */
    int auditCaseBatch(SpecialCaseAuditParam auditParam);

    /**
     * 获取专案审核列表
     *
     * @param auditInfoParam 数据信息
     * @return 结果
     */
    List<AuditInfoResult> selectCaseAuditList(AuditInfoParam auditInfoParam);

    List<SpecialCaseAppointmentVo> selectCaseWebAuditList(SpecialCaseAppointment specialCaseAppointment);

    boolean selectIsOccupied(SpecialCaseAppointment specialCaseAppointment);

    List<NameCountResult> selectDayCount(SpecialCaseListParam param);

    Map<String, List<TimeApplyResult>> selectTimeApplyList(SpecialCaseListParam param);

    List<SpecialCaseAppointment> selectAppointmentByDay();
}
