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
import com.mkst.umap.app.mall.common.entity.DeliveryPlace;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.DeliveryPlaceService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

import java.util.List;

/**
 * 发货地
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-02-09 22:23:53
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/deliveryplace")
@Api(value = "deliveryplace", tags = "发货地管理")
public class DeliveryPlaceController {

    private final DeliveryPlaceService deliveryPlaceService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param deliveryPlace 发货地
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:deliveryplace:index")
    public R getPage(Page page, DeliveryPlace deliveryPlace) {
        return R.ok(deliveryPlaceService.page(page, Wrappers.query(deliveryPlace)));
    }

    /**
     * 发货地查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "发货地查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:deliveryplace:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(deliveryPlaceService.getById(id));
    }

	/**
	 * list查询
	 * @param deliveryPlace
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@GetMapping("/list")
	@RequiresPermissions("mall:deliveryplace:index")
	public List<DeliveryPlace> getList(DeliveryPlace deliveryPlace) {
		return deliveryPlaceService.list(Wrappers.query(deliveryPlace).lambda()
				.select(DeliveryPlace::getId,
						DeliveryPlace::getPlace));
	}

    /**
     * 发货地新增
     * @param deliveryPlace 发货地
     * @return R
     */
	@ApiOperation(value = "发货地新增")
    @Log(title = "发货地", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:deliveryplace:add")
    public R save(@RequestBody DeliveryPlace deliveryPlace) {
        return R.ok(deliveryPlaceService.save(deliveryPlace));
    }

    /**
     * 发货地修改
     * @param deliveryPlace 发货地
     * @return R
     */
	@ApiOperation(value = "发货地修改")
    @Log(title = "发货地", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:deliveryplace:edit")
    public R updateById(@RequestBody DeliveryPlace deliveryPlace) {
        return R.ok(deliveryPlaceService.updateById(deliveryPlace));
    }

    /**
     * 发货地删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "发货地删除")
    @Log(title = "删除发货地", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:deliveryplace:del")
    public R removeById(@PathVariable String id) {
        return R.ok(deliveryPlaceService.removeById(id));
    }

}
