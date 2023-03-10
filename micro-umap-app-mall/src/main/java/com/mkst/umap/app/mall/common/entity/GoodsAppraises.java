/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import java.time.LocalDateTime;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mkst.umap.app.mall.common.mybatis.typehandler.ArrayStringTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品评价
 *
 * @author www.joolun.com
 * @date 2019-09-23 15:51:01
 */
@Data
@TableName("goods_appraises")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品评价")
public class GoodsAppraises extends Model<GoodsAppraises> {
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
   * 逻辑删除标记（0：显示；1：隐藏）
   */
	@ApiModelProperty(value = "逻辑删除标记")
    private String delFlag;
    /**
   * 订单ID
   */
	@ApiModelProperty(value = "订单ID")
    private String orderId;
	/**
	 * 订单详情ID
	 */
	@ApiModelProperty(value = "订单详情ID")
    private String orderItemId;
    /**
   * 用户编号
   */
	@ApiModelProperty(value = "用户编号")
    private String userId;
	/**
	 * 用户昵称
	 */
	@ApiModelProperty(value = "用户昵称")
	private String nickName;
	/**
	 * 用户头像
	 */
	@ApiModelProperty(value = "用户头像")
	private String headimgUrl;
    /**
   * 商品Id
   */
	@ApiModelProperty(value = "商品Id")
    private String spuId;
    /**
   * sku_id
   */
	@ApiModelProperty(value = "sku_id")
    private String skuId;
    /**
   * 图片
   */
	@ApiModelProperty(value = "图片")
	@TableField(typeHandler = ArrayStringTypeHandler.class, jdbcType= JdbcType.VARCHAR)
	private String[] picUrls;
    /**
   * 规格信息
   */
	@ApiModelProperty(value = "规格信息")
    private String specInfo;
    /**
   * 商品评分
   */
	@ApiModelProperty(value = "商品评分")
    private Integer goodsScore;
    /**
   * 服务评分
   */
	@ApiModelProperty(value = "服务评分")
    private Integer serviceScore;
    /**
   * 物流评分
   */
	@ApiModelProperty(value = "物流评分")
    private Integer logisticsScore;
    /**
   * 评论内容
   */
	@ApiModelProperty(value = "评论内容")
    private String content;
    /**
   * 卖家回复
   */
	@ApiModelProperty(value = "卖家回复")
    private String sellerReply;
    /**
   * 回复时间
   */
	@ApiModelProperty(value = "回复时间")
    private LocalDateTime replyTime;

	@TableField(exist = false)
	private GoodsSpu goodsSpu;

	@TableField(exist = false)
	private OrderInfo orderInfo;

	@TableField(exist = false)
	private OrderItem orderItem;
}
