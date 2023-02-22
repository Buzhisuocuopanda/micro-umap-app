/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.constant.MyReturnCode;
import com.mkst.umap.app.mall.common.dto.PlaceOrderDTO;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.common.enums.OrderInfoEnum;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.config.TenantContextHolder;
import com.mkst.umap.app.mall.service.BargainUserService;
import com.mkst.umap.app.mall.service.GrouponInfoService;
import com.mkst.umap.app.mall.service.GrouponUserService;
import com.mkst.umap.app.mall.service.OrderInfoService;
import com.mkst.umap.app.mall.service.OrderLogisticsService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/orderinfo")
@Api(value = "orderinfo", tags = "商城订单API")
public class OrderInfoApi {

    private final OrderInfoService orderInfoService;
//	private final FeignWxPayService feignWxPayService;
//	private final MallConfigProperties mallConfigProperties;
	private final OrderLogisticsService orderLogisticsService;
	private final GrouponInfoService grouponInfoService;
	private final GrouponUserService grouponUserService;
	private final BargainUserService bargainUserService;

	/**
	* 分页查询
	* @param page 分页对象
	* @param orderInfo 商城订单
	* @return
	*/
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getOrderInfoPage(HttpServletRequest request, Page page, OrderInfo orderInfo) {
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(orderInfo, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(orderInfoService.page2(page,orderInfo));
    }

    /**
    * 通过id查询商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id查询商城订单")
    @GetMapping("/{id}")
    public R getById(HttpServletRequest request, @PathVariable("id") String id){
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(orderInfoService.getById2(id));
    }

    /**
    * 新增商城订单
    * @param placeOrderDTO 商城订单
    * @return R
    */
	@ApiOperation(value = "新增商城订单")
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody PlaceOrderDTO placeOrderDTO){
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(placeOrderDTO, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		if(StrUtil.isBlank(placeOrderDTO.getPaymentType())){
			R.failed(MyReturnCode.ERR_70002.getCode(), MyReturnCode.ERR_70002.getMsg());
		}
		placeOrderDTO.setAppType(MallConstants.APP_TYPE_1);
		placeOrderDTO.setPaymentWay(MallConstants.PAYMENT_WAY_2);
		OrderInfo orderInfo = orderInfoService.orderSub(placeOrderDTO);
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70003.getCode(), MyReturnCode.ERR_70003.getMsg());
		}
		return R.ok(orderInfo);
    }

    /**
    * 通过id删除商城订单
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除商城订单")
    @DeleteMapping("/{id}")
    public R removeById(HttpServletRequest request, @PathVariable String id){
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus()) || MallConstants.YES.equals(orderInfo.getIsPay())){
			return R.failed(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		return R.ok(orderInfoService.removeById(id));
    }

	/**
	 * 取消商城订单
	 * @param id 商城订单
	 * @return R
	 */
	@ApiOperation(value = "取消商城订单")
	@PutMapping("/cancel/{id}")
	public R orderCancel(HttpServletRequest request, @PathVariable String id){
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
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
	 * 商城订单确认收货
	 * @param id 商城订单
	 * @return R
	 */
	@ApiOperation(value = "商城订单确认收货")
	@PutMapping("/receive/{id}")
	public R orderReceive(HttpServletRequest request, @PathVariable String id){
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		OrderInfo orderInfo = orderInfoService.getById(id);
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())){//只有待收货订单能确认收货
			return R.failed(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderReceive(orderInfo);
		return R.ok();
	}

	/**
	 * 调用统一下单接口，并组装生成支付所需参数对象.
	 *
	 * @param request 统一下单请求参数
	 * @return 返回 {@link com.github.binarywang.wxpay.bean.order}包下的类对象
	 */
	@ApiOperation(value = "调用统一下单接口")
	@PostMapping("/unifiedOrder")
	public R unifiedOrder(HttpServletRequest request, @RequestBody OrderInfo orderInfo){
		//检验用户session登录
//		WxUser wxUser = new WxUser();
//		R checkThirdSession = BaseApi.checkThirdSession(wxUser, request);
//		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
//			return checkThirdSession;
//		}
		orderInfo = orderInfoService.getById(orderInfo.getId());
		if(orderInfo == null){
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if(!MallConstants.NO.equals(orderInfo.getIsPay())){//只有未支付的详单能发起支付
			return R.failed(MyReturnCode.ERR_70004.getCode(), MyReturnCode.ERR_70004.getMsg());
		}
		if(orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO)==0){//0元购买不调支付
			orderInfo.setPaymentTime(LocalDateTime.now());
			orderInfoService.notifyOrder(orderInfo);
		}
		return R.ok();
//		if(MallConstants.ORDER_TYPE_1.equals(orderInfo.getOrderType())) {//砍价订单
//			BargainUser bargainUser = bargainUserService.getById(orderInfo.getRelationId());
//			if(MallConstants.YES.equals(bargainUser.getIsBuy())){
//				return R.failed(MyReturnCode.ERR_80006.getCode(), MyReturnCode.ERR_80006.getMsg());
//			}
//		}
//		if(MallConstants.ORDER_TYPE_2.equals(orderInfo.getOrderType())){//拼团订单
//			GrouponInfo grouponInfo = grouponInfoService.getOne(Wrappers.<GrouponInfo>lambdaQuery()
//					.eq(GrouponInfo::getId,orderInfo.getMarketId())
//					.eq(GrouponInfo::getEnable,MallConstants.YES)
//					.lt(GrouponInfo::getValidBeginTime,LocalDateTime.now())
//					.gt(GrouponInfo::getValidEndTime,LocalDateTime.now()));
//			if(grouponInfo == null){//判断拼团的有效性
//				return R.failed(MyReturnCode.ERR_80010.getCode(), MyReturnCode.ERR_80010.getMsg());
//			}
//			if(StrUtil.isNotBlank(orderInfo.getRelationId())){
//				//校验当前用户是否已经参与
//				GrouponUser grouponUser1 = grouponUserService.getOne(Wrappers.<GrouponUser>lambdaQuery()
//						.eq(GrouponUser::getGroupId,orderInfo.getRelationId())
//						.eq(GrouponUser::getUserId,wxUser.getMallUserId()));
//				if(grouponUser1 != null){
//					return R.failed(MyReturnCode.ERR_80012.getCode(), MyReturnCode.ERR_80012.getMsg());
//				}
//				//校验拼团人数
//				GrouponUser grouponUser = grouponUserService.getById(orderInfo.getRelationId());
//				Integer havCountGrouponing = grouponUserService.selectCountGrouponing(orderInfo.getRelationId());
//				if(havCountGrouponing >= grouponUser.getGrouponNum()){
//					return R.failed(MyReturnCode.ERR_80011.getCode(), MyReturnCode.ERR_80011.getMsg());
//				}
//			}
//		}
//		String appId = BaseApi.getAppId(request);
//		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
//		wxPayUnifiedOrderRequest.setAppid(appId);
//		wxPayUnifiedOrderRequest.setBody(orderInfo.getName());
//		wxPayUnifiedOrderRequest.setOutTradeNo(orderInfo.getOrderNo());
//		wxPayUnifiedOrderRequest.setTotalFee(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
//		wxPayUnifiedOrderRequest.setTradeType("JSAPI");
//		wxPayUnifiedOrderRequest.setNotifyUrl(mallConfigProperties.getNotifyHost()+"/mall/api/ma/orderinfo/notify-order");
//		wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
//		wxPayUnifiedOrderRequest.setOpenid(wxUser.getOpenId());
//		return feignWxPayService.unifiedOrder(wxPayUnifiedOrderRequest, SecurityConstants.FROM_IN);
	}

	/**
	 * 支付回调
	 * @param xmlData
	 * @return
	 * @throws WxPayException
	 */
	@ApiOperation(value = "支付回调")
	@PostMapping("/notify-order")
	public String notifyOrder(@RequestBody String xmlData) {
		log.info("支付回调:"+xmlData);
//		R r = feignWxPayService.notifyOrder(xmlData, SecurityConstants.FROM_IN);
//		if(r.isOk()){
//			TenantContextHolder.setTenantId(r.getMsg());
//			WxPayOrderNotifyResult notifyResult = BeanUtil.mapToBean((Map<Object, Object>) r.getData(),WxPayOrderNotifyResult.class,true);
//			OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery()
//					.eq(OrderInfo::getOrderNo,notifyResult.getOutTradeNo()));
//			if(orderInfo != null){
//				if(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue() == notifyResult.getTotalFee()){
//					String timeEnd = notifyResult.getTimeEnd();
//					LocalDateTime paymentTime = LocalDateTimeUtils.parse(timeEnd);
//					orderInfo.setPaymentTime(paymentTime);
//					orderInfo.setTransactionId(notifyResult.getTransactionId());
//					orderInfoService.notifyOrder(orderInfo);
//					return WxPayNotifyResponse.success("成功");
//				}else{
//					return WxPayNotifyResponse.fail("付款金额与订单金额不等");
//				}
//			}else{
				return WxPayNotifyResponse.fail("无此订单");
//			}
//		}else{
//			return WxPayNotifyResponse.fail(r.getMsg());
//		}
	}

	/**
	 * 订单支付
	 *
	 * @return
	 */
	@ApiOperation(value = "订单支付")
	@PostMapping("/orderPayment/{id}")
	public R orderPayment(HttpServletRequest request, @PathVariable String id) {
		//检验用户session登录
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if (!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}

		log.info("订单支付，订单id[{}]", id);
		OrderInfo orderInfo = orderInfoService.getById(id);

		if (orderInfo == null) {
			return R.failed(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}

		orderInfoService.orderPayment(orderInfo);
		return R.ok();
	}

	/**
	 * 物流信息回调
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "物流信息回调")
	@PostMapping("/notify-logisticsr")
	public String notifyLogisticsr(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("param");
		String logisticsId = request.getParameter("logisticsId");
		String tenantId = request.getParameter("tenantId");
		TenantContextHolder.setTenantId(tenantId);
		log.info("物流信息回调:"+param);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result",false);
		map.put("returnCode","500");
		map.put("message","保存失败");
		try {
			JSONObject jsonObject = JSONUtil.parseObj(param);
			orderInfoService.notifyLogisticsr(logisticsId, jsonObject);
			map.put("result",true);
			map.put("returnCode","200");
			map.put("message","保存成功");
			//这里必须返回，否则认为失败，过30分钟又会重复推送。
			response.getWriter().print(JSONUtil.parseObj(map));
		} catch (Exception e) {
			map.put("message","保存失败" + e.getMessage());
			//保存失败，服务端等30分钟会重复推送。
			try {
				response.getWriter().print(JSONUtil.parseObj(map));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 通过物流id查询订单物流
	 * @param logisticsId
	 * @return R
	 */
	@ApiOperation(value = "通过物流id查询订单物流")
	@GetMapping("/orderlogistics/{logisticsId}")
	public R getOrderLogistics(HttpServletRequest request, @PathVariable("logisticsId") String logisticsId){
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		return R.ok(orderLogisticsService.getById(logisticsId));
	}

	/**
	 * 统计各个状态订单计数
	 * @param orderInfo
	 * @return R
	 */
	@ApiOperation(value = "统计各个状态订单计数")
	@GetMapping("/countAll")
	public R count(HttpServletRequest request,OrderInfo orderInfo){
		R checkThirdSession = MallBaseApi.checkThirdSession(orderInfo, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		Map<String, Integer> countAll = new HashMap<>();
		countAll.put(OrderInfoEnum.STATUS_0.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.isNull(OrderInfo::getStatus)
				.eq(OrderInfo::getIsPay,MallConstants.NO)));

		countAll.put(OrderInfoEnum.STATUS_1.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_1.getValue())
				.eq(OrderInfo::getIsPay,MallConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_2.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_2.getValue())
				.eq(OrderInfo::getIsPay,MallConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_3.getValue(),orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus,OrderInfoEnum.STATUS_3.getValue())
				.eq(OrderInfo::getIsPay,MallConstants.YES)
				.eq(OrderInfo::getAppraisesStatus,MallConstants.APPRAISES_STATUS_0)));
		return R.ok(countAll);
	}
}
