/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.dto.WxOpenDataDTO;
import com.mkst.umap.app.mall.common.entity.PointsConfig;
import com.mkst.umap.app.mall.common.entity.PointsRecord;
import com.mkst.umap.app.mall.common.entity.UserInfo;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.mapper.UserInfoMapper;
import com.mkst.umap.app.mall.service.PointsConfigService;
import com.mkst.umap.app.mall.service.PointsRecordService;
import com.mkst.umap.app.mall.service.UserInfoService;

import lombok.AllArgsConstructor;

/**
 * 商城用户
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-04 11:09:55
 */
@Service
@AllArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

//	private final FeignWxUserService feignWxUserService;
	private final PointsConfigService pointsConfigService;
	private final PointsRecordService pointsRecordService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveByWxUser(WxOpenDataDTO wxOpenDataDTO) {
		//修改微信用户信息
//		R r = feignWxUserService.save(wxOpenDataDTO, SecurityConstants.FROM_IN);
//		if(!r.isOk()){
//			throw new RuntimeException(r.getMsg());
//		}
//		//修改商城用户信息
//		Map map = (Map<String, Object>) r.getData();
//		if(map.get("mallUserId") != null){
//			UserInfo userInfo = baseMapper.selectById(String.valueOf(map.get("mallUserId")));
//			userInfo.setNickName(String.valueOf(map.get("nickName")));
//			userInfo.setHeadimgUrl(String.valueOf(map.get("headimgUrl")));
//			userInfo.setSex(String.valueOf(map.get("sex")));
//			userInfo.setCity(String.valueOf(map.get("city")));
//			userInfo.setCountry(String.valueOf(map.get("country")));
//			userInfo.setProvince(String.valueOf(map.get("province")));
//			if(MallConstants.USER_GRADE_0.equals(userInfo.getUserGrade())){
//				userInfo.setUserGrade(MallConstants.USER_GRADE_1);//1：普通会员
//				//获取会员初始积分
//				PointsConfig pointsConfig = pointsConfigService.getOne(Wrappers.emptyWrapper());
//				int initVipPosts = pointsConfig != null ? pointsConfig.getInitVipPosts() : 0;
//				userInfo.setPointsCurrent(userInfo.getPointsCurrent() + initVipPosts);
//				if(initVipPosts > 0){
//					//新增积分变动记录
//					PointsRecord pointsRecord = new PointsRecord();
//					pointsRecord.setUserId(userInfo.getId());
//					pointsRecord.setAmount(initVipPosts);
//					pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_1);
//					pointsRecord.setDescription("会员初始积分");
//					pointsRecordService.save(pointsRecord);
//				}
//			}
//			baseMapper.updateById(userInfo);
//		}
//		return r;
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveInside(UserInfo userInfo) {
		//获取用户初始积分
		PointsConfig pointsConfig = pointsConfigService.getOne(Wrappers.emptyWrapper());
		int initPosts = pointsConfig != null ? pointsConfig.getInitPosts() : 0;
		//新增商城用户
		userInfo.setUserGrade(MallConstants.USER_GRADE_0);
		userInfo.setPointsCurrent(initPosts);
		baseMapper.insert(userInfo);
		if(initPosts > 0){
			//新增积分变动记录
			PointsRecord pointsRecord = new PointsRecord();
			pointsRecord.setUserId(userInfo.getId());
			pointsRecord.setAmount(initPosts);
			pointsRecord.setRecordType(MallConstants.POINTS_RECORD_TYPE_0);
			pointsRecord.setDescription("用户初始积分");
			pointsRecordService.save(pointsRecord);
		}
		return Boolean.TRUE;
	}
}
