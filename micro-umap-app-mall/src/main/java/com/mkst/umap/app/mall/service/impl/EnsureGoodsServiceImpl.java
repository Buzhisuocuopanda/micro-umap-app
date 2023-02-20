/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.entity.Ensure;
import com.mkst.umap.app.mall.common.entity.EnsureGoods;
import com.mkst.umap.app.mall.mapper.EnsureGoodsMapper;
import com.mkst.umap.app.mall.service.EnsureGoodsService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品保障
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-02-10 00:02:09
 */
@Service
public class EnsureGoodsServiceImpl extends ServiceImpl<EnsureGoodsMapper, EnsureGoods> implements EnsureGoodsService {

	@Override
	public List<Ensure> listEnsureBySpuId(String spuId) {
		return baseMapper.listEnsureBySpuId(spuId);
	}
}
