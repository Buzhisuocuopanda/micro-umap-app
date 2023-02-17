/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.dto.UserCollectAddDTO;
import com.mkst.umap.app.mall.common.entity.UserCollect;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.UserCollectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户收藏
 *
 * @author JL
 * @date 2019-09-22 20:45:45
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/usercollect")
@Api(value = "usercollect", tags = "用户收藏API")
public class UserCollectApi {

    private final UserCollectService userCollectService;

    /**
    * 分页查询
    * @param page 分页对象
    * @param userCollect 用户收藏
    * @return
    */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getUserCollectPage(HttpServletRequest request, Page page, UserCollect userCollect) {
		R checkThirdSession = MallBaseApi.checkThirdSession(userCollect, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(userCollectService.page2(page,Wrappers.query(userCollect)));
    }

    /**
    * 新增用户收藏
    * @param userCollectAddDTO 用户收藏
    * @return R
    */
	@ApiOperation(value = "新增用户收藏")
    @PostMapping
    public R save(HttpServletRequest request, @RequestBody UserCollectAddDTO userCollectAddDTO){
		UserCollect userCollect = new UserCollect();
		R checkThirdSession = MallBaseApi.checkThirdSession(userCollect, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
		List<UserCollect> listUserCollect = new ArrayList<>();
		userCollectAddDTO.getRelationIds().forEach(relationId ->{
			UserCollect userCollect2 = new UserCollect();
			userCollect2.setUserId(userCollect.getUserId());
			userCollect2.setType(userCollectAddDTO.getType());
			userCollect2.setRelationId(relationId);
			List list = userCollectService.list(Wrappers.query(userCollect2));
			if(list == null || list.size() <= 0){
				listUserCollect.add(userCollect2);
			}
		});
		userCollectService.saveBatch(listUserCollect);
        return R.ok(listUserCollect);
    }

    /**
    * 通过id删除用户收藏
    * @param id
    * @return R
    */
	@ApiOperation(value = "通过id删除用户收藏")
    @DeleteMapping("/{id}")
    public R removeById(HttpServletRequest request, @PathVariable String id){
		R checkThirdSession = MallBaseApi.checkThirdSession(null, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(userCollectService.removeById(id));
    }

}
