package com.mkst.umap.app.admin.api.bean.result.basecase;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName CaseGuideResult
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/6/25 0025 下午 5:08
 */
@Data
public class CaseGuideResult {

    private Integer id;
    /** 案件类型 */
    private String caseTypeCode;
    /** 主标题 */
    private String maintitle;
    /** 副标题 */
    private String subhead;
    /** 页面地址 */
    private String detail;
    /** 创建时间 */
    private Date createTime;
    /** 回复 */
    private Integer replytype;
    /** 内容 */
    private String lable;


}
