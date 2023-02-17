/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
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
 * @author JL
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
