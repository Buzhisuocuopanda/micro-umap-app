/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.entity.CouponGoods;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 电子券
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-14 11:30:58
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/couponinfo")
@Api(value = "couponinfo", tags = "电子券API")
public class CouponInfoApi {

    private final CouponInfoService couponInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param couponInfo 电子券
     * @return
     */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getPage(HttpServletRequest request, Page page, CouponInfo couponInfo, CouponGoods cuponGoods) {
		CouponUser couponUser = new CouponUser();
		R checkThirdSession = MallBaseApi.checkThirdSession(couponUser, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(couponInfoService.page2(page, couponInfo, cuponGoods, couponUser));
    }

    /**
     * 通过id查询电子券
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id查询电子券")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(couponInfoService.getById2(id));
    }

}
