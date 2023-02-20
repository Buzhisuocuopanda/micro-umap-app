/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.mkst.umap.app.mall.common.entity.GoodsAppraises;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.GoodsAppraisesService;
import com.mkst.umap.app.mall.service.OrderInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品评价
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-23 15:51:01
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/goodsappraises")
@Api(value = "goodsappraises", tags = "商品评价Api")
public class GoodsAppraisesApi {

	private final OrderInfoService orderInfoService;
    private final GoodsAppraisesService goodsAppraisesService;
//	private final FeignWxUserService feignWxUserService;

	/**
    * 分页查询
    * @param page 分页对象
    * @param goodsAppraises 商品评价
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getGoodsAppraisesPage(HttpServletRequest request, Page page, GoodsAppraises goodsAppraises) {
		R checkThirdSession = MallBaseApi.checkThirdSession(goodsAppraises, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(goodsAppraisesService.page(page,Wrappers.query(goodsAppraises)));
    }

    /**
    * 通过id查询商品评价
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商品评价")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id){
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(goodsAppraisesService.getById(id));
    }

    /**
    * 新增商品评价
    * @param listGoodsAppraises 商品评价
    * @return R
    */
	@ApiOperation(value = "新增商品评价")
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody List<GoodsAppraises> listGoodsAppraises){
//    	WxUser wxUser = new WxUser();
//		R checkThirdSession = BaseApi.checkThirdSession(wxUser, request);
//		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
//			return checkThirdSession;
//		}
//		OrderInfo orderInfo = orderInfoService.getById(listGoodsAppraises.get(0).getOrderId());
//		if(!OrderInfoEnum.STATUS_3.getValue().equals(orderInfo.getStatus())){
//			return R.failed(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
//		}
//		R r = feignWxUserService.getById(wxUser.getId(), SecurityConstants.FROM_IN);
//		Map map = (Map<Object, Object>)r.getData();
//		listGoodsAppraises.forEach(goodsAppraises -> {
//			goodsAppraises.setUserId(wxUser.getId());
//			goodsAppraises.setHeadimgUrl(map.get("headimgUrl")!=null ? String.valueOf(map.get("headimgUrl")) : null);
//			goodsAppraises.setNickName(map.get("nickName")!=null ? String.valueOf(map.get("nickName")) : null);
//		});
//		goodsAppraisesService.doAppraises(listGoodsAppraises);
        return R.ok();
    }

    /**
    * 修改商品评价
    * @param goodsAppraises 商品评价
    * @return R
    */
	@ApiOperation(value = "修改商品评价")
    @PutMapping
    public R updateById(HttpServletRequest request, @RequestBody GoodsAppraises goodsAppraises){
		R checkThirdSession = MallBaseApi.checkThirdSession(goodsAppraises, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(goodsAppraisesService.updateById(goodsAppraises));
    }

    /**
    * 通过id删除商品评价
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商品评价")
    @DeleteMapping("/{id}")
    public R removeById(HttpServletRequest request, @PathVariable String id){
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(goodsAppraisesService.removeById(id));
    }

}
