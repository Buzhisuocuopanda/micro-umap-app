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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mkst.umap.app.mall.common.constant.MallConstants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价
 *
 * @author www.joolun.com
 * @date 2019-12-28 18:07:51
 */
@Data
@TableName("bargain_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "砍价")
public class BargainInfo extends Model<BargainInfo> {
    private static final long serialVersionUID=1L;

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
     * 创建者ID
     */
	@ApiModelProperty(value = "创建者ID")
    private String createId;
    /**
     * 排序字段
     */
	@ApiModelProperty(value = "排序字段")
    private Integer sort;
    /**
     * （1：开启；0：关闭）
     */
	@ApiModelProperty(value = "1：开启；0：关闭")
    private String enable;
    /**
     * 商品Id
     */
	@ApiModelProperty(value = "商品Id")
    private String spuId;
    /**
     * skuId
     */
	@ApiModelProperty(value = "skuId")
    private String skuId;
	/**
	 * 砍价名称
	 */
	@ApiModelProperty(value = "砍价名称")
	private String name;
    /**
     * 开始时间
     */
	@ApiModelProperty(value = "开始时间")
    private LocalDateTime validBeginTime;
    /**
     * 结束时间
     */
	@ApiModelProperty(value = "结束时间")
    private LocalDateTime validEndTime;
    /**
     * 砍价底价
     */
	@ApiModelProperty(value = "砍价底价")
    private BigDecimal bargainPrice;
    /**
     * 自砍一刀（1：开启；0：关闭）
     */
	@ApiModelProperty(value = "自砍一刀（1：开启；0：关闭）")
    private String selfCut;
    /**
     * 必须底价购买（1：是；0：否）
     */
	@ApiModelProperty(value = "必须底价购买（1：是；0：否）")
    private String floorBuy;
    /**
     * 发起人数
     */
	@ApiModelProperty(value = "发起人数")
    private Integer launchNum;
    /**
     * 单次可砍最高金额
     */
	@ApiModelProperty(value = "单次可砍最高金额")
    private BigDecimal cutMax;
    /**
     * 单次可砍最低金额
     */
	@ApiModelProperty(value = "单次可砍最低金额")
    private BigDecimal cutMin;
    /**
     * 砍价规则
     */
	@ApiModelProperty(value = "砍价规则")
    private String cutRule;
    /**
     * 分享标题 
     */
	@ApiModelProperty(value = "分享标题")
    private String shareTitle;
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
	private BargainUser bargainUser;

	@TableField(exist = false)
	private String status;

	public String getStatus() {
		if(this.validEndTime != null){
			if(LocalDateTime.now().isBefore(this.validBeginTime)){
				return MallConstants.BARGAIN_INFO_STATUS_0;
			}else if(LocalDateTime.now().isAfter(this.validEndTime)){
				return MallConstants.BARGAIN_INFO_STATUS_2;
			}else{
				return MallConstants.BARGAIN_INFO_STATUS_1;
			}
		}
		return null;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
