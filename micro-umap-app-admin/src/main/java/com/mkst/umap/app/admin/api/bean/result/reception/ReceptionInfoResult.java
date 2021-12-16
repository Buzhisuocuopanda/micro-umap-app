package com.mkst.umap.app.admin.api.bean.result.reception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ReceptionInfo
 * @Description 接待申请简短信息
 * @Author wangyong
 * @Date 2020-07-08 18:58
 */
@Data
public class ReceptionInfoResult {

    /**
     * id
     */
    private Long id;

    private String roomName;

    /**
     * startTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /**
     * endTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /**
     * type
     */
    private String type;
    /**
     * type
     */
    private String typeName;

    /**
     * deptName
     */
    private String dept;

    /**
     * nowStatus
     */
    private String nowStatus;

    /**
     * 审核状态
     */
    private String auditName;
    /**
     * 审核状态
     */
    private String auditStatus;
    /**
     * 使用人姓名
     */
    private String useByName;
}
