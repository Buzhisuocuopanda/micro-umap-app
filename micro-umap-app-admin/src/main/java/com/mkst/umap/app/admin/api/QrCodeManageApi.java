package com.mkst.umap.app.admin.api;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.mkst.mini.systemplus.api.web.base.BaseApi;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.util.Sm4Util;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.admin.api.bean.param.qrCodeManage.QrCodeManageParam;
import com.mkst.umap.app.admin.domain.QrCodeManage;
import com.mkst.umap.app.admin.service.IQrCodeManageService;
import com.mkst.umap.base.core.util.QrcodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api("远程二维码管理服务接口")
@RestController
@RequestMapping("/api/qrCodeManage")
public class QrCodeManageApi extends BaseApi {

    private static final String SM4_KEY = "sm4_key";

    @Autowired
    private IQrCodeManageService qrCodeManageService;

    @PostMapping("/addSave")
    @ApiOperation("添加二维码管理")
    public Result addSave(@RequestBody @ApiParam(name = "qrCodeManageParam", value = "二维码管理信息", required = true) QrCodeManageParam qrCodeManageParam) {
        try {
            if(qrCodeManageParam.getEncryptWhether() == null || qrCodeManageParam.getJumpWhether() == null) {
                return ResultGenerator.genFailResult("传入参数不规范，请检查参数");
            }
            QrCodeManage qrCodeManage = new QrCodeManage();
            //装载数据
            BeanUtils.copyProperties(qrCodeManageParam, qrCodeManage);

            //判断是否需要加密  如果是 则进行sm4加密，如果不是 则不操作
            if(qrCodeManage.getEncryptWhether()){
                String key = SysConfigUtil.getKey(SM4_KEY);
                String encryptEcb = Sm4Util.encryptEcb(key,qrCodeManage.getEncryptDataArea());
                qrCodeManage.setEncryptDataArea(encryptEcb);
            }

            //先插入数据  如果插入失败 直接返回   插入成功则生成二维码并修改
            int row = qrCodeManageService.insertQrCodeManage(qrCodeManage);
            if(row <= 0){
                return ResultGenerator.genFailResult("新增失败，请联系管理员或稍后重试！");
            }
            row = updateQrCodeAddress(qrCodeManage);
            return row > 0 ? ResultGenerator.genSuccessResult("新增二维码管理成功") : ResultGenerator.genFailResult("新增二维码地址失败，请联系管理员或稍后重试！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("新增失败，请联系管理员或稍后重试！");
        }
    }

    public Integer updateQrCodeAddress(QrCodeManage qrCodeManage){
        try{
            //生成二维码地址
            String fileId = ((int)(Math.random()*5+2)*100000) +"";
            String QrCodeAddress = Global.getProfile()+fileId+ ".png";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id",qrCodeManage.getQrCodeId());
            jsonObject.addProperty("event",qrCodeManage.getBusinessMatter());
            jsonObject.addProperty("data",qrCodeManage.getEncryptDataArea());
            if("printer".equals(qrCodeManage.getBusinessMatter())) {
                jsonObject.addProperty("path",SysConfigUtil.getKey("printer_url"));
            }
            QrcodeUtil.createQrcode(jsonObject.toString(), QrCodeAddress);
            qrCodeManage.setQrCodeAddress(fileId+ ".png");

            int row = qrCodeManageService.updateQrCodeManage(qrCodeManage);
            return row;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    @GetMapping("/selectById")
    @ApiOperation("通过id查询数据")
    public Result selectById(@RequestParam(value = "id", required = true) @ApiParam(name = "id", value = "二维码id", required = true) Long id) {

        try{
            QrCodeManage qrCodeManage = qrCodeManageService.selectQrCodeManageById(id);
            if(qrCodeManage == null) {
                return ResultGenerator.genFailResult("查询失败，结果不存在，请联系管理员或稍后重试！");
            }
            JSONObject jsonObject = new JSONObject();

            if(qrCodeManage.getJumpWhether()){
                jsonObject.put("type",0);
                jsonObject.put("jumpUrl", qrCodeManage.getJumpUrl());
                return ResultGenerator.genSuccessResult("获取成功",jsonObject);
            }else{
                jsonObject.put("type",1);
                //判断是否需要加密  如果是 则进行sm4加密，如果不是 则不操作
                String encryptEcb = qrCodeManage.getEncryptDataArea();
                //防止在调此接口时  通过其他方式将原本 不加密状态 改为加密状态  所以在此处再次判断
                if(qrCodeManage.getEncryptWhether()){
                    String key = SysConfigUtil.getKey(SM4_KEY);
                    encryptEcb = Sm4Util.encryptEcb(key,qrCodeManage.getEncryptDataArea());
                }
                jsonObject.put("encryptDataArea",encryptEcb);
                return ResultGenerator.genSuccessResult("获取成功", jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genFailResult("查询失败，请联系管理员或稍后重试！");
        }
    }

}
