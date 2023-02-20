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
import com.mkst.umap.app.mall.common.entity.ShoppingCart;
import com.mkst.umap.app.mall.mapper.ShoppingCartMapper;
import com.mkst.umap.app.mall.service.ShoppingCartService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 购物车
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-29 21:27:33
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(ShoppingCart entity) {
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId,entity.getUserId())
				.eq(ShoppingCart::getSpuId,entity.getSpuId())
				.eq(ShoppingCart::getSkuId,entity.getSkuId()));
		if(shoppingCart != null){
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			baseMapper.deleteById(shoppingCart);
			return super.save(entity);
		}else{
			return super.save(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(ShoppingCart entity) {
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId,entity.getUserId())
				.eq(ShoppingCart::getSpuId,entity.getSpuId())
				.eq(ShoppingCart::getSkuId,entity.getSkuId()));
		if(shoppingCart != null && !entity.getId().equals(shoppingCart.getId())){
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			baseMapper.deleteById(shoppingCart);
			return super.updateById(entity);
		}else{
			return super.updateById(entity);
		}
	}

	@Override
	public IPage<ShoppingCart> page2(IPage<ShoppingCart> page, ShoppingCart shoppingCart) {
		return baseMapper.selectPage2(page, shoppingCart);
	}
}
