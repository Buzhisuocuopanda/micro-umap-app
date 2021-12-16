package com.mkst.umap.app.admin.api.bean.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SpecialCaseAuditParam
 * @Description 审批专案的参数
 * @Author wangyong
 * @Date 2020-07-06 19:30
 */
@Data
@ApiModel(value = "审批专案的参数")
public class SpecialCaseAuditParam {

    @ApiModelProperty(value = "要审批的申请的-数组")
    private Long[] ids;

    @ApiModelProperty(value = "审核结果")
    private String status;

    @ApiModelProperty(value = "理由、原因")
    private String reason;

    private String updateBy;
}
