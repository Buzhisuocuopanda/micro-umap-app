/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.mapper.CouponUserMapper;
import com.mkst.umap.app.mall.service.CouponInfoService;
import com.mkst.umap.app.mall.service.CouponUserService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电子券用户记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-17 10:43:53
 */
@Service
@AllArgsConstructor
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements CouponUserService {

	private final CouponInfoService couponInfoService;

	@Override
	public IPage<CouponUser> page1(IPage<CouponUser> page, CouponUser couponUser) {
		return baseMapper.selectPage1(page, couponUser);
	}

	@Override
	public IPage<CouponUser> page2(IPage<CouponUser> page, CouponUser couponUser) {
		return baseMapper.selectPage2(page, couponUser);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean receiveCoupon(CouponUser couponUser, CouponInfo cuponInfo) {
		couponUser.setName(cuponInfo.getName());
		couponUser.setType(cuponInfo.getType());
		couponUser.setPremiseAmount(cuponInfo.getPremiseAmount());
		couponUser.setReduceAmount(cuponInfo.getReduceAmount());
		couponUser.setDiscount(cuponInfo.getDiscount());
		couponUser.setSuitType(cuponInfo.getSuitType());
		baseMapper.insert(couponUser);
		//更新电子券的库存，乐观锁处理
		cuponInfo.setStock(cuponInfo.getStock() - 1);
		if(!couponInfoService.updateById(cuponInfo)){
			throw new RuntimeException("请重新领取");
		}
		return Boolean.TRUE;
	}
}
