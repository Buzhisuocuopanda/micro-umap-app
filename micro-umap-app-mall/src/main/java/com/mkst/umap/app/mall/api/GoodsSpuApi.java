/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.constant.MyReturnCode;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.entity.UserCollect;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.mapper.UserCollectMapper;
import com.mkst.umap.app.mall.service.CouponUserService;
import com.mkst.umap.app.mall.service.GoodsSpuService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品api
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-08-12 16:25:10
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/goodsspu")
@Api(value = "goodsspu", tags = "商品接口API")
public class GoodsSpuApi {

    private final GoodsSpuService goodsSpuService;
	private final UserCollectMapper userCollectMapper;
	private final CouponUserService couponUserService;

	/**
	* 分页查询
	* @param page 分页对象
	* @param goodsSpu spu商品
	* @return
	*/
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getGoodsSpuPage(HttpServletRequest request, Page page, GoodsSpu goodsSpu, String couponUserId) {
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		goodsSpu.setShelf(MallConstants.YES);
		CouponUser couponUser = null;
		if(StrUtil.isNotBlank(couponUserId)){
			couponUser = couponUserService.getById(couponUserId);
		}
        return R.ok(goodsSpuService.page2(page, goodsSpu, couponUser));
    }

    /**
    * 通过id查询spu商品
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询spu商品")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id){
		UserCollect userCollect = new UserCollect();
		R checkThirdSession = MallBaseApi.checkThirdSession(userCollect, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		GoodsSpu goodsSpu = goodsSpuService.getById2(id);
		if(goodsSpu == null){
			return R.failed(MyReturnCode.ERR_80004.getCode(), MyReturnCode.ERR_80004.getMsg());
		}
		//查询用户是否收藏
		userCollect.setType(MallConstants.COLLECT_TYPE_1);
		userCollect.setRelationId(id);
		goodsSpu.setCollectId(userCollectMapper.selectCollectId(userCollect));
        return R.ok(goodsSpu);
    }

}
