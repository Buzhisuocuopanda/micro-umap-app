/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkst.umap.app.mall.common.entity.Ensure;
import com.mkst.umap.app.mall.common.entity.EnsureGoods;

import java.util.List;

/**
 * 商品保障
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-02-10 00:02:09
 */
public interface EnsureGoodsMapper extends BaseMapper<EnsureGoods> {

	/**
	 * 通过spuID，查询商品保障ID
	 *
	 * @param spuId
	 * @return
	 */
	List<String> listEnsureIdsBySpuId(String spuId);

	/**
	 * 通过spuID，查询商品保障
	 *
	 * @param spuId
	 * @return
	 */
	List<Ensure> listEnsureBySpuId(String spuId);
}
