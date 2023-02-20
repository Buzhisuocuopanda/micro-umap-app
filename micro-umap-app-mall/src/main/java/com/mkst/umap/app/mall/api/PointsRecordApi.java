/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mkst.umap.app.mall.common.entity.PointsRecord;
import com.mkst.umap.app.mall.common.vo.R;
import com.mkst.umap.app.mall.service.PointsRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 积分变动记录
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-12-05 19:47:22
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/ma/pointsrecord")
@Api(value = "pointsrecord", tags = "积分变动记录API")
public class PointsRecordApi {

    private final PointsRecordService pointsRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pointsRecord 积分变动记录
     * @return
     */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R getPointsRecordPage(HttpServletRequest request, Page page, PointsRecord pointsRecord) {
		R checkThirdSession = MallBaseApi.checkThirdSession(pointsRecord, request);
		if(!checkThirdSession.isOk()) {//检验失败，直接返回失败信息
			return checkThirdSession;
		}
        return R.ok(pointsRecordService.page(page, Wrappers.query(pointsRecord)));
    }

}
