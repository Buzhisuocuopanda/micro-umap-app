package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.api.bean.param.AuditCommonParam;
import com.mkst.umap.app.admin.api.bean.param.SpecialCaseAuditParam;
import com.mkst.umap.app.admin.api.bean.result.auditcommon.CommonAuditInfoResult;
import com.mkst.umap.app.admin.mapper.AuditCommonMapper;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;
import com.mkst.umap.app.admin.service.IAuditCommonService;
import com.mkst.umap.app.admin.service.ISpecialCaseAppointmentService;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AuditCommonServiceImpl
 * @Description
 * @Author wangyong
 * @Date 2020-07-14 11:41
 */
@Service
public class AuditCommonServiceImpl implements IAuditCommonService {

    @Autowired
    private AuditCommonMapper auditCommonMapper;
    @Autowired
    private IArraignAppointmentService arraignAppointmentService;
    @Autowired
    private ISpecialCaseAppointmentService specialCaseAppointmentService;

    @Override
    public List<CommonAuditInfoResult> getAuditInfoList(AuditCommonParam auditCommonParam) {
        return auditCommonMapper.getAuditInfoList(auditCommonParam);
    }

    @Override
    public int audit(AuditCommonParam auditCommonParam) {

        String type = auditCommonParam.getType();
        int row = 0;

        if (type.equals(AuditRecordTypeEnum.ArraignAudit.getValue())) {
            row = doArraignAudit(auditCommonParam);
        } else if (type.equals(AuditRecordTypeEnum.SpecialCaseAudit.getValue())) {
            row = doSpecialAudit(auditCommonParam);
        }
        return row;
    }

    private int doSpecialAudit(AuditCommonParam auditCommonParam) {

        SpecialCaseAuditParam auditParam = new SpecialCaseAuditParam();

        auditParam.setIds(Convert.toLongArray(auditCommonParam.getIds()));
        auditParam.setUpdateBy(auditCommonParam.getOperator());
        auditParam.setReason(auditCommonParam.getReason());
        auditParam.setStatus(auditCommonParam.getStatus());

        return specialCaseAppointmentService.auditCaseList(auditParam);
    }

    private int doArraignAudit(AuditCommonParam auditCommonParam) {
        return arraignAppointmentService.updateStatusByIds(auditCommonParam.getIds(), auditCommonParam.getType(), auditCommonParam.getStatus(), auditCommonParam.getReason(), auditCommonParam.getOperator());
    }
}
