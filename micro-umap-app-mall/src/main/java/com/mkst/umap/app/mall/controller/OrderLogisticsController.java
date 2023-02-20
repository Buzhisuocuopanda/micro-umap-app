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
import com.mkst.umap.app.mall.common.entity.OrderLogistics;
import com.mkst.umap.app.mall.common.enums.OrderLogisticsEnum;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.OrderLogisticsService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 订单物流
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-16 09:53:17
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderlogistics")
@Api(value = "orderlogistics", tags = "订单物流管理")
public class OrderLogisticsController {

    private final OrderLogisticsService orderLogisticsService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param orderLogistics 订单物流
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:orderlogistics:index")
    public R getOrderLogisticsPage(Page page, OrderLogistics orderLogistics) {
        return R.ok(orderLogisticsService.page(page,Wrappers.query(orderLogistics)));
    }

    /**
    * 通过id查询订单物流
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询订单物流")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:orderlogistics:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(orderLogisticsService.getById(id));
    }

    /**
    * 新增订单物流
    * @param orderLogistics 订单物流
    * @return R
    */
	@ApiOperation(value = "新增订单物流")
    @Log(title = "订单物流", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:orderlogistics:add")
    public R save(@RequestBody OrderLogistics orderLogistics){
        return R.ok(orderLogisticsService.save(orderLogistics));
    }

    /**
    * 修改订单物流
    * @param orderLogistics 订单物流
    * @return R
    */
	@ApiOperation(value = "修改订单物流")
    @Log(title = "订单物流", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:orderlogistics:edit")
    public R updateById(@RequestBody OrderLogistics orderLogistics){
        return R.ok(orderLogisticsService.updateById(orderLogistics));
    }

    /**
    * 通过id删除订单物流
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除订单物流")
    @Log(title = "删除订单物流", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:orderlogistics:del")
    public R removeById(@PathVariable String id){
        return R.ok(orderLogisticsService.removeById(id));
    }

	/**
	 * 获取相关枚举数据的字典
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "获取相关枚举数据的字典")
	@GetMapping("/dict/{type}")
	public R getDictByType(@PathVariable String type) {
		return R.ok(OrderLogisticsEnum.queryAll(type));
	}
}
