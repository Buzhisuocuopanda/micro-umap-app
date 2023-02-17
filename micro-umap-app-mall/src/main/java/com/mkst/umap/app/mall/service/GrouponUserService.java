/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mkst.umap.app.mall.common.entity.GrouponUser;

/**
 * 拼团记录
 *
 * @author JL
 * @date 2020-03-17 12:01:53
 */
public interface GrouponUserService extends IService<GrouponUser> {

	IPage<GrouponUser> page1(IPage<GrouponUser> page, GrouponUser grouponUser);

	IPage<GrouponUser> page2(IPage<GrouponUser> page, GrouponUser grouponUser);

	IPage<GrouponUser> getPageGrouponing(IPage<GrouponUser> page, GrouponUser grouponUser);

	Integer selectCountGrouponing(String groupId);
}
