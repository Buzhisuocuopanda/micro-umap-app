package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName ArraignAuditVo
 * @Description
 * @Author wangyong
 * @Date 2020-07-09 21:24
 */
@Data
public class ArraignAuditDto {

    /**
     * ID
     */
    private Long id;
    /**
     * 房间ID
     */
    private String roomId;

    private String roomName;

    private String deptName;
    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    /**
     * 罪犯姓名
     */
    private String criminalName;
    /**
     * 犯罪类型
     */
    private String criminalType;
    /**
     * 罪犯生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date criminalBirth;
    /**
     * 罪犯认罪认罚
     */
    private String criminalAdmit;
    /**
     * 需要法律援助
     */
    private String needLegalAid;
    /**
     * 案件进展：批捕、已抓捕
     */
    private String stage;
    /**
     * 检察官姓名
     */
    private String prosecutorName;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 按日期查询时的日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date appointmentDate;
    /**
     * 创建人
     */
    private String undertaker;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备注
     */
    private String remark;


}
