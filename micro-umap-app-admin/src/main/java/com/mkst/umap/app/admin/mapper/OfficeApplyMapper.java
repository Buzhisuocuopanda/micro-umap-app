package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.officeapply.OfficeApplyParam;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyDetailResult;
import com.mkst.umap.app.admin.api.bean.result.officeapply.OfficeApplyInfoResult;
import com.mkst.umap.app.admin.domain.OfficeApply;
import com.mkst.umap.app.admin.dto.meeting.MeetingAuditProgressInfoDto;
import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 办公申请 数据层
 *
 * @author wangyong
 * @date 2020-08-07
 */
public interface OfficeApplyMapper {
    /**
     * 查询办公申请信息
     *
     * @param id 办公申请ID
     * @return 办公申请信息
     */
    public OfficeApply selectOfficeApplyById(Long id);

    /**
     * 查询办公申请列表
     *
     * @param officeApply 办公申请信息
     * @return 办公申请集合
     */
    public List<OfficeApply> selectOfficeApplyList(OfficeApply officeApply);

    /**
     * 新增办公申请
     *
     * @param officeApply 办公申请信息
     * @return 结果
     */
    public int insertOfficeApply(OfficeApply officeApply);

    /**
     * 修改办公申请
     *
     * @param officeApply 办公申请信息
     * @return 结果
     */
    public int updateOfficeApply(OfficeApply officeApply);

    /**
     * 删除办公申请
     *
     * @param id 办公申请ID
     * @return 结果
     */
    public int deleteOfficeApplyById(Long id);

    /**
     * 批量删除办公申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOfficeApplyByIds(String[] ids);

    List<OfficeApplyInfoResult> selectApplyInfoListByParam(OfficeApplyParam officeApplyParam);

    int updateOfficeApplyByParam(OfficeApplyParam officeApplyParam);

    OfficeApplyDetailResult selectDetailId(Long id);

    List<OfficeApplyInfoResult> selectAuditList(OfficeApplyParam officeApplyParam);

    List<MeetingAuditProgressInfoDto> selectAuditListByParam(OfficeApplyParam officeApplyParam);

    Integer selectTotalByDay(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<AnalysisCountResult> selectCarOrderList(@Param("startDay") String startDay, @Param("endDay") String endDay);

    Long selectTotal();


}