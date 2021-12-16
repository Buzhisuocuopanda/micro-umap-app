package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName RoomScheduleParam
 * @Description 房间信息查询
 * @Author wangyong
 * @Date 2020-07-06 16:27
 */
@Data
@ApiModel(value = "获取房间排班的参数")
public class RoomScheduleParam {

    @ApiModelProperty(value = "房间Id")
    private String roomId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ApiModelProperty(value = "选择的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date chooseStartTime;
}
