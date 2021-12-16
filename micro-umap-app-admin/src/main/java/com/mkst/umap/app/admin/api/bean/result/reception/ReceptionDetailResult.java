package com.mkst.umap.app.admin.api.bean.result.reception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ReceptionDetailResult
 * @Description 接待申请-申请详情
 * @Author wangyong
 * @Date 2020-07-09 10:58
 */
@Data
public class ReceptionDetailResult {
    /**
     * id
     */
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
    /**
     * type
     */
    private String type;
    /**
     * deptName
     */
    private String dept;
    /**
     * roomName
     */
    private String roomName;
    /**
     * 状态：取消、未取消
     */
    private String status;
    /**
     * 使用人
     */
    private String userName;

}
