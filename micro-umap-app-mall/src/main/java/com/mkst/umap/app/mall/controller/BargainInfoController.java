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
import com.mkst.umap.app.mall.common.entity.BargainInfo;
import com.mkst.umap.app.mall.common.entity.BargainUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.BargainInfoService;
import com.mkst.umap.app.mall.service.BargainUserService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 砍价
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-28 18:07:51
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/bargaininfo")
@Api(value = "bargaininfo", tags = "砍价管理")
public class BargainInfoController {

    private final BargainInfoService bargainInfoService;
	private final BargainUserService bargainUserService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param bargainInfo 砍价
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:bargaininfo:index")
    public R getPage(Page page, BargainInfo bargainInfo) {
        return R.ok(bargainInfoService.page(page, Wrappers.query(bargainInfo)));
    }

	/**
	 * list列表
	 * @param bargainInfo 砍价
	 * @return
	 */
	@ApiOperation(value = "list列表")
	@GetMapping("/list")
	@RequiresPermissions("mall:bargaininfo:index")
	public R getList(BargainInfo bargainInfo) {
		return R.ok(bargainInfoService.list(Wrappers.query(bargainInfo)));
	}

    /**
     * 砍价查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:bargaininfo:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bargainInfoService.getById(id));
    }

    /**
     * 砍价新增
     * @param bargainInfo 砍价
     * @return R
     */
	@ApiOperation(value = "砍价新增")
    @Log(title = "砍价", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:bargaininfo:add")
    public R save(@RequestBody BargainInfo bargainInfo) {
        return R.ok(bargainInfoService.save(bargainInfo));
    }

    /**
     * 砍价修改
     * @param bargainInfo 砍价
     * @return R
     */
	@ApiOperation(value = "砍价修改")
    @Log(title = "砍价", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:bargaininfo:edit")
    public R updateById(@RequestBody BargainInfo bargainInfo) {
        return R.ok(bargainInfoService.updateById(bargainInfo));
    }

    /**
     * 砍价删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价删除")
    @Log(title = "删除砍价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:bargaininfo:del")
    public R removeById(@PathVariable String id) {
		int count = bargainUserService.count(Wrappers.<BargainUser>lambdaQuery()
				.eq(BargainUser::getBargainId,id));
		if(count > 0){
			return R.failed(MyReturnCode.ERR_80020.getMsg());
		}
        return R.ok(bargainInfoService.removeById(id));
    }

}
