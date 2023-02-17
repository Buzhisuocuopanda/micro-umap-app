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
import com.mkst.umap.app.mall.common.entity.BargainCut;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.BargainCutService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 砍价帮砍记录
 *
 * @author JL
 * @date 2019-12-31 12:34:28
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/bargaincut")
@Api(value = "bargaincut", tags = "砍价帮砍记录管理")
public class BargainCutController {

    private final BargainCutService bargainCutService;

    /**
     * 分页列表
     * @param page 分页对象
     * @param bargainCut 砍价帮砍记录
     * @return
     */
	@ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @RequiresPermissions("mall:bargainuser:index")
    public R getPage(Page page, BargainCut bargainCut) {
        return R.ok(bargainCutService.page(page, Wrappers.query(bargainCut)));
    }

    /**
     * 砍价帮砍记录查询
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价帮砍记录查询")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:bargaincut:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(bargainCutService.getById(id));
    }

    /**
     * 砍价帮砍记录新增
     * @param bargainCut 砍价帮砍记录
     * @return R
     */
	@ApiOperation(value = "砍价帮砍记录新增")
    @Log(title = "砍价帮砍记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:bargaincut:add")
    public R save(@RequestBody BargainCut bargainCut) {
        return R.ok(bargainCutService.save(bargainCut));
    }

    /**
     * 砍价帮砍记录修改
     * @param bargainCut 砍价帮砍记录
     * @return R
     */
	@ApiOperation(value = "砍价帮砍记录修改")
    @Log(title = "砍价帮砍记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:bargaincut:edit")
    public R updateById(@RequestBody BargainCut bargainCut) {
        return R.ok(bargainCutService.updateById(bargainCut));
    }

    /**
     * 砍价帮砍记录删除
     * @param id
     * @return R
     */
	@ApiOperation(value = "砍价帮砍记录删除")
    @Log(title = "删除砍价帮砍记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:bargaincut:del")
    public R removeById(@PathVariable String id) {
        return R.ok(bargainCutService.removeById(id));
    }

}
