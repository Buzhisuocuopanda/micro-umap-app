package com.mkst.umap.app.admin.statistics;

import cn.hutool.core.date.DateUtil;
import com.mkst.umap.app.admin.mapper.CanteenManageMapper;
import com.mkst.umap.app.admin.util.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName UserStatisticsData
 * @Description
 * @Author
 * @Modified By:
 * @Date 2020/12/4 0004 下午 5:45
 */
@Component("canteenAnalysisData")
public class CanteenAnalysisData implements AnalysisDataBase {
    @Autowired
    public CanteenManageMapper canteenManageMapper;

    @Override
    public AppAnalysisResult getData(Map<String, Object> params) {

        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");


        AppAnalysisResult data = new AppAnalysisResult<>();
        data.setToday(canteenManageMapper.selectTotalByDay(MyDateUtil.format(DateUtil.date()) + " 00:00:00"
                ,MyDateUtil.format(DateUtil.date()) + " 23:59:59"));
        data.setTotal(canteenManageMapper.selectTotal());
        data.setList(canteenManageMapper.analysisList(startDate,endDate));
        return data;
    }
}
