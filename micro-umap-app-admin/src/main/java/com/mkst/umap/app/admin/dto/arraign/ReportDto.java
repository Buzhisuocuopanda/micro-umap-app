package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 报表dto
 */
@Data
public class ReportDto extends BaseEntity {

    //统计类型
    private String totalType;

    //报表查询日期格式类型(年报 DATE_FORMAT(start_time,"%Y")
    // 月报 DATE_FORMAT(start_time,"%Y%m")  季报 FLOOR((date_format(a.day, '%m')+2)/3)) quarter
    // 周报 DATE_FORMAT(start_time,'%Y%u'))  日报 DATE_FORMAT(postDateTime,'%Y年%m月%d日')
    private String dateType;

    //查到数据的 总数
    private Integer count;

    //部门分组
    private Long deptId;
    //部门分组
    private String deptName;

    //日期格式输出
    private String dateFormat;


    //部门idList
    private Long[] deptIds;

    //前端搜索条件
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String criminalName;

    private String criminalType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date criminalBirth;

    private String criminalAdmit;

    private String needLegalAid;

    private String stage;

    private String prosecutorId;

    private Long prosecutorUserId;

    private String prosecutorName;

    private String status;


    private String delFlag;

    private List<String> criminalTypeList;
}
