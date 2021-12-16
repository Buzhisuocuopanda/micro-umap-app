package com.mkst.umap.app.admin.api.bean.result.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName IntelReplyDto
 * @Description 回复的统一dto
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-14 11:10
 */
@Data
public class ArticleReplyResult {

    private Long id;

    //回复用户名
    private String Responder;

    //内容
    private String content;

    //回复时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //头像地址
    private String avatar;
}
