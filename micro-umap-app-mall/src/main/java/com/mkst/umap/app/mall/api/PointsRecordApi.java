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
 * @author JL
 * @date 2019-12-05 19:47:22
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
