package com.mkst.umap.app.admin.statistics;

import java.util.Map;

/**
 * @ClassName AppStatisticsResult
 * @Description 统计基础接口
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 4:22
 */
public interface AnalysisDataBase {

    public AppAnalysisResult getData(Map<String,Object> params);

}
