package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.Data;

/**
 * @ClassName CasePlotResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/22 0022 下午 5:55
 */
@Data
public class CasePlotResult {

    /**  */
    private Integer id;
    /** 案件类型 */
    private Integer caseTypeCode;
    /** 名称 */
    private String plotName;
    /** 类型 */
    private Integer plotType;
    /** 单位 */
    private String dataformat;
    /** 开始计量 */
    private Integer offset;
    /** 排序 */
    private Integer orders;

    private String valuess;

}
