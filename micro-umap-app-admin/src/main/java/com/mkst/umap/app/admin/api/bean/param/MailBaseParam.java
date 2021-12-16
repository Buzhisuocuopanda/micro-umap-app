package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MailBaseParam
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-24 16:04
 */
@Data
@ApiModel(value = "邮件基础信息",description = "邮件的基础信息")
public class MailBaseParam {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "邮件类型",example = "umap_mail_opinion_deputies")
    private String type;

    @ApiModelProperty(value = "邮件类型数组",example = "umap_mail_deputies")
    private String[] typeArr;

    @ApiModelProperty(value = "是否已回复" ,example = "0",hidden = true,notes = "留存字段")
    private String hasReplied;

    @ApiModelProperty(value = "标题", example = "测试标题")
    private String title;

    @ApiModelProperty(value = "详细内容", example = "测试内容")
    private String content;

    @ApiModelProperty(value = "创建人id", example = "1")
    private Long createBy;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String fileEntityIds;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "检查时间", example = "2020-08-27 00:00:00", hidden = true)
    private Date checkDate;
}
