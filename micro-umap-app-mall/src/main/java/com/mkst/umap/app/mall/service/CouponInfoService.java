/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mkst.umap.app.mall.common.entity.CouponGoods;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.entity.CouponUser;

import java.io.Serializable;

/**
 * 电子券
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-14 11:30:58
 */
public interface CouponInfoService extends IService<CouponInfo> {

	boolean updateById1(CouponInfo entity);

	CouponInfo getById2(Serializable id);

	IPage<CouponInfo> page2(IPage<CouponInfo> page, CouponInfo couponInfo, CouponGoods cuponGoods, CouponUser couponUser);
}
