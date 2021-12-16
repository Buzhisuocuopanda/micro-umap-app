package com.mkst.umap.app.admin.api.bean.param.lhBook;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: ltt
 * @Date: 2020/09/17/15:23
 * @Description:
 */
@Data
public class LhWsReplyParam {

    /**id*/
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "内容", example = "如题")
    private String content;

}
