package com.mkst.umap.app.common.enums;

/**
 * @Author wangyong
 * @Description 标注不同的业务类型，value建议以表名为准
 * @Date 15:27 2020-08-12
 * @Param
 * @return
 */
public enum BusinessTypeEnum {

    UMAP_INTELLECTUAL_PROPERTY("知识产权", "uamp_intellectual_property"),
    UMAP_ARRAIGN_APPOINTMENT("提审预约", "umap_arraign_appointment"),
    UMAP_SPECIAL_CASE("专案预约", "umap_special_case"),
    UMAP_OFFICE_APPLY("办公预约", "umap_office_apply"),
    UMAP_MEETING("会议预约", "umap_meeting"),
    UMAP_VACATION("请假申请", "umap_vacation"),
    UMAP_CAR_MANAGE("车辆预约", "umap_car"),
    UMAP_CANTEEN_MANAGE("食堂预约", "umap_canteen"),
    UMAP_BACKUP_MANAGE("备勤间预约", "umap_backup"),
    UMAP_IDENTITY("身份类型", "umap_identity"),
    UMAP_ARTICLE("文章", "umap_article"),
    UMAP_LHWS("龙华文书", "umap_lhws"),
    UMAP_PETITION("信访", "umap_petition"),
    UMAP_PETITION_ACCUSE("信访-控告", "umap_petition_accuse"),
    UMAP_PETITION_CRIMINAL("信访-刑事诉讼", "umap_petition_criminal"),
    UMAP_PETITION_RESCUE("信访-国家救助", "umap_petition_rescue"),
    UMAP_PETITION_COMPENSATION("信访-国家赔偿", "umap_petition_compensation"),
    UMAP_PETITION_CIVIL("信访-民行诉讼","umap_petition_civil"),
    UMAP_PETITION_LEGAL_ADVICE("信访-法律咨询","umap_petition_legal_advice"),
    UMAP_REPORT("举报","umap_report"),
    UMAP_REPORT_OTHER("举报-其他","umap_report_other"),
    UMAP_REPORT_ENVIRONMENT_RESOURCES("举报-生态环境和资源保护","umap_report_environment_resources"),
    UMAP_REPORT_FOOD_MEDICINE("举报-食品和药品安全","umap_report_food_medicine"),
    UMAP_REPORT_STATE_PROPERTY("举报-国有财产保护","umap_report_state_property"),
    UMAP_REPORT_STATE_LAND("举报-国有土地使用出让","umap_report_state_land"),
    UMAP_REPORT_HERO("举报-侵害英烈名誉","umap_report_hero"),
    UMAP_MAIL_BASE("信箱基础","umap_mail_base"),
    UMAP_MAIL_OPINION_NPC_DEPUTIES("人大代表意见","umap_mail_opinion_deputies"),
    UMAP_MAIL_OPINION_JUDICIAL("司法监督意见","umap_mail_opinion_judicial"),
    UMAP_MAIL_OPINION_CPPCC("政协委员意见","umap_mail_opinion_cppcc"),
    UMAP_MAIL_OPINION_INSPECTORS("人大代表意见","umap_mail_opinion_inspectors"),
    UMAP_MAIL_NOPUBLIC_LEGAL_ADVICE("非公经济-法律咨询","umap_mail_nopublic_legal_advice"),
    UMAP_REPAST_APPOINTMENT("就餐预约","umap_Repast_appointment"),
    UMAP_LEGAL_AUDIT("法律意见审核", "umap_legal_audit");

    private String type;
    private String value;

    BusinessTypeEnum(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
