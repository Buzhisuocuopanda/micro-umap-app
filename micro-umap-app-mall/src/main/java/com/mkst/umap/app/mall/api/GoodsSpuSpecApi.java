/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mkst.umap.app.mall.common.entity.GoodsSpuSpec;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSpuSpecService;

import javax.servlet.http.HttpServletRequest;

/**
 * spu规格
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 16:56:46
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/goodsspuspec")
@Api(value = "goodsspuspec", tags = "spu规格接口API")
public class GoodsSpuSpecApi {

    private final GoodsSpuSpecService goodsSpuSpecService;


	/**
	 * 获取商品规格
	 * @param goodsSpuSpec
	 * @return
	 */
	@ApiOperation(value = "获取商品规格")
	@GetMapping("/tree")
	public R getGoodsSpuSpecTree(HttpServletRequest request, GoodsSpuSpec goodsSpuSpec) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		return R.ok(goodsSpuSpecService.tree(goodsSpuSpec));
	}

}
