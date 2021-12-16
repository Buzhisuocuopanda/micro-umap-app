package com.mkst.umap.app.admin.dto.device;

import lombok.Data;

/**
 * @ClassName DeviceInfoDto
 * @Description 设备信息
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-08 20:35
 */
@Data
public class DeviceInfoDto {
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
     *
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

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

    private String devType;

    private String brushStatus;
}
