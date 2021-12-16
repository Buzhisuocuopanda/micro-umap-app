package com.mkst.umap.app.admin.domain.vo;

import org.springframework.beans.factory.annotation.Value;

public class BackUpRoomVo {

    /** 房间id */
    private Long roomId;
    /**  房间号  */
    private String roomNum;
    /** 房间类型 ('0' 单人间    '1'  双人间)*/
    private Integer roomType;
    /**
     * 状态 （0 正常    1停用  2 使用）
     */
    private String status;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
