package com.mkst.umap.app.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.appmanager.domain.AppVersion;
import com.mkst.mini.systemplus.appmanager.service.IAppVersionService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/systemplus"})
public class AppUrlController extends BaseController {

    @Autowired
    private IAppVersionService appVersionService;

    @PostMapping("/getAndroidApkUrl")
    @ApiOperation("获取安卓apk安装路径")
    @ResponseBody
    public Result getAndroidApkUrl() {
        AppVersion select = new AppVersion();
        //查询安卓 安装路径
        select.setType("1101");
        List<AppVersion> list = appVersionService.selectAppVersionList(select);
        if(CollUtil.isEmpty(list)){
            return ResultGenerator.genFailResult("没有apk路径");
        }
        list = list.stream().filter(o -> (o.getAttachUrl().contains(".apk"))).sorted(Comparator.comparing(AppVersion::getCreateTime).reversed()).collect(Collectors.toList());
        return ResultGenerator.genSuccessResult("apkUrl",list.get(0).getAttachUrl());
    }

    @PostMapping("/getIosAppStoreUrl")
    @ApiOperation("获取IOS appStore路径")
    @ResponseBody
    public Result getIosApkUrl() {
        AppVersion select = new AppVersion();
        //查询安卓 安装路径
        select.setType("1102");
        List<AppVersion> list = appVersionService.selectAppVersionList(select);
        if(CollUtil.isEmpty(list)){
            return ResultGenerator.genFailResult("没有appStore路径");
        }
        list = list.stream().filter(o -> (o.getAttachUrl().contains(".html"))).sorted(Comparator.comparing(AppVersion::getCreateTime).reversed()).collect(Collectors.toList());
        return ResultGenerator.genSuccessResult("appStoreUrl",list.get(0).getAttachUrl());
    }
}
