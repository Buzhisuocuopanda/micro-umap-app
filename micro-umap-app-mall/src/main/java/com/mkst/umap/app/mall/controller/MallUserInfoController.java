/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商城用户
 *
 * @author JL
 * @date 2019-12-04 11:09:55
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/userinfo")
@Api(value = "userinfo", tags = "商城用户管理")
public class MallUserInfoController {

    private final UserInfoService userInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param userInfo 商城用户
     * @return
     */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:userinfo:index")
    public R getUserinfoPage(Page page, UserInfo userInfo) {
        return R.ok(userInfoService.page(page, Wrappers.query(userInfo)));
    }

	/**
	 * 查询数量
	 * @param userInfo
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public R getCount(UserInfo userInfo) {
		return R.ok(userInfoService.count(Wrappers.query(userInfo)));
	}

    /**
     * 通过id查询商城用户
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id查询商城用户")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:userinfo:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(userInfoService.getById(id));
    }

    /**
     * 新增商城用户
     * @param userInfo 商城用户
     * @return R
     */
	@ApiOperation(value = "新增商城用户")
    @Log(title = "商城用户", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:userinfo:add")
    public R save(@RequestBody UserInfo userInfo) {
        return R.ok(userInfoService.save(userInfo));
    }

    /**
     * 修改商城用户
     * @param userInfo 商城用户
     * @return R
     */
	@ApiOperation(value = "修改商城用户")
    @Log(title = "商城用户", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:userinfo:edit")
    public R updateById(@RequestBody UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 通过id删除商城用户
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id删除商城用户")
    @Log(title = "删除商城用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:userinfo:del")
    public R removeById(@PathVariable String id) {
        return R.ok(userInfoService.removeById(id));
    }

	/**
	 * 新增商城用户（供服务间调用）
	 * @param userInfo 商城用户
	 * @return R
	 */
	@ApiOperation(value = "新增商城用户")
	@PostMapping("/inside")
	public R saveInside(@RequestBody UserInfo userInfo) {
		userInfoService.saveInside(userInfo);
		return R.ok(userInfo);
	}
}
