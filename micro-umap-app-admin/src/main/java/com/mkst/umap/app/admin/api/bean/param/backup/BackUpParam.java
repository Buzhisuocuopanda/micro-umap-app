package com.mkst.umap.app.admin.api.bean.param.backup;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName BackUpParam
 * @Description
 * @Author yinyuanping
 * @Modified By:
 * @Date 2020/12/8 0008 下午 5:09
 */
@ApiModel(value = "备勤间的参数")
@Data
public class BackUpParam {

    @ApiModelProperty(value = "年份",example = "2020",name = "年份")
    private String year;

    @ApiModelProperty(value = "月份",example = "12",name = "月份")
    private String month;

    @ApiModelProperty(value = "申请状态（0：正常 1：已处理）", example = "2", required = false, name = "申请状态")
    private String applyStatus;

    @ApiModelProperty(value = "申请人ID", required = false, name = "申请人ID")
    private Long applicantId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询日期", example = "2020-07-05", required = false, name = "查询日期")
    private Date checkDate;

    private Long applyId;

    private Integer approveStatus;

}
