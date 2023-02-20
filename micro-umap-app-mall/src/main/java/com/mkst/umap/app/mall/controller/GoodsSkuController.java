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
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.GoodsSku;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsSkuService;
import com.mkst.umap.app.mall.service.GoodsSpuService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 商品sku
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-13 10:18:34
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/goodssku")
@Api(value = "goodssku", tags = "商品sku管理")
public class GoodsSkuController {

    private final GoodsSkuService goodsSkuService;
	private final GoodsSpuService goodsSpuService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param goodsSku 商品sku
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:goodssku:index")
    public R getGoodsSkuPage(Page page, GoodsSku goodsSku) {
        return R.ok(goodsSkuService.page(page,Wrappers.query(goodsSku)));
    }

	/**
	 * list查询
	 * @param spuId
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list/{spuId}")
	@RequiresPermissions("mall:goodsspu:index")
	public List<GoodsSku> getList(@PathVariable("spuId") String spuId) {
		GoodsSpu goodsSpu = goodsSpuService.getById(spuId);
		List<GoodsSku> listRs = goodsSkuService.listGoodsSkuBySpuId(spuId).stream()
				.map(goodsSku -> {
					if(MallConstants.SPU_SPEC_TYPE_0.equals(goodsSpu.getSpecType())){
						goodsSku.setName("统一规格");
					}else{
						AtomicReference<String> name = new AtomicReference<>("");
						goodsSku.getSpecs().forEach(goodsSkuSpecValue -> {
							name.set(name +goodsSkuSpecValue.getSpecValueName() + "|");
						});
						String nameStr = name.get();
						goodsSku.setName(nameStr.substring(0,nameStr.length()-1));
					}
					goodsSku.setName(goodsSku.getName() + "  销售价：¥" + goodsSku.getSalesPrice());
					return goodsSku;
				}).collect(Collectors.toList());
		return listRs;
	}

    /**
    * 通过id查询商品sku
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商品sku")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:goodssku:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(goodsSkuService.getById(id));
    }

    /**
    * 新增商品sku
    * @param goodsSku 商品sku
    * @return R
    */
	@ApiOperation(value = "新增商品sku")
    @Log(title = "商品sku", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:goodssku:add")
    public R save(@RequestBody GoodsSku goodsSku){
        return R.ok(goodsSkuService.save(goodsSku));
    }

    /**
    * 修改商品sku
    * @param goodsSku 商品sku
    * @return R
    */
	@ApiOperation(value = "修改商品sku")
    @Log(title = "商品sku", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:goodssku:edit")
    public R updateById(@RequestBody GoodsSku goodsSku){
        return R.ok(goodsSkuService.updateById(goodsSku));
    }

    /**
    * 通过id删除商品sku
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商品sku")
    @Log(title = "删除商品sku", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:goodssku:del")
    public R removeById(@PathVariable String id){
        return R.ok(goodsSkuService.removeById(id));
    }

}
