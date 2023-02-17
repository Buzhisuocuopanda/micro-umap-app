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
import com.mkst.umap.app.mall.common.entity.GoodsSpuSpec;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSpuSpecService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * spu规格
 *
 * @author JL
 * @date 2019-08-13 16:56:46
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsspuspec")
@Api(value = "goodsspuspec", tags = "spu规格管理")
public class GoodsSpuSpecController {

    private final GoodsSpuSpecService goodsSpuSpecService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpuSpec spu规格
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsspu:index")
    public R getGoodsSpuSpecPage(Page page, GoodsSpuSpec goodsSpuSpec) {
        return R.ok(goodsSpuSpecService.page(page,Wrappers.query(goodsSpuSpec)));
    }

	/**
	 * 获取商品规格
	 * @param goodsSpuSpec
	 * @return
	 */
	@ApiOperation(value = "获取商品规格")
	@GetMapping("/tree")
	@RequiresPermissions("mall:goodsspu:index")
	public R getGoodsSpuSpecTree(GoodsSpuSpec goodsSpuSpec) {
		return R.ok(goodsSpuSpecService.tree(goodsSpuSpec));
	}

    /**
    * 通过id查询spu规格
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询spu规格")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsspu:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSpuSpecService.getById(id));
    }

    /**
    * 新增spu规格
    * @param goodsSpuSpec spu规格
    * @return R
    */
	@ApiOperation(value = "新增spu规格")
    @Log(title = "spu规格", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsspu:add")
    public R save(@RequestBody GoodsSpuSpec goodsSpuSpec){
        return R.ok(goodsSpuSpecService.save(goodsSpuSpec));
    }

    /**
    * 修改spu规格
    * @param goodsSpuSpec spu规格
    * @return R
    */
	@ApiOperation(value = "修改spu规格")
    @Log(title = "spu规格", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsspu:edit")
    public R updateById(@RequestBody GoodsSpuSpec goodsSpuSpec){
        return R.ok(goodsSpuSpecService.updateById(goodsSpuSpec));
    }

    /**
    * 通过id删除spu规格
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除spu规格")
    @Log(title = "删除spu规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsspu:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSpuSpecService.removeById(id));
    }

}
