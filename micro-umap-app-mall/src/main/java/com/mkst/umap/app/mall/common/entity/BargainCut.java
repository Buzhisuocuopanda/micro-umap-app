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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价帮砍记录
 *
 * @author www.joolun.com
 * @date 2019-12-31 12:34:28
 */
@Data
@TableName("bargain_cut")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "砍价帮砍记录")
public class BargainCut extends Model<BargainCut> {
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
	 * 砍价ID
	 */
	@ApiModelProperty(value = "砍价ID")
	private String bargainId;
	/**
	 * 砍价用户ID
	 */
	@ApiModelProperty(value = "砍价用户ID")
	private String bargainUserId;
    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 砍价金额
     */
	@ApiModelProperty(value = "砍价金额")
    private BigDecimal cutPrice;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickName;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String headimgUrl;

}
