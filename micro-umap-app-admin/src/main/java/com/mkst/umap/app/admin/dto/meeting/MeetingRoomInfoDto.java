package com.mkst.umap.app.admin.dto.meeting;

import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MeetingRoomInfoDto
 * @Description 会议室的dto
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-03 14:13
 */
@Data
public class MeetingRoomInfoDto {
    /**
     * 会议室名
     */
    private String name;

    /**
     * 会议室地址
     */
    private String address;

    /**
     * 申请信息
     */
    private List<MeetingInfoResult> meetingList;
}
