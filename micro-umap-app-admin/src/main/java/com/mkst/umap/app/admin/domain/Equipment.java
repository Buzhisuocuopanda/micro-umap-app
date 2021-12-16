package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 设备表 p_face_equipment
 *
 * @author systemplus
 * @date 2019-03-01
 */
@Data
public class Equipment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String id;
    /**
     * 设备编码
     */
    private String devNo;
    /**
     * 设备名称
     */
    private String devName;
    /**
     * 序列号
     */
    private String serialNo;
    /**
     * 设备型号
     */
    private String devModel;
    /**
     * 地址
     */
    private String devPosition;
    /**
     * 认证方式
     */
    private String identification;
    /**
     *
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 备注信息
     */
    private String remarks;
    /**
     * 删除标记
     */
    private String delFlag;
    /**
     * IP
     */
    private String ipAddress;
    /**
     * 是否开启
     */
    private String isOpen;
    /**
     *
     */
    private String port;
    /**
     * 厂家
     */
    private String manufacture;
    /**
     *
     */
    private String devType;

    /**
     *
     */
    private String groupId;

    /**
     *
     */
    private String brushStatus;

    private String[] brushStatusList;

}
