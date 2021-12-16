package com.mkst.umap.app.admin.api.bean.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName RoomListParam
 * @Description 获取提审室列表的参数
 * @Author wangyong
 * @Date 2020-07-06 11:01
 */
@Data
@ApiModel(value = "获取提审室列表的参数")
public class RoomListParam {

    private static final long serialVersionUID = 1L;

    /**
     * 房间id
     */
    @ApiModelProperty(value = "提审室ID")
    private String id;
    /**
     * 查询某个时间点的状况
     */
    @ApiModelProperty(value = "时间检查点 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDatetime;
    /**
     * 提审室状态
     */
    @ApiModelProperty(value = "提审室当前状态（0：空闲 1：使用中）")
    private String nowStatus;
}
