package com.mkst.umap.app.admin.dto.meeting;

import com.mkst.umap.app.admin.domain.Meeting;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MeetingChangeCheckDto
 * @Description
 * @Author wangyong
 * @Modified By:
 * @Date 2020-10-12 09:58
 */
@Data
public class MeetingChangeCheckDto {

    private Long id;

    private Meeting currentMeeting;

    private boolean conflict;

    private List<MeetingDetailDto> conflictMeetingList;

}
