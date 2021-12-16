package com.mkst.umap.app.admin.api;

import cn.hutool.core.bean.BeanUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.admin.api.bean.param.SpendParam;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.service.IUserSpendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

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
        BigDecimal userLastBalance = spendService.getUserLastBalance(Math.toIntExact(getUserId(request)));
        BigDecimal result = BeanUtil.isEmpty(userLastBalance) ? new BigDecimal(0) : userLastBalance;
        return ResultGenerator.genSuccessResult("success",result);
    }

    @Login
    @ApiOperation(value = "查询我的消费记录")
    @PostMapping(value = "/myList")
    @Log(title = "查询我的消费记录", businessType = BusinessType.OTHER)
    public Result myList(HttpServletRequest request,@ApiParam @RequestBody UserSpend userSpend){
        userSpend.setUserId(Math.toIntExact(getUserId(request)));
        startPage();
        return ResultGenerator.genSuccessResult("success",spendService.selectUserSpendList(userSpend));
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
