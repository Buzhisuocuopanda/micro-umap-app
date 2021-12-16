package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.annotation.Excel;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;
@Data
public class ArraignAppointmentTotalDto extends BaseEntity {

    private Long id;

    //报表类型 (noReport 只按时间分组  deptReport 按部门分组   prosecutorReport 按检察官分组   allReport 按部门和检察官分组 )
    private String reportType;

    //报表查询日期格式类型(年报 DATE_FORMAT(start_time,"%Y")
    // 月报 DATE_FORMAT(start_time,"%Y%m")  季报 FLOOR((date_format(a.day, '%m')+2)/3)) quarter
    // 周报 DATE_FORMAT(start_time,'%Y%u'))  日报 DATE_FORMAT(postDateTime,'%Y年%m月%d日')
    private String dateType;

    /**
     * 排班id
     */
    private Long scheduleId;
    /**
     * 房间ID
     */
    private String roomId;
    //统计类型
    private String totalType;

    //查到数据的 总数
    private Integer value;

    //查到数据的分组名
    private String name;


    //线形图 开始时间轴
    private String startTimeAxis;

    //结束时间轴
    private String endTimeAxis;



    //前端搜索条件
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

    private String prosecutorName;

    private Long prosecutorUserId;

    private String status;

    private String appointmentDate;

    private String needDeal;

    private String delFlag;
}
