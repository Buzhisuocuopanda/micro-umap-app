package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.mapper.SysUserMapper;
import com.mkst.umap.app.admin.domain.MeetingAttendee;
import com.mkst.umap.app.admin.mapper.MeetingAttendeeMapper;
import com.mkst.umap.app.admin.service.IMeetingAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议参与人员 服务层实现
 *
 * @author wangyong
 * @date 2020-07-31
 */
@Service
public class MeetingAttendeeServiceImpl implements IMeetingAttendeeService {
	@Autowired
	private MeetingAttendeeMapper meetingAttendeeMapper;
	@Autowired
	private SysUserMapper userMapper;

	/**
	 * 查询会议参与人员信息
	 *
	 * @param id 会议参与人员ID
	 * @return 会议参与人员信息
	 */
	@Override
	public MeetingAttendee selectMeetingAttendeeById(Long id) {
		return meetingAttendeeMapper.selectMeetingAttendeeById(id);
	}

	/**
	 * 查询会议参与人员列表
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 会议参与人员集合
	 */
	@Override
	public List<MeetingAttendee> selectMeetingAttendeeList(MeetingAttendee meetingAttendee) {
		return meetingAttendeeMapper.selectMeetingAttendeeList(meetingAttendee);
	}

	/**
	 * 新增会议参与人员
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 结果
	 */
	@Override
	public int insertMeetingAttendee(MeetingAttendee meetingAttendee) {
		return meetingAttendeeMapper.insertMeetingAttendee(meetingAttendee);
	}

	/**
	 * 修改会议参与人员
	 *
	 * @param meetingAttendee 会议参与人员信息
	 * @return 结果
	 */
	@Override
	public int updateMeetingAttendee(MeetingAttendee meetingAttendee) {
		return meetingAttendeeMapper.updateMeetingAttendee(meetingAttendee);
	}

	/**
	 * 删除会议参与人员对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteMeetingAttendeeByIds(String ids) {
		return meetingAttendeeMapper.deleteMeetingAttendeeByIds(Convert.toStrArray(ids));
	}


	@Override
	public int deleteByMeetingId(Long meetingId) {
		return meetingAttendeeMapper.deleteByMeetingId(meetingId);
	}

	/**
	 * @return java.util.List<com.mkst.mini.systemplus.system.domain.SysUser>
	 * @Author wangyong
	 * @Description 获取所有会议参与者
	 * @Date 11:31 2020-08-05
	 * @Param [meetingId]
	 */
	@Override
	public List<SysUser> selectAttendeeListByMeetingId(Long meetingId) {
		ArrayList<SysUser> resultList = new ArrayList<>();
		MeetingAttendee selectAttendee = new MeetingAttendee();
		selectAttendee.setMeetingId(meetingId);
		List<MeetingAttendee> attendeeList = meetingAttendeeMapper.selectMeetingAttendeeList(selectAttendee);
		if (attendeeList == null || attendeeList.isEmpty()) {
			return resultList;
		}
		attendeeList.stream().forEach(attendee -> {
			resultList.add(userMapper.selectUserById(attendee.getUserId()));
		});
		return resultList;
	}
}
