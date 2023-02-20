/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.entity.GrouponInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GrouponInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 拼团
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-03-17 11:55:32
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/grouponinfo")
@Api(value = "grouponinfo", tags = "拼团Api")
public class GrouponInfoApi {

    private final GrouponInfoService grouponInfoService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param grouponInfo 拼团
     * @return
     */
    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    public R getPage(HttpServletRequest request, Page page, GrouponInfo grouponInfo) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(grouponInfoService.page2(page, grouponInfo));
    }

    /**
     * 拼团查询
     * @param id
     * @return R
     */
    @ApiOperation(value = "拼团查询")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(grouponInfoService.getById2(id));
    }

}
