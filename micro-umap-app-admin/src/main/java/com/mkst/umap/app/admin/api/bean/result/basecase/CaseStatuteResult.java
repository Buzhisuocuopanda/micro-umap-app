package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.Data;

/**
 * @ClassName CaseStatuteResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/22 0022 下午 5:31
 */
@Data
public class CaseStatuteResult {

    private Integer id;
    /** 案件类型 */
    private String caseTypeCode;
    /** 标题 */
    private String name;
    /** 内容地址 */
    private String content;
    /** 副标题 */
    private String disc;
    /** 内容 */
    private String lable;

}
