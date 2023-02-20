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
import com.mkst.umap.app.mall.common.entity.BargainUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.BargainUserService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 砍价记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-30 11:53:14
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/bargainuser")
@Api(value = "bargainuser", tags = "砍价记录管理")
public class BargainUserController {

    private final BargainUserService bargainUserService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param bargainUser 砍价记录
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:bargainuser:index")
    public R getPage(Page page, BargainUser bargainUser) {
        return R.ok(bargainUserService.page1(page, bargainUser));
    }

    /**
     * 砍价记录查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价记录查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:bargainuser:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bargainUserService.getById(id));
    }

    /**
     * 砍价记录新增
     * @param bargainUser 砍价记录
     * @return R
     */
	@ApiOperation(value = "砍价记录新增")
    @Log(title = "砍价记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:bargainuser:add")
    public R save(@RequestBody BargainUser bargainUser) {
        return R.ok(bargainUserService.save(bargainUser));
    }

    /**
     * 砍价记录修改
     * @param bargainUser 砍价记录
     * @return R
     */
	@ApiOperation(value = "砍价记录修改")
    @Log(title = "砍价记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:bargainuser:edit")
    public R updateById(@RequestBody BargainUser bargainUser) {
        return R.ok(bargainUserService.updateById(bargainUser));
    }

    /**
     * 砍价记录删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价记录删除")
    @Log(title = "删除砍价记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:bargainuser:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bargainUserService.removeById(id));
    }

}
