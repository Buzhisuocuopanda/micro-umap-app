package com.mkst.umap.app.admin.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ReportInfoDto
 * @Description 举报列表
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-27 15:19
 */
@Data
public class ReportInfoDto {

    /** id */
    private Long id;
    /** 举报类型 */
    private String type;
    /** 详细内容 */
    private String content;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
