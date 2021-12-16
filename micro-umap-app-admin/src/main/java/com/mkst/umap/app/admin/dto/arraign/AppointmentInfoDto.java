package com.mkst.umap.app.admin.dto.arraign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AppointmentDetailResult
 * @Description 查看提审申请
 * @Author hsw
 * @Date 2020-06-16 18:51
 */
@Data
@ApiModel(value = "请求提审列表的结果")
public class AppointmentInfoDto {

    @ApiModelProperty(value = "提审id")
    private Long id;
    @ApiModelProperty(value = "房间名")
    private String roomName;
    @ApiModelProperty(value = "经办人")
    private String undertaker;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "罪犯姓名")
    private String criminalName;
    @ApiModelProperty(value = "犯罪类型")
    private String criminalType;
}
