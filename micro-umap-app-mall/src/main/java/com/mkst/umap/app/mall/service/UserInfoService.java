/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
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
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-04 11:09:55
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
