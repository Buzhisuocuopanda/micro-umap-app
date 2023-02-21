/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.dto.PlaceOrderDTO;
import com.mkst.umap.app.mall.common.entity.BargainUser;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.entity.GoodsSku;
import com.mkst.umap.app.mall.common.entity.GoodsSkuSpecValue;
import com.mkst.umap.app.mall.common.entity.GoodsSpu;
import com.mkst.umap.app.mall.common.entity.GrouponInfo;
import com.mkst.umap.app.mall.common.entity.GrouponUser;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.common.entity.OrderItem;
import com.mkst.umap.app.mall.common.entity.OrderLogistics;
import com.mkst.umap.app.mall.common.entity.OrderLogisticsDetail;
import com.mkst.umap.app.mall.common.entity.PointsRecord;
import com.mkst.umap.app.mall.common.entity.ShoppingCart;
import com.mkst.umap.app.mall.common.entity.UserAddress;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.enums.OrderInfoEnum;
import com.mkst.umap.app.mall.common.enums.OrderItemEnum;
import com.mkst.umap.app.mall.common.enums.OrderLogisticsEnum;
import com.mkst.umap.app.mall.common.util.LocalDateTimeUtils;
import com.mkst.umap.app.mall.config.TenantContextHolder;
import com.mkst.umap.app.mall.mapper.BargainUserMapper;
import com.mkst.umap.app.mall.mapper.GoodsSkuSpecValueMapper;
import com.mkst.umap.app.mall.mapper.GrouponInfoMapper;
import com.mkst.umap.app.mall.mapper.GrouponUserMapper;
import com.mkst.umap.app.mall.mapper.OrderInfoMapper;
import com.mkst.umap.app.mall.service.CouponUserService;
import com.mkst.umap.app.mall.service.GoodsSkuService;
import com.mkst.umap.app.mall.service.GoodsSpuService;
import com.mkst.umap.app.mall.service.OrderInfoService;
import com.mkst.umap.app.mall.service.OrderItemService;
import com.mkst.umap.app.mall.service.OrderLogisticsDetailService;
import com.mkst.umap.app.mall.service.OrderLogisticsService;
import com.mkst.umap.app.mall.service.PointsRecordService;
import com.mkst.umap.app.mall.service.ShoppingCartService;
import com.mkst.umap.app.mall.service.UserAddressService;
import com.mkst.umap.app.mall.service.UserInfoService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
@Service
@AllArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

	private final GoodsSkuService goodsSkuService;
	private final GoodsSpuService goodsSpuService;
	private final OrderItemService orderItemService;
	private final GoodsSkuSpecValueMapper goodsSkuSpecValueMapper;
	private final ShoppingCartService shoppingCartService;
	private final OrderLogisticsService orderLogisticsService;
	private final UserAddressService userAddressService;
	private final RedisTemplate<String, String> redisTemplate;
//	private final MallConfigProperties mallConfigProperties;
	private final OrderLogisticsDetailService orderLogisticsDetailService;
	private final PointsRecordService pointsRecordService;
	private final UserInfoService userInfoService;
