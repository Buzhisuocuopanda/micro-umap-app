/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mkst.umap.app.mall.common.entity.UserCollect;

import org.apache.ibatis.annotations.Param;

/**
 * 用户收藏
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-22 20:45:45
 */
public interface UserCollectMapper extends BaseMapper<UserCollect> {

	IPage<UserCollect> selectPage2(IPage<UserCollect> page,  @Param("query") UserCollect userCollect);

	String selectCollectId(@Param("query") UserCollect userCollect);
}
