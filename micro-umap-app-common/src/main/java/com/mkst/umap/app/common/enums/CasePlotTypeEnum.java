package com.mkst.umap.app.common.enums;

/**
 * @ClassName CasePlotTypeEnum
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/23 0023 上午 9:56
 */
public enum CasePlotTypeEnum {

    PARAM("参数考量",1 , 1),
    SEVERITY("从轻情节",3 , 2),
    LIGHTER("从重情节",4, 2),
    CRIMINAL_TYPE("犯罪类型",5, 2)
    ;

    private String name;
    private Integer value;
    private Integer type;

    CasePlotTypeEnum(String name , Integer value ,Integer  type){
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static String getName(int value) {
        for (CasePlotTypeEnum applyStatusEnum : CasePlotTypeEnum.values()) {
            if (applyStatusEnum.getValue() == value) {
                return applyStatusEnum.name;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
