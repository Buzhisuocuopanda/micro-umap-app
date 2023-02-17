/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSpuService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import java.util.List;

/**
 * spu商品
 *
 * @author JL
 * @date 2019-08-12 16:25:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodsspu")
@Api(value = "goodsspu", tags = "spu商品管理")
public class GoodsSpuController {

    private final GoodsSpuService goodsSpuService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSpu spu商品
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodsspu:index")
    public R getGoodsSpuPage(Page page, GoodsSpu goodsSpu) {
		return R.ok(goodsSpuService.page1(page, goodsSpu));
    }

	/**
	 * list查询
	 * @param goodsSpu
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	@RequiresPermissions("mall:goodsspu:index")
	public List<GoodsSpu> getList(GoodsSpu goodsSpu) {
		return goodsSpuService.list(Wrappers.query(goodsSpu).lambda()
						.select(GoodsSpu::getId,
								GoodsSpu::getName)
				);
	}

	/**
	 * 查询数量
	 * @param goodsSpu
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public R getCount(GoodsSpu goodsSpu) {
		return R.ok(goodsSpuService.count(Wrappers.query(goodsSpu)));
	}

    /**
    * 通过id查询spu商品
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询spu商品")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodsspu:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSpuService.getById1(id));
    }

    /**
    * 新增spu商品
    * @param goodsSpu spu商品
    * @return R
    */
	@ApiOperation(value = "新增spu商品")
    @Log(title = "spu商品", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodsspu:add")
    public R save(@RequestBody GoodsSpu goodsSpu){
        return R.ok(goodsSpuService.save1(goodsSpu));
    }

    /**
    * 修改spu商品
    * @param goodsSpu spu商品
    * @return R
    */
	@ApiOperation(value = "修改spu商品")
    @Log(title = "spu商品", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodsspu:edit")
    public R updateById(@RequestBody GoodsSpu goodsSpu){
        return R.ok(goodsSpuService.updateById1(goodsSpu));
    }

	/**
	 * 商品上下架操作
	 * @param shelf
	 * @param ids
	 * @return R
	 */
	@ApiOperation(value = "商品上下架操作")
	@Log(title = "商品上下架操作", businessType = BusinessType.UPDATE)
	@PutMapping("/shelf")
	@RequiresPermissions("mall:goodsspu:edit")
	public R updateById(@RequestParam(value = "shelf") String shelf, @RequestParam(value = "ids") String ids){
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setShelf(shelf);
		return R.ok(goodsSpuService.update(goodsSpu,Wrappers.<GoodsSpu>lambdaQuery()
				.in(GoodsSpu::getId, Convert.toList(ids))));
	}

    /**
    * 通过id删除spu商品
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除spu商品")
    @Log(title = "删除spu商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodsspu:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSpuService.removeById(id));
    }

}
