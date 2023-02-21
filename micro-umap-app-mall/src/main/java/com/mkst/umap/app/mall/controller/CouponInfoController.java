/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gexin.rp.sdk.base.uitls.StringUtils;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.mall.common.entity.CouponInfo;
import com.mkst.umap.app.mall.service.CouponInfoService;

import io.swagger.annotations.Api;

/**
 * 电子券
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-14 11:30:58
 */
@Controller
@RequestMapping("/couponinfo")
@Api(value = "couponinfo", tags = "电子券管理")
public class CouponInfoController extends BaseController {

	private String prefix = "mall/couponinfo";
	@Autowired
	private CouponInfoService couponInfoService;

	@RequiresPermissions("mall:couponinfo:index")
	@GetMapping()
	public String view() {
		return prefix + "/list";
	}

	/**
	 * 分页查询
	 * 
	 * @param couponInfo 电子券
	 * @return
	 */
	@ResponseBody
	@PostMapping("/list")
	@RequiresPermissions("mall:couponinfo:index")
	public TableDataInfo list(CouponInfo couponInfo) {
		LambdaQueryWrapper<CouponInfo> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(StringUtils.isNotBlank(couponInfo.getType()), CouponInfo::getType, couponInfo.getType());
		wrapper.eq(StringUtils.isNotBlank(couponInfo.getExpireType()), CouponInfo::getExpireType, couponInfo.getExpireType());
		wrapper.eq(StringUtils.isNotBlank(couponInfo.getSuitType()), CouponInfo::getSuitType, couponInfo.getSuitType());
		wrapper.eq(StringUtils.isNotBlank(couponInfo.getEnable()), CouponInfo::getEnable, couponInfo.getEnable());
		wrapper.orderByAsc(CouponInfo::getSort);
		List<CouponInfo> list = couponInfoService.list(wrapper);
		return getDataTable(list);
	}

	/**
	 * 新增卡券管理
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存卡券管理
	 */
	@RequiresPermissions("mall:couponinfo:add")
	@Log(title = "卡券管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CouponInfo couponInfo) {
		return toAjax(couponInfoService.save(couponInfo));
	}

	/**
	 * 修改卡券管理
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		CouponInfo couponInfo = couponInfoService.getById2(id);
		mmap.put("couponInfo", couponInfo);
		return prefix + "/edit";
	}

	/**
	 * 修改保存卡券管理
	 */
	@RequiresPermissions("mall:couponinfo:edit")
	@Log(title = "卡券管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CouponInfo couponInfo) {
		return toAjax(couponInfoService.updateById1(couponInfo));
	}

	/**
	 * 删除卡券管理
	 */
	@RequiresPermissions("mall:couponinfo:del")
	@Log(title = "卡券管理", businessType = BusinessType.DELETE)
	@PostMapping("/remove/{id}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("id") String id) {
		return toAjax(couponInfoService.removeById(id));
	}

}
