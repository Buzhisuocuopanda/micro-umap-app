/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.common.entity;

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
 * 商城用户
 *
 * @author www.joolun.com
 * @date 2019-12-04 11:09:55
 */
@Data
@TableName("user_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商城用户")
public class UserInfo extends Model<UserInfo> {
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
	 * 用户编码
	 */
	@ApiModelProperty(value = "用户编码")
	private Integer userCode;
	/**
	 * 手机号码
	 */
	@ApiModelProperty(value = "手机号码")
//	@Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
	private String phone;
	/**
	 * 来源应用类型1、小程序；2、公众号
	 */
	@ApiModelProperty(value = "来源应用类型")
	private String appType;
	/**
	 * 来源应用id
	 */
	@ApiModelProperty(value = "来源应用id")
	private String appId;
	/**
	 * 用户等级
	 */
	@ApiModelProperty(value = "用户等级")
	private Integer userGrade;
	/**
	 * 当前积分
	 */
	@ApiModelProperty(value = "当前积分")
	private Integer pointsCurrent;
	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickName;
	/**
	 * 性别（1：男，2：女，0：未知）
	 */
	@ApiModelProperty(value = "性别")
	private String sex;
	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String headimgUrl;
	/**
	 * 所在城市
	 */
	@ApiModelProperty(value = "所在城市")
	private String city;
	/**
	 * 所在国家
	 */
	@ApiModelProperty(value = "所在国家")
	private String country;
	/**
	 * 所在省份
	 */
	@ApiModelProperty(value = "所在省份")
	private String province;
	/**
	 * 电子券数量
	 */
	@TableField(exist = false)
	private Integer couponNum;
}
