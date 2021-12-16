package com.mkst.umap.app.admin.domain.vo.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MeetingWebInfoVo
 * @Description MeetingWeb列表的Vo
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-04 11:05
 */
@Data
public class MeetingWebInfoVo {
    /**
     * 会议id
     */
    private Long id;

    /**
     * 会议室名
     */
    private String roomName;
    /**
     * 房间地址
     */
    private String roomAddr;

    /**
     * startTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * endTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 会议主题
     */
    private String subject;

    /**
     * 部门名
     */
    private String dept;

    /**
     * 申请人姓名
     */
    private String useBy;
    /**
     * 创建人姓名
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 申请状态：是否取消
     */
    private String status;

    /**
     * 申请的审核状态
     */
    private String auditStatus;

    private String rAuditStatus;
}
