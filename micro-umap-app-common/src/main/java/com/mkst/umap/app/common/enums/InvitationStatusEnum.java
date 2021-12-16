package com.mkst.umap.app.common.enums;

public enum InvitationStatusEnum {


    NO("未使用","0"),
    YES("已使用","1");
    private String name;
    private String value;

    InvitationStatusEnum(String name, String value){
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(int value) {
        for (InvitationStatusEnum invitationStatusEnum : InvitationStatusEnum.values()) {
            if (invitationStatusEnum.getValue().equals(value)) {
                return invitationStatusEnum.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
