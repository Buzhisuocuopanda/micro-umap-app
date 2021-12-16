package com.mkst.umap.app.admin.task;

import cn.hutool.core.date.DateUtil;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.umap.app.admin.domain.ApplyInfo;
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.service.IApplyInfoService;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.umap.app.admin.service.ICanteenManageService;
import com.mkst.umap.app.common.enums.ApplyStatusEnum;
import com.mkst.umap.app.common.enums.ApproveStatusEnum;
import com.mkst.umap.app.common.enums.DiningStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BackUpApplyTask
 * @Description 备勤间定时任务
 * @Author ltt
 * @Modified By:
 * @Date 2020-08-11 11:12
 */
@Component("BackUpApplyTask")
public class BackUpApplyTask {

    @Autowired
    private IApplyInfoService applyInfoService;


    public void checkBackUpStatus() {
        try{

            //转换提日期输出格式
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ApplyInfo applyInfo = new ApplyInfo();
            applyInfo.setEndTime(dateFormat.parse(DateUtil.today()));
            List<ApplyInfo> list = applyInfoService.selectApplyInfoListTask(applyInfo);

            list.stream().forEach(o-> {
                if(o.getEndTime().before(new Date())){
                    o.setApplyStatus(ApproveStatusEnum.FINISH.getValue());
                    applyInfoService.updateApplyInfo(o);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
