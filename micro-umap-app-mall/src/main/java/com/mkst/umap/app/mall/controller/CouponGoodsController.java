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
import com.mkst.umap.app.mall.common.entity.CouponGoods;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponGoodsService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 电子券商品关联
 *
 * @author JL
 * @date 2019-12-16 17:42:19
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/coupongoods")
@Api(value = "coupongoods", tags = "电子券商品关联管理")
public class CouponGoodsController {

    private final CouponGoodsService couponGoodsService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param couponGoods 电子券商品关联
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:coupongoods:index")
    public R getCouponGoodsPage(Page page, CouponGoods couponGoods) {
        return R.ok(couponGoodsService.page(page, Wrappers.query(couponGoods)));
    }

    /**
     * 电子券商品关联查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "电子券商品关联查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:coupongoods:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(couponGoodsService.getById(id));
    }

    /**
     * 电子券商品关联新增
     * @param couponGoods 电子券商品关联
     * @return R
     */
	@ApiOperation(value = "电子券商品关联新增")
    @Log(title = "电子券商品关联", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:coupongoods:add")
    public R save(@RequestBody CouponGoods couponGoods) {
        return R.ok(couponGoodsService.save(couponGoods));
    }

    /**
     * 电子券商品关联修改
     * @param couponGoods 电子券商品关联
     * @return R
     */
	@ApiOperation(value = "电子券商品关联修改")
    @Log(title = "电子券商品关联", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:coupongoods:edit")
    public R updateById(@RequestBody CouponGoods couponGoods) {
        return R.ok(couponGoodsService.updateById(couponGoods));
    }

    /**
     * 电子券商品关联删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "电子券商品关联删除")
    @Log(title = "删除电子券商品关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:coupongoods:del")
    public R removeById(@PathVariable String id) {
        return R.ok(couponGoodsService.removeById(id));
    }

}
