/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * 购物车
 *
 * @author www.joolun.com
 * @date 2019-08-29 21:27:33
 */
@Data
@TableName("shopping_cart")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "购物车")
public class ShoppingCart extends Model<ShoppingCart> {
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
   * 用户编号
   */
	@ApiModelProperty(value = "用户编号")
    private String userId;
    /**
   * 商品 SPU 编号
   */
	@ApiModelProperty(value = "spuId")
    private String spuId;
    /**
   * 商品 SKU 编号
   */
	@ApiModelProperty(value = "skuId")
    private String skuId;
    /**
   * 商品购买数量
   */
	@ApiModelProperty(value = "商品购买数量")
    private Integer quantity;
	/**
	 * 加入时价格
	 */
	@ApiModelProperty(value = "加入时价格")
	private BigDecimal addPrice;
	/**
	 * 加入时的spu名字
	 */
	@ApiModelProperty(value = "加入时的spu名字")
	private String spuName;
	/**
	 * 加入时的规格信息
	 */
	@ApiModelProperty(value = "加入时的规格信息")
	private String specInfo;
	/**
	 * 图片
	 */
	@ApiModelProperty(value = "图片")
	private String picUrl;

	@TableField(exist = false)
	private GoodsSpu goodsSpu;

	@TableField(exist = false)
	private GoodsSku goodsSku;

	@TableField(exist = false)
	private List<GoodsSkuSpecValue> specs;
}
