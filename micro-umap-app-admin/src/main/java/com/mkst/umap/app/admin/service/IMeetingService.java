package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.api.bean.param.device.MeetingDeviceParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult;
import com.mkst.umap.app.admin.domain.Meeting;
import com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo;
import com.mkst.umap.app.admin.dto.device.MeetingDeviceInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingAuditProgressInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingDetailDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingRoomInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingRoomScheduleDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会议 服务层
 *
 * @author wangyong
 * @date 2020-07-31
 */
public interface IMeetingService {
	/**
	 * 查询会议信息
	 *
	 * @param id 会议ID
	 * @return 会议信息
	 */
	public Meeting selectMeetingById(Long id);

	/**
	 * 查询会议列表
	 *
	 * @param meeting 会议信息
	 * @return 会议集合
	 */
	public List<Meeting> selectMeetingList(Meeting meeting);

	/**
	 * 新增会议
	 *
	 * @param meeting 会议信息
	 * @return 结果
	 */
	public int insertMeeting(Meeting meeting);

	/**
	 * 修改会议
	 *
	 * @param meeting 会议信息
	 * @return 结果
	 */
	public int updateMeeting(Meeting meeting);

	/**
	 * 删除会议信息
	 *
	 * @param ids 需要删除的数据ID
     * @return 结果
	 */
	public int deleteMeetingByIds(String ids);

	List<MeetingRoomScheduleDto> getRoomSchedule(RoomScheduleParam roomScheduleParam);

    Date getNextStartTime(MeetingParam meetingParam);

    List<MeetingRoomInfoDto> getRoomInfoList(MeetingParam meetingParam);

    List<MeetingInfoResult> selectMyMeetingList(MeetingParam meetingParam);

    MeetingDetailDto getDetailById(Long meetingId, String loginName);

    int cancelMeeting(Long meetingId, String remark);

    List<MeetingWebInfoVo> selectMeetingWebList(Meeting meeting);

    int auditMeeting(Long id, String auditStatus, String reason, String updateBy, MeetingParam meetingParam);

    List<MeetingInfoResult> getMyAttendedMeetingInfo(MeetingParam meetingParam);

    List<MeetingInfoResult> selectApplyList(MeetingParam meetingParam);

    MeetingDeviceInfoDto getMeetingInfo(MeetingDeviceParam meetingDeviceParam);

    void changeMeeting(Meeting meeting);

    List<MeetingDetailDto> getOccupyMeeting(MeetingParam meetingParam);

	int cancelMeetingMsg(Long meetingId, String remark);

    List<NameCountResult> selectDayCount(MeetingParam param);

	Map<String, List<TimeApplyResult>> selectTimeApplyList(MeetingParam param);

    List<MeetingWebInfoVo> selectMeetingAuditList(Meeting selectMeeting);

	List<MeetingAuditProgressInfoDto> selectAuditList(Long meetingId);
}
