package com.mkst.umap.app.admin.dto.intellectualproperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName IntellectualProperty
 * @Description 知识产权web
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-17 18:27
 */
@Data
public class IntelProInfoWebDto {

    /** 咨询id */
    private Long id;

    /** 咨询标题 */
    private String title;

    /** 咨询内容 */
    private String content;

    /** 是否已回复 */
    private String hasReplied;

    /** 创建人姓名 */
    private String createByName;

    /** 提出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
