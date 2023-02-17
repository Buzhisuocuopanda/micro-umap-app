/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.GrouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GrouponUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 拼团记录
 *
 * @author JL
 * @date 2020-03-17 12:01:53
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/grouponuser")
@Api(value = "grouponuser", tags = "拼团记录管理")
public class GrouponUserController {

    private final GrouponUserService grouponUserService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param grouponUser 拼团记录
     * @return
     */
    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:grouponuser:index")
    public R getPage(Page page, GrouponUser grouponUser) {
        return R.ok(grouponUserService.page1(page, grouponUser));
    }

    /**
     * 拼团记录查询
     * @param id
     * @return R
     */
    @ApiOperation(value = "拼团记录查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:grouponuser:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(grouponUserService.getById(id));
    }

    /**
     * 拼团记录新增
     * @param grouponUser 拼团记录
     * @return R
     */
    @ApiOperation(value = "拼团记录新增")
    @Log(title = "拼团记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:grouponuser:add")
    public R save(@RequestBody GrouponUser grouponUser) {
        return R.ok(grouponUserService.save(grouponUser));
    }

    /**
     * 拼团记录修改
     * @param grouponUser 拼团记录
     * @return R
     */
    @ApiOperation(value = "拼团记录修改")
    @Log(title = "拼团记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:grouponuser:edit")
    public R updateById(@RequestBody GrouponUser grouponUser) {
        return R.ok(grouponUserService.updateById(grouponUser));
    }

    /**
     * 拼团记录删除
     * @param id
     * @return R
     */
    @ApiOperation(value = "拼团记录删除")
    @Log(title = "删除拼团记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:grouponuser:del")
    public R removeById(@PathVariable String id) {
        return R.ok(grouponUserService.removeById(id));
    }

}
