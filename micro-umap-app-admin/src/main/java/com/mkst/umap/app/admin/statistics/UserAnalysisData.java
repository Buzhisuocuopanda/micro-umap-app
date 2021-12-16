package com.mkst.umap.app.admin.statistics;

import cn.hutool.core.date.DateUtil;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysUserMapper;
import com.mkst.umap.app.admin.mapper.SysUserAnalysisMapper;
import com.mkst.umap.app.admin.util.MyDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserStatisticsData
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 5:45
 */
@Component("userAnalysisData")
public class UserAnalysisData implements AnalysisDataBase {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserAnalysisMapper sysUserAnalysisMapper;

    @Override
    public AppAnalysisResult getData(Map params) {
        String startDate = (String)params.get("startDate");
        String endDate = (String)params.get("endDate");
        AppAnalysisResult data = new AppAnalysisResult<>();
        data.setList(sysUserAnalysisMapper.analysisList(startDate,endDate));
        data.setToday(sysUserAnalysisMapper.getTodayRegister(DateUtil.format(DateUtil.date(),"yyyy-MM-dd")));
        data.setTotal((long)sysUserMapper.selectUserCounts(new SysUser()));
        return data;
    }
}
