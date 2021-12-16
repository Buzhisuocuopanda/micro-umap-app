package com.mkst.umap.app.common.enums;

/**
 * @author wangyong
 */

public enum PetitionPersonnelTypeEnum {

    ACCUSE_ACCUSER("控告-控告人","accuse_accuser"),
    ACCUSE_DEFENDANT("控告-被告人","accuse_defendant"),
    CRIMINAL_COMPLAINT("刑事申诉-申诉人","criminal_complaint"),
    CRIMINAL_RESPONDENT("刑事申诉-被申诉人","criminal_respondent"),
    RESCUE_RESCUER("国家救助-救助人","rescue_rescuer"),
    COMPENSATION_APPLICANT("国家赔偿-申请人","compensation_applicant"),
    COMPENSATION_AGENT("国家赔偿-代理人","compensation_agent"),
    CIVIL_APPLICANT("民行申诉-申请人","civil_applicant"),
    CIVIL_AGENT("民行申诉-代理人","civil_agent"),
    CIVIL_RESPONDENT("民行申诉-被申请人","cavil_respondent");

    private String type;
    private String value;

    PetitionPersonnelTypeEnum(String type, String value) {
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
