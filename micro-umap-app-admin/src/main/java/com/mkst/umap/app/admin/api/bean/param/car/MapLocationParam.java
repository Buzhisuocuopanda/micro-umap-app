package com.mkst.umap.app.admin.api.bean.param.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ApiModel(value = "位置传参对象")
@Data
public class MapLocationParam {

    @ApiModelProperty(value = "位置id", hidden = true)
    private Long locationId;

    @ApiModelProperty(value = "位置名称")
    private String locationName;

    @ApiModelProperty(value = "纬度")
    private Integer latitude;

    @ApiModelProperty(value = "经度")
    private Integer longitude;
}
