/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponUserService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

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
@RequestMapping("/couponuser")
@Api(value = "couponuser", tags = "电子券用户记录管理")
public class CouponUserController {

    private final CouponUserService couponUserService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param couponUser 电子券用户记录
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:couponuser:index")
    public R getCouponUserPage(Page page, CouponUser couponUser) {
        return R.ok(couponUserService.page1(page, couponUser));
    }

    /**
     * 电子券用户记录查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "电子券用户记录查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:couponuser:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(couponUserService.getById(id));
    }

    /**
     * 电子券用户记录新增
     * @param couponUser 电子券用户记录
     * @return R
     */
	@ApiOperation(value = "电子券用户记录新增")
    @Log(title = "电子券用户记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:couponuser:add")
    public R save(@RequestBody CouponUser couponUser) {
        return R.ok(couponUserService.save(couponUser));
    }

    /**
     * 电子券用户记录修改
     * @param couponUser 电子券用户记录
     * @return R
     */
	@ApiOperation(value = "电子券用户记录修改")
    @Log(title = "电子券用户记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:couponuser:edit")
    public R updateById(@RequestBody CouponUser couponUser) {
        return R.ok(couponUserService.updateById(couponUser));
    }

    /**
     * 电子券用户记录删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "电子券用户记录删除")
    @Log(title = "删除电子券用户记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:couponuser:del")
    public R removeById(@PathVariable String id) {
        return R.ok(couponUserService.removeById(id));
    }

}
