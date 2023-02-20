/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;

/**
 * 积分变动记录
 *
 * @author www.joolun.com
 * @date 2019-12-05 19:47:22
 */
@Data
@TableName("points_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "积分变动记录")
public class PointsRecord extends Model<PointsRecord> {
	private static final long serialVersionUID = 1L;

	/**
	 * PK
	 */
	@ApiModelProperty(value = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户")
	private String tenantId;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	@ApiModelProperty(value = "逻辑删除标记")
	private String delFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后更新时间
	 */
	@ApiModelProperty(value = "最后更新时间")
	private LocalDateTime updateTime;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private String userId;
	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer amount;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;
	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID")
	private String spuId;
	/**
	 * 订单详情ID
	 */
	@ApiModelProperty(value = "订单详情ID")
	private String orderItemId;
	/**
	 * 记录类型1、系统初始化；2、商品赠送；3、退款赠送减回；4、商品抵扣；5、退款抵扣加回
	 */
	@ApiModelProperty(value = "记录类型")
	private String recordType;
	@TableField(exist = false)
	private UserInfo userInfo;
}
