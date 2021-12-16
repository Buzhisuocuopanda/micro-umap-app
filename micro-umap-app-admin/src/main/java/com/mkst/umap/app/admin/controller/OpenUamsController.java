package com.mkst.umap.app.admin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.mkst.mini.systemplus.common.shiro.session.UserToken;
import com.mkst.mini.systemplus.common.shiro.utils.LoginType;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.uams.Uams;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @ClassName OpenLoginController
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/15 0015 下午 8:47
 */

@Slf4j
@Controller
@RequestMapping({"/open/uams"})
public class OpenUamsController {

    @Autowired
    private ISysUserService userService;

    @GetMapping("testAdmin")
    public String test(String loginName)
    {
        UserToken token = new UserToken(loginName, LoginType.OAUTH2);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return "redirect:" + "/index";
    }

    @GetMapping("loginByToken")
    public String loginByToken(String tokenId , String random ,  String amUrl){
        log.info("login by token params: token[{}],amUrl[{}]",tokenId,amUrl);
        System.out.println(StrUtil.format("login by token params: token[{}],amUrl[{}]",tokenId,amUrl));
        if(StrUtil.isBlank(tokenId)){
            return "redirect:" + "/login";
        }
        Uams uams = new Uams();
        if(StrUtil.isNotBlank(amUrl)){
            uams.setAmServerUrl(amUrl);

        }
        SysUser user = null;
        try {
            user = userService.selectUserByLoginName(uams.getUserPhone(tokenId , random));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user == null){
            return "redirect:" + "/login";
        }
        UserToken userToken = new UserToken(user.getLoginName(), LoginType.OAUTH2);
        Subject subject = SecurityUtils.getSubject();
        subject.login(userToken);
        return "redirect:" + "/index";
    }

    @GetMapping("loginByPwd")
    public String loginByPwd(String username , String pwd , String amUrl){
        log.info("login by token pwd: username[{}],password[{}],amUrl[{}]",username,pwd,amUrl);
        System.out.println(StrUtil.format("login by token pwd: username[{}],password[{}],amUrl[{}]",username,pwd,amUrl));
        if(StrUtil.isBlank(username) || StrUtil.isBlank(pwd)){
            return "redirect:" + "/login";
        }
        Uams uams = new Uams();
        if(StrUtil.isNotBlank(amUrl)){
            uams.setAmServerUrl(amUrl);

        }
        String token = uams.loginByPwd(username,pwd);
        SysUser user = null;
        try {
            user = userService.selectUserByLoginName(uams.getUserPhone(token,""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(user == null){
            return "redirect:" + "/login";
        }
        UserToken userToken = new UserToken(user.getLoginName(), LoginType.OAUTH2);
        Subject subject = SecurityUtils.getSubject();
        subject.login(userToken);
        return "redirect:" + "/index";
    }

    /**
     *
     * @param operateID 操作用户标识，
     * 11:增加、修改、授权用户；
     * 13:删除用户；
     * 51:冻结用户权限（用户已经没有权限访问该系统）。
     * 必填项。
     * @param userIdCode 用户唯一标识，用户在应用系统的唯一标识 必填项。
     * @param synType 0 必填项
     * @param userType 备用参数
     */
    @RequestMapping("synchronizeInfo")
    @ResponseBody
    public Boolean synchronizeInfo(String operateID , String userIdCode , String synType ,
                                String userType){
        //TODO
        System.out.println(StrUtil.format("open sysn operateID:{} ,  userIdCode:{}",operateID,userIdCode));
        if("11".equals(operateID)){
            //增加、修改、授权用户
            Uams uams = new Uams();
            Map<String,Object> map = uams.QueryUserInfoDetail(userIdCode);
            if(map == null){
                return false;
            }
            String userName =  (String)map.get("userName");
            String userPhone =  (String)map.get("userPhone");
            SysUser user = userService.selectUserByLoginName(userPhone);
            if(user == null){
                SysUser sysUser = new SysUser();
                sysUser.setLoginName(userPhone);
                sysUser.setUserName(userName);
                sysUser.setSalt("111111");
                sysUser.setPassword(SecureUtil.md5("111111"+"111111"));
                sysUser.setPostIds(new Long[]{});
                sysUser.setRoleIds(new Long[]{17L});
                userService.insertUser(sysUser);
            }

        }else if("13".equals(operateID)){
            //删除用户
        }else if("51".equals(operateID)){
            //冻结用户
        }
        return true;
    }


    @GetMapping("testCreateUser")
    @ResponseBody
    public String testCreateUser(String loginName){
        SysUser sysUser = new SysUser();
        sysUser.setLoginName(loginName);
        sysUser.setUserName(loginName);
        sysUser.setSalt("111111");
        sysUser.setPassword("");
        sysUser.setPostIds(new Long[]{});
        sysUser.setRoleIds(new Long[]{17L});
        userService.insertUser(sysUser);
        return "OK";
    }

}
