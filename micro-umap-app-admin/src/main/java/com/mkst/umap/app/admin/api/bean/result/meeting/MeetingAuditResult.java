package com.mkst.umap.app.admin.api.bean.result.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MeetingAuditResult
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-12 11:07
 */
@Data
public class MeetingAuditResult {

    private Long pId;
    private String mAuditStatus;
    private String pCreateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date pCreateTime;
    private Integer pProgress;
    private String pTarget;
    private String reason;
    private String rStatus;
}
