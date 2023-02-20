/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.*;
import com.mkst.umap.app.mall.mapper.CouponInfoMapper;
import com.mkst.umap.app.mall.service.CouponGoodsService;
import com.mkst.umap.app.mall.service.CouponInfoService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 电子券
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-14 11:30:58
 */
@Service
@AllArgsConstructor
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

	private final CouponGoodsService couponGoodsService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(CouponInfo entity) {
		super.save(entity);
		if(MallConstants.COUPON_SUIT_TYPE_2.equals(entity.getSuitType())){//是否指定商品
			doCouponGoods(entity);
		}
		return Boolean.TRUE;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById1(CouponInfo entity) {
		super.updateById(entity);
		//先删除关联商品
		couponGoodsService.remove(Wrappers.<CouponGoods>lambdaQuery()
				.eq(CouponGoods::getCouponId,entity.getId()));
		if(MallConstants.COUPON_SUIT_TYPE_2.equals(entity.getSuitType())){//是否指定商品
			doCouponGoods(entity);
		}
		return Boolean.TRUE;
	}

	/**
	 * 批量添加关联商品
	 * @param entity
	 */
	protected void doCouponGoods(CouponInfo entity){
		List<GoodsSpu> listGoodsSpu = entity.getListGoodsSpu();
		List<CouponGoods> listCouponGoods = new ArrayList<>();
		if(listGoodsSpu != null && listGoodsSpu.size() > 0){
			listGoodsSpu.forEach(goodsSpu -> {
				CouponGoods couponGoods = new CouponGoods();
				couponGoods.setCouponId(entity.getId());
				couponGoods.setSpuId(goodsSpu.getId());
				listCouponGoods.add(couponGoods);
			});
			//添加关联商品
			couponGoodsService.saveBatch(listCouponGoods);
		}
	}

	@Override
	public CouponInfo getById2(Serializable id) {
		return baseMapper.selectById2(id);
	}

	@Override
	public IPage<CouponInfo> page2(IPage<CouponInfo> page, CouponInfo couponInfo, CouponGoods cuponGoods, CouponUser couponUser) {
		return baseMapper.selectPage2(page, couponInfo, cuponGoods, couponUser);
	}

	@Override
	public boolean removeById(Serializable id) {
		//先删除关联商品
		couponGoodsService.remove(Wrappers.<CouponGoods>lambdaQuery()
				.eq(CouponGoods::getCouponId,id));
		return super.removeById(id);
	}
}
