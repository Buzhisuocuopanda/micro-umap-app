package com.mkst.umap.app.common.constant;

/**
 * @ClassName SmsNoticeMsgConstant
 * @Description 短信通知模板
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-06 14:19
 */
public interface SmsNoticeMsgConstant {

    /*
     * 会议通知相关模板
     */

    /**
     * 会议信息
     */
    String MEETING_INFO = "会议部门：${dept}，会议主题：${subject}，会议时间：${dd} ${time}，会议室地址：${roomAddr}。";

    /**
     * 审核结果-通过-短信通知会议申请人
     */
    String MEETING_AUDIT_PASS_SMS_TO_USE_BY = "尊敬的${useByName}您好，您的会议申请通过审核啦!代办人${createByName}，";

    /**
     * 取消会议的短信通知
     */
    String MEETING_CANCEL_SMS_TO_USE_BY = "尊敬的${useByName}您好，您的会议申请已成功取消!";

    /**
     * 审核结果-驳回-短信通知会议申请人
     */
    String MEETING_AUDIT_FAIL_SMS_TO_USE_BY = "尊敬的${useByName}您好，您的会议未通过审核!代办人${createByName}，";

    /**
     * 审核通过-短信通知创建人
     */
    String MEETING_AUDIT_PASS_SMS_TO_CREATE_BY = "尊敬的${createByName}您好，由您代申请的会议通过审核啦!申请人${useByName}，";

    /**
     * 审核通过-短信通知创建人
     */
    String MEETING_AUDIT_FAIL_SMS_TO_CREATE_BY = "尊敬的${createByName}您好，由您代申请的会议未通过审核!申请人${useByName}，";


    /**
     * 审核通过-通知与会者
     */
    String MEETING_AUDIT_PASS_SMS_TO_ATTENDEE = "尊敬的${attendeeName}您好，${deptName}${useByName}邀请您一同参与会议，请按时到场。";

    /**
     * 会议取消-通知与会者
     */
    String MEETING_CANCEL_SMS_TO_ATTENDEE = "尊敬的${attendeeName}您好，您有一场会议已取消。";

    /**
     * 会议即将超时通知
     */
    String MEETING_OVERDUE_SMS_TO_MANAGER = "尊敬的${useByName}您好，您有一条会议申请亟待审核（会议主题：${subject}），" +
            "该申请将在一个小时内开始，逾期未审核将导致会议室被空占，请联系会议室管理员尽快进行审核！";

}
