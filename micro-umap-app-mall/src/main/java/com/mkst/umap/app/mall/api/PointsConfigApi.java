/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.PointsConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 积分设置
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-06 16:15:01
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/pointsconfig")
@Api(value = "pointsconfig", tags = "积分设置API")
public class PointsConfigApi {

    private final PointsConfigService pointsConfigService;

	/**
	 * 查询积分设置
	 * @return R
	 */
	@ApiOperation(value = "查询积分设置")
	@GetMapping()
	public R get(HttpServletRequest request) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		return R.ok(pointsConfigService.getOne(Wrappers.emptyWrapper()));
	}
}
