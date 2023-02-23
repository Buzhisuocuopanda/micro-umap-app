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
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.enums.OrderInfoEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商城订单
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:21:22
 */
@Data
@TableName("order_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商城订单")
public class OrderInfo extends Model<OrderInfo> {
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
	 * 应用类型1、小程序
	 */
	@ApiModelProperty(value = "应用类型")
	private String appType;
	/**
	 * 应用ID
	 */
	@ApiModelProperty(value = "应用ID")
	private String appId;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String userId;
	/**
	 * 用户名
	 */
	@TableField(exist = false)
	private String userName;
	/**
	 * 订单单号
	 */
	@ApiModelProperty(value = "订单单号")
	private String orderNo;
	/**
	 * 销售金额
	 */
	@ApiModelProperty(value = "销售金额")
	private BigDecimal salesPrice;
	/**
	 * 运费金额
	 */
	@ApiModelProperty(value = "运费金额")
	private BigDecimal freightPrice;
	/**
	 * 支付方式1、货到付款；2、在线支付
	 */
	@ApiModelProperty(value = "支付方式")
	private String paymentWay;
	/**
	 * 配送方式1、普通快递；2、上门自提
	 */
	@ApiModelProperty(value = "配送方式")
	private String deliveryWay;
	/**
	 * 付款方式1、微信支付
	 */
	@ApiModelProperty(value = "付款方式")
	private String paymentType;
	/**
	 * 支付金额（销售金额+运费金额-积分抵扣金额-电子券抵扣金额）
	 */
	@ApiModelProperty(value = "支付金额（销售金额+运费金额-积分抵扣金额-电子券抵扣金额）")
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
	 * 付款时间
	 */
	@ApiModelProperty(value = "付款时间")
	private LocalDateTime paymentTime;
	/**
	 * 发货时间
	 */
	@ApiModelProperty(value = "发货时间")
	private LocalDateTime deliveryTime;
	/**
	 * 收货时间
	 */
	@ApiModelProperty(value = "收货时间")
	private LocalDateTime receiverTime;
	/**
	 * 成交时间
	 */
	@ApiModelProperty(value = "成交时间")
	private LocalDateTime closingTime;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 是否支付0、未支付 1、已支付
	 */
	@ApiModelProperty(value = "是否支付0、未支付 1、已支付")
	private String isPay;
	/**
	 * 订单名
	 */
	@ApiModelProperty(value = "订单名")
	private String name;
	/**
	 * 状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭
	 */
	@ApiModelProperty(value = "状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭")
	private String status;
	/**
	 * 评价状态0、未评；1、已评；2、已追评
	 */
	@ApiModelProperty(value = "评价状态0、未评；1、已评；2、已追评")
	private String appraisesStatus;
	/**
	 * 订单状态0、已下单；1、制作中；2、可取餐
	 */
	@ApiModelProperty(value = "订单状态0、已下单；1、制作中；2、可取餐")
	private String orderStatus;
	/**
	 * 取餐号
	 */
	@ApiModelProperty(value = "取餐号")
	private String queueNumber;
	/**
	 * 买家留言
	 */
	@ApiModelProperty(value = "买家留言")
	private String userMessage;
	/**
	 * 物流id
	 */
	@ApiModelProperty(value = "物流id")
	private String logisticsId;
	/**
	 * 支付交易ID
	 */
	@ApiModelProperty(value = "支付交易ID")
	private String transactionId;
	/**
	 * 订单类型（0、普通订单；1、砍价订单；2、拼团订单；3、秒杀订单）
	 */
	@ApiModelProperty(value = "订单类型（0、普通订单；1、砍价订单；2、拼团订单；3、秒杀订单）")
	private String orderType;
	/**
	 * 营销ID（砍价ID、拼团ID）
	 */
	@ApiModelProperty(value = "营销ID（砍价ID、拼团ID）")
	private String marketId;
	/**
	 * 营销记录ID（砍价记录ID、拼团记录组ID（团长的拼团记录ID））
	 */
	@ApiModelProperty(value = "营销记录ID（砍价记录ID、拼团记录组ID（团长的拼团记录ID））")
	private String relationId;
	/**
	 * 订单详情
	 */
	@TableField(exist = false)
	private List<OrderItem> listOrderItem;
	/**
	 * 订单物流
	 */
	@TableField(exist = false)
	private OrderLogistics orderLogistics;
	/**
	 * 物流商家
	 */
	@TableField(exist = false)
	private String logistics;
	/**
	 * 物流单号
	 */
	@TableField(exist = false)
	private String logisticsNo;
	/**
	 * 用户信息
	 */
	@TableField(exist = false)
	private UserInfo userInfo;
	/**
	 * 订单来源
	 */
	@TableField(exist = false)
	private Map app;
	/**
	 * 订单状态过期时间
	 */
	@TableField(exist = false)
	private Long outTime;
	/**
	 * 状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭
	 */
	@TableField(exist = false)
	private String statusDesc;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime beginTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime endTime;

	public String getStatusDesc() {
		if (MallConstants.NO.equals(this.isPay) && this.status == null) {
			return "待付款";
		}
		if (this.status == null) {
			return null;
		}
		if (MallConstants.DELIVERY_WAY_2.equals(this.deliveryWay) && OrderInfoEnum.STATUS_1.getValue().equals(this.status)){
			return "待自提";
		}
		return OrderInfoEnum.valueOf(OrderInfoEnum.STATUS_PREFIX + "_" + this.status).getDesc();
	}
}