//	private final FeignWxPayService feignWxPayService;
	private final CouponUserService couponUserService;
	private final BargainUserMapper bargainUserMapper;
	private final GrouponInfoMapper grouponInfoMapper;
	private final GrouponUserMapper grouponUserMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(OrderInfo entity) {
		if(StrUtil.isNotBlank(entity.getLogistics()) && StrUtil.isNotBlank(entity.getLogisticsNo())){//发货。更新快递单号
			entity.setDeliveryTime(LocalDateTime.now());
			OrderLogistics orderLogistics = orderLogisticsService.getOne(Wrappers.<OrderLogistics>lambdaQuery()
					.eq(OrderLogistics::getId,entity.getLogisticsId()));
			//第一次发货调起收到倒计时
			boolean sendRedis = false;
			if(StrUtil.isBlank(orderLogistics.getLogistics()) && StrUtil.isBlank(orderLogistics.getLogisticsNo())){
				sendRedis = true;
			}
			orderLogistics.setLogistics(entity.getLogistics());
			orderLogistics.setLogisticsNo(entity.getLogisticsNo());
			orderLogistics.setStatus(OrderLogisticsEnum.STATUS_1.getValue());
			orderLogisticsService.updateById(orderLogistics);
//			//订阅快递100
//			String key = mallConfigProperties.getLogisticsKey();					//企业授权key
//			String company = entity.getLogistics();			//快递公司编码
//			String number = entity.getLogisticsNo();	//快递单号
//			String phone = orderLogistics.getTelNum();					//手机号
//			String callbackurl = StrUtil.format("{}{}{}", mallConfigProperties.getNotifyHost(),
//					"/mall/api/ma/orderinfo/notify-logisticsr?tenantId="+orderLogistics.getTenantId()+"&logisticsId=",orderLogistics.getId());//回调地址
//			String from = "";					//出发地城市
//			String to = "";						//目的地城市
//			String salt = "";					//加密串
//			int resultv2 = 1;					//行政区域解析
//			int autoCom = 0;					//单号智能识别
//			int interCom = 0;					//开启国际版
//			String departureCountry = "";		//出发国
//			String departureCom = "";			//出发国快递公司编码
//			String destinationCountry = "";		//目的国
//			String destinationCom = "";			//目的国快递公司编码
//
//			Kuaidi100Utils kuaidi100Utils = new Kuaidi100Utils(key);
//			String result = kuaidi100Utils.subscribeData(company, number, from, to, callbackurl, salt, resultv2, autoCom, interCom, departureCountry, departureCom, destinationCountry, destinationCom, phone);
//			JSONObject jSONObject = JSONUtil.parseObj(result);
//			if(!(Boolean)jSONObject.get("result")){
//				log.error("快递订阅失败：returnCode：{}；message：{}",jSONObject.get("returnCode"),jSONObject.get("message"));
//				throw new RuntimeException(String.valueOf(jSONObject.get("message")));
//			}
			if(sendRedis){
				//加入redis，7天后自动确认收货
				String keyRedis = String.valueOf(StrUtil.format("{}{}:{}",MallConstants.REDIS_ORDER_KEY_STATUS_2, TenantContextHolder.getTenantId(),entity.getId()));
				redisTemplate.opsForValue().set(keyRedis, entity.getOrderNo() , MallConstants.ORDER_TIME_OUT_2, TimeUnit.DAYS);//设置过期时间
			}
		}
		return super.updateById(entity);
	}

	@Override
	public IPage<OrderInfo> page1(IPage<OrderInfo> page, Wrapper<OrderInfo> queryWrapper) {
		return baseMapper.selectPage1(page,queryWrapper.getEntity());
	}

	@Override
	public IPage<OrderInfo> page2(IPage<OrderInfo> page, OrderInfo orderInfo) {
		return baseMapper.selectPage2(page,orderInfo);
	}

	@Override
	public OrderInfo getById2(Serializable id) {
		OrderInfo orderInfo = baseMapper.selectById2(id);
		if(orderInfo != null){
			String keyRedis = null;
			//获取自动取消倒计时
			if(MallConstants.NO.equals(orderInfo.getIsPay())){
				keyRedis = String.valueOf(StrUtil.format("{}{}:{}",MallConstants.REDIS_ORDER_KEY_IS_PAY_0, TenantContextHolder.getTenantId(),orderInfo.getId()));
			}
			//获取自动收货倒计时
			if(OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())){
				keyRedis = String.valueOf(StrUtil.format("{}{}:{}",MallConstants.REDIS_ORDER_KEY_STATUS_2, TenantContextHolder.getTenantId(),orderInfo.getId()));
			}
			if(keyRedis != null){
				Long outTime = redisTemplate.getExpire(keyRedis);
				if(outTime != null && outTime > 0){
					orderInfo.setOutTime(outTime);
				}
			}
		}
		return orderInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderCancel(OrderInfo orderInfo) {
		if(MallConstants.NO.equals(orderInfo.getIsPay()) && !OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus())){//校验
			//实时查询订单是否已支付
			WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
			request.setAppid(orderInfo.getAppId());
			request.setTransactionId(orderInfo.getTransactionId());
			request.setOutTradeNo(orderInfo.getOrderNo());
//			R r = feignWxPayService.queryOrder(request, SecurityConstants.FROM_IN);
//			if(r.isOk()){
//				WxPayOrderQueryResult wxPayOrderQueryResult = BeanUtil.mapToBean((Map<Object, Object>) r.getData(), WxPayOrderQueryResult.class,true);
//				if(!WxPayConstants.WxpayTradeStatus.NOTPAY.equals(wxPayOrderQueryResult.getTradeState())){//只有未支付的订单能取消
//					throw new RuntimeException("只有未支付的订单能取消");
//				}
//			}
			orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
			//回滚库存
			List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
					.eq(OrderItem::getOrderId,orderInfo.getId()));
			//！注意，这种库存操作会有并发问题，导致库存不准确。如对库存要求很精确，需单独新建库存表
			listOrderItem.forEach(orderItem -> {
				GoodsSku goodsSku = goodsSkuService.getById(orderItem.getSkuId());
				if(goodsSku != null){
					goodsSku.setStock(goodsSku.getStock() + orderItem.getQuantity());
					goodsSkuService.updateById(goodsSku);
				}
				//回滚积分
				PointsRecord pointsRecord = new PointsRecord();
				pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_4);
				pointsRecord.setOrderItemId(orderItem.getId());
				pointsRecord = pointsRecordService.getOne(Wrappers.query(pointsRecord));
				//查询该订单详情是否有抵扣积分
				if(pointsRecord !=null && StrUtil.isNotBlank(pointsRecord.getId())){
					//减回赠送的积分
					pointsRecord.setId(null);
					pointsRecord.setTenantId(null);
					pointsRecord.setCreateTime(null);
					pointsRecord.setUpdateTime(null);
					pointsRecord.setDescription("【订单取消】 " + pointsRecord.getDescription());
					pointsRecord.setAmount(- pointsRecord.getAmount());
					pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_5);
					//新增积分记录
					pointsRecordService.save(pointsRecord);
					//减去赠送积分
					UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
					userInfo.setPointsCurrent(userInfo.getPointsCurrent() + pointsRecord.getAmount());
					userInfoService.updateById(userInfo);
				}
			});
			baseMapper.updateById(orderInfo);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderReceive(OrderInfo orderInfo) {
		orderInfo.setStatus(OrderInfoEnum.STATUS_3.getValue());
		orderInfo.setAppraisesStatus(MallConstants.APPRAISES_STATUS_0);
		orderInfo.setReceiverTime(LocalDateTime.now());
		baseMapper.updateById(orderInfo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		orderItemService.remove(Wrappers.<OrderItem>lambdaQuery()
				.eq(OrderItem::getOrderId,id));//删除订单详情
		return super.removeById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderInfo orderSub(PlaceOrderDTO placeOrderDTO) {
		OrderInfo orderInfo = new OrderInfo();
		BeanUtil.copyProperties(placeOrderDTO,orderInfo);
		orderInfo.setIsPay(MallConstants.NO);
		orderInfo.setOrderNo(IdUtil.getSnowflake(0,0).nextIdStr());
		orderInfo.setSalesPrice(BigDecimal.ZERO);
		orderInfo.setPaymentPrice(BigDecimal.ZERO);
		orderInfo.setFreightPrice(BigDecimal.ZERO);
		orderInfo.setPaymentPoints(0);
		orderInfo.setPaymentCouponPrice(BigDecimal.ZERO);
		orderInfo.setPaymentPointsPrice(BigDecimal.ZERO);
		List<OrderItem> listOrderItem = new ArrayList<>();
		List<GoodsSku> listGoodsSku = new ArrayList<>();
		List<CouponUser> listCouponUser = new ArrayList<>();
		List<PointsRecord> listPointsRecord = new ArrayList<>();
		placeOrderDTO.getSkus().forEach(placeOrderSkuDTO -> {//过滤
			GoodsSku goodsSku = goodsSkuService.getOne(Wrappers.<GoodsSku>lambdaQuery()
					.eq(GoodsSku::getSpuId,placeOrderSkuDTO.getSpuId())
					.eq(GoodsSku::getId,placeOrderSkuDTO.getSkuId())
					.ge(GoodsSku::getStock,placeOrderSkuDTO.getQuantity())
					.eq(GoodsSku::getEnable,MallConstants.YES));
			if(goodsSku != null){
				GoodsSpu goodsSpu = goodsSpuService.getOne(Wrappers.<GoodsSpu>lambdaQuery()
						.eq(GoodsSpu::getId,goodsSku.getSpuId())
						.eq(GoodsSpu::getShelf, MallConstants.YES));
				if(goodsSpu != null){
					OrderItem orderItem = new OrderItem();
					orderItem.setOrderId(orderInfo.getId());
					orderItem.setStatus(OrderItemEnum.STATUS_0.getValue());
					orderItem.setIsRefund(MallConstants.NO);
					orderItem.setSpuId(goodsSpu.getId());
					orderItem.setSkuId(goodsSku.getId());
					orderItem.setSpuName(goodsSpu.getName());
					orderItem.setPicUrl(StrUtil.isNotBlank(goodsSku.getPicUrl()) ? goodsSku.getPicUrl() : goodsSpu.getPicUrls()[0]);
					orderItem.setQuantity(placeOrderSkuDTO.getQuantity());
					orderItem.setSalesPrice(goodsSku.getSalesPrice());
					if(MallConstants.DELIVERY_WAY_1.equals(orderInfo.getDeliveryWay())){//快递配送要算运费
						orderItem.setFreightPrice(placeOrderSkuDTO.getFreightPrice());
					}else{//自提配送不算运费
						orderItem.setFreightPrice(BigDecimal.ZERO);
					}
					orderItem.setPaymentPrice(placeOrderSkuDTO.getPaymentPrice().add(orderItem.getFreightPrice()));
					orderItem.setPaymentPoints(placeOrderSkuDTO.getPaymentPoints());
					orderItem.setPaymentCouponPrice(placeOrderSkuDTO.getPaymentCouponPrice());
					orderItem.setPaymentPointsPrice(placeOrderSkuDTO.getPaymentPointsPrice());
					orderItem.setCouponUserId(placeOrderSkuDTO.getCouponUserId());
					BigDecimal quantity = new BigDecimal(placeOrderSkuDTO.getQuantity());
					if(StrUtil.isNotBlank(orderItem.getCouponUserId())){
						//校验电子券
						CouponUser couponUser = couponUserService.getById(orderItem.getCouponUserId());
						if(couponUser == null){
							throw new RuntimeException("非法优惠券");
						}
						if(!MallConstants.COUPON_USER_STATUS_0.equals(couponUser.getStatus())){
							throw new RuntimeException("优惠券已经使用");
						}
						if(couponUser.getValidBeginTime().after(new Date())){
							throw new RuntimeException("优惠券未在使用期");
						}
						if(couponUser.getValidEndTime().before(new Date())){
							throw new RuntimeException("优惠券已过期");
						}
						couponUser.setStatus(MallConstants.COUPON_USER_STATUS_1);
						couponUser.setUsedTime(LocalDateTime.now());
						listCouponUser.add(couponUser);
					}
					List<GoodsSkuSpecValue> listGoodsSkuSpecValue = goodsSkuSpecValueMapper.listGoodsSkuSpecValueBySkuId(goodsSku.getId());
					listGoodsSkuSpecValue.forEach(goodsSkuSpecValue -> {
						String specInfo = orderItem.getSpecInfo();
						specInfo = StrUtil.isNotBlank(specInfo) ? specInfo : "";
						orderItem.setSpecInfo(specInfo
								+ goodsSkuSpecValue.getSpecValueName()
								+  "，" );
					});
					String specInfo = orderItem.getSpecInfo();
					if(StrUtil.isNotBlank(specInfo)){
						orderItem.setSpecInfo(specInfo.substring(0,specInfo.length() - 1));
					}
					listOrderItem.add(orderItem);
					orderInfo.setSalesPrice(orderInfo.getSalesPrice().add(goodsSku.getSalesPrice().multiply(quantity)));
					orderInfo.setFreightPrice(orderInfo.getFreightPrice().add(orderItem.getFreightPrice()));
					orderInfo.setPaymentPrice(orderInfo.getPaymentPrice().add(orderItem.getPaymentPrice()));
					orderInfo.setPaymentPoints(orderInfo.getPaymentPoints() + (orderItem.getPaymentPoints() != null ? orderItem.getPaymentPoints() : 0));
					orderInfo.setPaymentCouponPrice(orderInfo.getPaymentCouponPrice().add((orderItem.getPaymentCouponPrice() != null ? orderItem.getPaymentCouponPrice() : BigDecimal.ZERO)));
					orderInfo.setPaymentPointsPrice(orderInfo.getPaymentPointsPrice().add((orderItem.getPaymentPointsPrice() != null ? orderItem.getPaymentPointsPrice() : BigDecimal.ZERO)));
					goodsSku.setStock(goodsSku.getStock() - orderItem.getQuantity());
					listGoodsSku.add(goodsSku);
					//删除购物车
					shoppingCartService.remove(Wrappers.<ShoppingCart>lambdaQuery()
							.eq(ShoppingCart::getSpuId,goodsSpu.getId())
							.eq(ShoppingCart::getSkuId,goodsSku.getId())
							.eq(ShoppingCart::getUserId,placeOrderDTO.getUserId()));
				}
			}
		});
		if(listOrderItem.size() > 0 && listGoodsSku.size()>0){
			if(MallConstants.DELIVERY_WAY_1.equals(orderInfo.getDeliveryWay())){//配送方式1、普通快递的订单要新增订单物流
				UserAddress userAddress = userAddressService.getById(placeOrderDTO.getUserAddressId());
				OrderLogistics orderLogistics = new OrderLogistics();
				orderLogistics.setPostalCode(userAddress.getPostalCode());
				orderLogistics.setUserName(userAddress.getUserName());
				orderLogistics.setTelNum(userAddress.getTelNum());
				orderLogistics.setAddress(userAddress.getProvinceName()+userAddress.getCityName()+userAddress.getCountyName()+userAddress.getDetailInfo());
				//新增订单物流
				orderLogisticsService.save(orderLogistics);
				orderInfo.setLogisticsId(orderLogistics.getId());
			}
			orderInfo.setName(listOrderItem.get(0).getSpuName());
			super.save(orderInfo);//保存订单
			listOrderItem.forEach(orderItem -> orderItem.setOrderId(orderInfo.getId()));
			//保存订单详情
			orderItemService.saveBatch(listOrderItem);
			//修改用户电子券状态
			if(listCouponUser != null && listCouponUser.size() > 0){
				couponUserService.updateBatchById(listCouponUser);
			}
			listOrderItem.forEach(orderItem -> {
				if(orderItem.getPaymentPoints() != null && orderItem.getPaymentPoints() > 0){
					//处理积分抵扣
					PointsRecord pointsRecord = new PointsRecord();
					pointsRecord.setUserId(orderInfo.getUserId());
					pointsRecord.setDescription("【抵扣】购买商品【" + orderItem.getSpuName() + "】 * " +orderItem.getQuantity());
					pointsRecord.setSpuId(orderItem.getSpuId());
					pointsRecord.setOrderItemId(orderItem.getId());
					pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_4);
					pointsRecord.setAmount(- orderItem.getPaymentPoints());
					listPointsRecord.add(pointsRecord);
				}
			});
			//这种库存操作会有并发问题，导致库存不准确。如对库存要求很精确，需单独新建库存表
			goodsSkuService.updateBatchById(listGoodsSku);//更新库存
			if(orderInfo.getPaymentPoints() > 0){
				//新增积分记录
				pointsRecordService.saveBatch(listPointsRecord);
				//更新用户积分
				UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
				if(userInfo.getPointsCurrent() < orderInfo.getPaymentPoints()){
					throw new RuntimeException("积分不足");
				}
				userInfo.setPointsCurrent(userInfo.getPointsCurrent() - orderInfo.getPaymentPoints());
				userInfoService.updateById(userInfo);
			}
			//加入redis，30分钟自动取消
			String keyRedis = String.valueOf(StrUtil.format("{}{}:{}",MallConstants.REDIS_ORDER_KEY_IS_PAY_0, TenantContextHolder.getTenantId(),orderInfo.getId()));
			redisTemplate.opsForValue().set(keyRedis, orderInfo.getOrderNo() , MallConstants.ORDER_TIME_OUT_0, TimeUnit.MINUTES);//设置过期时间
		}else{
			return null;
		}
		return orderInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void notifyOrder(OrderInfo orderInfo) {
		orderInfo.setIsPay(MallConstants.YES);
		orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
		List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
				.eq(OrderItem::getOrderId,orderInfo.getId()));
		Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
		AtomicReference<Integer> pointsGiveAmount = new AtomicReference<>(0);
		List<PointsRecord> listPointsRecord = new ArrayList<>();
		List<GoodsSpu> listGoodsSpu = (List<GoodsSpu>) goodsSpuService.listByIds(resultList.keySet());
		listGoodsSpu.forEach(goodsSpu -> {
			resultList.get(goodsSpu.getId()).forEach(orderItem -> {
				//更新销量
				goodsSpu.setSaleNum(goodsSpu.getSaleNum()+orderItem.getQuantity());
				//处理积分赠送
				if(MallConstants.YES.equals(goodsSpu.getPointsGiveSwitch())){
					PointsRecord pointsRecord = new PointsRecord();
					pointsRecord.setUserId(orderInfo.getUserId());
					pointsRecord.setDescription("【赠送】购买商品【" + goodsSpu.getName() + "】 * " +orderItem.getQuantity());
					pointsRecord.setSpuId(goodsSpu.getId());
					pointsRecord.setOrderItemId(orderItem.getId());
					pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_2);
					pointsRecord.setAmount(orderItem.getQuantity() * goodsSpu.getPointsGiveNum());
					listPointsRecord.add(pointsRecord);
					pointsGiveAmount.updateAndGet(v -> v + orderItem.getQuantity() * goodsSpu.getPointsGiveNum());
				}
			});
			goodsSpuService.updateById(goodsSpu);
		});
		//新增积分记录
		pointsRecordService.saveBatch(listPointsRecord);
		//更新用户积分
		UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
		userInfo.setPointsCurrent(userInfo.getPointsCurrent() + pointsGiveAmount.get());
		userInfoService.updateById(userInfo);
		//处理砍价订单，更新砍价记录
		if(MallConstants.ORDER_TYPE_1.equals(orderInfo.getOrderType())){
			BargainUser bargainUser = bargainUserMapper.selectById(orderInfo.getRelationId());
			bargainUser.setIsBuy(MallConstants.YES);
			bargainUser.setOrderId(orderInfo.getId());
			bargainUserMapper.updateById(bargainUser);
		}
		//处理拼团订单
		if(MallConstants.ORDER_TYPE_2.equals(orderInfo.getOrderType())){
			GrouponInfo grouponInfo = grouponInfoMapper.selectById(orderInfo.getMarketId());
			//新增拼团记录
			GrouponUser grouponUser = new GrouponUser();
			grouponUser.setGrouponId(grouponInfo.getId());
			grouponUser.setGroupId(orderInfo.getMarketId());
			grouponUser.setUserId(orderInfo.getUserId());
			grouponUser.setNickName(userInfo.getNickName());
			grouponUser.setHeadimgUrl(userInfo.getHeadimgUrl());
			grouponUser.setSpuId(grouponInfo.getSpuId());
			grouponUser.setSkuId(grouponInfo.getSkuId());
			grouponUser.setGrouponNum(grouponInfo.getGrouponNum());
			grouponUser.setGrouponPrice(grouponInfo.getGrouponPrice());
			grouponUser.setStatus(MallConstants.GROUPON_USER_STATUS_0);
			grouponUserMapper.insert(grouponUser);
			if(StrUtil.isBlank(orderInfo.getRelationId())){//新团，当前用户为团长
				grouponUser.setValidBeginTime(orderInfo.getPaymentTime());
				grouponUser.setValidEndTime(orderInfo.getPaymentTime().plusHours(grouponInfo.getDuration()));
				grouponUser.setIsLeader(MallConstants.YES);
				grouponUser.setGroupId(grouponUser.getId());
				orderInfo.setRelationId(grouponUser.getId());
				orderInfo.setStatus(OrderInfoEnum.STATUS_10.getValue());
			}else{
				grouponUser.setGroupId(orderInfo.getRelationId());
				grouponUser.setIsLeader(MallConstants.NO);
				//查出团长拼团记录
				GrouponUser grouponUser2 = grouponUserMapper.selectById(orderInfo.getRelationId());
				grouponUser.setValidBeginTime(grouponUser2.getValidBeginTime());
				grouponUser.setValidEndTime(grouponUser2.getValidEndTime());
				Integer count = grouponUserMapper.selectCountGrouponing(orderInfo.getRelationId()) + 1;
				if(count == grouponUser2.getGrouponNum()){//已满成团
					GrouponUser grouponUser3 = new GrouponUser();
					grouponUser.setStatus(MallConstants.GROUPON_USER_STATUS_1);
					grouponUser3.setStatus(MallConstants.GROUPON_USER_STATUS_1);
					grouponUserMapper.update(grouponUser3, Wrappers.<GrouponUser>lambdaQuery()
							.eq(GrouponUser::getGroupId, grouponUser2.getId()));
					//更新团里所有订单状态
					OrderInfo orderInfo1 = new OrderInfo();
					orderInfo1.setStatus(OrderInfoEnum.STATUS_1.getValue());
					baseMapper.update(orderInfo1, Wrappers.<OrderInfo>lambdaQuery()
							.eq(OrderInfo::getRelationId, grouponUser2.getId())
							.eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_10.getValue()));
				}else if(count > grouponUser2.getGrouponNum()){//满团开新团
					grouponUser.setValidBeginTime(orderInfo.getPaymentTime());
					grouponUser.setValidEndTime(orderInfo.getPaymentTime().plusHours(grouponInfo.getDuration()));
					grouponUser.setIsLeader(MallConstants.YES);
					grouponUser.setGroupId(grouponUser.getId());
					orderInfo.setRelationId(grouponUser.getId());
					orderInfo.setStatus(OrderInfoEnum.STATUS_10.getValue());
				}else{//未满入团
					orderInfo.setStatus(OrderInfoEnum.STATUS_10.getValue());
				}
			}
			grouponUser.setOrderId(orderInfo.getId());
			grouponUserMapper.updateById(grouponUser);
		}

		Map<String, List<GoodsSpu>> resultGoodsSpu = listGoodsSpu.stream().collect(Collectors.groupingBy(GoodsSpu::getDeliveryPlaceId));
		if(resultGoodsSpu.size() <= 1){//只有一个发货地址
			if(MallConstants.DELIVERY_WAY_2.equals(orderInfo.getDeliveryWay())){//配送方式2、上门自提，设置自提地址
				orderInfo.setLogisticsId(listGoodsSpu.get(0).getDeliveryPlaceId());
			}
			baseMapper.updateById(orderInfo);//更新订单
		}else{//处理订单是否需要拆分（通过不同发货地进行订单拆分）,有多个发货地，要拆分订单
			resultGoodsSpu.forEach((key, value) -> {
				List<OrderItem> listOrderItem1 = new ArrayList<>();
				value.forEach(item ->{
					listOrderItem1.addAll(resultList.get(item.getId()));
				});
				OrderInfo orderInfo1 = new OrderInfo();
				BeanUtil.copyProperties(orderInfo,orderInfo1);
				orderInfo1.setOrderNo(IdUtil.getSnowflake(0,0).nextIdStr());
				orderInfo1.setTenantId(null);
				orderInfo1.setId(null);
				orderInfo1.setSalesPrice(BigDecimal.ZERO);
				orderInfo1.setPaymentPrice(BigDecimal.ZERO);
				orderInfo1.setFreightPrice(BigDecimal.ZERO);
				orderInfo1.setPaymentPoints(0);
				orderInfo1.setPaymentCouponPrice(BigDecimal.ZERO);
				orderInfo1.setPaymentPointsPrice(BigDecimal.ZERO);
				listOrderItem1.forEach(item -> {
					GoodsSku goodsSku = goodsSkuService.getById(item.getSkuId());
					orderInfo1.setSalesPrice(orderInfo1.getSalesPrice().add(goodsSku.getSalesPrice().multiply(new BigDecimal(item.getQuantity()))));
					orderInfo1.setFreightPrice(orderInfo1.getFreightPrice().add(item.getFreightPrice()));
					orderInfo1.setPaymentPrice(orderInfo1.getPaymentPrice().add(item.getPaymentPrice().add(item.getFreightPrice())));
					orderInfo1.setPaymentPoints(orderInfo1.getPaymentPoints() + (item.getPaymentPoints() != null ? item.getPaymentPoints() : 0));
					orderInfo1.setPaymentCouponPrice(orderInfo1.getPaymentCouponPrice().add((item.getPaymentCouponPrice() != null ? item.getPaymentCouponPrice() : BigDecimal.ZERO)));
					orderInfo1.setPaymentPointsPrice(orderInfo1.getPaymentPointsPrice().add((item.getPaymentPointsPrice() != null ? item.getPaymentPointsPrice() : BigDecimal.ZERO)));
				});
				if(MallConstants.DELIVERY_WAY_2.equals(orderInfo1.getDeliveryWay())){//配送方式2、上门自提，设置自提地址
					orderInfo1.setLogisticsId(key);
				}
				//新建订单
				baseMapper.insert(orderInfo1);
				//更新订单详情
				listOrderItem1.forEach(item -> item.setOrderId(orderInfo1.getId()));
				orderItemService.updateBatchById(listOrderItem1);
			});
			//删除旧订单
			baseMapper.deleteById(orderInfo.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void notifyLogisticsr(String logisticsId, JSONObject jsonObject) {
		OrderLogistics orderLogistics = orderLogisticsService.getById(logisticsId);
		if(orderLogistics != null){
			String status = jsonObject.getStr("status");
			if("abort".equals(status)){//中止
				orderLogistics.setStatus(OrderLogisticsEnum.STATUS_ER.getValue());
				orderLogistics.setMessage(jsonObject.getStr("message"));
			}else{
				orderLogisticsDetailService.remove(Wrappers.<OrderLogisticsDetail>lambdaQuery()
						.eq(OrderLogisticsDetail::getLogisticsId,logisticsId));//先删除
				JSONObject jsonResult =(JSONObject) jsonObject.get("lastResult");
				orderLogistics.setStatus(jsonResult.getStr("state"));
				orderLogistics.setIsCheck(jsonResult.getStr("ischeck"));
				JSONArray jSONArray = jsonResult.getJSONArray("data");
				List<OrderLogisticsDetail> listOrderLogisticsDetail = new ArrayList<>();
				jSONArray.forEach(object -> {
					JSONObject jsonData = JSONUtil.parseObj(object);
					OrderLogisticsDetail orderLogisticsDetail = new OrderLogisticsDetail();
					orderLogisticsDetail.setLogisticsId(logisticsId);
					orderLogisticsDetail.setLogisticsTime(LocalDateTimeUtils.parse(jsonData.getStr("time")));
					orderLogisticsDetail.setLogisticsInformation(jsonData.getStr("context"));
					listOrderLogisticsDetail.add(orderLogisticsDetail);
				});
				orderLogisticsDetailService.saveBatch(listOrderLogisticsDetail);
				//获取最近一条物流信息
				Optional<OrderLogisticsDetail> orderLogisticsDetail = listOrderLogisticsDetail.stream().collect(Collectors.maxBy(Comparator.comparing(OrderLogisticsDetail::getLogisticsTime)));
				orderLogistics.setMessage(orderLogisticsDetail.get().getLogisticsInformation());
			}
			orderLogisticsService.updateById(orderLogistics);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void editPrice(OrderItem orderItem) {
		BigDecimal editPrice = orderItem.getPaymentPrice();
		orderItem = orderItemService.getById(orderItem.getId());
		BigDecimal oldPrice = orderItem.getPaymentPrice();
		orderItem.setPaymentPrice(editPrice);
		OrderInfo orderInfo = baseMapper.selectById(orderItem.getOrderId());
		orderInfo.setPaymentPrice(orderInfo.getPaymentPrice().add(editPrice.subtract(oldPrice)));
		orderItemService.updateById(orderItem);
		orderInfo.setOrderNo(IdUtil.getSnowflake(0,0).nextIdStr());
		baseMapper.updateById(orderInfo);
	}

}
