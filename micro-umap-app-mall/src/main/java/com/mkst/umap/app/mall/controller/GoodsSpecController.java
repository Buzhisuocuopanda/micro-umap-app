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
import com.mkst.umap.app.mall.common.entity.GoodsSpec;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSpecService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 规格
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 16:10:54
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsspec")
@Api(value = "goodsspec", tags = "规格管理")
public class GoodsSpecController {

    private final GoodsSpecService goodsSpecService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpec 规格
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsspec:index")
    public R getGoodsSpecPage(Page page, GoodsSpec goodsSpec) {
        return R.ok(goodsSpecService.page(page,Wrappers.query(goodsSpec)));
    }

	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	public R getGoodsSpecList(GoodsSpec goodsSpec) {
		return R.ok(goodsSpecService.list(Wrappers.query(goodsSpec)));
	}

    /**
    * 通过id查询规格
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询规格")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsspec:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSpecService.getById(id));
    }

    /**
    * 新增规格
    * @param goodsSpec 规格
    * @return R
    */
	@ApiOperation(value = "新增规格")
    @Log(title = "规格", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsspu:index")
    public R save(@RequestBody GoodsSpec goodsSpec){
		goodsSpecService.save(goodsSpec);
        return R.ok(goodsSpec);
    }

    /**
    * 修改规格
    * @param goodsSpec 规格
    * @return R
    */
	@ApiOperation(value = "修改规格")
    @Log(title = "规格", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsspec:edit")
    public R updateById(@RequestBody GoodsSpec goodsSpec){
        return R.ok(goodsSpecService.updateById(goodsSpec));
    }

    /**
    * 通过id删除规格
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除规格")
    @Log(title = "删除规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsspec:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSpecService.removeById(id));
    }

}
