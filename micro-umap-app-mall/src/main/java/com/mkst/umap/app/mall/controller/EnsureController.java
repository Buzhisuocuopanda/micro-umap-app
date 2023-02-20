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
import com.mkst.umap.app.mall.common.entity.DeliveryPlace;
import com.mkst.umap.app.mall.common.entity.Ensure;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.EnsureService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 保障服务
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-02-09 23:31:36
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/ensure")
@Api(value = "ensure", tags = "保障服务管理")
public class EnsureController {

    private final EnsureService ensureService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param ensure 保障服务
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:ensure:index")
    public R getPage(Page page, Ensure ensure) {
        return R.ok(ensureService.page(page, Wrappers.query(ensure)));
    }

    /**
     * 保障服务查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "保障服务查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:ensure:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(ensureService.getById(id));
    }

	/**
	 * list查询
	 * @param ensure
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	@RequiresPermissions("mall:ensure:index")
	public List<Ensure> getList(Ensure ensure) {
		return ensureService.list(Wrappers.query(ensure).lambda()
				.select(Ensure::getId,
						Ensure::getName));
	}

    /**
     * 保障服务新增
     * @param ensure 保障服务
     * @return R
     */
	@ApiOperation(value = "保障服务新增")
    @Log(title = "保障服务", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:ensure:add")
    public R save(@RequestBody Ensure ensure) {
        return R.ok(ensureService.save(ensure));
    }

    /**
     * 保障服务修改
     * @param ensure 保障服务
     * @return R
     */
	@ApiOperation(value = "保障服务修改")
    @Log(title = "保障服务", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:ensure:edit")
    public R updateById(@RequestBody Ensure ensure) {
        return R.ok(ensureService.updateById(ensure));
    }

    /**
     * 保障服务删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "保障服务删除")
    @Log(title = "删除保障服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:ensure:del")
    public R removeById(@PathVariable String id) {
        return R.ok(ensureService.removeById(id));
    }

}
