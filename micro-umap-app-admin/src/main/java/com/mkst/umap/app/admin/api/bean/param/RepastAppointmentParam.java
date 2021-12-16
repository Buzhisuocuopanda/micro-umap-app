package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
public class RepastAppointmentParam {

    @ApiModelProperty(value = "就餐时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repastDate;
    @ApiModelProperty(value = "餐次")
    private String type;
    @ApiModelProperty(value = "是否有外来人员")
    private String outsider;
    @ApiModelProperty(value = "外来人员人数")
    private Integer outsiderNum;
    @ApiModelProperty(value = "申请人")
    private Integer userId;

    @ApiModelProperty(value = "通知人")
    private Long messageUserId;


    @ApiModelProperty(value = "年份",example = "2020",name = "年份")
    private String year;

    @ApiModelProperty(value = "月份",example = "12",name = "月份")
    private String month;
}
