/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.EnsureGoods;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.EnsureGoodsService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

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
@RequestMapping("/ensuregoods")
@Api(value = "ensuregoods", tags = "商品保障管理")
public class EnsureGoodsController {

    private final EnsureGoodsService ensureGoodsService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param ensureGoods 商品保障
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:ensuregoods:index")
    public R getPage(Page page, EnsureGoods ensureGoods) {
        return R.ok(ensureGoodsService.page(page, Wrappers.query(ensureGoods)));
    }

    /**
     * 商品保障查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "商品保障查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:ensuregoods:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(ensureGoodsService.getById(id));
    }

    /**
     * 商品保障新增
     * @param ensureGoods 商品保障
     * @return R
     */
	@ApiOperation(value = "商品保障新增")
    @Log(title = "商品保障", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:ensuregoods:add")
    public R save(@RequestBody EnsureGoods ensureGoods) {
        return R.ok(ensureGoodsService.save(ensureGoods));
    }

    /**
     * 商品保障修改
     * @param ensureGoods 商品保障
     * @return R
     */
	@ApiOperation(value = "商品保障修改")
    @Log(title = "商品保障", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:ensuregoods:edit")
    public R updateById(@RequestBody EnsureGoods ensureGoods) {
        return R.ok(ensureGoodsService.updateById(ensureGoods));
    }

    /**
     * 商品保障删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "商品保障删除")
    @Log(title = "删除商品保障", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:ensuregoods:del")
    public R removeById(@PathVariable String id) {
        return R.ok(ensureGoodsService.removeById(id));
    }

}
