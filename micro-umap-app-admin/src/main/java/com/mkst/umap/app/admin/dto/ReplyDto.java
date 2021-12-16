package com.mkst.umap.app.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ReplyDto
 * @Description 回复的建议dto
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 19:48
 */
@Data
public class ReplyDto {
    private Long id;

    private String responder;

    private String avatar;

    private boolean modify;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
