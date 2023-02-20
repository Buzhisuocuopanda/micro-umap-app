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
import com.mkst.umap.app.mall.common.vo.GoodsSpecLeafVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * 规格
 *
 * @author www.joolun.com
 * @date 2019-08-13 16:10:54
 */
@Data
@TableName("goods_spec")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "规格")
public class GoodsSpec extends Model<GoodsSpec> {
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
   * 名称
   */
	@ApiModelProperty(value = "名称")
    private String name;
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

	@TableField(exist = false)
	private String spuId;

	@TableField(exist = false)
	private String value;

	@TableField(exist = false)
	private List<GoodsSpecLeafVO> leaf;
  
}
