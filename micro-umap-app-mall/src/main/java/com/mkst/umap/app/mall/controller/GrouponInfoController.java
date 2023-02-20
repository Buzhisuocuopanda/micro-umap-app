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
import com.mkst.umap.app.mall.common.constant.MyReturnCode;
import com.mkst.umap.app.mall.common.entity.GrouponInfo;
import com.mkst.umap.app.mall.common.entity.GrouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GrouponInfoService;
import com.mkst.umap.app.mall.service.GrouponUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 拼团
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-03-17 11:55:32
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/grouponinfo")
@Api(value = "grouponinfo", tags = "拼团管理")
public class GrouponInfoController {

    private final GrouponInfoService grouponInfoService;
	private final GrouponUserService grouponUserService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param grouponInfo 拼团
     * @return
     */
    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:grouponinfo:index")
    public R getPage(Page page, GrouponInfo grouponInfo) {
        return R.ok(grouponInfoService.page(page, Wrappers.query(grouponInfo)));
    }

	/**
	 * list列表
	 * @param grouponInfo 拼团
	 * @return
	 */
	@ApiOperation(value = "list列表")
	@GetMapping("/list")
	@RequiresPermissions("mall:grouponinfo:index")
	public R getList(GrouponInfo grouponInfo) {
		return R.ok(grouponInfoService.list(Wrappers.query(grouponInfo)));
	}


	/**
     * 拼团查询
     * @param id
     * @return R
     */
    @ApiOperation(value = "拼团查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:grouponinfo:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(grouponInfoService.getById(id));
    }

    /**
     * 拼团新增
     * @param grouponInfo 拼团
     * @return R
     */
    @ApiOperation(value = "拼团新增")
    @Log(title = "拼团", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:grouponinfo:add")
    public R save(@RequestBody GrouponInfo grouponInfo) {
        return R.ok(grouponInfoService.save(grouponInfo));
    }

    /**
     * 拼团修改
     * @param grouponInfo 拼团
     * @return R
     */
    @ApiOperation(value = "拼团修改")
    @Log(title = "拼团", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:grouponinfo:edit")
    public R updateById(@RequestBody GrouponInfo grouponInfo) {
        return R.ok(grouponInfoService.updateById(grouponInfo));
    }

    /**
     * 拼团删除
     * @param id
     * @return R
     */
    @ApiOperation(value = "拼团删除")
    @Log(title = "删除拼团", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:grouponinfo:del")
    public R removeById(@PathVariable String id) {
		int count = grouponUserService.count(Wrappers.<GrouponUser>lambdaQuery()
				.eq(GrouponUser::getGrouponId,id));
		if(count > 0){
			return R.failed(MyReturnCode.ERR_80020.getMsg());
		}
        return R.ok(grouponInfoService.removeById(id));
    }

}
