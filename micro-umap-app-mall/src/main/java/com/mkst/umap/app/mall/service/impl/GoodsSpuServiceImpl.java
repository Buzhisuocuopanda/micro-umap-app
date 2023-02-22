/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mkst.umap.app.mall.common.entity.*;
import com.mkst.umap.app.mall.common.util.ArrayUtils;
import com.mkst.umap.app.mall.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.mapper.GoodsSpuMapper;

import lombok.AllArgsConstructor;

/**
 * spu商品
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 16:25:10
 */
@Service
@AllArgsConstructor
public class GoodsSpuServiceImpl extends ServiceImpl<GoodsSpuMapper, GoodsSpu> implements GoodsSpuService {

	private final GoodsSkuService goodsSkuService;
	private final GoodsSpuSpecService goodsSpuSpecService;
	private final GoodsSkuSpecValueService goodsSkuSpecValueService;
	private final UserCollectService userCollectService;
	private final EnsureGoodsService ensureGoodsService;
	private final GoodsSpecService goodsSpecService;
	private final GoodsSpecValueService goodsSpecValueService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		super.removeById(id);
		//删除SpuSpec、SkuSpecValue、Sku、用户收藏
		goodsSpuSpecService.remove(Wrappers.<GoodsSpuSpec>update().lambda()
				.eq(GoodsSpuSpec::getSpuId, id));
		goodsSkuSpecValueService.remove(Wrappers.<GoodsSkuSpecValue>update().lambda()
				.eq(GoodsSkuSpecValue::getSpuId, id));
		goodsSkuService.remove(Wrappers.<GoodsSku>update().lambda()
				.eq(GoodsSku::getSpuId, id));
		userCollectService.remove(Wrappers.<UserCollect>update().lambda()
				.eq(UserCollect::getType, MallConstants.COLLECT_TYPE_1)
				.eq(UserCollect::getRelationId, id));
		return true;
	}

	@Override
	public IPage<GoodsSpu> page1(IPage<GoodsSpu> page, GoodsSpu goodsSpu) {
		return baseMapper.selectPage1(page, goodsSpu);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save1(GoodsSpu goodsSpu) {
		goodsSpu.setPriceDown(null);
		goodsSpu.setPriceUp(null);
		baseMapper.insert(goodsSpu);
		List<GoodsSku> listGoodsSku = goodsSpu.getSkus();
		if(listGoodsSku !=null && listGoodsSku.size()>0){
			//新增sku
			listGoodsSku.forEach(goodsSku -> {
				priceUpDown(goodsSpu,goodsSku);
				goodsSku.setSpuId(goodsSpu.getId());
				goodsSkuService.save(goodsSku);
				goodsSku.setId(goodsSku.getId());
			});
			baseMapper.updateById(goodsSpu);
			if(MallConstants.SPU_SPEC_TYPE_1.equals(goodsSpu.getSpecType())){//多规格处理
				goodsSpu.setId(goodsSpu.getId());
				addSpec(goodsSpu);
			}
		}
		if(goodsSpu.getEnsureIds() != null && goodsSpu.getEnsureIds().size() > 0){//新增保障服务
			List<EnsureGoods> listEnsureGoods = goodsSpu.getEnsureIds()
					.stream().map(ensureId -> {
						EnsureGoods ensureGoods = new EnsureGoods();
						ensureGoods.setEnsureId(ensureId);
						ensureGoods.setSpuId(goodsSpu.getId());
						return ensureGoods;
					}).collect(Collectors.toList());
			ensureGoodsService.saveBatch(listEnsureGoods);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save2(GoodsSpu goodsSpu) {
		goodsSpu.setPriceUp(null);
		baseMapper.insert(goodsSpu);
		String spuId = goodsSpu.getId();
		BigDecimal priceDown = goodsSpu.getPriceDown();

		String specId = goodsSpu.getSpecIds();
		if (StrUtil.isNotEmpty(specId)) {
			String[] ids = specId.split(",");
			QueryWrapper<GoodsSpec> wrapper = new QueryWrapper<>();
			wrapper.in("id", Arrays.asList(ids));
			List<GoodsSpec> goodsSpecList = goodsSpecService.list(wrapper);

			//新增SpuSpec
			List<GoodsSpuSpec> listGoodsSpuSpec = goodsSpecList.stream().map(spuSpec->{
				GoodsSpuSpec goodsSpuSpec = new GoodsSpuSpec();
				goodsSpuSpec.setSpuId(goodsSpu.getId());
				goodsSpuSpec.setSpecId(spuSpec.getId());
				return goodsSpuSpec;
			}).collect(Collectors.toList());
			goodsSpuSpecService.saveBatch(listGoodsSpuSpec);

			QueryWrapper<GoodsSpecValue> wrapper2 = new QueryWrapper<>();
			wrapper2.in("spec_id", Arrays.asList(ids));
			List<GoodsSpecValue> goodsSpecValueList = goodsSpecValueService.list(wrapper2);

			Map<String, GoodsSpecValue> goodsSpecValueMap = goodsSpecValueList.stream().collect(Collectors.toMap(GoodsSpecValue::getId, t -> t));

			Map<String, List<GoodsSpecValue>> map = goodsSpecValueList.stream().collect(Collectors.groupingBy(GoodsSpecValue::getSpecId));

			ArrayList<List<String>> list1 = new ArrayList<>();
			map.forEach((key, value) -> {
				List<String> list = value.stream().map(GoodsSpecValue::getId).collect(Collectors.toList());
				list1.add(list);
			});

			// 排列组合
			List<String> descartes = ArrayUtils.getPermutations(list1);

			// 新增sku
			List<GoodsSku> listGoodsSku = new ArrayList<>();
			descartes.forEach(t -> {
				GoodsSku goodsSku = new GoodsSku();
				goodsSku.setSpuId(spuId);
				goodsSku.setId(goodsSku.getId());
				goodsSku.setSalesPrice(priceDown);
				goodsSku.setStock(0);
				goodsSkuService.save(goodsSku);

				//新增SkuSpecValue
				List<GoodsSkuSpecValue> listGoodsSkuSpecValue = new ArrayList<>();
				List<String> list = Arrays.asList(t.split(","));
				list.forEach(m -> {
					GoodsSkuSpecValue goodsSkuSpecValue = new GoodsSkuSpecValue();
					goodsSkuSpecValue.setSpuId(spuId);
					goodsSkuSpecValue.setSkuId(goodsSku.getId());

					GoodsSpecValue goodsSpecValue = goodsSpecValueMap.get(m);
					if (null != goodsSpecValue) {
						goodsSkuSpecValue.setSpecId(goodsSpecValue.getSpecId());
						goodsSkuSpecValue.setSpecValueId(goodsSpecValue.getId());
						goodsSkuSpecValue.setSpecValueName(goodsSpecValue.getName());
					}
					listGoodsSkuSpecValue.add(goodsSkuSpecValue);
				});
				goodsSkuSpecValueService.saveBatch(listGoodsSkuSpecValue);
			});
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById1(GoodsSpu goodsSpu) {
		goodsSpu.setPriceDown(null);
		goodsSpu.setPriceUp(null);
		List<GoodsSku> listGoodsSku = goodsSpu.getSkus();
		if(listGoodsSku !=null && listGoodsSku.size()>0){
			//先删除旧sku
			goodsSkuService.remove(Wrappers.<GoodsSku>update().lambda()
					.eq(GoodsSku::getSpuId, goodsSpu.getId()));
			//新增sku
			listGoodsSku.forEach(goodsSku -> {
				priceUpDown(goodsSpu,goodsSku);
				goodsSku.setSpuId(goodsSpu.getId());
				goodsSku.setTenantId(null);
				goodsSkuService.saveOrUpdate(goodsSku);
				goodsSku.setId(goodsSku.getId());
			});
			baseMapper.updateById(goodsSpu);
			//统一删除SpuSpec、SkuSpecValue
			goodsSpuSpecService.remove(Wrappers.<GoodsSpuSpec>update().lambda()
					.eq(GoodsSpuSpec::getSpuId, goodsSpu.getId()));
			goodsSkuSpecValueService.remove(Wrappers.<GoodsSkuSpecValue>update().lambda()
					.eq(GoodsSkuSpecValue::getSpuId, goodsSpu.getId()));
			if(MallConstants.SPU_SPEC_TYPE_1.equals(goodsSpu.getSpecType())) {//多规格处理
				addSpec(goodsSpu);
			}
			//修改保障服务
			ensureGoodsService.remove(Wrappers.<EnsureGoods>update().lambda()
					.eq(EnsureGoods::getSpuId, goodsSpu.getId()));
			if(goodsSpu.getEnsureIds() != null && goodsSpu.getEnsureIds().size() > 0){
				List<EnsureGoods> listEnsureGoods = new ArrayList<>();
				goodsSpu.getEnsureIds().forEach(ensureId -> {
					EnsureGoods ensureGoods = new EnsureGoods();
					ensureGoods.setEnsureId(ensureId);
					ensureGoods.setSpuId(goodsSpu.getId());
					listEnsureGoods.add(ensureGoods);
				});
				ensureGoodsService.saveBatch(listEnsureGoods);
			}
		}
		return true;
	}

	@Override
	public GoodsSpu getById1(String id) {
		return baseMapper.selectById1(id);
	}

	@Override
	public GoodsSpu getById2(String id) {
		return baseMapper.selectById2(id);
	}

	@Override
	public IPage<GoodsSpu> page2(IPage<GoodsSpu> page, GoodsSpu goodsSpu, CouponUser couponUser) {
		return baseMapper.selectPage2(page, goodsSpu, couponUser);
	}

	/**
	 * 多规格处理
	 * @param goodsSpu
	 */
	void addSpec(GoodsSpu goodsSpu){
		//新增SpuSpec
		List<GoodsSpuSpec> listGoodsSpuSpec = goodsSpu.getSpuSpec().stream().map(spuSpec->{
			GoodsSpuSpec goodsSpuSpec = new GoodsSpuSpec();
			goodsSpuSpec.setSpuId(goodsSpu.getId());
			goodsSpuSpec.setSpecId(spuSpec.getId());
			return goodsSpuSpec;
		}).collect(Collectors.toList());
		goodsSpuSpecService.saveBatch(listGoodsSpuSpec);
		//新增SkuSpecValue
		List<GoodsSkuSpecValue> listGoodsSkuSpecValue = new ArrayList<>();
		goodsSpu.getSkus().forEach(goodsSku -> {
			goodsSku.getSpecs().forEach(goodsSkuSpecValue -> {
				goodsSkuSpecValue.setSpuId(goodsSpu.getId());
				goodsSkuSpecValue.setSkuId(goodsSku.getId());
				listGoodsSkuSpecValue.add(goodsSkuSpecValue);
			});
		});
		goodsSkuSpecValueService.saveBatch(listGoodsSkuSpecValue);
	}

	/**
	 * 获取商品最高最低价
	 * @param goodsSpu
	 * @param goodsSku
	 */
	void priceUpDown(GoodsSpu goodsSpu,GoodsSku goodsSku){
		if(MallConstants.YES.equals(goodsSku.getEnable())){
			BigDecimal priceDown = goodsSpu.getPriceDown();
			if(priceDown == null || priceDown.compareTo(goodsSku.getSalesPrice()) == 1){
				goodsSpu.setPriceDown(goodsSku.getSalesPrice());
			}
			BigDecimal priceUp = goodsSpu.getPriceUp();
			if(priceUp == null || priceUp.compareTo(goodsSku.getSalesPrice()) == -1){
				goodsSpu.setPriceUp(goodsSku.getSalesPrice());
			}
		}
	}
}
