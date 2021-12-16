package com.mkst.umap.app.common.enums;

/**
 * @author wangyong
 */
public enum PetitionCivilMatterTypeEnum {

    FIRST_INSTANCE("一审","first_instance"),
    SECOND_INSTANCE("二审","second_instance"),
    RETRIAL_INSTANCE("再审","retrial_instance"),
    LAST_INSTANCE("终审","last_instance");

    private String type;
    private String value;

    PetitionCivilMatterTypeEnum(String type, String value) {
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
