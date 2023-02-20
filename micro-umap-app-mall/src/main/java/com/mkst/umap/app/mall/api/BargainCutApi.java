/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.entity.BargainCut;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.BargainCutService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 砍价帮砍记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-31 12:34:28
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/bargaincut")
@Api(value = "bargaincut", tags = "砍价帮砍记录API")
public class BargainCutApi {

    private final BargainCutService bargainCutService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param bargainCut 砍价帮砍记录
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    public R getPage(HttpServletRequest request, Page page, BargainCut bargainCut) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(bargainCutService.page(page, Wrappers.query(bargainCut)));
    }

    /**
     * 砍价帮砍记录新增
     * @param bargainCut 砍价帮砍记录
     * @return R
     */
	@ApiOperation(value = "砍价帮砍记录新增")
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody BargainCut bargainCut) {
		R checkThirdSession = MallBaseApi.checkThirdSession(bargainCut, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		bargainCutService.save2(bargainCut);
        return R.ok(bargainCut);
    }


}
