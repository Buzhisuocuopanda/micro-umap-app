/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.OrderItemService;

import javax.servlet.http.HttpServletRequest;

/**
 * 商城订单详情
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-10 15:31:40
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/orderitem")
@Api(value = "orderitem", tags = "商城订单详情API")
public class OrderItemApi {

    private final OrderItemService orderItemService;

    /**
    * 通过id查询商城订单详情
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商城订单详情")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id){
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(orderItemService.getById2(id));
    }

}
