package com.mkst.umap.app.admin.statistics;

import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.service.IVisitApplyService;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("visitAnalysisData")
public class VisitAnalysisData implements AnalysisDataBase{

    @Autowired
    private IVisitApplyService visitApplyService;
    @Override
    public AppAnalysisResult getData(Map<String, Object> params) {
        List<AnalysisCountResult> list = visitApplyService.analysisData(params);
        AppAnalysisResult appAnalysisResult = new AppAnalysisResult();
        appAnalysisResult.setList(list);
        VisitApply visitApply = new VisitApply();
        visitApply.setStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString());
        List<VisitApply> visitApplies = visitApplyService.selectVisitApplyList(visitApply);
        if(CollectionUtils.isEmpty(visitApplies)){
            appAnalysisResult.setTotal(0L);
        }else {
            appAnalysisResult.setTotal((long) visitApplies.size());
        }
        visitApply.setStartTime(new Date());
        List<VisitApply> vis = visitApplyService.selectVisitApplyList(visitApply);
        if(CollectionUtils.isEmpty(vis)){
            appAnalysisResult.setToday(0);
        }else {
            appAnalysisResult.setToday(vis.size());
        }
        return appAnalysisResult;
    }
}
