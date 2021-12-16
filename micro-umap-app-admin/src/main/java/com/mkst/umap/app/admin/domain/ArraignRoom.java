package com.mkst.umap.app.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 提审室表 umap_arraign_room
 *
 * @author lijinghui
 * @date 2020-06-12
 */
@Data
public class ArraignRoom extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 提审室id
     */
    private String id;
    /**
     * 提审室id
     */
    private String type;
    /**
     * 房间名
     */
    private String name;
    /**
     * 房间地址
     */
    private String address;
    /**
     * 管理部门
     */
    private Integer deptId;
    /** 屏幕id */
    private String screenId;
    /**
     * 管理人
     */
    private String managerId;
    /**
     * 工作开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date workStartTime;
    /**
     * 工作结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date workEndTime;
    /**
     * 休息开始时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date restStartTime;
    /**
     * 休息结束时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date restEndTime;


    /**
     * 最大同时预约数
     */
    private Integer capacity;

    /**
     * 容纳人数
     */
    private Integer galleryful;

    private String duration;
    /**
     * 提审室状态
     */
    private String status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String reamrk;
    /**
     * 逻辑删除标识（0：正常 1：删除）
     */
    private String delFlag;


}
