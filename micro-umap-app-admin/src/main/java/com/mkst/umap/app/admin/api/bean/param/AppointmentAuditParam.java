package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AppointmentAuditParam
 * @Description
 * @Author wangyong
 * @Date 2020-06-29 11:55
 */
@Data
@ApiModel(value = "审批数据传输")
public class AppointmentAuditParam {
    @ApiModelProperty(value = "审批id（可批量审批）")
    private String ids;
    @ApiModelProperty(value = "审批后状态（1:驳回 2:通过）")
    private String status;
    @ApiModelProperty(value = "原因")
    private String reason;
    @ApiModelProperty(value = "类型")
    private String type;
}
