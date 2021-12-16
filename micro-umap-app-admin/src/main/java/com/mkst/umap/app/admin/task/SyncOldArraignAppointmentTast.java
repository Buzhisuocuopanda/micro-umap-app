package com.mkst.umap.app.admin.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mkst.umap.app.admin.domain.ArraignSyncData;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName SyncOldArraignAppointmentTast
 * @Description 同步老系统提审记录
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/3 0003 下午 3:25
 */
@Component("syncOldArraignAppointmentTast")
@Slf4j
public class SyncOldArraignAppointmentTast {

    /**默认拉取单页数据数*/
    private static final int DEFAULT_ROWS = 1000;

    @Autowired
    private IArraignAppointmentService service;

    private String tokenUsername = "cs1";
    private String tokenPwd = "e10adc3949ba59abbe56e057f20f883e";

    public void sync(){
        //
        log.info("同步老系统提审记录开始");
        String date = null;
        if(service.haveSyncArraign()){
            date = DateUtil.format(DateUtil.offsetDay(new Date() , -1) , "yyyy-MM-dd HH:mm:ss");
        }
        Set<ArraignSyncData> all= getAll(date);
        service.syncArraign(all);
        log.info("同步老系统提审记录结束");
    }

    public static void main(String[] args) {
        SyncOldArraignAppointmentTast sync = new SyncOldArraignAppointmentTast();
        /*String r = sync.getArraignStrResult(1,"2020-10-10 00:00:00");
        System.out.println(r);
        List<ArraignSyncData> list = sync.getArraignList(r);*/
        /*Set<ArraignSyncData> list= sync.getAll(null);
        System.out.println(list.size());
        for(ArraignSyncData a:list){
            System.out.println(a.getId());
        }*/
        System.out.println(sync.getAll("2021-01-08 00:00:00"));
    }

    public Set<ArraignSyncData> getAll(String date){
        int page = 1;
        Set<ArraignSyncData> all = new HashSet<>();
        String r = getArraignStrResult(page,date);
        if(getSuccess(r)){
            all.addAll(getArraignList(r));
            while (haveNext(r,page++)){
                r = getArraignStrResult(page,date);
                all.addAll(getArraignList(r));
            }
        }
        return all;
    }

    /**
     * 获取列表
     * @return
     */
    public List<ArraignSyncData> getArraignList(String arraignStrResult ){
        if(!getSuccess(arraignStrResult)){
            return null;
        }
        JSONObject obj = JSONObject.parseObject(arraignStrResult);
        JSONObject objdata = obj.getJSONObject("data");
        JSONArray arr = objdata.getJSONArray("data");
        List<ArraignSyncData> list = arr.toJavaList(ArraignSyncData.class);
        return list;
    }

    /**
     * 数据是否还有下一页
     * @param arraignStrResult
     * @param page
     * @return
     */
    public boolean haveNext(String arraignStrResult , int page){
        //是否具有下一页
        if(getSuccess(arraignStrResult)){
            int total = getTotal(arraignStrResult);
            log.debug("total:" + total);
            return total - page * DEFAULT_ROWS > 0;
        }
        return false;
    }

    //获取总记录数
    public int getTotal(String arraignStrResult ){
        JSONObject obj = JSONObject.parseObject(arraignStrResult);
        JSONObject objdata = obj.getJSONObject("data");
        Integer total = objdata.getInteger("total");
        return total;
    }

    //正常获取
    public boolean getSuccess(String arraignStrResult){
        JSONObject obj = JSONObject.parseObject(arraignStrResult);
        return obj.getBoolean("success");
    }

    /**
     * 获取老系统提审记录
     * @param page 页码
     * @param date 日期
     * @return
     */
    public String getArraignStrResult(int page,String date){
        String url = this.getUrl();
        String token = this.getToken();
        Map params = new HashMap() ;
        params.put("token",token);
        params.put("page",page);
        Date searchDay = null;
        try{
            searchDay = DateUtil.parse(date,"yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){
            //忽略日期处理错误
        }
        if(searchDay == null){
            params.put("date",0);
        }else{
            params.put("date", date);
        }
        params.put("rows",DEFAULT_ROWS);
        String r = HttpUtil.post(url,params);
        return r;
    }

    public String getUrl(){
        String url = "https://jcy.axaet.com:6443/lj_web/lj_trial_supportListByPage";
        return url;
    }

    public String getToken(){
        String url = "https://jcy.axaet.com:6443/lj_web/lj_login_checkUser";
        Map<String,Object> params = new HashMap<>();
        params.put("username",tokenUsername);
        params.put("pwd",tokenPwd);
        String r = HttpUtil.get(url,params);
        Token token = JSONObject.parseObject(r , Token.class);
        return token.getToken();
    }

    @Data
    static class Token{
        private String message;
        private String token;
        private Boolean success;
    }

}
