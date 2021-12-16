package com.mkst.umap.app.admin.api.bean.param.reply;

import com.mkst.umap.app.admin.domain.Vacation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ReplyParam
 * @Description 回复通用参数
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 19:22
 */
@Data
@ApiModel(value = "回复信息的参数")
public class ReplyParam {

    @ApiModelProperty(value = "id",example = "1")
    private Long id;

    @ApiModelProperty(value = "父id",example = "1")
    private Long parentId;

    @ApiModelProperty(value = "业务类型",example = "umap_petition_rescue")
    private String businessType;

    @ApiModelProperty(value = "关联对象id",example = "1")
    private String objectId;

    @ApiModelProperty(value = "内容",example = "测试回复")
    private String content;
}
