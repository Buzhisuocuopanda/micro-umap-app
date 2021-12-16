package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @ClassName ArraignRoomInfo
 * @Description 提审室信息
 * @Author WangYong
 * @Date 2020-06-16 10:11
 */
public class ArraignRoomInfoDto {
    private String roomName;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date endTime;
    private String status;
    private String useBy;
    private String deptName;
    private Long appointmentId;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUseBy() {
        return useBy;
    }

    public void setUseBy(String useBy) {
        this.useBy = useBy;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    /*public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }*/
}
