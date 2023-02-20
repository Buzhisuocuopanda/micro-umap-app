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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 秒杀会场
 *
 * @author www.joolun.com
 * @date 2020-08-11 16:38:56
 */
@Data
@TableName("seckill_hall")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "秒杀会场")
public class SeckillHall extends Model<SeckillHall> {
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
     * 会场日期
     */
    @NotNull(message = "会场日期不能为空")
    @ApiModelProperty(value = "会场日期")
    private String hallDate;
    /**
     * 会场时间（单位时：0~23）
     */
    @NotNull(message = "会场时间（单位时：0~23）不能为空")
    @ApiModelProperty(value = "会场时间（单位时：0~23）")
    private Integer hallTime;

	/**
	 * （1：开启；0：关闭）
	 */
	@NotNull(message = "（1：开启；0：关闭）不能为空")
	@ApiModelProperty(value = "（1：开启；0：关闭）")
	private String enable;

	@TableField(exist = false)
	private List<SeckillInfo> listSeckillInfo;
}
