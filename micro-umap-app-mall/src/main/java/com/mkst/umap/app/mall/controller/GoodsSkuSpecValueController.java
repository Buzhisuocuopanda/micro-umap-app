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
import com.mkst.umap.app.mall.common.entity.GoodsSkuSpecValue;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSkuSpecValueService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 商品sku规格值
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 10:19:09
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsskuspecvalue")
@Api(value = "goodsskuspecvalue", tags = "商品sku规格值管理")
public class GoodsSkuSpecValueController {

    private final GoodsSkuSpecValueService goodsSkuSpecValueService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSkuSpecValue 商品sku规格值
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsskuspecvalue:index")
    public R getGoodsSkuSpecValuePage(Page page, GoodsSkuSpecValue goodsSkuSpecValue) {
        return R.ok(goodsSkuSpecValueService.page(page,Wrappers.query(goodsSkuSpecValue)));
    }


    /**
    * 通过id查询商品sku规格值
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商品sku规格值")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsskuspecvalue:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSkuSpecValueService.getById(id));
    }

    /**
    * 新增商品sku规格值
    * @param goodsSkuSpecValue 商品sku规格值
    * @return R
    */
	@ApiOperation(value = "新增商品sku规格值")
    @Log(title = "商品sku规格值", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsskuspecvalue:add")
    public R save(@RequestBody GoodsSkuSpecValue goodsSkuSpecValue){
        return R.ok(goodsSkuSpecValueService.save(goodsSkuSpecValue));
    }

    /**
    * 修改商品sku规格值
    * @param goodsSkuSpecValue 商品sku规格值
    * @return R
    */
	@ApiOperation(value = "修改商品sku规格值")
    @Log(title = "商品sku规格值", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsskuspecvalue:edit")
    public R updateById(@RequestBody GoodsSkuSpecValue goodsSkuSpecValue){
        return R.ok(goodsSkuSpecValueService.updateById(goodsSkuSpecValue));
    }

    /**
    * 通过id删除商品sku规格值
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商品sku规格值")
    @Log(title = "删除商品sku规格值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsskuspecvalue:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSkuSpecValueService.removeById(id));
    }

}
