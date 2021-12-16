package com.mkst.umap.app.admin.api;

import com.mkst.mini.systemplus.api.common.annotation.Login;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.umap.app.admin.api.bean.param.AuditCommonParam;
import com.mkst.umap.app.admin.api.bean.result.auditcommon.CommonAuditInfoResult;
import com.mkst.umap.app.admin.service.IAuditCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName AuditCommonApi
 * @Description 审核的通用接口
 * @Author wangyong
 * @Date 2020-07-14 11:32
 */
@Slf4j
@Api("审核的通用接口")
@RestController
@RequestMapping("/api/auditCommon")
public class AuditCommonApi extends BaseApi {

    @Autowired
    private IAuditCommonService auditCommonService;

    @PostMapping("auditList")
    @ApiOperation("审核列表")
    @Login
    @Deprecated
    public Result auditList(HttpServletRequest request, @RequestBody @ApiParam(name = "auditCommonParam", value = "审核公共参数") AuditCommonParam auditCommonParam) {
        List<CommonAuditInfoResult> auditInfoList = auditCommonService.getAuditInfoList(auditCommonParam);
        return ResultGenerator.genSuccessResult("查询成功", auditInfoList);
    }

    @PostMapping("audit")
    @ApiOperation("审核")
    @Login
    @Deprecated
    public Result audit(HttpServletRequest request, @RequestBody @ApiParam(name = "auditCommonParam", value = "审核公共参数") AuditCommonParam auditCommonParam) {

        auditCommonParam.setOperator(getLoginName(request));
        int row = auditCommonService.audit(auditCommonParam);
        return row > 0 ? ResultGenerator.genSuccessResult("审核成功") : ResultGenerator.genSuccessResult("审核失败，请检查参数！");
    }
}
