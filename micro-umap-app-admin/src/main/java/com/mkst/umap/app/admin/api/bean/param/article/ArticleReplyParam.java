package com.mkst.umap.app.admin.api.bean.param.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName IntelProParam
 * @Description 知识产权相关参数
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-12 15:45
 */
@Data
@ApiModel(value = "知识产权相关参数")
public class ArticleReplyParam {

    @ApiModelProperty(value = "id", example = "1",required = true)
    private String id;

    @ApiModelProperty(value = "标题", example = "作品的概念与条件？")
    private String title;

    @ApiModelProperty(value = "内容", example = "如题")
    private String content;

    @ApiModelProperty(value = "是否开启回复 0 否  1 是", example = "1")
    private String commentFlag;

    @ApiModelProperty(value = "回复数", example = "0")
    private Long replyNum;

    @ApiModelProperty(value = "查询日期",example = "2020-07-05 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    @ApiModelProperty(value = "回复内容",example = "示例回复",required = true)
    private String replyContent;
}
