package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "预约申请")
public class VisitParam {

    /** 姓名 */
    private String name;
    /** 电话 */
    private String phone;
    /** 证件类型 */
    private String certificateType;
    /** 证件号 */
    private String certificateCode;
    /** 身体状况 */
    private String bodyStatus;
    /** 体温 */
    private String temperature;
    /** 是否入境人员 */
    private String entry;
    /** 是否有接触史 */
    private String contact;
    /**  */
    private String userType;
    /** 邀请码 */
    private String invitationCode;
    /** 开始时间 */
    private Date startTime;
    /** 结束时间 */
    private Date endTime;
    /** 邀请人 */
    private Integer invitationUser;
    /** 状态 */
    private String status;
    /** 拜访事由 */
    private String reason;

    private String company;

    private String type;

    /** 备注 */
    private String reamrk;

    private String visitPhone;

}
