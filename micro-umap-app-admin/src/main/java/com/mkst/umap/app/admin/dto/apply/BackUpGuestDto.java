package com.mkst.umap.app.admin.dto.apply;

import io.swagger.annotations.ApiModelProperty;

public class BackUpGuestDto {
    /** 申请原因 */
    @ApiModelProperty(value = "申请原因")
    private String guestName;
    /** 申请原因 */
    @ApiModelProperty(value = "申请原因")
    private Integer guestSex;

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Integer getGuestSex() {
        return guestSex;
    }

    public void setGuestSex(Integer guestSex) {
        this.guestSex = guestSex;
    }
}
