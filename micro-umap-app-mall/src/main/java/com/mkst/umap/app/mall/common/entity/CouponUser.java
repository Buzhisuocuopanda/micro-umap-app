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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * 电子券用户记录
 *
 * @author www.joolun.com
 * @date 2019-12-17 10:43:53
 */
@Data
@TableName("coupon_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "电子券用户记录")
public class CouponUser extends Model<CouponUser> {
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
	 * 创建者ID
	 */
	@ApiModelProperty(value = "创建者ID")
	private String createId;
	/**
	 * 电子券ID
	 */
	@ApiModelProperty(value = "电子券ID")
	private String couponId;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String userId;
	/**
	 * 电子券码
	 */
	@ApiModelProperty(value = "电子券码")
	private Integer couponCode;
	/**
	 * 状态0、未使用；1、已使用
	 */
	@ApiModelProperty(value = "状态0、未使用；1、已使用")
	private String status;
	/**
	 * 使用时间
	 */
	@ApiModelProperty(value = "使用时间")
	private LocalDateTime usedTime;
	/**
	 * 有效开始时间（固定时间段特有）
	 */
	@ApiModelProperty(value = "有效开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date validBeginTime;
	/**
	 * 有效结束时间（固定时间段特有）
	 */
	@ApiModelProperty(value = "有效结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date validEndTime;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 类型1、代金券；2：折扣券
	 */
	@ApiModelProperty(value = "类型1、代金券；2：折扣券")
	private String type;
	/**
	 * 订单金额满多少可使用
	 */
	@ApiModelProperty(value = "订单金额满多少可使用")
	private BigDecimal premiseAmount;
	/**
	 * 减免金额（代金券特有）
	 */
	@ApiModelProperty(value = "减免金额")
	private BigDecimal reduceAmount;
	/**
	 * 折扣率（折扣券特有）
	 */
	@ApiModelProperty(value = "折扣率")
	private BigDecimal discount;
	/**
	 * 适用类型1、全部商品；2、指定商品可用
	 */
	@ApiModelProperty(value = "适用类型1、全部商品；2、指定商品可用")
	private String suitType;

	@TableField(exist = false)
	private CouponInfo couponInfo;

	@TableField(exist = false)
	private UserInfo userInfo;

	@TableField(exist = false)
	private GoodsSpu goodsSpu;

	@TableField(exist = false)
	private List<String> spuIds;

	public String getStatus() {
		if(MallConstants.COUPON_USER_STATUS_0.equals(status) && this.validEndTime != null){
			if(this.validEndTime.after(new Date())){
				return status;
			}else{
				return MallConstants.COUPON_USER_STATUS_2;
			}
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
