package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CasePlotSubResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/24 0024 下午 2:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasePlotTypeResult {

    private String plotType;
    private String plotTypeName;
    private List<CasePlotResult> casePlotList;

}
