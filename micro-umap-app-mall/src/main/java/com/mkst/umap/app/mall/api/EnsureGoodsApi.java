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
import org.springframework.web.bind.annotation.*;

import com.mkst.umap.app.mall.common.entity.Ensure;
import com.mkst.umap.app.mall.common.entity.EnsureGoods;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.EnsureGoodsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商品保障
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-02-10 00:02:09
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/ensuregoods")
@Api(value = "ensuregoods", tags = "商品保障API")
public class EnsureGoodsApi {

    private final EnsureGoodsService ensureGoodsService;

    /**
     * 通过spuID，查询商品保障
     * @param ensureGoods 商品保障
     * @return
     */
	@ApiOperation(value = "通过spuID，查询商品保障")
    @GetMapping("/listEnsureBySpuId")
    public R listEnsureBySpuId(HttpServletRequest request, EnsureGoods ensureGoods) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		List<Ensure> listEnsure = ensureGoodsService.listEnsureBySpuId(ensureGoods.getSpuId());
        return R.ok(listEnsure);
    }
}
