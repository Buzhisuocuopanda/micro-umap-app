package com.mkst.umap.app.admin.api;

import cn.hutool.core.date.DateUtil;
import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.framework.util.SpringUtils;
import com.mkst.umap.app.admin.statistics.AnalysisDataBase;
import com.mkst.umap.app.admin.statistics.AppAnalysisResult;
import com.mkst.umap.app.admin.util.MyDateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName StatisticsUserApi
 * @Description 用户统计接口
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 4:34
 */
@Api("用户统计接口")
@Slf4j
@RestController
@RequestMapping("/api/analysis")
public class AnalysisApi {

    @ApiOperation(value = "查询统计数据")
    @PostMapping(value = "/count")
    @Log(title = "查询统计数据", businessType = BusinessType.OTHER)
    public Result analysis(@RequestParam(value = "startDate") @ApiParam(name = "startDate", value = "开始日期", required = true) String startDate,
                             @RequestParam(value = "endDate") @ApiParam(name = "endDate", value = "结束日期", required = true) String endDate,
                             @RequestParam(value = "model") @ApiParam(name = "model", value = "模块") String model){
        Date sdate = MyDateUtil.parse(startDate);
        if(sdate == null){
            return ResultGenerator.genFailResult("没有开始日期");
        }
        Date edate = MyDateUtil.parse(endDate);
        if(edate == null){
            return ResultGenerator.genFailResult("没有结束日期");
        }
        if(edate.getTime() < sdate.getTime()){
            return ResultGenerator.genFailResult("开始日期不能小于结束日期");
        }
        AnalysisDataBase sd = SpringUtils.getBean(model + "AnalysisData");
        if(sd == null){
            sd = SpringUtils.getBean( "userAnalysisData");
        }
        Map<String,Object> params = new HashMap<>();
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        AppAnalysisResult data = sd.getData(params);
        log.info("data：{}",data);
        return ResultGenerator.genSuccessResult("success",data);
    }

}
