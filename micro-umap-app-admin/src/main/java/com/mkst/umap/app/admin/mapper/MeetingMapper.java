package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.api.bean.param.device.MeetingDeviceParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingAuditResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult;
import com.mkst.umap.app.admin.domain.Meeting;
import com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo;
import com.mkst.umap.app.admin.dto.meeting.MeetingDetailDto;

import java.util.Date;
import java.util.List;

/**
 * 会议 数据层
 *
 * @author wangyong
 * @date 2020-07-31
 */
public interface MeetingMapper {
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
     * 删除会议
     *
     * @param id 会议ID
     * @return 结果
     */
    public int deleteMeetingById(Long id);

    /**
     * 批量删除会议
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMeetingByIds(String[] ids);

    Date getNextStartTime(MeetingParam meetingParam);

    List<MeetingInfoResult> selectMeetingInfoList(Meeting selectMeeting);

    MeetingDetailDto getDetailById(Long meetingId);

    List<MeetingWebInfoVo> selectMeetingWebList(Meeting meeting);

    List<MeetingInfoResult> selectMyApply(MeetingParam meetingParam);

    List<MeetingDetailDto> getNowMeetingInfoList(MeetingDeviceParam meetingDeviceParam);

    List<MeetingDetailDto> getNextMeetingInfoList(MeetingDeviceParam meetingDeviceParam);

    List<MeetingDetailDto> getOccupyMeeting(MeetingParam meetingParam);

    List<NameCountResult> selectDayCount(MeetingParam param);

    List<TimeApplyResult> selectTimeApplyList(MeetingParam param);

    List<MeetingWebInfoVo> selectMeetingAuditList(Meeting selectMeeting);

    List<MeetingAuditResult> selectAuditProgressList(Long meetingId);
}