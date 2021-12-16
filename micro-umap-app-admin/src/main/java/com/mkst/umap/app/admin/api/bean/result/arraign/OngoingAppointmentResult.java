package com.mkst.umap.app.admin.api.bean.result.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName OngoingAppointmentResult
 * @Description 提审室查询时，正在进行的中的预约
 * @Author wangyong
 * @Date 2020-07-02 17:32
 */
@Data
@ApiModel(value = "预约信息")
public class OngoingAppointmentResult {

    @ApiModelProperty(value = "预约Id")
    private Long id;

    @ApiModelProperty(value = "使用人")
    private String useBy;

    @ApiModelProperty(value = "部门")
    private String dept;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    @ApiModelProperty(value = "申请类型")
    private String type;
}
