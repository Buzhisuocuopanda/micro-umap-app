package com.mkst.umap.app.admin.api.bean.param.canteen;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(value = "食堂包厢传参对象")
public class CanteenBoxParam {

    @ApiModelProperty(value = "包厢id", hidden = true)
    private Long id;

    @ApiModelProperty(value = "餐次")
    private String name;

    @ApiModelProperty(value = "使用状态")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期", required = true)
    private Date dateTime;

}
