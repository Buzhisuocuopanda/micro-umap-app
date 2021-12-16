package com.mkst.umap.app.admin.api.bean.param.petition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName PetitionParam
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 11:23
 */
@Data
@ApiModel(value = "信访相关参数")
public class PetitionParam {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "信访类型",example = "umap_petition_accuse")
    private String type;

    @ApiModelProperty(value = "标题",example = "测试标题")
    private String title;

    @ApiModelProperty(value = "详细内容",example = "测试内容")
    private String content;

    @ApiModelProperty(value = "是否已被回复(0:否 1:是)",example = "1")
    private String hasReplied;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String personnelIds;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String matterIds;

    @ApiModelProperty(value = "附件ids", example = "1,2,3")
    private String fileEntityIds;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询日期",example = "2020-08-24 00:00:00")
    private Date createTime;

    @ApiModelProperty(value = "是否匿名",example = "1")
    private String anonymous;




}
