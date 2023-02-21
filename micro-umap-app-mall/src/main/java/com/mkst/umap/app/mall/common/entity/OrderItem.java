/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mkst.umap.app.mall.common.enums.OrderItemEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商城订单详情
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:31:40
 */
@Data
@TableName("order_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商城订单详情")
public class OrderItem extends Model<OrderItem> {
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
	@TableField(fill = FieldFill.INSERT)
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
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderId;
	/**
	 * 商品Id
	 */
	@ApiModelProperty(value = "商品Id")
	private String spuId;
	/**
	 * 商品名
	 */
	@ApiModelProperty(value = "商品名")
	private String spuName;
	/**
	 * 规格信息
	 */
	@ApiModelProperty(value = "规格信息")
	private String specInfo;
	/**
	 * skuId
	 */
	@ApiModelProperty(value = "skuId")
	private String skuId;
	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片")
	private String picUrl;
	/**
	 * 商品数量
	 */
	@ApiModelProperty(value = "商品数量")
	private Integer quantity;
	/**
	 * 购买单价
	 */
	@ApiModelProperty(value = "购买单价")
	private BigDecimal salesPrice;
	/**
	 * 运费金额
	 */
	@ApiModelProperty(value = "运费金额")
	private BigDecimal freightPrice;
	/**
	 * 支付金额（购买单价*商品数量+运费金额-积分抵扣金额-电子券抵扣金额）
	 */
	@ApiModelProperty(value = "支付金额（购买单价*商品数量+运费金额-积分抵扣金额-电子券抵扣金额）")
	private BigDecimal paymentPrice;
	/**
	 * 支付积分
	 */
	@ApiModelProperty(value = "支付积分")
	private Integer paymentPoints;
	/**
	 * 电子券支付金额
	 */
	@ApiModelProperty(value = "电子券支付金额")
	private BigDecimal paymentCouponPrice;
	/**
	 * 积分抵扣金额
	 */
	@ApiModelProperty(value = "积分抵扣金额")
	private BigDecimal paymentPointsPrice;
	/**
	 * 用户电子券ID
	 */
	@ApiModelProperty(value = "用户电子券ID")
	private String couponUserId;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 状态1：退款中；2、拒绝退款；3、同意退款
	 */
	@ApiModelProperty(value = "状态1：退款中；2、拒绝退款；3、同意退款")
	private String status;
	/**
	 * 是否退款0:否 1：是
	 */
	@ApiModelProperty(value = "是否退款0:否 1：是")
	private String isRefund;
	/**
	 * 状态1：申请退款；2、拒绝退款；3、同意退款
	 */
	@ApiModelProperty(value = "状态")
	@TableField(exist = false)
	private String statusDesc;
	/**
	 * 自提地址
	 */
	@TableField(exist = false)
	private DeliveryPlace deliveryPlace;

	public String getStatusDesc() {
		if (this.status == null) {
			return null;
		}
		return OrderItemEnum.valueOf(OrderItemEnum.STATUS_PREFIX + "_" + this.status).getDesc();
	}

	@TableField(exist = false)
	private List<OrderRefunds> listOrderRefunds;
}
