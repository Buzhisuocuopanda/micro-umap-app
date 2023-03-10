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
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;

/**
 * 用户收货地址
 *
 * @author www.joolun.com
 * @date 2019-09-11 14:28:59
 */
@Data
@TableName("user_address")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户收货地址")
public class UserAddress extends Model<UserAddress> {
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
   * 收货人名字
   */
	@ApiModelProperty(value = "收货人名字")
    private String userName;
    /**
   * 邮编
   */
	@ApiModelProperty(value = "邮编")
    private String postalCode;
    /**
   * 省名
   */
	@ApiModelProperty(value = "省名")
    private String provinceName;
    /**
   * 市名
   */
	@ApiModelProperty(value = "市名")
    private String cityName;
    /**
   * 区名
   */
	@ApiModelProperty(value = "区名")
    private String countyName;
    /**
   * 详情地址
   */
	@ApiModelProperty(value = "详情地址")
    private String detailInfo;
    /**
   * 电话号码
   */
	@ApiModelProperty(value = "电话号码")
    private String telNum;
    /**
   * 是否默认 1是0否
   */
	@ApiModelProperty(value = "是否默认")
    private String isDefault;
  
}
