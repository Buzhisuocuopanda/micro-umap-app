/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.entity.OrderLogistics;
import com.mkst.umap.app.mall.mapper.OrderLogisticsMapper;
import com.mkst.umap.app.mall.service.OrderLogisticsService;

import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 订单物流
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-16 09:53:17
 */
@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements OrderLogisticsService {

	@Override
	public OrderLogistics getById(Serializable id) {
		return baseMapper.selectById2(id);
	}
}
