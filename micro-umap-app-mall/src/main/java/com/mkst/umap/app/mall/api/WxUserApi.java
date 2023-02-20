package com.mkst.umap.app.mall.api;
//
//import com.joolun.cloud.common.core.constant.SecurityConstants;
//import com.mkst.umap.app.mall.common.vo.R;
//import com.mkst.umap.app.mall.admin.service.UserInfoService;
//import com.mkst.umap.app.mall.common.feign.FeignWxUserService;
//import com.mkst.umap.app.mall.common.dto.WxOpenDataDTO;
//import com.joolun.cloud.weixin.common.entity.WxUser;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 微信用户
// *
// * @since JDK 1.8
// * @author 李景辉
// * @Email lijinghui@szmkst.com
// * @date 2019-08-25 15:39:39
// */
//@Slf4j
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/ma/wxuser")
//@Api(value = "userinfo", tags = "微信用户API")
//public class WxUserApi {
//
////	private final FeignWxUserService feignWxUserService;
//	private final UserInfoService userInfoService;
//
//	/**
//	 * 获取用户信息
//	 * @param request
//	 * @return
//	 */
//	@ApiOperation(value = "获取用户信息")
//	@GetMapping
//	public R getById(HttpServletRequest request){
//		WxUser wxUser = new WxUser();
//		//检验用户session登录
//		R checkThirdSession = BaseApi.checkThirdSession(wxUser, request);
//		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
//			return checkThirdSession;
//		}
//		return feignWxUserService.getById(wxUser.getId(), SecurityConstants.FROM_IN);
//	}
//
//	/**
//	 * 保存用户信息
//	 * @param wxOpenDataDTO
//	 * @return
//	 */
//	@ApiOperation(value = "保存用户信息")
//	@PostMapping
//	public R save(HttpServletRequest request, @RequestBody WxOpenDataDTO wxOpenDataDTO){
//		WxUser wxUser = new WxUser();
//		//检验用户session登录
//		R checkThirdSession = BaseApi.checkThirdSession(wxUser, request);
//		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
//			return checkThirdSession;
//		}
//		wxOpenDataDTO.setAppId(wxUser.getAppId());
//		wxOpenDataDTO.setUserId(wxUser.getId());
//		wxOpenDataDTO.setSessionKey(wxUser.getSessionKey());
//		return userInfoService.saveByWxUser(wxOpenDataDTO);
//	}
//}
