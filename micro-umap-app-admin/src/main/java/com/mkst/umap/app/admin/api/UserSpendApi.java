package com.mkst.umap.app.admin.api;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mkst.umap.app.admin.domain.UserBalance;
import com.mkst.umap.app.mall.common.constant.MallConstants;
import com.mkst.umap.app.mall.common.entity.CouponUser;
import com.mkst.umap.app.mall.common.entity.OrderInfo;
import com.mkst.umap.app.mall.service.CouponUserService;
import com.mkst.umap.app.mall.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gexin.rp.sdk.base.uitls.StringUtils;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.dto.userspend.UserSpendQrery;
import com.mkst.umap.app.admin.service.IUserSpendService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName UserSpendApi
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 19:21
 */
@Api(value = "用户消费接口")
@RestController
@RequestMapping(value = "/api/spend")
@Slf4j
public class UserSpendApi extends BaseApi {

    @Autowired
    private IUserSpendService spendService;
    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private OrderInfoService orderInfoService;


    @Login
    @ApiOperation(value = "查询我的饭卡余额和点券余额")
    @PostMapping(value = "/getUserBalance")
    @Log(title = "查询我的饭卡余额和点券余额", businessType = BusinessType.OTHER)
    public Result getUserBalance(HttpServletRequest request){
        Long userId = getUserId(request);
        UserBalance userBalance = new UserBalance(new BigDecimal(0), new BigDecimal(0), 0);

        // 用户余额
        UserSpend userLastBalance = spendService.getUserLastBalance(userId);
        if (null != userLastBalance) {
            userBalance.setBalance(userLastBalance.getBalance());
        }

        // 卡券余额
        CouponUser couponUser = couponUserService.getOne(Wrappers.<CouponUser>lambdaQuery()
                .eq(CouponUser::getUserId, userId)
                .eq(CouponUser::getStatus, MallConstants.NO)
                .eq(CouponUser::getType, MallConstants.COUPON_TYPE_1)
                .gt(CouponUser::getValidEndTime, LocalDateTime.now()).last("limit 1"));
        if (null != couponUser) {
            userBalance.setCouponBalance(couponUser.getReduceAmount());
        }

        // 奖励券数量
        int count = couponUserService.count(Wrappers.<CouponUser>lambdaQuery()
                .eq(CouponUser::getUserId, userId)
                .eq(CouponUser::getStatus, MallConstants.NO)
                .eq(CouponUser::getType, MallConstants.COUPON_TYPE_2)
                .gt(CouponUser::getValidEndTime, LocalDateTime.now()));
        userBalance.setAwardTicketNum(count);

        return ResultGenerator.genSuccessResult("success", userBalance);
    }

    @Login
    @ApiOperation(value = "订单支付")
    @PostMapping(value = "/orderPayment")
    @Log(title = "订单支付", businessType = BusinessType.OTHER)
    public Result orderPayment(HttpServletRequest request, @RequestBody OrderInfo orderInfo) {
        log.info("订单支付，订单id[{}]", orderInfo.getId());
        OrderInfo order = orderInfoService.getById(orderInfo.getId());
        if (order == null) {
            return ResultGenerator.genFailResult("订单无效");
        }
        spendService.orderPayment(order);

        return ResultGenerator.genSuccessResult("支付成功");
    }

    @Login
    @ApiOperation(value = "查询我的饭卡余额")
    @PostMapping(value = "/checkBalance")
    @Log(title = "查询我的饭卡余额", businessType = BusinessType.OTHER)
    public Result checkBalance(HttpServletRequest request){
    	UserSpend userLastBalance = spendService.getUserLastBalance(getUserId(request));
        return ResultGenerator.genSuccessResult("success", userLastBalance);
    }

    @Login
    @ApiOperation(value = "查询我的消费记录")
    @PostMapping(value = "/myList")
    @Log(title = "查询我的消费记录", businessType = BusinessType.OTHER)
    public Result myList(HttpServletRequest request,@ApiParam @RequestBody UserSpend userSpend){
        userSpend.setUserId(getUserId(request));
        startPage();
        return ResultGenerator.genSuccessResult("success",spendService.selectUserSpendList(userSpend));
    }
    
    @Login
    @ApiOperation(value = "获取月度交易流水总金额")
    @PostMapping(value = "/totalTransactionAmount")
    @Log(title = "获取月度交易流水总金额", businessType = BusinessType.OTHER)
    public Result totalTransactionAmount(HttpServletRequest request, @ApiParam @RequestBody UserSpendQrery userSpendQrery){
    	if(StringUtils.isBlank(userSpendQrery.getTransactionMonth())) {
    		return ResultGenerator.genFailResult("请选择月份");
    	}
    	return ResultGenerator.genSuccessResult("success",spendService.getTotalTransactionAmount(userSpendQrery.getTransactionMonth()));
    }
    
    @Login
    @ApiOperation(value = "获取所有用户余额")
    @PostMapping(value = "/allUserBalance")
    @Log(title = "获取所有用户余额", businessType = BusinessType.OTHER)
    public Result allUserBalance(HttpServletRequest request, @ApiParam @RequestBody UserSpendQrery userSpendQrery){
    	startPage();
    	return ResultGenerator.genSuccessResult("success",spendService.getAllUserBalance(userSpendQrery));
    }

    @Login
    @ApiOperation(value = "查询我的统计数据")
    @PostMapping(value = "/statistics")
    @Log(title = "查询我的统计数据", businessType = BusinessType.OTHER)
    public Result statistics(HttpServletRequest request, @RequestBody SpendParam param){
        param.setUserId(getUserId(request));
        return ResultGenerator.genSuccessResult("success",spendService.selectStatistics(param));
    }
}
