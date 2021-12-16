package com.mkst.umap.app.admin.dto.report;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ReportDetailDto
 * @Description 举报详情
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-27 15:52
 */
@Data
public class ReportDetailDto {
    /** id */
    private Long id;
    /** 举报类型 */
    private String type;
    /** 举报地址 */
    private String address;
    /** 是否实名（默认匿名） */
    private String realName;
    /** 详细内容 */
    private String content;

    private List<String> fileList;
}
