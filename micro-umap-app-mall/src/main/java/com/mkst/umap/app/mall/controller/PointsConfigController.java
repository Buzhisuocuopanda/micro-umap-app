/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.entity.PointsConfig;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.PointsConfigService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import io.swagger.annotations.Api;

/**
 * 积分设置
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-06 16:15:01
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/pointsconfig")
@Api(value = "pointsconfig", tags = "积分设置管理")
public class PointsConfigController {

    private final PointsConfigService pointsConfigService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pointsConfig 积分设置
     * @return
     */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:pointsconfig:index")
    public R getPointsConfigPage(Page page, PointsConfig pointsConfig) {
        return R.ok(pointsConfigService.page(page, Wrappers.query(pointsConfig)));
    }

    /**
     * 通过id查询积分设置
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id查询积分设置")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:pointsconfig:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(pointsConfigService.getById(id));
    }

    /**
     * 新增积分设置
     * @param pointsConfig 积分设置
     * @return R
     */
	@ApiOperation(value = "新增积分设置")
    @Log(title = "积分设置", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:pointsconfig:add")
    public R save(@RequestBody PointsConfig pointsConfig) {
        return R.ok(pointsConfigService.save(pointsConfig));
    }

    /**
     * 修改积分设置
     * @param pointsConfig 积分设置
     * @return R
     */
	@ApiOperation(value = "修改积分设置")
    @Log(title = "积分设置", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:pointsconfig:edit")
    public R updateById(@RequestBody PointsConfig pointsConfig) {
    	if(StrUtil.isNotBlank(pointsConfig.getId())){
			pointsConfigService.updateById(pointsConfig);
			return R.ok(pointsConfig);
		}else{
			pointsConfigService.save(pointsConfig);
			return R.ok(pointsConfig);
		}
    }

    /**
     * 通过id删除积分设置
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id删除积分设置")
    @Log(title = "删除积分设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:pointsconfig:del")
    public R removeById(@PathVariable String id) {
        return R.ok(pointsConfigService.removeById(id));
    }

	/**
	 * 查询积分设置
	 * @return R
	 */
	@ApiOperation(value = "查询积分设置")
	@GetMapping()
	@RequiresPermissions("mall:pointsconfig:get")
	public R get() {
		return R.ok(pointsConfigService.getOne(Wrappers.emptyWrapper()));
	}
}
