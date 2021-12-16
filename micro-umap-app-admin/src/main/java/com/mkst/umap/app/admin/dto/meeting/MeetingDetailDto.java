package com.mkst.umap.app.admin.dto.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * @ClassName MeetingDetailDto
 * @Description MeetingDetailDto
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-03 17:14
 */
@Data
public class MeetingDetailDto {

    private Long id;

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

    private String roomName;
    private String roomId;

    /**
     * 会议主题
     */
    private String subject;

    /**
     * 部门名
     */
    private String dept;

    private String useBy;

    private Long useById;

    private String status;

    private String auditStatus;

    private Integer auditProgress;

    private String canAudit;

    private String nextRole;

    private ArrayList<String> attendeeArr;
}
