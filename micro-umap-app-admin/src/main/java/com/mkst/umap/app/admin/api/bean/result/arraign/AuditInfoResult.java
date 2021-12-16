package com.mkst.umap.app.admin.api.bean.result.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AuditInfoResult
 * @Description 展示审核列表信息
 * @Author wangyong
 * @Date 2020-07-07 15:56
 */
@Data
public class AuditInfoResult {

    /**
     * 申请id
     */
    private Long id;

    /**
     * 申请开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /**
     * 申请结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * 申请结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private String useBy;

    private String status;

    private String reason;

    private String type;

    private String dept;
}
