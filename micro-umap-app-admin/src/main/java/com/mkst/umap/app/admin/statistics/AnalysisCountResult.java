package com.mkst.umap.app.admin.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName StatisticsCountResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/4 0004 下午 5:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisCountResult {

    private String date;
    private Integer registerNum;
    private Integer onlineNum;
    private Integer value;

}
