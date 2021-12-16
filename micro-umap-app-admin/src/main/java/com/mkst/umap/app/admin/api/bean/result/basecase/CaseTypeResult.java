package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.Data;

/**
 * @ClassName CaseTypeResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/22 0022 下午 5:08
 */
@Data
public class CaseTypeResult {

    private Integer id;
    /** 代码 */
    private String code;
    /** 名称 */
    private String name;
    /** 图标 */
    private String icon;
    /** 排序 */
    private Integer order;

}
