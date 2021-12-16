package com.mkst.umap.app.admin.api.bean.param.petition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName MatterParam
 * @Description 民行申诉-事项
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-26 09:59
 */
@Data
@ApiModel(value = "民行申诉事项参数",description = "民行申诉事项参数")
public class MatterParam {

    @ApiModelProperty(value = "事项id",example = "1")
    private Long id;

    @ApiModelProperty(value = "事项类型",example = "first_instance")
    private String type;

    @ApiModelProperty(value = "法院",example = "1")
    private String court;

    @ApiModelProperty(value = "判决人",example = "1")
    private String referee;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "调解日期",example = "2020-08-04 00:00:00")
    private Date rulingTime;

    @ApiModelProperty(value = "裁决/调解书文号",example = "1")
    private String rulingSymbol;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "法院受理再审日期",example = "2020-08-04 00:00:00")
    private Date retrialTime;

    @ApiModelProperty(value = "法院受理再审申请通知书文号",example = "1")
    private String retrialSymbol;

    @ApiModelProperty(value = "创建人",example = "1",hidden = true)
    private Long createBy;

    @ApiModelProperty(value = "更新者",example = "1" ,hidden = true)
    private Long updateBy;

    @ApiModelProperty(value = "备注",example = "1",hidden = true)
    private String remark;
}
