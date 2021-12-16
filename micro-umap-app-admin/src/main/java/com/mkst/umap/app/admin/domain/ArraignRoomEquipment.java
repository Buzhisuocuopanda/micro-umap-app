package com.mkst.umap.app.admin.domain;

import com.mkst.mini.systemplus.common.base.BaseEntity;
import lombok.Data;

/**
 * 提审室设配表 umap_arraign_room_equipment
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Data
public class ArraignRoomEquipment extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 提审室id
	 */
	private String roomId;
	/**
	 * 设备id
	 */
	private String equipmentId;
	/**
	 * 状态
	 */
	private String status;
}
