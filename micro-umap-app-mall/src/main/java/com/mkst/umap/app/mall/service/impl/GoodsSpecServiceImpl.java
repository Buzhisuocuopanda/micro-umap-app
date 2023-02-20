/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.entity.GoodsSpec;
import com.mkst.umap.app.mall.common.entity.GoodsSpecValue;
import com.mkst.umap.app.mall.mapper.GoodsSpecMapper;
import com.mkst.umap.app.mall.mapper.GoodsSpecValueMapper;
import com.mkst.umap.app.mall.service.GoodsSpecService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 规格
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 16:10:54
 */
@Service
@AllArgsConstructor
public class GoodsSpecServiceImpl extends ServiceImpl<GoodsSpecMapper, GoodsSpec> implements GoodsSpecService {

	private final GoodsSpecValueMapper goodsSpecValueMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		goodsSpecValueMapper.delete(Wrappers.<GoodsSpecValue>update().lambda()
				.eq(GoodsSpecValue::getSpecId, id));
		return super.removeById(id);
	}
}
