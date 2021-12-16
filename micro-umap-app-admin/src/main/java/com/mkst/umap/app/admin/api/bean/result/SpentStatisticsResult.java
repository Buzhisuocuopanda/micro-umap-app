package com.mkst.umap.app.admin.api.bean.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName SpentStatisticsResult
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 20:50
 */
@Data
public class SpentStatisticsResult {
    private String key;
    private BigDecimal value;
}
