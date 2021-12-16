package com.mkst.umap.app.admin.statistics;

import cn.hutool.core.date.DateUtil;
import com.mkst.umap.app.admin.mapper.ArraignAppointmentMapper;
import com.mkst.umap.app.admin.util.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName ArraignAnalysisData
 * @Description 提审预约统计曲线
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/7 0007 下午 2:14
 */
@Component("arraignAnalysisData")
public class ArraignAnalysisData implements AnalysisDataBase{

    @Autowired
    private ArraignAppointmentMapper arraignAppointmentMapper;

    @Override
    public AppAnalysisResult getData(Map<String, Object> params) {
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        AppAnalysisResult data = new AppAnalysisResult<>();
        data.setList(arraignAppointmentMapper.analysisList(startDate,endDate));
        data.setToday(arraignAppointmentMapper.selectTotalByDay(MyDateUtil.format(DateUtil.date()) + " 00:00:00"
                ,MyDateUtil.format(DateUtil.date()) + " 23:59:59"));
        data.setTotal((long)arraignAppointmentMapper.selectTotal());
        return data;
    }

}
