package com.mkst.umap.app.admin.dto.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MeetingAuditListInfoDto
 * @Description 审核进度展示列表
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-11 18:45
 */
@Data
public class MeetingAuditProgressInfoDto {
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("mAuditStatus")
    private String mAuditStatus;
    @JsonProperty("pCreateBy")
    private String pCreateBy;
    @JsonProperty("pCreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date pCreateTime;
    @JsonProperty("pProgress")
    private Integer pProgress;
    @JsonProperty("pTarget")
    private String pTarget;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("rStatus")
    private String rStatus;
    @JsonProperty("roleName")
    private String roleName;
    @JsonProperty("userName")
    private String userName;
}
