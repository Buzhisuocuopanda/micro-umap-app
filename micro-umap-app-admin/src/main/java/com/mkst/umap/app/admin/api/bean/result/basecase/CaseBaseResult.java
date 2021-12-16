package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CaseBaseResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/23 0023 下午 2:09
 */
@Data
public class CaseBaseResult {

    private Integer id;
    /** 案件类型代码 */
    private Integer caseTypeCode;
    /** 案件类型名称 */
    private String caseTypeName;
    /** 案件标题 */
    private String title;
    /** 判刑时长（单位：月） */
    private Integer length;
    /** 罚金（单位：元） */
    private Integer penalty;
    /** 缓刑时间（单位：月） */
    private Integer reprieve;
    /** 详细地址 */
    private String details;
    /** 内容 */
    private String lable;

    private List<CasePlotResult> plotList;

    private List<CasePlotResult> plotParamList;

}
