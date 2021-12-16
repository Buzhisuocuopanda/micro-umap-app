package com.mkst.umap.app.admin.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MailBaseInfoDto
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-24 16:20
 */
@Data
public class MailBaseInfoDto {
    /** id */
    private Long id;
    /** 标题 */
    private String title;

    /** 角色 */
    private String type;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
