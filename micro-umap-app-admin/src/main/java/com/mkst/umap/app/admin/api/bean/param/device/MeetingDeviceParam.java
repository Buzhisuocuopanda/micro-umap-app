package com.mkst.umap.app.admin.api.bean.param.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MeetingDeviceParam
 * @Description 会议设备相关信息
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-04 16:08
 */
@Data
@ApiModel
public class MeetingDeviceParam {

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "房间Id")
    private String roomId;

    @ApiModelProperty(value = "group key")
    private String groupKey;

    @ApiModelProperty(value = "设备类型", example = "9")
    private String devType;

    @ApiModelProperty(value = "groupId")
    private String groupId;

    @ApiModelProperty(value = "时间状态", hidden = true)
    private String timeStatus;

    @ApiModelProperty(value = "检查时间点", hidden = true)
    private Date checkDate;

    @ApiModelProperty(value = "parentGroupName",example = "灯光")
    private String parentGroupName;

    @ApiModelProperty(value = "parentId",example = "父组id")
    private String parentId;

    @ApiModelProperty(value = "mode",example = "灯光模式选择")
    private String mode;
}
