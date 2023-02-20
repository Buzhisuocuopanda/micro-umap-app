/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.constant.MyReturnCode;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponInfoService;
import com.mkst.umap.app.mall.service.CouponUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 电子券用户记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-17 10:43:53
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/couponuser")
@Api(value = "couponuser", tags = "电子券用户记录API")
public class CouponUserApi {

    private final CouponUserService couponUserService;
	private final CouponInfoService couponInfoService;
    /**
     * 分页列表
     * @param page 分页对象
     * @param couponUser 电子券用户记录
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    public R getCouponUserPage(HttpServletRequest request, Page page, CouponUser couponUser) {
		R checkThirdSession = MallBaseApi.checkThirdSession(couponUser, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(couponUserService.page2(page, couponUser));
    }

    /**
     * 电子券用户记录查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "电子券用户记录查询")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(couponUserService.getById(id));
    }

    /**
     * 电子券用户记录新增
     * @param couponUser 电子券用户记录
     * @return R
     */
	@ApiOperation(value = "电子券用户记录新增")
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody CouponUser couponUser) {
		R checkThirdSession = MallBaseApi.checkThirdSession(couponUser, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		//核验用户是否领取过该券，并未使用
		int couponNum = couponUserService.count(Wrappers.<CouponUser>lambdaQuery()
				.eq(CouponUser :: getUserId, couponUser.getUserId())
				.eq(CouponUser :: getCouponId, couponUser.getCouponId())
				.eq(CouponUser :: getStatus, MallConstants.COUPON_USER_STATUS_0)
				.gt(CouponUser :: getValidEndTime, LocalDateTime.now()));
		if(couponNum > 0){
			return R.failed(MyReturnCode.ERR_80001.getCode(), MyReturnCode.ERR_80001.getMsg());
		}
		CouponInfo cuponInfo = couponInfoService.getById(couponUser.getCouponId());
		if(cuponInfo == null){
			return R.failed(MyReturnCode.ERR_80002.getCode(), MyReturnCode.ERR_80002.getMsg());
		}
		//核验库存
		if(cuponInfo.getStock() <= 0){
			return R.failed(MyReturnCode.ERR_80003.getCode(), MyReturnCode.ERR_80003.getMsg());
		}
		couponUser.setStatus(MallConstants.NO);
		//计数有效期
		if(MallConstants.COUPON_EXPIRE_TYPE_1.equals(cuponInfo.getExpireType())){
			couponUser.setValidBeginTime(LocalDateTime.now());
			couponUser.setValidEndTime(LocalDateTime.now().plusDays(cuponInfo.getValidDays()));
		}
		if(MallConstants.COUPON_EXPIRE_TYPE_2.equals(cuponInfo.getExpireType())){
			couponUser.setValidBeginTime(cuponInfo.getValidBeginTime());
			couponUser.setValidEndTime(cuponInfo.getValidEndTime());
		}
		couponUserService.receiveCoupon(couponUser, cuponInfo);
        return R.ok(couponUser);
    }

}
