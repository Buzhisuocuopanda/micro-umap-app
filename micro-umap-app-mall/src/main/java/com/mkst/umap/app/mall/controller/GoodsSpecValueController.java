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
import com.mkst.umap.app.mall.common.entity.GoodsSpecValue;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSpecValueService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 规格值
 *
 * @author JL
 * @date 2019-08-13 16:11:05
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsspecvalue")
@Api(value = "goodsspecvalue", tags = "规格值管理")
public class GoodsSpecValueController {

    private final GoodsSpecValueService goodsSpecValueService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpecValue 规格值
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsspec:index")
    public R getGoodsSpecValuePage(Page page, GoodsSpecValue goodsSpecValue) {
        return R.ok(goodsSpecValueService.page(page,Wrappers.query(goodsSpecValue)));
    }

	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	@RequiresPermissions("mall:goodsspec:index")
	public R getGoodsSpecValueList(GoodsSpecValue goodsSpecValue) {
		return R.ok(goodsSpecValueService.list(Wrappers.query(goodsSpecValue)));
	}

    /**
    * 通过id查询规格值
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询规格值")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsspec:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSpecValueService.getById(id));
    }

    /**
    * 新增规格值
    * @param goodsSpecValue 规格值
    * @return R
    */
	@ApiOperation(value = "新增规格值")
    @Log(title = "规格值", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsspu:index")
    public R save(@RequestBody GoodsSpecValue goodsSpecValue){
		goodsSpecValueService.save(goodsSpecValue);
        return R.ok(goodsSpecValue);
    }

    /**
    * 修改规格值
    * @param goodsSpecValue 规格值
    * @return R
    */
	@ApiOperation(value = "修改规格值")
    @Log(title = "规格值", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsspec:edit")
    public R updateById(@RequestBody GoodsSpecValue goodsSpecValue){
        return R.ok(goodsSpecValueService.updateById(goodsSpecValue));
    }

    /**
    * 通过id删除规格值
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除规格值")
    @Log(title = "删除规格值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsspec:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSpecValueService.removeById(id));
    }

}
