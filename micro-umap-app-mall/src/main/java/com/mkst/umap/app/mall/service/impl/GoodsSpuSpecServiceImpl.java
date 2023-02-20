/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.entity.GoodsSpec;
import com.mkst.umap.app.mall.common.entity.GoodsSpuSpec;
import com.mkst.umap.app.mall.mapper.GoodsSpuSpecMapper;
import com.mkst.umap.app.mall.service.GoodsSpuSpecService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * spu规格
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 16:56:46
 */
@Service
public class GoodsSpuSpecServiceImpl extends ServiceImpl<GoodsSpuSpecMapper, GoodsSpuSpec> implements GoodsSpuSpecService {

	@Override
	public List<GoodsSpec> tree(GoodsSpuSpec goodsSpuSpec) {
		return baseMapper.selectTree(goodsSpuSpec.getSpuId());
	}
}
