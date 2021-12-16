package com.mkst.umap.app.admin.api.bean.result.auditcommon;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AuditInfoResult
 * @Description
 * @Author wangyong
 * @Date 2020-07-14 12:01
 */
@Data
public class CommonAuditInfoResult {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date endTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private String useBy;

    private String title;

    private String status;

    private String type;

}
