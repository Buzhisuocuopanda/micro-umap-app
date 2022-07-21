package com.mkst.umap.app.admin.dto.apply;

import lombok.Data;

/**
 * 门锁DTO
 * 
 * @author lijinghui
 *
 */
@Data
public class DoorLockDeviceDto {

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
	 *用户名称
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
	 *端开
	 */
	private String port;
	/**
	 * 厂家
	 */
	private String manufacture;

	/**
	 * 设备类型
	 */
	private String devType;

	private String brushStatus;

	private String remarks;

	/**
	 * 房间ID
	 */
	private String roomId;
	
	private boolean flag;
}
