/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;

/**
 * spu规格
 *
 * @author www.joolun.com
 * @date 2019-08-13 16:56:46
 */
@Data
@TableName("goods_spu_spec")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "spu规格")
public class GoodsSpuSpec extends Model<GoodsSpuSpec> {
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
   * spu_id
   */
	@ApiModelProperty(value = "spu_id")
    private String spuId;
    /**
   * spec_id
   */
	@ApiModelProperty(value = "spec_id")
    private String specId;
	/**
	 * 规格名
	 */
	@ApiModelProperty(value = "规格名")
	private String specName;
    /**
   * 创建时间
   */
	@ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
   * 更新时间
   */
	@ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
	/**
	 * 排序字段
	 */
	@ApiModelProperty(value = "排序字段")
	private Integer sort;
  
}
