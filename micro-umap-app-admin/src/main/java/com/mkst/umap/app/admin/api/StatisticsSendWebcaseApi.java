package com.mkst.umap.app.admin.api;


import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.common.util.HttpKits;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.StatisticsParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api("统计接口")
@Slf4j
@RestController
@RequestMapping("/api/statistic")
public class StatisticsSendWebcaseApi extends BaseApi {


    private String webcaseUrl = SysConfigUtil.getKey("webcase_url");

    private String webcaseSecret = SysConfigUtil.getKey("webcase_secret");
    private String webcaseAppId = SysConfigUtil.getKey("webcase_appId");


    @PostMapping("/getRzrf")
    @ApiOperation("认罪认罚")
    @Login
    public Result getRzrf(@RequestBody StatisticsParam statisticsParam, HttpServletRequest request){
        Long userId = getUserId(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", statisticsParam.getStartTime());
        param.put("endTime", statisticsParam.getEndTime());
        String requestUrl = webcaseUrl + "/api/statistic/getRzrf";
        return HttpKits.requestApiPost(requestUrl, webcaseAppId, webcaseSecret, param, null);
    }

    @PostMapping("/getTclxjy")
    @ApiOperation("提出量刑建议")
    @Login
    public Result getTclxjy(@RequestBody StatisticsParam statisticsParam,HttpServletRequest request)  {
        Long userId = getUserId(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", statisticsParam.getStartTime());
        param.put("endTime", statisticsParam.getEndTime());
        String requestUrl = webcaseUrl + "/api/statistic/getTclxjy";
        return HttpKits.requestApiPost(requestUrl, webcaseAppId, webcaseSecret, param, null);
    }



    @PostMapping("/getCnlxjy")
    @ApiOperation("采纳量刑建议")
    @Login
    public Result getCnlxjy(@RequestBody StatisticsParam statisticsParam,HttpServletRequest request)  {
        Long userId = getUserId(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", statisticsParam.getStartTime());
        param.put("endTime", statisticsParam.getEndTime());
        String requestUrl = webcaseUrl + "/api/statistic/getCnlxjy";
        return HttpKits.requestApiPost(requestUrl, webcaseAppId, webcaseSecret, param, null);
    }

    @PostMapping("/getAsum")
    @ApiOperation("获取案")
    @Login
    public Result getAsum(@RequestBody StatisticsParam statisticsParam,HttpServletRequest request)  {

        Long userId = getUserId(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", statisticsParam.getStartTime());
        param.put("endTime", statisticsParam.getEndTime());
        String requestUrl = webcaseUrl + "/api/statistic/getAsum";
        return HttpKits.requestApiPost(requestUrl, webcaseAppId, webcaseSecret, param, null);
    }


    @PostMapping("/getJsum")
    @ApiOperation("获取件")
    @Login
    public Result getJsum(@RequestBody StatisticsParam statisticsParam,HttpServletRequest request)  {
        Long userId = getUserId(request);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", statisticsParam.getStartTime());
        param.put("endTime", statisticsParam.getEndTime());
        String requestUrl = webcaseUrl + "/api/statistic/getJsum";
        return HttpKits.requestApiPost(requestUrl, webcaseAppId, webcaseSecret, param, null);
    }



    }
