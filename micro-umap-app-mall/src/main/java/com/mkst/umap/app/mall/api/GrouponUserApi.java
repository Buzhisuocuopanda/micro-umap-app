/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.entity.GrouponUser;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GrouponInfoService;
import com.mkst.umap.app.mall.service.GrouponUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 拼团记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @date 2020-03-17 12:01:53
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/grouponuser")
@Api(value = "grouponuser", tags = "拼团记录Api")
public class GrouponUserApi {

    private final GrouponUserService grouponUserService;
	private final GrouponInfoService grouponInfoService;

	/**
	 * 拼团中分页列表
	 * @param page 分页对象
	 * @param grouponUser 拼团记录
	 * @return
	 */
	@ApiOperation(value = "拼团中分页列表")
	@GetMapping("/page/grouponing")
	public R getPageGrouponing(HttpServletRequest request, Page page, GrouponUser grouponUser) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		return R.ok(grouponUserService.getPageGrouponing(page, grouponUser));
	}

    /**
     * 分页列表
     * @param page 分页对象
     * @param grouponUser 拼团记录
     * @return
     */
    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    public R getPage(HttpServletRequest request, Page page, GrouponUser grouponUser) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(grouponUserService.page2(page, grouponUser));
    }

	/**
	 * 拼团记录查询
	 * @param id
	 * @return R
	 */
	@ApiOperation(value = "拼团记录查询")
	@GetMapping("/{id}")
	public R getById(HttpServletRequest request, @PathVariable("id") String id) {
		GrouponUser grouponUser = new GrouponUser();
		R checkThirdSession = MallBaseApi.checkThirdSession(grouponUser, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		String userId = grouponUser.getUserId();
		grouponUser = grouponUserService.getById(id);
		grouponUser.setGrouponInfo(grouponInfoService.getById2((grouponUser.getGrouponId())));
		//查询当前用户是否已参与
		grouponUser.setGrouponUser(
				grouponUserService.getOne(Wrappers.<GrouponUser>lambdaQuery()
				.eq(GrouponUser::getGroupId,id)
				.eq(GrouponUser::getUserId,userId)));
		return R.ok(grouponUser);
	}

}
