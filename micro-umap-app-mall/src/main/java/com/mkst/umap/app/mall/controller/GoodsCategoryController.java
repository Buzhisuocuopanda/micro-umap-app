/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.GoodsCategory;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsCategoryService;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 商品类目
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 11:46:28
 */
@Controller
@RequestMapping("/goodscategory")
@Api(value = "goodscategory", tags = "商品类目管理")
public class GoodsCategoryController extends BaseController {

	private String prefix = "mall/category";
	@Autowired
	private GoodsCategoryService goodsCategoryService;

	@RequiresPermissions("mall:goodscategory:index")
	@GetMapping()
	public String category() {
		return prefix + "/goodsCategory";
	}

	@RequiresPermissions("mall:goodscategory:index")
	@GetMapping("/list")
	@ResponseBody
	public List<GoodsCategory> list(GoodsCategory goodsCategory) {
		QueryWrapper<GoodsCategory> wrapper = Wrappers.query();
		if (goodsCategory.getName() != null && goodsCategory.getName().trim() != "") {
			wrapper.like("name", goodsCategory.getName());
		}
		wrapper.orderByAsc("sort");
		List<GoodsCategory> list = goodsCategoryService.list(wrapper);
		return list;
	}

	/**
	 * 返回树形集合
	 * 
	 * @return
	 */
	@ApiOperation(value = "返回树形集合")
	@GetMapping("/selectTree/{id}")
	@RequiresPermissions("mall:goodscategory:index")
	public String getGoodsCategoryTree(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.put("group", goodsCategoryService.getById(id));
		return prefix + "/selectTree";
	}

	/**
	 * 加载列表树
	 */
	@RequiresPermissions("mall:goodscategory:index")
	@GetMapping("/treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData() {
		List<Map<String, Object>> tree = goodsCategoryService.selectTree(null);
		return tree;
	}

	/**
	 * 通过id查询商品类目
	 * 
	 * @param id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询商品类目")
	@GetMapping("/{id}")
	@RequiresPermissions("mall:goodscategory:index")
	public R getById(@PathVariable("id") String id) {
		return R.ok(goodsCategoryService.getById(id));
	}

	/**
	 * 新增分类
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") String parentId, ModelMap mmap) {
		mmap.put("group", goodsCategoryService.getById(parentId));
		return prefix + "/add";
	}

	/**
	 * 新增商品类目
	 * 
	 * @param goodsCategory 商品类目
	 * @return R
	 */
	@ResponseBody
	@ApiOperation(value = "新增商品类目")
	@Log(title = "商品类目", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@RequiresPermissions("mall:goodscategory:add")
	public AjaxResult save(GoodsCategory goodsCategory) {
		GoodsCategory selectCategory = new GoodsCategory();
		selectCategory.setParentId(goodsCategory.getParentId());
		selectCategory.setName(goodsCategory.getName());
		QueryWrapper<GoodsCategory> wrapper = Wrappers.query(selectCategory);
		List<GoodsCategory> list = goodsCategoryService.list(wrapper);
		if (CollectionUtil.isNotEmpty(list)) {
			return error("同一商品类目下不可存在相同类目名称");
		} else {
			GoodsCategory category = goodsCategoryService.getById(goodsCategory.getParentId());
			if(category == null || goodsCategory.getParentId() == null) {
				goodsCategory.setParentId("0");
			}
			return toAjax(goodsCategoryService.save(goodsCategory));
		}
	}

	/**
	 * 修改分类
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		GoodsCategory goodsCategory = goodsCategoryService.getById(id);
		GoodsCategory parentCategory = goodsCategoryService.getById(goodsCategory.getParentId());
		mmap.put("goodsCategory", goodsCategory);
		mmap.put("parentName", parentCategory.getName());
		return prefix + "/edit";
	}

	/**
	 * 修改商品类目
	 * 
	 * @param goodsCategory 商品类目
	 * @return R
	 */
	@ResponseBody
	@ApiOperation(value = "修改商品类目")
	@Log(title = "商品类目", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@RequiresPermissions("mall:goodscategory:edit")
	public AjaxResult updateById(@RequestBody GoodsCategory goodsCategory) {
		if (goodsCategory.getId().equals(goodsCategory.getParentId())) {
			return error("不能将本级设为父类");
		}
		return toAjax(goodsCategoryService.updateById(goodsCategory));
	}

	/**
	 * 通过id删除商品类目
	 * 
	 * @param id
	 * @return R
	 */
	@ResponseBody
	@ApiOperation(value = "通过id删除商品类目")
	@Log(title = "删除商品类目", businessType = BusinessType.DELETE)
	@PostMapping("/remove/{id}")
	@RequiresPermissions("mall:goodscategory:del")
	public AjaxResult removeById(@PathVariable("id") String id) {
		QueryWrapper<GoodsCategory> wrapper = Wrappers.query();
		wrapper.eq("parent_id", id);
		List<GoodsCategory> list = goodsCategoryService.list(wrapper);
		if(list.size() > 0) {
			return error("请先删除所选商品类目下的子类目。");
		}
		return toAjax(goodsCategoryService.removeById(id));
	}

}
