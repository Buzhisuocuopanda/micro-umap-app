/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀商品
 *
 * @author www.joolun.com
 * @date 2020-08-12 11:03:50
 */
@Data
@TableName("seckill_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "秒杀商品")
public class SeckillInfo extends Model<SeckillInfo> {
    private static final long serialVersionUID=1L;

    /**
     * PK
     */
    @TableId(type = IdType.ASSIGN_ID)
    @NotNull(message = "PK不能为空")
    @ApiModelProperty(value = "PK")
    private String id;
    /**
     * 所属租户
     */
    @NotNull(message = "所属租户不能为空")
    @ApiModelProperty(value = "所属租户")
	@TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 逻辑删除标记（0：显示；1：隐藏）
     */
    @NotNull(message = "逻辑删除标记（0：显示；1：隐藏）不能为空")
    @ApiModelProperty(value = "逻辑删除标记（0：显示；1：隐藏）")
    private String delFlag;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @NotNull(message = "最后更新时间不能为空")
    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime updateTime;
    /**
     * 创建者ID
     */
    @ApiModelProperty(value = "创建者ID")
    private String createId;
    /**
     * 排序字段
     */
    @NotNull(message = "排序字段不能为空")
    @ApiModelProperty(value = "排序字段")
    private Integer sort;
    /**
     * （1：开启；0：关闭）
     */
    @NotNull(message = "（1：开启；0：关闭）不能为空")
    @ApiModelProperty(value = "（1：开启；0：关闭）")
    private String enable;
    /**
     * 商品Id
     */
    @NotNull(message = "商品Id不能为空")
    @ApiModelProperty(value = "商品Id")
    private String spuId;
    /**
     * skuId
     */
    @NotNull(message = "skuId不能为空")
    @ApiModelProperty(value = "skuId")
    private String skuId;
    /**
     * 秒杀名称
     */
    @ApiModelProperty(value = "秒杀名称")
    private String name;
    /**
     * 秒杀底价
     */
    @NotNull(message = "秒杀底价不能为空")
    @ApiModelProperty(value = "秒杀底价")
    private BigDecimal seckillPrice;
    /**
     * 已售数量
     */
    @ApiModelProperty(value = "已售数量")
    private Integer seckillNum;
    /**
     * 总限购数量
     */
    @ApiModelProperty(value = "总限购数量")
    private Integer limitNum;
	/**
	 * 每人限购数量
	 */
	@ApiModelProperty(value = "每人限购数量")
	private Integer eachLimitNum;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String picUrl;
    /**
     * 秒杀规则
     */
    @ApiModelProperty(value = "秒杀规则")
    private String seckillRule;
    /**
     * 分享标题 
     */
    @NotNull(message = "分享标题 不能为空")
    @ApiModelProperty(value = "分享标题 ")
    private String shareTitle;
	/**
	 * 版本号
	 */
	@Version
	private Integer version;

	@TableField(exist = false)
	private GoodsSpu goodsSpu;

	@TableField(exist = false)
	private GoodsSku goodsSku;

	@TableField(exist = false)
	private SeckillHall seckillHall;

	@TableField(exist = false)
	private SeckillHallInfo seckillHallInfo;
}
