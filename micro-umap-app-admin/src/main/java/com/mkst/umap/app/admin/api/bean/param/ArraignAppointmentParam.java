package com.mkst.umap.app.admin.api.bean.param;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName RoomScheduleParam
 * @Description
 * @Author hsw
 * @Date 2020-06-17 17:35
 */
@Data
public class ArraignAppointmentParam {

    private String roomId;
    private Date startTime;
    private Date endTime;
    private String criminalName;
    private String criminalType;
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
    private Long prosecutorUserId;
    private String prosecutorId;
    private String prosecutorName;
    private String year;
    private String month;
    private String createBy;
    private String status;
    /**
     * @Description 当前时间状态（0：未开始 1：进行中 2：已结束）
     */
    private String nowStatus;

    /**
     * @Description 提审类型（0：办案提审 1：社工调查）
     */
    private String arraignType;

    private Date checkDate;

    private String remark;
    private String passStatus;
    private String timePie;

    private String plus;//加号 1表示加号 0表示正常 默认为0
}
