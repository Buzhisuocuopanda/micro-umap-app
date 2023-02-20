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
import com.mkst.umap.app.mall.common.entity.GoodsAppraises;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsAppraisesService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 商品评价
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-23 15:51:01
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsappraises")
@Api(value = "goodsappraises", tags = "商品评价管理")
public class GoodsAppraisesController {

    private final GoodsAppraisesService goodsAppraisesService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsAppraises 商品评价
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsappraises:index")
    public R getGoodsAppraisesPage(Page page, GoodsAppraises goodsAppraises) {
        return R.ok(goodsAppraisesService.page1(page,goodsAppraises));
    }

    /**
    * 通过id查询商品评价
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商品评价")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsappraises:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsAppraisesService.getById(id));
    }

    /**
    * 新增商品评价
    * @param goodsAppraises 商品评价
    * @return R
    */
	@ApiOperation(value = "新增商品评价")
    @Log(title = "商品评价", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsappraises:add")
    public R save(@RequestBody GoodsAppraises goodsAppraises){
        return R.ok(goodsAppraisesService.save(goodsAppraises));
    }

    /**
    * 修改商品评价
    * @param goodsAppraises 商品评价
    * @return R
    */
	@ApiOperation(value = "修改商品评价")
    @Log(title = "商品评价", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsappraises:edit")
    public R updateById(@RequestBody GoodsAppraises goodsAppraises){
        return R.ok(goodsAppraisesService.updateById(goodsAppraises));
    }

    /**
    * 通过id删除商品评价
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商品评价")
    @Log(title = "删除商品评价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsappraises:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsAppraisesService.removeById(id));
    }

}
