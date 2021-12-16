package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.statistics.AnalysisCountResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName SysUserAnalysisMapper
 * @Description 用户统计 数据层
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 8:46
 */
@Repository
public interface SysUserAnalysisMapper {

    public int getTodayRegister(String today);

    public List<AnalysisCountResult> analysisList(@Param("startDay") String startDay , @Param("endDay") String endDay );


}
