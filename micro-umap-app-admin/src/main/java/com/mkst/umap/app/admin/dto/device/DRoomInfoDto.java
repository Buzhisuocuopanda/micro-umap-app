package com.mkst.umap.app.admin.dto.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName MeetingRoomInfoDto
 * @Description 会议室信息
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-07 15:00
 */
@Data
@ApiModel("会议室信息")
public class DRoomInfoDto {

    @ApiModelProperty(value = "房间id", example = "8c2a9582d31811ea93401c1b0dbdb961")
    private String id;

    @ApiModelProperty(value = "房间名", example = "会议室1")
    private String name;

    @ApiModelProperty(value = "房间地址", example = "五栋207")
    private String address;

    @ApiModelProperty(value = "容纳人数", example = "100")
    private String galleryful;
}
