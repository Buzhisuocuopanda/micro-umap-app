/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.mall.common.entity.GoodsCategory;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsCategoryService;
import com.mkst.umap.app.mall.service.GoodsSpuService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spu商品
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 16:25:10
 */
@Slf4j
@Controller
@RequestMapping("/goodsspu")
@Api(value = "goodsspu", tags = "商品管理")
public class GoodsSpuController extends BaseController {

	@Autowired
    private GoodsSpuService goodsSpuService;

	@Autowired
    private GoodsCategoryService goodsCategoryService;

	private String prefix = "mall/goodsspu";

	@RequiresPermissions("mall:goodsspu:index")
	@GetMapping()
	public String goodsspu() {
		return prefix + "/goodsspu";
	}

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
	@PostMapping("/list")
	@ResponseBody
	@RequiresPermissions("mall:goodsspu:index")
	public TableDataInfo getList(GoodsSpu goodsSpu) {
		startPage();

		QueryWrapper<GoodsSpu> wrapper = Wrappers.query();
		if(goodsSpu.getName() != null  && goodsSpu.getName().trim() != "") {wrapper.like("name", goodsSpu.getName());}
		if(goodsSpu.getCategoryFirst() != null  && goodsSpu.getCategoryFirst().trim() != "") {wrapper.eq("category_first", goodsSpu.getCategoryFirst());}
		if(goodsSpu.getCategorySecond() != null  && goodsSpu.getCategorySecond().trim() != "") {wrapper.eq("category_second", goodsSpu.getCategorySecond());}
		if(goodsSpu.getShelf() != null  && goodsSpu.getShelf().trim() != "") {wrapper.eq("shelf", goodsSpu.getShelf());}

		List<GoodsSpu> list = goodsSpuService.list(wrapper);

		Map<String, List<GoodsCategory>> cateGoryMap = goodsCategoryService.list().stream().collect(Collectors.groupingBy(GoodsCategory::getId));
		list = list.stream().map(t -> {
			if (cateGoryMap.get(t.getCategoryFirst()) != null) {
				t.setCategoryFirstName(cateGoryMap.get(t.getCategoryFirst()).get(0).getName());
			}
			if (cateGoryMap.get(t.getCategorySecond()) != null) {
				t.setCategorySecondName(cateGoryMap.get(t.getCategorySecond()).get(0).getName());
			}
			return t;
		}).collect(Collectors.toList());
		return getDataTable(list);
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
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
//		mmap.put("group", goodsCategoryService.getById(parentId));
		return prefix + "/add";
	}

    /**
    * 新增spu商品
    * @param goodsSpu spu商品
    * @return R
    */
	@ApiOperation(value = "新增spu商品")
    @Log(title = "spu商品", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
    @RequiresPermissions("mall:goodsspu:add")
    public AjaxResult save(GoodsSpu goodsSpu){
        return toAjax(goodsSpuService.save1(goodsSpu));
    }

	/**
	 * 修改分类
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		GoodsSpu goodsSpu = goodsSpuService.getById(id);
		mmap.put("goodsSpu", goodsSpu);
		return prefix + "/edit";
	}

    /**
    * 修改spu商品
    * @param goodsSpu spu商品
    * @return R
    */
	@ApiOperation(value = "修改spu商品")
    @Log(title = "spu商品", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
    @RequiresPermissions("mall:goodsspu:edit")
    public AjaxResult edit(GoodsSpu goodsSpu){
        return toAjax(goodsSpuService.updateById(goodsSpu));
    }

	/**
	 * 商品上下架操作
	 * @param shelf
	 * @param ids
	 * @return R
	 */
	@ApiOperation(value = "商品上下架操作")
	@Log(title = "商品上下架操作", businessType = BusinessType.UPDATE)
	@PostMapping("/shelf")
	@ResponseBody
	@RequiresPermissions("mall:goodsspu:edit")
	public AjaxResult updateById(@RequestParam(value = "shelf") String shelf, @RequestParam(value = "ids") String ids){
		GoodsSpu goodsSpu = new GoodsSpu();
		goodsSpu.setShelf(shelf);
		return toAjax(goodsSpuService.update(goodsSpu,Wrappers.<GoodsSpu>lambdaQuery()
				.in(GoodsSpu::getId, Convert.toList(ids))));
	}

    /**
    * 通过id删除spu商品
    * @param ids
    * @return AjaxResult
    */
	@ApiOperation(value = "通过id删除spu商品")
    @Log(title = "删除spu商品", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
	@ResponseBody
    @RequiresPermissions("mall:goodsspu:del")
    public AjaxResult removeById(String ids){
		if(null == ids) {
			return error("入参错误");
		}
		List<String> idlist = Arrays.asList(ids.split(","));
		return toAjax(goodsSpuService.removeByIds(idlist));
    }

}
