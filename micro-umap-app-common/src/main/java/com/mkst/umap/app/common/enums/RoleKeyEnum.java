package com.mkst.umap.app.common.enums;

public enum RoleKeyEnum {

    CLEAN_PERSON("清洁人员", "qjry"),
    ROLE_ADMIN("备勤间管理员", "bqgly"),
    ROLE_COMMON("普通角色", "apply_common"),
    ROLE_KITCHEN_ADMIN("厨房管理员", "cfgly"),
    ROLE_CAR_ADMIN("车辆管理员", "clgly"),
    ROLE_LOGISTICS_ADMIN("后勤管理员", "hqgly"),
    ROLE_LEADERSHIP("领导", "apply_leadership"),
    ROLE_ZSCQHF("知识产权回复", "zscqhf"),
    ROLE_KGSSHF("控告申诉回复", "kgsshf"),
    ROLE_SPTSGLY("视频提审管理员", "sptsgly"),
    ROLE_ZAGLY("专案管理员", "zagly"),
    ROLE_HYSGLY("会议室管理员", "hysgly"),
    ROLE_BGSQGLY("办公申请管理员", "bgsqgly"),
    ROLE_QJSQGLY("请假申请管理员", "qjsqgly"),
    ROLE_XFHF("信访回复专员", "admin:petition"),
    ROLE_JBHF("举报回复专员回复", "admin:report");


    private String name;
    private String value;

    RoleKeyEnum(String name, String value) {
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
