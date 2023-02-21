/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.controller;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.constant.MyReturnCode;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.common.entity.OrderItem;
import com.mkst.umap.app.mall.common.entity.OrderLogistics;
import com.mkst.umap.app.mall.common.enums.OrderInfoEnum;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.OrderInfoService;
import com.mkst.umap.app.mall.service.OrderLogisticsService;
import com.mkst.umap.app.mall.service.UserInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 商城订单
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-10 15:21:22
 */
@Slf4j
@Controller
@RequestMapping("/orderinfo")
@Api(value = "orderinfo", tags = "商城订单管理")
public class OrderInfoController extends BaseController {

	@Autowired
    private OrderInfoService orderInfoService;

	@Autowired
	private UserInfoService userInfoService;
//	private FeignWxAppService feignWxAppService;

	@Autowired
	private OrderLogisticsService orderLogisticsService;

	private String prefix = "mall/orderinfo";


	@RequiresPermissions("mall:orderinfo:index")
	@GetMapping()
	public String orderinfo() {
		return prefix + "/orderinfo";
	}


	/**
    * 分页查询
    * @param page 分页对象
    * @param orderInfo 商城订单
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @RequiresPermissions("mall:orderinfo:index")
    public R getOrderInfoPage(Page page, OrderInfo orderInfo) {
        return R.ok(orderInfoService.page1(page, Wrappers.query(orderInfo)));
    }


	/**
	 * list查询
	 * @param orderInfo
	 * @return
	 */
	@ApiOperation(value = "list查询")
	@PostMapping("/list")
	@ResponseBody
	@RequiresPermissions("mall:orderInfo:index")
	public TableDataInfo getList(OrderInfo orderInfo) {
		startPage();

		QueryWrapper<OrderInfo> wrapper = Wrappers.query();
		if(orderInfo.getIsPay() != null  && orderInfo.getIsPay().trim() != "") {wrapper.eq("is_pay", orderInfo.getIsPay());}
		if(orderInfo.getName() != null  && orderInfo.getName().trim() != "") {wrapper.like("name", orderInfo.getName());}

		List<OrderInfo> list = orderInfoService.list(wrapper);
		return getDataTable(list);
	}


	/**
	 * 查询数量
	 * @param orderInfo
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public R getCount(OrderInfo orderInfo) {
		return R.ok(orderInfoService.count(Wrappers.query(orderInfo)));
	}

    /**
    * 通过id查询商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商城订单")
    @GetMapping("/{id}")
    @RequiresPermissions("mall:orderinfo:get")
    public R getById(@PathVariable("id") String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
//		R r2 = feignWxAppService.getById(orderInfo.getAppId(), SecurityConstants.FROM_IN);
		orderInfo.setUserInfo(userInfoService.getById(orderInfo.getUserId()));
//		orderInfo.setApp((Map<Object, Object>)r2.getData());
		OrderLogistics orderLogistics = orderLogisticsService.getById(orderInfo.getLogisticsId());
		orderInfo.setOrderLogistics(orderLogistics);
        return R.ok(orderInfo);
    }

    /**
    * 新增商城订单
    * @param orderInfo 商城订单
    * @return R
    */
	@ApiOperation(value = "新增商城订单")
    @Log(title = "商城订单", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("mall:orderinfo:add")
    public R save(@RequestBody OrderInfo orderInfo){
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
    * 修改商城订单
    * @param orderInfo 商城订单
    * @return R
    */
	@ApiOperation(value = "修改商城订单")
    @Log(title = "商城订单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("mall:orderinfo:edit")
    public R updateById(@RequestBody OrderInfo orderInfo){
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
    * 通过id删除商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商城订单")
    @Log(title = "删除商城订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @RequiresPermissions("mall:orderinfo:del")
    public R removeById(@PathVariable String id){
        return R.ok(orderInfoService.removeById(id));
    }

	/**
	 * 修改商城订单价格
	 * @param orderItem
	 * @return R
	 */
	@ApiOperation(value = "修改商城订单价格")
	@Log(title = "商城订单价格", businessType = BusinessType.UPDATE)
	@PutMapping("/editPrice")
	@RequiresPermissions("mall:orderinfo:edit")
	public R editPrice(@RequestBody OrderItem orderItem){
		orderInfoService.editPrice(orderItem);
		return R.ok();
	}

	/**
	 * 取消商城订单
	 * @param id 商城订单
	 * @return R
	 */
	@ApiOperation(value = "取消商城订单")
	@Log(title = "取消商城订单", businessType = BusinessType.OTHER)
	@PutMapping("/cancel/{id}")
	@RequiresPermissions("mall:orderinfo:edit")
	public R orderCancel(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!MallConstants.NO.equals(orderInfo.getIsPay())){//只有未支付订单能取消
			return R.failed(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		return R.ok();
	}

	/**
	 * 商城订单自提
	 * @param id 商城订单ID
	 * @return R
	 */
	@ApiOperation(value = "商城订单自提")
	@PutMapping("/takegoods/{id}")
	@RequiresPermissions("mall:orderinfo:edit")
	public R takeGoods(@PathVariable String id){
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!MallConstants.DELIVERY_WAY_2.equals(orderInfo.getDeliveryWay())
				&& !OrderInfoEnum.STATUS_1.getValue().equals(orderInfo.getStatus())){//只有待自提订单能确认收货
			return R.failed(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderReceive(orderInfo);
		return R.ok();
	}
}
