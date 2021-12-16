package com.mkst.umap.app.admin.api.bean.param.shift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ArraignShiftApproveParam
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2021/5/19 0019 上午 10:23
 */
@Data
@ApiModel(value = "提审预约换班审核对象")
public class ArraignShiftApproveParam {

    @ApiModelProperty(value = "换班请求ID")
    private Integer shiftId ;
    @ApiModelProperty(value = "审核状态，1同意，2驳回")
    private String status ;
}
