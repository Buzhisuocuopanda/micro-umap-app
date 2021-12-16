package com.mkst.umap.app.admin.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName MyDateUtil
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/7 0007 下午 3:04
 */
public class MyDateUtil {

    public static Date parse(String dateStr){
        if(StrUtil.isBlank(dateStr)){
            return null;
        }
        Date result = null;
        try{
            result = DateUtil.parse(dateStr,"yyyy-MM-dd");
        }catch (Exception e){}
        return result;
    }

    public static String format(Date date){
        if(date == null){
            return null;
        }
        return DateUtil.format(date,"yyyy-MM-dd");
    }

    /**
     * @param
     * @return boolean
     * @author zdy
     * @description 超过中午十二点取消第二日预约，提示取消失败
     * @date 10:26 2021-03-03
     */
    public static boolean isCancel(Date startTime) {

        //预约时间的前一天的12点
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date time = c.getTime();

        if (time.after(new Date())) {
            return true; //可取消
        } else {
            return false;  //不可取消
        }
    }

    /**
     * 是否能够取消或者修改
     * @param startTime
     * @param configStr 取消或者修改的配置，如值为1-12表示前一天12点之前，
     *                  还可以设置成0-16:20:00 表示当天16:20:00之前
     * @return
     */
    public static boolean canCancelOrUpdate(Date startTime , String configStr){
        if(StrUtil.isEmpty(configStr)){
            //默认配置是前一天的12点之前
            configStr = "1-12";
        }
        String[] config = configStr.split("-");
        Integer preDay = Integer.parseInt(config[0]);
        String[] limitArr =  config[1].split(":");
        Integer limitHour = Integer.parseInt(limitArr[0]);
        Integer limitMin = 0;
        Integer limitSec = 0;
        if(limitArr.length > 1){
            limitMin = Integer.parseInt(limitArr[1]);
        }
        if(limitArr.length > 2){
            limitSec = Integer.parseInt(limitArr[2]);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        c.add(Calendar.DAY_OF_MONTH, -preDay);
        c.set(Calendar.HOUR_OF_DAY, limitHour);
        c.set(Calendar.MINUTE, limitMin);
        c.set(Calendar.SECOND, limitSec);
        Date time = c.getTime();
        if (time.after(new Date())) {
            return true; //可取消
        } else {
            return false;  //不可取消
        }
    }

}
