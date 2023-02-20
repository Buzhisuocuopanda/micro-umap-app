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
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.CouponInfoService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 电子券
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-14 11:30:58
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/couponinfo")
@Api(value = "couponinfo", tags = "电子券管理")
public class CouponInfoController {

    private final CouponInfoService couponInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param couponInfo 电子券
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:couponinfo:index")
    public R getPage(Page page, CouponInfo couponInfo) {
        return R.ok(couponInfoService.page(page, Wrappers.query(couponInfo)));
    }

	/**
	 * list查询
	 * @param couponInfo
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	@RequiresPermissions("mall:couponinfo:index")
	public List<CouponInfo> getList(CouponInfo couponInfo) {
		return couponInfoService.list(Wrappers.query(couponInfo).lambda()
				.select(CouponInfo::getId,
						CouponInfo::getName));
	}

    /**
     * 通过id查询电子券
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id查询电子券")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:couponinfo:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(couponInfoService.getById2(id));
    }

    /**
     * 新增电子券
     * @param couponInfo 电子券
     * @return R
     */
	@ApiOperation(value = "新增电子券")
    @Log(title = "电子券", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:couponinfo:add")
    public R save(@RequestBody CouponInfo couponInfo) {
        return R.ok(couponInfoService.save(couponInfo));
    }

    /**
     * 修改电子券
     * @param couponInfo 电子券
     * @return R
     */
	@ApiOperation(value = "修改电子券")
    @Log(title = "电子券", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:couponinfo:edit")
    public R updateById(@RequestBody CouponInfo couponInfo) {
        return R.ok(couponInfoService.updateById1(couponInfo));
    }

    /**
     * 通过id删除电子券
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id删除电子券")
    @Log(title = "删除电子券", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:couponinfo:del")
    public R removeById(@PathVariable String id) {
        return R.ok(couponInfoService.removeById(id));
    }

}
