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
import com.mkst.umap.app.mall.common.entity.PointsRecord;
import com.mkst.umap.app.mall.mapper.PointsRecordMapper;
import com.mkst.umap.app.mall.service.PointsRecordService;

import org.springframework.stereotype.Service;

/**
 * 积分变动记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-05 19:47:22
 */
@Service
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements PointsRecordService {

	@Override
	public IPage<PointsRecord> page1(IPage<PointsRecord> page, PointsRecord pointsRecord) {
		return baseMapper.selectPage1(page,pointsRecord);
	}
}
