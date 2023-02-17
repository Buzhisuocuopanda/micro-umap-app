/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkst.umap.app.mall.common.dto.WxOpenDataDTO;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.vo.R;

/**
 * 商城用户
 *
 * @author JL
 * @date 2019-12-04 11:09:55
 */
public interface UserInfoService extends IService<UserInfo> {

	/**
	 * 通过微信用户增加商城用户
	 * @param wxOpenDataDTO
	 * @return
	 */
	R saveByWxUser(WxOpenDataDTO wxOpenDataDTO);

	/**
	 * 新增商城用户（供服务间调用）
	 * @param userInfo
	 * @return
	 */
	boolean saveInside(UserInfo userInfo);
}
