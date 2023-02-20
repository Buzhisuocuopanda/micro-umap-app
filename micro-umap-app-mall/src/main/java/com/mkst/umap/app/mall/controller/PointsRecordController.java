/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
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
import com.mkst.umap.app.mall.common.entity.PointsRecord;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.PointsRecordService;
import com.mkst.umap.app.mall.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分变动记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-05 19:47:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/pointsrecord")
@Api(value = "pointsrecord", tags = "积分变动记录管理")
public class PointsRecordController {

    private final PointsRecordService pointsRecordService;
	private final UserInfoService userInfoService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param pointsRecord 积分变动记录
	 * @return
	 */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:pointsrecord:index")
    public R getPointsRecordPage(Page page, PointsRecord pointsRecord, UserInfo userInfo) {
    	if(userInfo.getUserCode() != null && userInfo.getUserCode()!= 0){
    		int userCode = userInfo.getUserCode();
			userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
					.lambda().eq(UserInfo::getUserCode, userCode));
			if(userInfo != null){
				pointsRecord.setUserId(userInfo.getId());
			}else{
				pointsRecord.setUserId(String.valueOf(userCode));
			}
		}
        return R.ok(pointsRecordService.page1(page, pointsRecord));
    }

    /**
     * 通过id查询积分变动记录
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id查询积分变动记录")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:pointsrecord:get")
    public R getById(@PathVariable("id") String id) {
        return R.ok(pointsRecordService.getById(id));
    }

    /**
     * 新增积分变动记录
     * @param pointsRecord 积分变动记录
     * @return R
     */
	@ApiOperation(value = "新增积分变动记录")
    @Log(title = "积分变动记录", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:pointsrecord:add")
    public R save(@RequestBody PointsRecord pointsRecord) {
        return R.ok(pointsRecordService.save(pointsRecord));
    }

    /**
     * 修改积分变动记录
     * @param pointsRecord 积分变动记录
     * @return R
     */
	@ApiOperation(value = "修改积分变动记录")
    @Log(title = "积分变动记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:pointsrecord:edit")
    public R updateById(@RequestBody PointsRecord pointsRecord) {
        return R.ok(pointsRecordService.updateById(pointsRecord));
    }

    /**
     * 通过id删除积分变动记录
     * @param id
     * @return R
     */
	@ApiOperation(value = "通过id删除积分变动记录")
    @Log(title = "删除积分变动记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:pointsrecord:del")
    public R removeById(@PathVariable String id) {
        return R.ok(pointsRecordService.removeById(id));
    }

}
