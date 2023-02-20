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
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.GoodsAppraises;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.mapper.GoodsAppraisesMapper;
import com.mkst.umap.app.mall.service.GoodsAppraisesService;
import com.mkst.umap.app.mall.service.OrderInfoService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品评价
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-23 15:51:01
 */
@Service
@AllArgsConstructor
public class GoodsAppraisesServiceImpl extends ServiceImpl<GoodsAppraisesMapper, GoodsAppraises> implements GoodsAppraisesService {
	private final OrderInfoService orderInfoService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void doAppraises(List<GoodsAppraises> listGoodsAppraises) {
		super.saveBatch(listGoodsAppraises);
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(listGoodsAppraises.get(0).getOrderId());
		orderInfo.setAppraisesStatus(MallConstants.APPRAISES_STATUS_1);
		orderInfo.setClosingTime(LocalDateTime.now());
		orderInfoService.updateById(orderInfo);
	}

	@Override
	public IPage<GoodsAppraises> page1(IPage<GoodsAppraises> page, GoodsAppraises godsAppraises) {
		return baseMapper.selectPage1(page,godsAppraises);
	}
}
