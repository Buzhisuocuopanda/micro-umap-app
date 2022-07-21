package com.mkst.umap.app.admin.dto.apply;

import java.io.Serializable;

import lombok.Data;

/**
 * 单个房间申请数量DTO
 * 
 * @author gin
 *
 */
@Data
public class ApplyNumberDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String roomId;

	private Integer applyNumber;
}
