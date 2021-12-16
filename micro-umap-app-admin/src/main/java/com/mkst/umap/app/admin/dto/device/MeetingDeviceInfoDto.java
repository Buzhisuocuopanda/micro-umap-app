package com.mkst.umap.app.admin.dto.device;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName MeetingDeviceInfoDto
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-09-04 16:24
 */
@Data
public class MeetingDeviceInfoDto {

    private String subject;

    private Date startTime;

    private Date endTime;

    private String dept;

    private String useBy;

    private Integer attendeeAmount;

}
