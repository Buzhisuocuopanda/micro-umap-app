package com.mkst.umap.app.admin.task;

import cn.hutool.core.date.DateUtil;
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.umap.app.admin.service.ICanteenManageService;
import com.mkst.umap.app.common.enums.ApproveStatusEnum;
import com.mkst.umap.app.common.enums.DiningStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MeetingTask
 * @Description 会议相关的定时任务
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-11 11:12
 */
@Component("CanteenManageTask")
public class CanteenManageTask {

    @Autowired
    private ICanteenManageService canteenManageService;
    @Autowired
    private IBoxMealService boxMealService;

    public void checkCanteenManageStatus() {
        try{
            //转换提日期输出格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //定时器启动 查询出当天  用餐状态为未开始的申请集合
            CanteenManage canteenManage = new CanteenManage();
            canteenManage.setDateTime(DateUtil.today());
            canteenManage.setDiningStatus(DiningStatusEnum.NotStart.getValue());
            canteenManage.setDelFlag("0");
            List<CanteenManage> list = canteenManageService.selectCanteenManageList(canteenManage);

            /**
             * 遍历集合  获取到包厢餐次开始时间和结束时间
             * 如果 当前时间小于开始时间 则不操作
             * 如果 当前时间大于开始时间 小于结束时间 则用餐状态改为 进行中
             * 如果 当前时间大于结束时间  则改为已结束
             */
            Date now = new Date();
            for(CanteenManage c : list){
                BoxMeal boxMeal = boxMealService.selectBoxMealById(c.getBoxMealId());
                String startTime = DateUtil.today()+" "+boxMeal.getStartTime();
                String endTime = DateUtil.today()+" "+boxMeal.getEndTime();
                Date start = dateFormat.parse(startTime);
                Date end = dateFormat.parse(endTime);
                if(now.before(start)){
                    continue;
                }else if(now.after(start) && now.before(end)){
                    if(DiningStatusEnum.Ongoing.getValue().equals(c.getDiningStatus())) {
                        continue;
                    }
                    c.setDiningStatus(DiningStatusEnum.Ongoing.getValue());
                    canteenManageService.updateCanteenManage(c);
                }else if( now.after(end)){
                    if(DiningStatusEnum.Finish.getValue().equals(c.getDiningStatus())) {
                        continue;
                    }
                    c.setDiningStatus(DiningStatusEnum.Finish.getValue());
                    canteenManageService.updateCanteenManage(c);
                }

                //
                if(now.after(c.getCreateTime()) && (ApproveStatusEnum.Pending.getValue().equals(c.getApplyStatus()) || ApproveStatusEnum.LOGISTICS.getValue().equals(c.getApplyStatus()) || ApproveStatusEnum.KITCHEN.getValue().equals(c.getApplyStatus()))){
                    c.setDiningStatus(DiningStatusEnum.Finish.getValue());
                    canteenManageService.updateCanteenManage(c);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
