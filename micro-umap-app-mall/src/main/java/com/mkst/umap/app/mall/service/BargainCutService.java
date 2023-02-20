/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mkst.umap.app.mall.common.entity.BargainCut;

import java.math.BigDecimal;

/**
 * 砍价帮砍记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-31 12:34:28
 */
public interface BargainCutService extends IService<BargainCut> {

	/**
	 * 获取砍价已砍总金额
	 * @param bargainUserId
	 * @return
	 */
	BigDecimal getTotalCutPrice(String bargainUserId);

	/**
	 * 砍一刀
	 * @param bargainCut
	 * @return
	 */
	boolean save2(BargainCut bargainCut);
}
