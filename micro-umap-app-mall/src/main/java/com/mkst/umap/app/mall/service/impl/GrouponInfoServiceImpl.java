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
import com.mkst.umap.app.mall.common.entity.GrouponInfo;
import com.mkst.umap.app.mall.mapper.GrouponInfoMapper;
import com.mkst.umap.app.mall.service.GrouponInfoService;

import org.springframework.stereotype.Service;

/**
 * 拼团
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-03-17 11:55:32
 */
@Service
public class GrouponInfoServiceImpl extends ServiceImpl<GrouponInfoMapper, GrouponInfo> implements GrouponInfoService {

	@Override
	public IPage<GrouponInfo> page2(IPage<GrouponInfo> page, GrouponInfo grouponInfo) {
		return baseMapper.selectPage2(page, grouponInfo);
	}

	@Override
	public GrouponInfo getById2(String id) {
		return baseMapper.selectById2(id);
	}
}
