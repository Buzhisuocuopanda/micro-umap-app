package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.OfficeApplyAudit;

import java.util.List;

/**
 * 办公申请审批 数据层
 *
 * @author wangyong
 * @date 2020-08-07
 */
public interface OfficeApplyAuditMapper {
    /**
     * 查询办公申请审批信息
     *
     * @param id 办公申请审批ID
     * @return 办公申请审批信息
     */
    public OfficeApplyAudit selectOfficeApplyAuditById(Long id);

    /**
     * 查询办公申请审批列表
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 办公申请审批集合
     */
    public List<OfficeApplyAudit> selectOfficeApplyAuditList(OfficeApplyAudit officeApplyAudit);

    /**
     * 新增办公申请审批
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 结果
     */
    public int insertOfficeApplyAudit(OfficeApplyAudit officeApplyAudit);

    /**
     * 修改办公申请审批
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 结果
     */
    public int updateOfficeApplyAudit(OfficeApplyAudit officeApplyAudit);

    /**
     * 删除办公申请审批
     *
     * @param id 办公申请审批ID
     * @return 结果
     */
    public int deleteOfficeApplyAuditById(Long id);

    /**
     * 批量删除办公申请审批
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOfficeApplyAuditByIds(String[] ids);

}