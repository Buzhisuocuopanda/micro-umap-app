package com.mkst.umap.app.mall.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mkst.umap.app.mall.common.vo.R;

/**
 * 小程序Configuration
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 *
 */
@Configuration
public class MallBaseApi {

	private static RedisTemplate redisTemplate;

	public MallBaseApi(RedisTemplate redisTemplate){
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 校验ThirdSession，并获取当前用户的信息
	 * @param baseEntity
	 * @param request
	 * @return
	 */
	public static R checkThirdSession(Model<?> baseEntity, HttpServletRequest request) {
//		String thirdSession = request.getHeader("thirdSession");
//		//获取缓存中的ThirdSession
//		String key = WxMaConstants.THIRD_SESSION_BEGIN  + ":" + thirdSession;
//		Object thirdSessionObj = redisTemplate.opsForValue().get(key);
//		if(thirdSessionObj == null) {//session过期
//			//返回超时错误码，触发小程序重新登录
//			return R.failed(MyReturnCode.ERR_60001.getCode(), MyReturnCode.ERR_60001.getMsg());
//		}else {
//			String thirdSessionStr = String.valueOf(thirdSessionObj);
//			ThirdSession thirdSessionData = JSONUtil.toBean(thirdSessionStr, ThirdSession.class);
//			TenantContextHolder.setTenantId(thirdSessionData.getTenantId());//指定租户ID
//			String appId_session = thirdSessionData.getAppId();
//			String userId_session = thirdSessionData.getWxUserId();
//			String sessionKey_session = thirdSessionData.getSessionKey();
//			String openId_session = thirdSessionData.getOpenId();
//			String mallUserId_session = thirdSessionData.getMallUserId();
//			if (baseEntity instanceof WxUser) {
//				((WxUser) baseEntity).setAppId(appId_session);
//				((WxUser) baseEntity).setId(userId_session);
//				((WxUser) baseEntity).setSessionKey(sessionKey_session);
//				((WxUser) baseEntity).setOpenId(openId_session);
//				((WxUser) baseEntity).setMallUserId(mallUserId_session);
//			}else if (baseEntity instanceof ShoppingCart){
//				((ShoppingCart) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof PlaceOrderDTO){
//				((PlaceOrderDTO) baseEntity).setUserId(mallUserId_session);
//				((PlaceOrderDTO) baseEntity).setAppId(appId_session);
//			}else if (baseEntity instanceof UserAddress){
//				((UserAddress) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof OrderInfo){
//				((OrderInfo) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof UserCollect){
//				((UserCollect) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof PointsRecord){
//				((PointsRecord) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof UserInfo){
//				((UserInfo) baseEntity).setId(mallUserId_session);
//			}else if (baseEntity instanceof CouponUser){
//				((CouponUser) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof BargainUser){
//				((BargainUser) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof BargainCut){
//				((BargainCut) baseEntity).setUserId(mallUserId_session);
//			}else if (baseEntity instanceof GrouponUser){
//				((GrouponUser) baseEntity).setUserId(mallUserId_session);
//			}
//			return R.ok(baseEntity);
//		}
		return R.ok();
	}

	/**
	 * 通过request获取appId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getAppId(HttpServletRequest request) {
		String appId = request.getHeader("app-id");
		return appId;
	}
}
