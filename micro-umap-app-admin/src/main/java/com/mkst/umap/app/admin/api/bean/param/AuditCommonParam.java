package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AuditCommonParam
 * @Description
 * @Author wangyong
 * @Date 2020-07-14 11:46
 */
@Data
@ApiModel(value = "申请审核相关参数")
public class AuditCommonParam {

    @ApiModelProperty(value = "申请id")
    private Long id;

    @ApiModelProperty(value = "批量审核ids")
    private String ids;

    @ApiModelProperty("申请类型")
    private String type;

    @ApiModelProperty("申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkDate;

    @ApiModelProperty("0:已审核 1:待审核")
    private String needDeal;

    @ApiModelProperty("理由")
    private String reason;

    @ApiModelProperty("审核状态")
    private String status;

    @ApiModelProperty("查找申请人")
    private String checkUseBy;

    //传递参数:当前操作者
    private String operator;

    private String[] types;
}
