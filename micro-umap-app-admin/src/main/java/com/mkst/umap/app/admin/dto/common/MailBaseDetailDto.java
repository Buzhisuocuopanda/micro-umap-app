package com.mkst.umap.app.admin.dto.common;

import lombok.Data;

/**
 * @ClassName MailBaseDetailDto
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-24 16:29
 */
@Data
public class MailBaseDetailDto {
    /** id */
    private Long id;
    /** 标题 */
    private String title;
    /** 类型 */
    private String type;
    /** 详细内容 */
    private String content;
}
