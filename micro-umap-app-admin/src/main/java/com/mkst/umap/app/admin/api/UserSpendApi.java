package com.mkst.umap.app.admin.api;

import javax.servlet.http.HttpServletRequest;

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

/**
 * @ClassName UserSpendApi
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 19:21
 */
@Api(value = "请假接口")
@RestController
@RequestMapping(value = "/api/spend")
public class UserSpendApi extends BaseApi {

    @Autowired
    private IUserSpendService spendService;

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
