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
import com.mkst.umap.app.mall.common.entity.ShoppingCart;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.ShoppingCartService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/shoppingcart")
@Api(value = "shoppingcart", tags = "购物车管理")
public class ShoppingCartController {

	private final ShoppingCartService shoppingCartService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param shoppingCart 购物车
	 * @return
	 */
	@ApiOperation(value = "分页查询")
	@GetMapping("/page")
	@RequiresPermissions("mall:shoppingcart:index")
	public R getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
		return R.ok(shoppingCartService.page(page,Wrappers.query(shoppingCart)));
	}

	/**
	 * 通过id查询购物车
	 * @param id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询购物车")
	@GetMapping("/{id}")
	@RequiresPermissions("mall:shoppingcart:get")
	public R getById(@PathVariable("id") String id){
		return R.ok(shoppingCartService.getById(id));
	}

	/**
	 * 新增购物车
	 * @param shoppingCart 购物车
	 * @return R
	 */
	@ApiOperation(value = "新增购物车")
	@Log(title = "购物车", businessType = BusinessType.INSERT)
	@PostMapping
	@RequiresPermissions("mall:shoppingcart:add")
	public R save(@RequestBody ShoppingCart shoppingCart){
		return R.ok(shoppingCartService.save(shoppingCart));
	}

	/**
	 * 修改购物车
	 * @param shoppingCart 购物车
	 * @return R
	 */
	@ApiOperation(value = "修改购物车")
	@Log(title = "购物车", businessType = BusinessType.UPDATE)
	@PutMapping
	@RequiresPermissions("mall:shoppingcart:edit")
	public R updateById(@RequestBody ShoppingCart shoppingCart){
		return R.ok(shoppingCartService.updateById(shoppingCart));
	}

	/**
	 * 通过id删除购物车
	 * @param id
	 * @return R
	 */
	@ApiOperation(value = "通过id删除购物车")
	@Log(title = "删除购物车", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
	@RequiresPermissions("mall:shoppingcart:del")
	public R removeById(@PathVariable String id){
		return R.ok(shoppingCartService.removeById(id));
	}

}
