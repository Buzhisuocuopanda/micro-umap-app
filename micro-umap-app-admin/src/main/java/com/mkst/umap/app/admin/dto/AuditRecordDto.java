package com.mkst.umap.app.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName AuditRecordDto
 * @Description 审核记录
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-24 14:49
 */
@Data
public class AuditRecordDto {
    private Long id;
    private String status;
    private String reason;
    private String auditBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;
}
