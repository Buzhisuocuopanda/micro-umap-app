package com.mkst.umap.app.admin.api;

import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.util.HttpKits;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/stayCase")
public class StayCaseApi extends BaseApi {

    @PostMapping("stayCaseInfo")
    @ApiOperation("待取件")
    @Login
    public Result stayCaseInfo(HttpServletRequest request,@RequestParam("status") String status){
        Long userId = getUserId(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("status",status);
        return HttpKits.requestApiPost(SysConfigUtil.getKey("webcase_url")+"/api/doubleark/stayCaseInfo",SysConfigUtil.getKey("webcase_appId"),SysConfigUtil.getKey("webcase_secret"),jsonObject,null);
    }

    @PostMapping("getAppCaseInfo")
    @ApiOperation("开柜")
    public Result getAppCaseInfo(@RequestParam("id") Long id)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return HttpKits.requestApiPost(SysConfigUtil.getKey("webcase_url")+"/api/doubleark/openArk",SysConfigUtil.getKey("webcase_appId"),SysConfigUtil.getKey("webcase_secret"),jsonObject,null);
    }


}
