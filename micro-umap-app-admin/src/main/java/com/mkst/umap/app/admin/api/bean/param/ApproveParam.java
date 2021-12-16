package com.mkst.umap.app.admin.api.bean.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "审批传参对象")
@Data
public class ApproveParam {

    @ApiModelProperty(value = "申请id", example = "1")
    private Long id;

    @ApiModelProperty(value = "操作类型")
    private String approveType;

    @ApiModelProperty(value = "申请id集合")
    private List<Long> applyIds;

    @ApiModelProperty(value = "创建者登录名", example = "admin")
    private String createBy;

    @ApiModelProperty(value = "目标审核人员id", example = "3210")
    private Long targetId;

    @ApiModelProperty(value = "审核状态（字典：event_audit_status）", example = "1")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核理由",example = "不好")
    private String reason;
}
