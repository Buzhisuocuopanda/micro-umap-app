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
import com.mkst.umap.app.mall.common.entity.BargainCut;

import java.math.BigDecimal;

/**
 * 砍价帮砍记录
 *
 * @author JL
 * @date 2019-12-31 12:34:28
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
