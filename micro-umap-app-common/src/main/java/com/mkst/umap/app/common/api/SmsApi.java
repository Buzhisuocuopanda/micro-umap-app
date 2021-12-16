package com.mkst.umap.app.common.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.mkst.mini.systemplus.common.base.Result;
import com.mkst.mini.systemplus.common.base.ResultGenerator;
import com.mkst.mini.systemplus.common.constant.ShiroConstants;
import com.mkst.mini.systemplus.common.util.RedisUtils;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.mini.systemplus.util.SysConfigUtil;
import com.mkst.umap.app.common.constant.KeyConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;


@Api(value = "短信接口")
@RestController
@Slf4j
@RequestMapping("/api/sms/")
public class SmsApi {

    /**
     * 默认短信验证码长度
     */
    private static final int DEFAULT_CODE_LENGTH = 6;
    /**
     * 默认每天最多发送次数
     */
    private static final int DEFAULT_SEND_DAY_MAX_NUM = 5;

    @Autowired
    private ISysUserService sysUserService;

    @PostMapping("sendSms")
    @ApiOperation(value = "发送短信")
    public Result stayCaseInfo(@RequestParam("phone") String phone,@RequestParam("type") String type
            ,@RequestParam("uuid") String uuid ,@RequestParam("imgCode") String imgCode) throws IOException {
        SysUser sysUser = sysUserService.selectUserByPhoneNumber(phone);
        if(KeyConstant.SMS_SEND_REGISTERED.equals(type)&&sysUser!=null){
            return ResultGenerator.genFailResult("手机号已注册");
        }
        if(KeyConstant.SMS_SEND_RESET_PASSWORD.equals(type)&&sysUser==null){
            return ResultGenerator.genFailResult("手机号未注册");
        }
        if(!checkSendNum(phone)){
            return ResultGenerator.genFailResult("您的手机号发送已到上限");
        }
        if(!checkImgCode(uuid,imgCode)){
            return ResultGenerator.genFailResult("验证码错误");
        }
        String code = RandomUtil.randomNumbers(getCodeLength());
        log.info("---------------------------------------------获取短信code："+code + ",phone:"+phone);
        try {
            YxtSmsConfig.getYxtSmsService().sendMsg(phone,"验证码："+code+", 请在5分钟内输入。如非本人操作请忽略本短信。");
            RedisUtils.set(phone,code,300);
        } catch (YxtSmsErrorException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("发送短信失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 校验图形验证码
     * @param uuid
     * @param code
     * @return
     */
    private boolean checkImgCode(String uuid , String code){
        String redisCode = RedisUtils.getStr(ShiroConstants.CURRENT_CAPTCHA+":"+uuid);
        if(StrUtil.isBlank(code) || StrUtil.isBlank(redisCode)){
            log.info("图形验证码不能为空");
            return false;
        }
        return redisCode.equalsIgnoreCase(code);
    }

    /**
     * 短信验证码长度
     * @return
     */
    private int getCodeLength(){
        int length = DEFAULT_CODE_LENGTH;
        try{
            String codeLengthStr = SysConfigUtil.getKey("sms_code_num_length");
            length = Integer.parseInt(codeLengthStr);
        }catch (Exception e){}
        return length;
    }

    /**
     * 校验短信发送次数
     * @return
     */
    private boolean checkSendNum(String phone){
        if(StrUtil.isBlank(phone)){
            return false;
        }
        int maxNum = DEFAULT_SEND_DAY_MAX_NUM;
        try{
            String sendSmsStr = SysConfigUtil.getKey("sms_send_day_max_num");
            maxNum = Integer.parseInt(sendSmsStr);
        }catch (Exception e){}
        String dayStr = DateUtil.format(new Date(),"yyyyMMdd");
        String  key = StrUtil.format("sms:send:num:{}:{}" , phone,dayStr);
        Integer num = RedisUtils.getInt(key);
        if(num != null && num>=maxNum){
            return false;
        }
        num = num == null?1:num+1;
        RedisUtils.set(key,num,60*60*24);
        return true;
    }


}
