package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.AuditCommonParam;
import com.mkst.umap.app.admin.api.bean.result.auditcommon.CommonAuditInfoResult;

import java.util.List;

/**
 * @ClassName IAuditCommonService
 * @Description
 * @Author wangyong
 * @Date 2020-07-14 11:40
 */
public interface IAuditCommonService {
    List<CommonAuditInfoResult> getAuditInfoList(AuditCommonParam auditCommonParam);

    int audit(AuditCommonParam auditCommonParam);
}
