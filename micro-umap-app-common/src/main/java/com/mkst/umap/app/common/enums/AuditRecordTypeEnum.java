package com.mkst.umap.app.common.enums;

public enum AuditRecordTypeEnum {


    BackUpAudit("备勤间审核", "0"),
    ArraignAudit("远程视频提审", "1"),
    CanteenLogisticsAudit("食堂后勤审核", "8"),
    CanteenKitchenAudit("食堂厨房审核", "9"),
    CarAudit("车辆审核", "4"),
    SpecialCaseAudit("专案", "2"),
    MeetingRoomAudit("会议申请审核", "5"),
    OfficeApplyAudit("办公申请审核", "6"),
    VacationApplyAudit("请假申请审核", "7"),
    INDUCTIONAUDIT("入职查询审核","INDUCTION"),
    CANTEEN_AUDIT("食堂审核","canteen");


    private String name;
    private String value;

    AuditRecordTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
