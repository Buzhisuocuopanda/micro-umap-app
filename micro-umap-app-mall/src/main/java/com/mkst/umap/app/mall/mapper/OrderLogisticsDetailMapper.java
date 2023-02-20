/**
 * Copyright (C) 2023-2024
 * All rights reserved, Designed By www.szmkst.com
 * 注意：
 * 本软件由深圳市迈科思腾科技有限公司开发研制，未经授权许可不得使用
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.mkst.umap.app.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkst.umap.app.mall.common.entity.OrderLogisticsDetail;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流详情
 *
 * @since JDK 1.8
 * @author 李景辉
 * @Email lijinghui@szmkst.com
 * @Date 2023-09-21 12:39:00
 */
public interface OrderLogisticsDetailMapper extends BaseMapper<OrderLogisticsDetail> {

	List<OrderLogisticsDetail> selectList2(@Param("query") OrderLogisticsDetail queryWrapper);
}
