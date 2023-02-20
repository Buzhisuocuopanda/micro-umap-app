/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mkst.umap.app.mall.common.entity.GrouponUser;

/**
 * 拼团记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-03-17 12:01:53
 */
public interface GrouponUserService extends IService<GrouponUser> {

	IPage<GrouponUser> page1(IPage<GrouponUser> page, GrouponUser grouponUser);

	IPage<GrouponUser> page2(IPage<GrouponUser> page, GrouponUser grouponUser);

	IPage<GrouponUser> getPageGrouponing(IPage<GrouponUser> page, GrouponUser grouponUser);

	Integer selectCountGrouponing(String groupId);
}
