package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName SpendParam
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-11-03 20:31
 */
@Data
@ApiModel(value = "消费相关参数",description = "消费相关参数")
public class SpendParam {
    @ApiModelProperty(value = "查询方式")
    private String way;
    @ApiModelProperty(value = "查询关键字")
    private String key;
    @ApiModelProperty(value = "userId")
    private Long userId;
    @ApiModelProperty(value = "数据开始时间")
    private Date checkStartTime;
}
