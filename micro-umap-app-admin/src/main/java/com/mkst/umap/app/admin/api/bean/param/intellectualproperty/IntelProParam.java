package com.mkst.umap.app.admin.api.bean.param.intellectualproperty;

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
public class IntelProParam {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "标题", example = "作品的概念与条件？")
    private String title;

    @ApiModelProperty(value = "内容", example = "如题")
    private String content;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String fileEntityIds;

    @ApiModelProperty(value = "已回复(0：未回复 1：已回复)", example = "0")
    private String hasReplied;

    @ApiModelProperty(value = "查询日期",example = "2020-07-05 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    @ApiModelProperty(value = "回复内容",example = "示例回复")
    private String replyContent;
}
