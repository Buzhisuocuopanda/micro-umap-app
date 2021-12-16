package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.AuditRecord;

import java.util.List;

/**
 * 申请审核记录 服务层
 *
 * @author lijinghui
 * @date 2020-06-11
 */
public interface IAuditRecordService {
    /**
     * 查询申请审核记录信息
     *
     * @param id 申请审核记录ID
     * @return 申请审核记录信息
     */
    public AuditRecord selectAuditRecordById(Long id);

    /**
     * 查询申请审核记录列表
     *
     * @param auditRecord 申请审核记录信息
     * @return 申请审核记录集合
     */
    public List<AuditRecord> selectAuditRecordList(AuditRecord auditRecord);

    /**
     * 通过申请id获取记录集合
     *
     * @param applyId
     * @return
     */
    List<AuditRecord> selectAuditRecordByApplyId(Long applyId);

    /**
     * 新增申请审核记录
     *
     * @param auditRecord 申请审核记录信息
     * @return 结果
     */
    public int insertAuditRecord(AuditRecord auditRecord);

    /**
     * 修改申请审核记录
     *
     * @param auditRecord 申请审核记录信息
     * @return 结果
     */
    public int updateAuditRecord(AuditRecord auditRecord);

    /**
     * 删除申请审核记录信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAuditRecordByIds(String ids);

    List<AuditRecord> selectAuditRecordVo(AuditRecord selectRecord);

    int updateRecordInfo(String value, Long id, String progress, String auditStatus, String reason);
}
