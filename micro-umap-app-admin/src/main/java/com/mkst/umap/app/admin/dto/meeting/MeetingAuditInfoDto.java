package com.mkst.umap.app.admin.dto.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MeetingAuditInfoDto
 * @Description 会议管理-会议-我的审批
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-13 14:27
 */
@Data
public class MeetingAuditInfoDto {

    private Long id;

    private String useBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String auditStatus;

    private String status;

    private String rAuditStatus;

}
