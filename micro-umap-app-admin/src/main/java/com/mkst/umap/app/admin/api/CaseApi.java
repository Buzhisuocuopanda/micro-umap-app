package com.mkst.umap.app.admin.api;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.mkst.mini.systemplus.api.common.annotation.ApiLog;
import com.mkst.mini.systemplus.api.common.enums.ApiOperatorType;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.util.DictUtil;
import com.mkst.umap.app.admin.api.bean.result.basecase.*;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName CaseApi
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/22 0022 下午 4:08
 */
@Slf4j
@Api("案件量刑建议")
@RestController
@RequestMapping("/api/case")
public class CaseApi extends BaseApi {

    @Autowired
    private ICaseTypeService caseTypeService;

    @Autowired
    private ICaseStatuteService caseStatuteService;

    @Autowired
    private ICasePlotService casePlotService;

    @Autowired
    private ICaseGuideService caseGuideService;

    @Autowired
    private ICaseBaseService caseBaseService;

    @ApiLog(title = "获取所有犯罪类型", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/allType")
    @ApiOperation("获取所有犯罪类型")
    @ResponseBody
    public Result allType() {
        List<CaseType> caseTypes = caseTypeService.selectCaseTypeList(new CaseType());
        return ResultGenerator.genSuccessResult("查询成功",transList(caseTypes, CaseTypeResult.class));
    }

    @ApiLog(title = "获取犯罪类型对应情节", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/typePlotList")
    @ApiOperation("获取犯罪类型对应情节")
    @ResponseBody
    public Result typePlotList(Integer caseTypeCode ){
        CasePlot searchBean = new CasePlot();
        searchBean.setCaseTypeCode(caseTypeCode);
        List<CasePlot> casePlotList = casePlotService.selectCasePlotList(searchBean);
        Map<Integer , List<CasePlotResult>> maps = new HashMap<>();
        if(casePlotList != null && casePlotList.size() > 0 ){
            for(CasePlot plot : casePlotList){
                List<CasePlotResult> list =  maps.get(plot.getPlotType());
                if(list == null){
                    list = new ArrayList<>();
                    maps.put(plot.getPlotType() , list);
                }
                list.add(transObject(plot,CasePlotResult.class));
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        List<CasePlotTypeResult> plotList = new ArrayList<CasePlotTypeResult>();
        for(Map.Entry<Integer , List<CasePlotResult>> entry: maps.entrySet()){
            if(entry.getKey() .equals(1)){
                resultMap.put("paramPlotList",entry.getValue());
            }else{
                String value = DictUtil.getDictLabel("case_plot_type" , entry.getKey()+"");
                value = value == null?"":value;
                plotList.add(new CasePlotTypeResult(entry.getKey()+"" ,value, entry.getValue()));
            }
        }
        resultMap.put("plotList" , plotList);
        return ResultGenerator.genSuccessResult("查询成功",resultMap);
    }

    @ApiLog(title = "获取相关法规列表", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/statuteList")
    @ApiOperation("获取相关法规列表")
    @ResponseBody
    public Result statuteList(String caseTypeCode ) {
        startPage();
        CaseStatute searchBean = new CaseStatute();
        searchBean.setCaseTypeCode(caseTypeCode);
        List<CaseStatute> caseStatuteList = caseStatuteService.selectCaseStatuteList(searchBean);
        Page page = (Page) caseStatuteList;
        Result result =  ResultGenerator.genSuccessResult("查询成功",transList(caseStatuteList, CaseStatuteResult.class));
        result.setCount(page.getTotal());
        return result;
    }

    @ApiLog(title = "获取指导案例列表", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/caseGuideList")
    @ApiOperation("获取指导案例列表")
    @ResponseBody
    public Result caseGuideList(String caseTypeCode ) {
        startPage();
        CaseGuide searchBean = new CaseGuide();
        searchBean.setCaseTypeCode(caseTypeCode);
        List<CaseGuide> caseGuideList = caseGuideService.selectCaseGuideList(searchBean);
        Page page = (Page) caseGuideList;
        Result result =  ResultGenerator.genSuccessResult("查询成功",transList(caseGuideList, CaseGuideResult.class));
        result.setCount(page.getTotal());
        return result;
    }

    @ApiLog(title = "获取相似案例列表", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/similarityCaseList")
    @ApiOperation("获取相似案例列表")
    @ResponseBody
    public Result similarityCase(String plotIds , String caseTypeCode , String plotParams){
        startPage();
        CaseBase caseBase = new CaseBase();
        caseBase.setCaseTypeCode(Integer.parseInt(caseTypeCode));
        //情节参数处理
        Map<String,Object> params = null;
        params = handlerPlotList(plotIds,params);
        params = parsePlotparams(plotParams , params);
        caseBase.setParams(params);
        List<CaseBase> list = caseBaseService.selectCaseBaseList(caseBase);
        Page page = (Page) list;
        List<CaseBaseResult> resultList = transList(list, CaseBaseResult.class);
        //处理重要情节
        if(resultList != null && resultList.size() > 0 ){
            resultList.forEach(o->{
                o.setPlotList(transList(casePlotService.selectCasePlotListByCaseId(o.getId()),CasePlotResult.class));
                o.setPlotParamList(transList(casePlotService.selectParamCasePlotListByCaseId(o.getId()),CasePlotResult.class));
            });
        }
        Result result =  ResultGenerator.genSuccessResult("查询成功",resultList);
        result.setCount(page.getTotal());
        return result;
    }

    @ApiLog(title = "刑期统计", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/statistics/prisonTerm")
    @ApiOperation("刑期统计")
    @ResponseBody
    public Result statisticsPrisonTerm(String plotIds , String caseTypeCode , String plotParams){
        CaseBase caseBase = new CaseBase();
        caseBase.setCaseTypeCode(Integer.parseInt(caseTypeCode));
        //情节参数处理
        Map<String,Object> params = null;
        params = handlerPlotList(plotIds,params);
        params = parsePlotparams(plotParams , params);
        caseBase.setParams(params);
        List<PointResult> list = caseBaseService.statisticsPrisonTerm(caseBase);
        return ResultGenerator.genSuccessResult("查询成功",list);
    }

    @ApiLog(title = "罚金统计", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/statistics/penalties")
    @ApiOperation("罚金统计")
    @ResponseBody
    public Result statisticsPenalties(String plotIds , String caseTypeCode , String plotParams){
        CaseBase caseBase = new CaseBase();
        caseBase.setCaseTypeCode(Integer.parseInt(caseTypeCode));
        Map<String,Object> params = null;
        params = handlerPlotList(plotIds,params);
        params = parsePlotparams(plotParams , params);
        caseBase.setParams(params);
        List<PointResult> list = caseBaseService.statisticsPenalties(caseBase);
        return ResultGenerator.genSuccessResult("查询成功",list);
    }

    @ApiLog(title = "缓刑统计", ApiOperatorType = ApiOperatorType.GET)
    @GetMapping("/statistics/suspension")
    @ApiOperation("缓刑统计")
    @ResponseBody
    public Result statisticsSuspension(String plotIds , String caseTypeCode , String plotParams ){
        CaseBase caseBase = new CaseBase();
        caseBase.setCaseTypeCode(Integer.parseInt(caseTypeCode));
        Map<String,Object> params = null;
        params = handlerPlotList(plotIds,params);
        params = parsePlotparams(plotParams , params);
        caseBase.setParams(params);
        List<PointResult> list = caseBaseService.statisticsSuspension(caseBase);
        return ResultGenerator.genSuccessResult("查询成功",list);
    }

    private Map<String,Object> handlerPlotList(String plotIds ,Map<String,Object> params){
        if(StrUtil.isNotBlank(plotIds)){
            List<String> plotIdList = Arrays.asList(StrUtil.split(plotIds,","));
            if(params == null){
                params = new HashMap<>();
            }
            params.put("plotIds",plotIdList);
            return params;
        }
        return null;
    }

    /**
     * 解析情节参数
     * @param plotParams 如 2468_8205,2469_1233
     */
    private Map<String,Object> parsePlotparams(String plotParams , Map<String,Object> params){
        if(StrUtil.isNotBlank(plotParams)){
            if(params == null){
                params = new HashMap<>();
            }
            String[] plotParamArr = plotParams.split(",");
            Map<String,Object> plotParamsMap = new HashMap<>();
            for(String plotParam:plotParamArr){
                String[] plotParamObject = plotParam.split("_");
                if(plotParamObject.length == 2){
                    plotParamsMap.put(plotParamObject[0],plotParamObject[1]);
                }
            }
            params.put("plotParams",plotParamsMap);
        }
        return params;
    }
}
