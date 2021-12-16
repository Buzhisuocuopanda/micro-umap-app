package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

/**
 * 提审室设配表 umap_room_group
 *
 * @author wangyong
 * @date 2020-09-08
 */
@Data
public class RoomEquipment extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 房间类型
	 */
	private String roomType;
	/**
	 * 提审室id
	 */
	private String roomId;
	/**
	 * 设备id
	 */
	private String equipmentId;
	/** 设备唯一标识 */
	private String uniqueId;
	/**
	 * 状态
	 */
	private String status;

}
