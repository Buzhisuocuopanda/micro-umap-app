package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.AuditCommonParam;
import com.mkst.umap.app.admin.api.bean.result.auditcommon.CommonAuditInfoResult;

import java.util.List;

/**
 * @author wangyong
 */
public interface AuditCommonMapper {

    List<CommonAuditInfoResult> getAuditInfoList(AuditCommonParam auditCommonParam);

}
