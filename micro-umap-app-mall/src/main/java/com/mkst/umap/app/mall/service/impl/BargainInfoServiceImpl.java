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
import com.mkst.umap.app.mall.common.entity.BargainInfo;
import com.mkst.umap.app.mall.common.entity.BargainUser;
import com.mkst.umap.app.mall.mapper.BargainInfoMapper;
import com.mkst.umap.app.mall.service.BargainInfoService;

import org.springframework.stereotype.Service;

/**
 * 砍价
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-28 18:07:51
 */
@Service
public class BargainInfoServiceImpl extends ServiceImpl<BargainInfoMapper, BargainInfo> implements BargainInfoService {

	@Override
	public IPage<BargainInfo> page2(IPage<BargainInfo> page, BargainInfo brgainInfo) {
		return baseMapper.selectPage2(page, brgainInfo);
	}

	@Override
	public BargainInfo getOne2(BargainUser bargainUser) {
		return baseMapper.selectOne2(bargainUser);
	}
}
