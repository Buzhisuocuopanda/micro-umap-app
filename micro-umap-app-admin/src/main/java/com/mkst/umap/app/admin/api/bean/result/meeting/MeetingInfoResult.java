package com.mkst.umap.app.admin.api.bean.result.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MeetingInfoResult
 * @Description MeetingInfoResult
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-03 14:15
 */
@Data
public class MeetingInfoResult {
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
     * 会议主题
     */
    private String subject;

    /**
     * 申请状态
     */
    private String status;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 审核状态
     */
    private String auditStatusName;

    /**
     * 部门名
     */
    private String dept;

    /**
     * nowStatus
     */
    private String nowStatus;
}
