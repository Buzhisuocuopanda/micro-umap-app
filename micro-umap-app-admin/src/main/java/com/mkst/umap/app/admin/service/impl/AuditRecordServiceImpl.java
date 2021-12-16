package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 申请审核记录 服务层实现
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Service
public class AuditRecordServiceImpl implements IAuditRecordService {
    @Autowired
    private AuditRecordMapper auditRecordMapper;

    /**
     * 查询申请审核记录信息
     *
     * @param id 申请审核记录ID
     * @return 申请审核记录信息
     */
    @Override
    public AuditRecord selectAuditRecordById(Long id) {
        return auditRecordMapper.selectAuditRecordById(id);
    }

    /**
     * 查询申请审核记录列表
     *
     * @param auditRecord 申请审核记录信息
     * @return 申请审核记录集合
     */
    @Override
    public List<AuditRecord> selectAuditRecordList(AuditRecord auditRecord) {
        return auditRecordMapper.selectAuditRecordList(auditRecord);
    }

    @Override
    public List<AuditRecord> selectAuditRecordByApplyId(Long applyId){
        return auditRecordMapper.selectAuditRecordByApplyId(applyId);
    }

    /**
     * 新增申请审核记录
     *
     * @param auditRecord 申请审核记录信息
     * @return 结果
     */
    @Override
    public int insertAuditRecord(AuditRecord auditRecord) {
        String updateBy = auditRecord.getUpdateBy();
        String createBy = auditRecord.getCreateBy();

        if (updateBy == null || updateBy.isEmpty()) {
            auditRecord.setUpdateBy(ShiroUtils.getLoginName());
        }

        if (createBy == null || createBy.isEmpty()) {
            auditRecord.setCreateBy(ShiroUtils.getLoginName());
        }

        return auditRecordMapper.insertAuditRecord(auditRecord);
    }

    /**
     * 修改申请审核记录
     *
     * @param auditRecord 申请审核记录信息
     * @return 结果
     */
    @Override
    public int updateAuditRecord(AuditRecord auditRecord) {
        return auditRecordMapper.updateAuditRecord(auditRecord);
    }

    /**
     * 删除申请审核记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditRecordByIds(String ids) {
        return auditRecordMapper.deleteAuditRecordByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<AuditRecord> selectAuditRecordVo(AuditRecord selectRecord) {
        return auditRecordMapper.selectAuditRecordVo(selectRecord);
    }

    @Override
    public int updateRecordInfo(String type, Long id, String progress, String auditStatus, String reason) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setProgress(progress);
        auditRecord.setApplyType(type);
        auditRecord.setApplyId(id);
        List<AuditRecord> auditRecords = this.selectAuditRecordList(auditRecord);

        if (CollectionUtil.isEmpty(auditRecords) || auditRecords.size() > 1){
            throw new IndexOutOfBoundsException("本应只有一条结果，但出现了多条或0条！");
        }

        AuditRecord auditRecordNow = auditRecords.get(0);
        auditRecordNow.setStatus(auditStatus);
        auditRecordNow.setReason(reason);

        return this.updateAuditRecord(auditRecordNow);
    }
}
