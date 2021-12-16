package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoomDetailParam
 * @Description 查询提审室的详细信息参数
 * @Author wangyong
 * @Date 2020-07-03 15:02
 */
@Data
@ApiModel(value = "查询提审室的详细信息参数")
public class RoomDetailParam {

    @ApiModelProperty(value = "事件ID")
    private Long id;

    @ApiModelProperty(value = "事件类型")
    private String type;
}
