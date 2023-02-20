/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.UserCollect;
import com.mkst.umap.app.mall.mapper.UserCollectMapper;
import com.mkst.umap.app.mall.service.UserCollectService;

import org.springframework.stereotype.Service;

/**
 * 用户收藏
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-22 20:45:45
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

	@Override
	public IPage<UserCollect> page2(IPage<UserCollect> page, Wrapper<UserCollect> queryWrapper) {
		return baseMapper.selectPage2(page, queryWrapper.getEntity());
	}
}
