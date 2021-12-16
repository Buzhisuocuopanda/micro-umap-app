package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.OfficeApplyAudit;
import com.mkst.umap.app.admin.mapper.OfficeApplyAuditMapper;
import com.mkst.umap.app.admin.service.IOfficeApplyAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 办公申请审批 服务层实现
 *
 * @author wangyong
 * @date 2020-08-07
 */
@Service
public class OfficeApplyAuditServiceImpl implements IOfficeApplyAuditService {
    @Autowired
    private OfficeApplyAuditMapper officeApplyAuditMapper;

    /**
     * 查询办公申请审批信息
     *
     * @param id 办公申请审批ID
     * @return 办公申请审批信息
     */
    @Override
    public OfficeApplyAudit selectOfficeApplyAuditById(Long id) {
        return officeApplyAuditMapper.selectOfficeApplyAuditById(id);
    }

    /**
     * 查询办公申请审批列表
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 办公申请审批集合
     */
    @Override
    public List<OfficeApplyAudit> selectOfficeApplyAuditList(OfficeApplyAudit officeApplyAudit) {
        return officeApplyAuditMapper.selectOfficeApplyAuditList(officeApplyAudit);
    }

    /**
     * 新增办公申请审批
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 结果
     */
    @Override
    public int insertOfficeApplyAudit(OfficeApplyAudit officeApplyAudit) {
        return officeApplyAuditMapper.insertOfficeApplyAudit(officeApplyAudit);
    }

    /**
     * 修改办公申请审批
     *
     * @param officeApplyAudit 办公申请审批信息
     * @return 结果
     */
    @Override
    public int updateOfficeApplyAudit(OfficeApplyAudit officeApplyAudit) {
        return officeApplyAuditMapper.updateOfficeApplyAudit(officeApplyAudit);
    }

    /**
     * 删除办公申请审批对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOfficeApplyAuditByIds(String ids) {
        return officeApplyAuditMapper.deleteOfficeApplyAuditByIds(Convert.toStrArray(ids));
    }

}
