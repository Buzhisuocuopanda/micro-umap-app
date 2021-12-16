package com.mkst.umap.app.admin.service;

import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.domain.MeetingAttendee;

import java.util.List;

/**
 * 会议参与人员 服务层
 *
 * @author wangyong
 * @date 2020-07-31
 */
public interface IMeetingAttendeeService {
	/**
	 * 查询会议参与人员信息
	 *
	 * @param id 会议参与人员ID
	 * @return 会议参与人员信息
	 */
	public MeetingAttendee selectMeetingAttendeeById(Long id);

	/**
	 * 查询会议参与人员列表
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 会议参与人员集合
	 */
	public List<MeetingAttendee> selectMeetingAttendeeList(MeetingAttendee meetingAttendee);

	/**
	 * 新增会议参与人员
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 结果
	 */
	public int insertMeetingAttendee(MeetingAttendee meetingAttendee);

	/**
	 * 修改会议参与人员
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 结果
	 */
	public int updateMeetingAttendee(MeetingAttendee meetingAttendee);

    /**
     * 删除会议参与人员信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMeetingAttendeeByIds(String ids);

    int deleteByMeetingId(Long meetingId);

    List<SysUser> selectAttendeeListByMeetingId(Long meetingId);
}
