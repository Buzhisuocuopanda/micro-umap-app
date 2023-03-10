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
import com.mkst.umap.app.mall.common.entity.FreightTemplat;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.FreightTemplatService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 运费模板
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-24 16:09:31
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/freighttemplat")
@Api(value = "freighttemplat", tags = "运费模板管理")
public class FreightTemplatController {

    private final FreightTemplatService freightTemplatService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param freightTemplat 运费模板
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:freighttemplat:index")
    public R getPage(Page page, FreightTemplat freightTemplat) {
        return R.ok(freightTemplatService.page(page, Wrappers.query(freightTemplat)));
    }

	/**
	 * list列表
	 * @param freightTemplat
	 * @return
	 */
	@ApiOperation(value = "list列表")
	@GetMapping("/list")
	@RequiresPermissions("mall:freighttemplat:index")
	public List<FreightTemplat> getList(FreightTemplat freightTemplat) {
		return freightTemplatService.list(Wrappers.query(freightTemplat));
	}

    /**
     * 运费模板查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "运费模板查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:freighttemplat:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(freightTemplatService.getById(id));
    }

    /**
     * 运费模板新增
     * @param freightTemplat 运费模板
     * @return R
     */
	@ApiOperation(value = "运费模板新增")
    @Log(title = "运费模板", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:freighttemplat:add")
    public R save(@RequestBody FreightTemplat freightTemplat) {
        return R.ok(freightTemplatService.save(freightTemplat));
    }

    /**
     * 运费模板修改
     * @param freightTemplat 运费模板
     * @return R
     */
	@ApiOperation(value = "运费模板修改")
    @Log(title = "运费模板", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:freighttemplat:edit")
    public R updateById(@RequestBody FreightTemplat freightTemplat) {
        return R.ok(freightTemplatService.updateById(freightTemplat));
    }

    /**
     * 运费模板删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "运费模板删除")
    @Log(title = "删除运费模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:freighttemplat:del")
    public R removeById(@PathVariable String id) {
        return R.ok(freightTemplatService.removeById(id));
    }

}
