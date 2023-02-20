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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponUserService;
import com.mkst.umap.app.mall.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商城用户
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-04 11:09:55
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/userinfo")
@Api(value = "userinfo", tags = "商城用户API")
public class UserInfoApi {

    private final UserInfoService userInfoService;
	private final CouponUserService couponUserService;

    /**
     * 查询商城用户
     * @return R
     */
	@ApiOperation(value = "查询商城用户")
    @GetMapping
    public R getById(HttpServletRequest request) {
		UserInfo userInfo = new UserInfo();
		R checkThirdSession = MallBaseApi.checkThirdSession(userInfo, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		int couponNum = couponUserService.count(Wrappers.<CouponUser>lambdaQuery()
				.eq(CouponUser :: getUserId, userInfo.getId())
				.eq(CouponUser :: getStatus, MallConstants.NO)
				.gt(CouponUser :: getValidEndTime, LocalDateTime.now()));
		userInfo = userInfoService.getById(userInfo.getId());
		userInfo.setCouponNum(couponNum);
        return R.ok(userInfo);
    }
}
