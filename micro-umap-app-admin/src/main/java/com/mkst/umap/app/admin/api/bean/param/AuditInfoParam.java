package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AuditInfoParam
 * @Description 申请审批列表时的参数
 * @Author wangyong
 * @Date 2020-07-07 16:12
 */
@Data
@ApiModel(value = "申请审批列表时的参数")
public class AuditInfoParam {

    @ApiModelProperty(value = "已处理0、待处理1")
    private String needDeal;

    @ApiModelProperty(value = "申请类型")
    private String type;

    @ApiModelProperty(value = "申请状态")
    private String status;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "申请日期")
    private Date createDate;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "申请日期")
    private Date checkDate;

}
