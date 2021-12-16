package com.mkst.umap.app.admin.statistics;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AppStatisticsResult
 * @Description 统计结果统一对象
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 4:22
 */
@Data
public class AppAnalysisResult<T> {

    /**
     * 统计总数
     */
    private Long total;

    /**
     * 今日数
     */
    private Integer today;
    /**
     * 返回数据列表
     */
    private List<T> list;


}
