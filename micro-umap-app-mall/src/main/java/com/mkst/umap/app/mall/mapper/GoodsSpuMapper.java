/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mkst.umap.app.mall.common.entity.CouponGoods;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu商品
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 16:25:10
 */
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {

	IPage<GoodsSpu> selectPage1(IPage<GoodsSpu> page, @Param("query") GoodsSpu goodsSpu);

	GoodsSpu selectById1(String id);

	GoodsSpu selectById2(String id);

	GoodsSpu selectById4(String id);

	GoodsSpu selectOneByShoppingCart(String id);

	/**
	 * 查询电子券的关联商品
	 * @param couponId
	 * @return
	 */
	List<GoodsSpu> selectListByCouponId(String couponId);

	IPage<GoodsSpu> selectPage2(IPage<GoodsSpu> page, @Param("query") GoodsSpu goodsSpu, @Param("query2") CouponUser couponUser);
}
