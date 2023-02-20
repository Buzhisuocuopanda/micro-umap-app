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
import com.mkst.umap.app.mall.common.entity.UserCollect;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.UserCollectService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 用户收藏
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-22 20:45:45
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/usercollect")
@Api(value = "usercollect", tags = "用户收藏管理")
public class UserCollectController {

    private final UserCollectService userCollectService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param userCollect 用户收藏
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:usercollect:index")
    public R getUserCollectPage(Page page, UserCollect userCollect) {
        return R.ok(userCollectService.page(page,Wrappers.query(userCollect)));
    }

    /**
    * 通过id查询用户收藏
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询用户收藏")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:usercollect:get")
    public R getById(@PathVariable("id") String id){
        return R.ok(userCollectService.getById(id));
    }

    /**
    * 新增用户收藏
    * @param userCollect 用户收藏
    * @return R
    */
	@ApiOperation(value = "新增用户收藏")
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:usercollect:add")
    public R save(@RequestBody UserCollect userCollect){
        return R.ok(userCollectService.save(userCollect));
    }

    /**
    * 修改用户收藏
    * @param userCollect 用户收藏
    * @return R
    */
	@ApiOperation(value = "修改用户收藏")
    @Log(title = "用户收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:usercollect:edit")
    public R updateById(@RequestBody UserCollect userCollect){
        return R.ok(userCollectService.updateById(userCollect));
    }

    /**
    * 通过id删除用户收藏
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除用户收藏")
    @Log(title = "删除用户收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:usercollect:del")
    public R removeById(@PathVariable String id){
        return R.ok(userCollectService.removeById(id));
    }

}
