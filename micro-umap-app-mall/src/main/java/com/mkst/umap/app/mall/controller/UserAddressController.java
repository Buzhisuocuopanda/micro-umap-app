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
import com.mkst.umap.app.mall.common.entity.UserAddress;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.UserAddressService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 用户收货地址
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-11 14:28:59
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/useraddress")
@Api(value = "useraddress", tags = "用户收货地址管理")
public class UserAddressController {

    private final UserAddressService userAddressService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param userAddress 用户收货地址
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:useraddress:index")
    public R getUserAddressPage(Page page, UserAddress userAddress) {
        return R.ok(userAddressService.page(page,Wrappers.query(userAddress)));
    }

    /**
    * 通过id查询用户收货地址
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询用户收货地址")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:useraddress:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(userAddressService.getById(id));
    }

    /**
    * 新增用户收货地址
    * @param userAddress 用户收货地址
    * @return R
    */
	@ApiOperation(value = "新增用户收货地址")
    @Log(title = "用户收货地址", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:useraddress:add")
    public R save(@RequestBody UserAddress userAddress){
        return R.ok(userAddressService.save(userAddress));
    }

    /**
    * 修改用户收货地址
    * @param userAddress 用户收货地址
    * @return R
    */
	@ApiOperation(value = "修改用户收货地址")
    @Log(title = "用户收货地址", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:useraddress:edit")
    public R updateById(@RequestBody UserAddress userAddress){
        return R.ok(userAddressService.updateById(userAddress));
    }

    /**
    * 通过id删除用户收货地址
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除用户收货地址")
    @Log(title = "删除用户收货地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:useraddress:del")
    public R removeById(@PathVariable String id){
        return R.ok(userAddressService.removeById(id));
    }

}
