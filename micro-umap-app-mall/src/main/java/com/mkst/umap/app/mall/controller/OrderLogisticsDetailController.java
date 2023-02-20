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
import com.mkst.umap.app.mall.common.entity.OrderLogisticsDetail;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.OrderLogisticsDetailService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 物流详情
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-21 12:39:00
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderlogisticsdetail")
@Api(value = "orderlogisticsdetail", tags = "物流详情管理")
public class OrderLogisticsDetailController {

    private final OrderLogisticsDetailService orderLogisticsDetailService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param orderLogisticsDetail 物流详情
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:orderlogisticsdetail:index")
    public R getOrderLogisticsDetailPage(Page page, OrderLogisticsDetail orderLogisticsDetail) {
        return R.ok(orderLogisticsDetailService.page(page,Wrappers.query(orderLogisticsDetail)));
    }

    /**
    * 通过id查询物流详情
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询物流详情")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:orderlogisticsdetail:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(orderLogisticsDetailService.getById(id));
    }

    /**
    * 新增物流详情
    * @param orderLogisticsDetail 物流详情
    * @return R
    */
	@ApiOperation(value = "新增物流详情")
    @Log(title = "物流详情", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:orderlogisticsdetail:add")
    public R save(@RequestBody OrderLogisticsDetail orderLogisticsDetail){
        return R.ok(orderLogisticsDetailService.save(orderLogisticsDetail));
    }

    /**
    * 修改物流详情
    * @param orderLogisticsDetail 物流详情
    * @return R
    */
	@ApiOperation(value = "修改物流详情")
    @Log(title = "物流详情", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:orderlogisticsdetail:edit")
    public R updateById(@RequestBody OrderLogisticsDetail orderLogisticsDetail){
        return R.ok(orderLogisticsDetailService.updateById(orderLogisticsDetail));
    }

    /**
    * 通过id删除物流详情
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除物流详情")
    @Log(title = "删除物流详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:orderlogisticsdetail:del")
    public R removeById(@PathVariable String id){
        return R.ok(orderLogisticsDetailService.removeById(id));
    }

}
