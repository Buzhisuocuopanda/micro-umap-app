package com.mkst.umap.app.common.api;


import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.util.Sm4Util;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.base.core.util.QrcodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Api(value = "二维码接口")
@RestController
@Slf4j
@RequestMapping("/api/qrcode/")
public class QrcodeApi {

    @PostMapping("getQrcode")
    @ApiOperation(value = "生成二维码")
    public Result stayCaseInfo(@RequestParam("qrcode") String qrcode) throws Exception {
        String QrcodeUrl = UUID.randomUUID().toString() + ".png";
        String cipherText = Sm4Util.encryptEcb(SysConfigUtil.getKey("sm4_key"),qrcode);
        QrcodeUtil.createQrcode(cipherText,Global.getProfile() + QrcodeUrl);
        return ResultGenerator.genSuccessResult("生成成功",QrcodeUrl);
    }

}
