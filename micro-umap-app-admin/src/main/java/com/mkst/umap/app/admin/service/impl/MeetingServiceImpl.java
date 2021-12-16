package com.mkst.umap.app.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.basic.domain.content.AppMsgContent;
import com.mkst.mini.systemplus.basic.utils.MsgPushUtils;
import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.mini.systemplus.common.utils.DateUtils;
import com.mkst.mini.systemplus.common.utils.StringUtils;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysConfigService;
import com.mkst.mini.systemplus.system.service.ISysRoleService;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.api.bean.param.RoomScheduleParam;
import com.mkst.umap.app.admin.api.bean.param.device.MeetingDeviceParam;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.api.bean.result.NameCountResult;
import com.mkst.umap.app.admin.api.bean.result.arraign.TimeApplyResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingAuditResult;
import com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult;
import com.mkst.umap.app.admin.domain.*;
import com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo;
import com.mkst.umap.app.admin.dto.device.MeetingDeviceInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingAuditProgressInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingDetailDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingRoomInfoDto;
import com.mkst.umap.app.admin.dto.meeting.MeetingRoomScheduleDto;
import com.mkst.umap.app.admin.mapper.ArraignRoomMapper;
import com.mkst.umap.app.admin.mapper.AuditRecordMapper;
import com.mkst.umap.app.admin.mapper.MeetingMapper;
import com.mkst.umap.app.admin.service.IArraignRoomEquipmentService;
import com.mkst.umap.app.admin.service.IAuditProgressService;
import com.mkst.umap.app.admin.service.IMeetingAttendeeService;
import com.mkst.umap.app.admin.service.IMeetingService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.constant.SmsNoticeMsgConstant;
import com.mkst.umap.app.common.enums.*;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 会议 服务层实现
 *
 * @author wangyong
 * @date 2020-07-31
 */
@Service
public class MeetingServiceImpl implements IMeetingService {
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private ArraignRoomMapper roomMapper;
    @Autowired
    private IMeetingAttendeeService attendeeService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private AuditRecordMapper auditRecordMapper;
    @Autowired
	private IArraignRoomEquipmentService equipmentService;
    @Autowired
	private ISysConfigService configService;
    @Autowired
	private IAuditProgressService progressService;
    @Autowired
	private ISysRoleService roleService;

    /**
     * 查询会议信息
     *
     * @param id 会议ID
     * @return 会议信息
     */
    @Override
    public Meeting selectMeetingById(Long id) {
        return meetingMapper.selectMeetingById(id);
	}

	/**
	 * 查询会议列表
	 *
	 * @param meeting 会议信息
	 * @return 会议集合
	 */
	@Override
	public List<Meeting> selectMeetingList(Meeting meeting) {
		return meetingMapper.selectMeetingList(meeting);
	}

	/**
	 * @return java.util.List<com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo>
	 * @Author wangyong
	 * @Description 获取Web信息List
	 * @Date 11:09 2020-08-04
	 * @Param [meeting]
	 */
	@Override
	public List<MeetingWebInfoVo> selectMeetingWebList(Meeting meeting) {
		return meetingMapper.selectMeetingWebList(meeting);
	}

	/**
	 * @return int
	 * @Author wangyong
	 * @Description 进行审核
	 * @Date 15:56 2020-08-06
	 * @Param [id, auditStatus, reason]
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int auditMeeting(Long id, String auditStatus, String reason, String updateBy, MeetingParam meetingParam) {

		Meeting meeting = this.selectMeetingById(id);
		// 防止二次审核
		if (meeting.getStatus().equals(KeyConstant.EVENT_IS_CANCEL_TRUE) || !meeting.getAuditStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT)) {
			return 0;
		}

		// 先插入审核表
		AuditRecord auditRecord = new AuditRecord(id, AuditRecordTypeEnum.MeetingRoomAudit.getValue(), auditStatus, "");
		auditRecord.setCreateBy(updateBy);
		auditRecord.setUpdateBy(updateBy);
		// 前端传递空值有错误，所以reason的控制用NoReasonFlag标识
		if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			auditRecord.setReason(reason);
		}
		auditRecordMapper.insertAuditRecord(auditRecord);

		// 进程
		AuditProgress auditProgress = new AuditProgress();
		auditProgress.setBusinessType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
		auditProgress.setProgress(meeting.getAuditProgress() + 2 );
		auditProgress.setBusinessId(id);
		List<AuditProgress> auditProgressesList = progressService.selectAuditProgressList(auditProgress);
		int nowProgressNum = meeting.getAuditProgress() + 1;
		if (StringUtils.equals(auditStatus,KeyConstant.EVENT_AUDIT_STATUS_PASS) && CollectionUtil.isNotEmpty(auditProgressesList)){
			AuditProgress progress = auditProgressesList.get(0);
			progress.setCreateBy(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			progressService.updateAuditProgress(progress);
			// 更新这个进程的详细信息
			// 低并发下没问题。。。
			AuditProgress updateProgress = new AuditProgress();
			updateProgress.setId(progress.getId() - 1);
			updateProgress.setTarget(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			updateProgress.setRecordId(auditRecord.getId());
			// updateProgress.setCreateBy(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			progressService.updateAuditProgress(updateProgress);
			if (KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(meetingParam.getAuditStatus())){
				// 新开线程发送短信
				new Thread(() -> {
					// 发送通知信息
					sendAppMsg(auditProgress.getTarget(), id);
				}).start();
			}else {
				nowProgressNum = MeetingAuditProgressEnum.FINISHED.getProgressCode();
				meeting.setAuditStatus(meetingParam.getAuditStatus());
				// 新开线程发送短信
				new Thread(() -> {
					// 发送通知信息
					sendAuditSmsNotice(id, auditStatus);
				}).start();
			}
		}else {
			auditProgress.setProgress(nowProgressNum);
			List<AuditProgress> progresses = progressService.selectAuditProgressList(auditProgress);
			if (CollectionUtil.isEmpty(progresses)){
				throw new NullPointerException("流程中未发现当前审核进度："+ nowProgressNum);
			}
			AuditProgress progress = progresses.get(0);
			progress.setRecordId(auditRecord.getId());
			// progress.setCreateBy(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			progressService.updateAuditProgress(progress);
			nowProgressNum = -1;
			// 准备修改会议表
			meeting.setAuditStatus(auditStatus);
			// 新开线程发送短信
			new Thread(() -> {
				// 发送通知信息
				sendAuditSmsNotice(id, auditStatus);
			}).start();
		}
		meeting.setAuditProgress(nowProgressNum);
		return this.updateMeeting(meeting);
	}

	private void sendAuditSmsNotice(Long meetingId, String auditStatus) {

		// 查询会议主体信息
		Meeting selectMeeting = new Meeting();
		selectMeeting.setId(meetingId);
		MeetingWebInfoVo meetingInfoVo = this.selectMeetingWebList(selectMeeting).get(0);

		// 会议相关人员信息
		Meeting meeting = this.selectMeetingById(meetingId);
		SysUser useByInfo = userService.selectUserById(meeting.getUseBy());
		SysUser createByInfo = userService.selectUserByLoginName(meeting.getCreateBy());

		// 拼接会议主体信息sms
		StringBuffer smsMeetingInfo = new StringBuffer(SmsNoticeMsgConstant.MEETING_INFO);
		replaceStringBuffer(smsMeetingInfo, "${dept}", meetingInfoVo.getDept());
		replaceStringBuffer(smsMeetingInfo, "${subject}", meetingInfoVo.getSubject());
		/*replaceStringBuffer(smsMeetingInfo, "${meetingId}", meetingInfoVo.getId().toString());*/
		replaceStringBuffer(smsMeetingInfo, "${dd}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD, meetingInfoVo.getStartTime()));
		replaceStringBuffer(smsMeetingInfo, "${time}", UmapDateUtils.combine2Str(meetingInfoVo.getStartTime(), meetingInfoVo.getEndTime()));
		replaceStringBuffer(smsMeetingInfo, "${roomAddr}", meetingInfoVo.getRoomAddr());

		String useByName = meetingInfoVo.getUseBy();
		String createByName = meetingInfoVo.getCreateBy();

		// 需要发送的信息
		StringBuffer smsToUseBy;
		StringBuffer smsToCreateBy;

		// 设置模板
		if ("1".equals(auditStatus)) {
			smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_USE_BY);
			smsToCreateBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_CREATE_BY);
		} else if ("2".equals(auditStatus)) {
			smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_FAIL_SMS_TO_USE_BY);
			smsToCreateBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_FAIL_SMS_TO_CREATE_BY);
		} else {
			return;
		}

		// 替换相关内容
		replaceStringBuffer(smsToUseBy, "${useByName}", useByName);
		replaceStringBuffer(smsToUseBy, "${createByName}", createByName);

		replaceStringBuffer(smsToCreateBy, "${useByName}", useByName);
		replaceStringBuffer(smsToCreateBy, "${createByName}", createByName);

		// 通知申请人和提交人
		try {
			YxtSmsConfig.getYxtSmsService().sendMsg(useByInfo.getPhonenumber(), smsToUseBy.append(smsMeetingInfo).toString());
			// 规避申请人与使用人是同一人
			if (!useByInfo.getLoginName().equals(createByInfo.getLoginName())){
				YxtSmsConfig.getYxtSmsService().sendMsg(createByInfo.getPhonenumber(), smsToCreateBy.append(smsMeetingInfo).toString());
			}
		} catch (YxtSmsErrorException e) {
			e.printStackTrace();
		}

		// 只有通过时才通知与会人
		if ("1".equals(auditStatus)) {
			StringBuffer smsToAttendee = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_ATTENDEE);
			replaceStringBuffer(smsToAttendee, "${deptName}", createByInfo.getDept().getDeptName());
			replaceStringBuffer(smsToAttendee, "${useByName}", useByName);

			List<SysUser> attendeeInfoList = attendeeService.selectAttendeeListByMeetingId(meetingId);
			attendeeInfoList.stream().forEach(attendee -> {
				// 规避 创建人是参与人
				if (useByInfo.getLoginName().equals(attendee.getLoginName())){
					return;
				}
				// 多线程下的本地数据
				StringBuffer hereSms = new StringBuffer(smsToAttendee.toString());
				replaceStringBuffer(hereSms, "${attendeeName}", attendee.getUserName());
				try {
					YxtSmsConfig.getYxtSmsService().sendMsg(attendee.getPhonenumber(), hereSms.append(smsMeetingInfo).toString());
				} catch (YxtSmsErrorException e) {
					e.printStackTrace();
				}
			});
		}
	}
	private void sendAppMsg(String target, Long meetingId) {
		AppMsgContent msgContent = new AppMsgContent();
		msgContent.setTitle("会议申请");
		msgContent.setContent("一个会议申请待审核，请及时处理！");

		Map<String, String> params = new HashMap<>();
		params.put("bizKey", meetingId.toString());
		params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
		msgContent.setParams(params);
		MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_MEETING.getValue(), target);
		MsgPushUtils.getMsgPushTask().execute();
	}
	/**
	 * @return 数据生效条数
	 * @Author wangyong
	 * @Description 取消会议申请，注解式事务，取消会议的操作不会删除会议参与表中的数据
	 * @Date 9:54 2020-08-04
	 * @Param [meetingId]
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int cancelMeeting(Long meetingId, String remark) {
		Meeting updateMeeting = new Meeting();
		updateMeeting.setId(meetingId);
		// 新开线程发送短信
		new Thread(() -> {
			// 发送通知信息
			sendCancelSmsNotice(meetingId,remark);
		}).start();
		// 设置会议的状态
		updateMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
		return meetingMapper.updateMeeting(updateMeeting);
	}

	private void sendCancelSmsNotice(Long meetingId, String remark) {
		// 查询会议主体信息
		Meeting selectMeeting = new Meeting();
		selectMeeting.setId(meetingId);
		MeetingWebInfoVo meetingInfoVo = this.selectMeetingWebList(selectMeeting).get(0);

		// 会议相关人员信息
		Meeting meeting = this.selectMeetingById(meetingId);
		SysUser useByInfo = userService.selectUserById(meeting.getUseBy());

		// 拼接会议主体信息sms
		StringBuffer smsMeetingInfo = new StringBuffer(SmsNoticeMsgConstant.MEETING_INFO);
		replaceStringBuffer(smsMeetingInfo, "${dept}", meetingInfoVo.getDept());
		replaceStringBuffer(smsMeetingInfo, "${subject}", meetingInfoVo.getSubject());
		//replaceStringBuffer(smsMeetingInfo, "${meetingId}", meetingInfoVo.getId().toString());
		replaceStringBuffer(smsMeetingInfo, "${dd}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD, meetingInfoVo.getStartTime()));
		replaceStringBuffer(smsMeetingInfo, "${time}", UmapDateUtils.combine2Str(meetingInfoVo.getStartTime(), meetingInfoVo.getEndTime()));
		replaceStringBuffer(smsMeetingInfo, "${roomAddr}", meetingInfoVo.getRoomAddr());

		String useByName = meetingInfoVo.getUseBy();

		// 需要发送的信息
		StringBuffer smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_CANCEL_SMS_TO_USE_BY);
		StringBuffer smsToAttendee = new StringBuffer(SmsNoticeMsgConstant.MEETING_CANCEL_SMS_TO_ATTENDEE);

		replaceStringBuffer(smsToUseBy, "${useByName}", useByName);
		try {
			YxtSmsConfig.getYxtSmsService().sendMsg(useByInfo.getPhonenumber(), smsToUseBy.append(smsMeetingInfo).toString());
		} catch (YxtSmsErrorException e) {
			e.printStackTrace();
		}


		//唯有审核通过的会议才通知与会人取消
		if ("1".equals(meetingInfoVo.getAuditStatus())) {
			List<SysUser> attendeeInfoList = attendeeService.selectAttendeeListByMeetingId(meetingId);
			attendeeInfoList.stream().forEach(attendee -> {
				replaceStringBuffer(smsToAttendee, "${attendeeName}", attendee.getUserName());
				try {
					YxtSmsConfig.getYxtSmsService().sendMsg(attendee.getPhonenumber(), smsToAttendee.append(smsMeetingInfo).toString());
				} catch (YxtSmsErrorException e) {
					e.printStackTrace();
				}
			});
		}

	}


	private void replaceStringBuffer(StringBuffer sb, String target, String newStr) {

		int l = target.length();
		int p = sb.lastIndexOf(target);
		sb.replace(p, p + l, newStr);
	}

	/**
	 * 新增会议
	 *
	 * @param meeting 会议信息
	 * @return 结果
	 */
	@Override
	public int insertMeeting(Meeting meeting) {
		return meetingMapper.insertMeeting(meeting);
	}

	/**
	 * 修改会议
	 *
	 * @param meeting 会议信息
	 * @return 结果
	 */
	@Override
	public int updateMeeting(Meeting meeting) {
		return meetingMapper.updateMeeting(meeting);
	}

	/**
	 * 删除会议对象
	 *
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteMeetingByIds(String ids) {
		return meetingMapper.deleteMeetingByIds(Convert.toStrArray(ids));
	}

	@Override
	public List<MeetingDetailDto> getOccupyMeeting(MeetingParam meetingParam) {
		return meetingMapper.getOccupyMeeting(meetingParam);
	}

	@Override
	public int cancelMeetingMsg(Long meetingId, String remark) {

		String attendeeMsg = configService.selectConfigByKey("meeting_cancel_attendee_msg");
		Meeting meeting = this.selectMeetingById(meetingId);
		attendeeMsg = StringUtils.replace(attendeeMsg,"${subject}",meeting.getSubject());

		List<SysUser> attendeeList = attendeeService.selectAttendeeListByMeetingId(meetingId);
		if (CollectionUtil.isEmpty(attendeeList)){
			return 1;
		}

		String finalAttendeeMsg = attendeeMsg;
		attendeeList.stream().forEach(attendee ->{
			String hereNotice =	StringUtils.replace(new String(finalAttendeeMsg),"${attendeeName}", attendee.getUserName());
			if (StringUtils.isNotEmpty(remark)){
				hereNotice = hereNotice + "备注："+remark;
			}
			try {
				YxtSmsConfig.getYxtSmsService().sendMsg(attendee.getPhonenumber(), hereNotice);
			} catch (YxtSmsErrorException e) {
				e.printStackTrace();
			}
		});
		return attendeeList.size();
	}

	@Override
	public void changeMeeting(Meeting meeting) {

		Meeting thisMeeting = meetingMapper.selectMeetingById(meeting.getId());

		String noticeSms = configService.selectConfigByKey("meeting_change_notice_sms");
		Date nowStartTime =  BeanUtil.isEmpty(meeting.getStartTime()) ? thisMeeting.getStartTime() : meeting.getStartTime();
		Date nowEndTime =  BeanUtil.isEmpty(meeting.getStartTime()) ? thisMeeting.getStartTime() : meeting.getStartTime();
		String nowRoomId = StringUtils.isEmpty(meeting.getRoomId()) ? thisMeeting.getRoomId() : meeting.getRoomId();

		// 替换过主题的会议文本串
		String meetingChangeNotice = StringUtils.replace(noticeSms, "${subject}", thisMeeting.getSubject());
		// 替换过开始时间的文本串
		meetingChangeNotice = StringUtils.replace(meetingChangeNotice, "${startTime}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD_HH_MM, nowStartTime));

		// 替换会议室
		ArraignRoom room = roomMapper.selectArraignRoomById(nowRoomId);
		if (BeanUtil.isNotEmpty(room)){
			meetingChangeNotice = StringUtils.replace(meetingChangeNotice,"${roomAddr}",room.getAddress());
		}

		final String notice = new String(meetingChangeNotice);
		List<SysUser> attendeeInfoList = attendeeService.selectAttendeeListByMeetingId(meeting.getId());
		attendeeInfoList.stream().forEach(attendee -> {
			String hereNotice =	StringUtils.replace(new String(notice),"${attendeeName}", attendee.getUserName());
			if (StringUtils.isNotEmpty(meeting.getRemark())){
				hereNotice = hereNotice + "备注："+meeting.getRemark();
			}
			try {
				YxtSmsConfig.getYxtSmsService().sendMsg(attendee.getPhonenumber(), hereNotice);
			} catch (YxtSmsErrorException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @return java.util.List<com.mkst.umap.app.admin.dto.meeting.MeetingRoomScheduleDto>
	 * @Author wangyong
	 * @Description 房间排班
	 * @Date 10:25 2020-08-04
	 * @Param [roomScheduleParam]
	 */
	@Override
	public List<MeetingRoomScheduleDto> getRoomSchedule(RoomScheduleParam roomScheduleParam) {

		ArrayList<MeetingRoomScheduleDto> resultList = new ArrayList<>();

		// 查询房间
		ArraignRoom selectMeetingRoom = new ArraignRoom();
		// 会议室+可用
		selectMeetingRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());
		selectMeetingRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		List<ArraignRoom> arraignRooms = roomMapper.selectArraignRoomList(selectMeetingRoom);
		// 提前处理
		if (arraignRooms == null || arraignRooms.size() == 0) {
			return new ArrayList<>();
		}

		for (ArraignRoom room : arraignRooms) {
			MeetingRoomScheduleDto meetingRoomScheduleDto = new MeetingRoomScheduleDto();
			meetingRoomScheduleDto.setId(room.getId());
			String address = room.getAddress();
			if (StringUtils.isNotEmpty(address)){
				String[] split = address.split("/");
				meetingRoomScheduleDto.setAddress(split[split.length-1]);
			}
			meetingRoomScheduleDto.setRoomName(room.getName());
			meetingRoomScheduleDto.setRoomAvailable(true);
			meetingRoomScheduleDto.setMaxNumber(room.getGalleryful());

			Meeting selectMeeting = new Meeting();
			selectMeeting.setRoomId(room.getId());
			selectMeeting.setCheckDate(roomScheduleParam.getDate());
			selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
			selectMeeting.setAuditStatusArr(new String[]{"0", "1"});

			List<Meeting> meetings = meetingMapper.selectMeetingList(selectMeeting);

			ArrayList<String> dateList = getDateList(meetings);
			meetingRoomScheduleDto.setDateList(dateList);
			resultList.add(meetingRoomScheduleDto);
		}
		return resultList;
	}

	private ArrayList<String> getDateList(List<Meeting> meetings) {
		ArrayList<String> dateList = new ArrayList<>();
		meetings.stream().forEach(meeting -> {
			dateList.add(UmapDateUtils.combine2Str(meeting.getStartTime(), meeting.getEndTime()));
		});
		return dateList;
	}


	@Override
	public Date getNextStartTime(MeetingParam meetingParam) {
		return meetingMapper.getNextStartTime(meetingParam);
	}


	/**
	 * @return java.util.List<com.mkst.umap.app.admin.dto.meeting.MeetingRoomInfoDto>
	 * @Author wangyong
	 * @Description 获取房间信息和房间使用信息
	 * @Date 10:12 2020-08-04
	 * @Param [meetingParam]
	 */
	@Override
	public List<MeetingRoomInfoDto> getRoomInfoList(MeetingParam meetingParam) {

		// 用于返回结果
		List<MeetingRoomInfoDto> resultList = new ArrayList<>();

		// 用于查询会议室
		ArraignRoom selectRoom = new ArraignRoom();
		// 设置房间当前可用且是会议室的类型
		selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		selectRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());

		List<ArraignRoom> meetingRooms = roomMapper.selectArraignRoomList(selectRoom);

		// 容错
		if (meetingRooms == null || meetingRooms.size() == 0) {
			return new ArrayList<>();
		}

		for (ArraignRoom meetingRoom : meetingRooms) {
			// 房间信息
			MeetingRoomInfoDto meetingRoomInfoDto = transObject(meetingRoom, MeetingRoomInfoDto.class);

			Meeting selectMeeting = new Meeting();
			// 会议未取消、已通过
			selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
			selectMeeting.setAuditStatus(String.valueOf(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue()));
			selectMeeting.setCheckDate(meetingParam.getCheckDate());
			selectMeeting.setRoomId(meetingRoom.getId());

			List<MeetingInfoResult> meetingInfoResults = meetingMapper.selectMeetingInfoList(selectMeeting);

			// 容错
			if (meetingInfoResults == null || meetingInfoResults.isEmpty()) {
				meetingInfoResults = new ArrayList<>();
			}

			meetingInfoResults.stream().forEach(meetingInfoResult -> {
				meetingInfoResult.setNowStatus(UmapDateUtils.eventNowStatus(meetingInfoResult.getStartTime(), meetingInfoResult.getEndTime()));
			});

			meetingRoomInfoDto.setMeetingList(meetingInfoResults);
			resultList.add(meetingRoomInfoDto);
		}
		return resultList;
	}

	/**
	 * @return java.util.List<com.mkst.umap.app.admin.api.bean.result.meeting.MeetingInfoResult>
	 * @Author wangyong
	 * @Description 查找我的会议列表
	 * @Date 10:09 2020-08-04
	 * @Param [meetingParam]
	 */
	@Override
	public List<MeetingInfoResult> selectMyMeetingList(MeetingParam meetingParam) {

		// 结果集
		List<MeetingInfoResult> results = new ArrayList<>();


		// useBy
		Meeting selectMeeting = transObject(meetingParam, Meeting.class);
		String checkNowStatus = meetingParam.getNowStatus();
		if (checkNowStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_WAITING) || checkNowStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING)) {
			selectMeeting.setAuditStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString());
			selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
		}

		List<MeetingInfoResult> meetingInfoResults = meetingMapper.selectMeetingInfoList(selectMeeting);

		// 处理最终结果
		for (MeetingInfoResult meetingInfo : meetingInfoResults) {

			meetingInfo.setAuditStatusName(AuditStatusEnum.getName(Integer.parseInt(meetingInfo.getAuditStatus())));
			String nowStatus = UmapDateUtils.eventNowStatus(meetingInfo.getStartTime(), meetingInfo.getEndTime());

			meetingInfo.setNowStatus(nowStatus);
			if (meetingParam.getNowStatus().equals(KeyConstant.EVENT_ALL) || meetingParam.getNowStatus().equals(nowStatus)) {
				results.add(meetingInfo);
				continue;
			}
		}
		return results;
	}

	@Override
	public List<MeetingInfoResult> getMyAttendedMeetingInfo(MeetingParam meetingParam) {

		ArrayList<MeetingInfoResult> results = new ArrayList<>();

		MeetingAttendee selectAttendee = new MeetingAttendee();
		selectAttendee.setUserId(meetingParam.getNowUserId());
		List<MeetingAttendee> myList = attendeeService.selectMeetingAttendeeList(selectAttendee);

		Meeting selectMeeting = new Meeting();
		selectMeeting.setCheckDate(meetingParam.getCheckDate());
		selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
		selectMeeting.setAuditStatus(KeyConstant.EVENT_AUDIT_STATUS_PASS);
		myList.stream().forEach(a -> {
			selectMeeting.setId(a.getMeetingId());
			List<MeetingInfoResult> meetingInfoResults = meetingMapper.selectMeetingInfoList(selectMeeting);
			if (CollectionUtil.isNotEmpty(meetingInfoResults)) {
				results.add(meetingInfoResults.get(0));
			}
		});

		return results;
	}

	@Override
	public List<MeetingInfoResult> selectApplyList(MeetingParam meetingParam) {
		return meetingMapper.selectMyApply(meetingParam);
	}

	@Override
	public MeetingDeviceInfoDto getMeetingInfo(MeetingDeviceParam meetingDeviceParam) {

		MeetingDeviceInfoDto result = null;
		meetingDeviceParam.setCheckDate(DateUtils.getNowDate());

		List<MeetingDetailDto> meetingDetailList = new ArrayList<>();
		String timeStatus = meetingDeviceParam.getTimeStatus();
		if (StringUtils.isNotEmpty(timeStatus) && timeStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING)){
			meetingDetailList = meetingMapper.getNowMeetingInfoList(meetingDeviceParam);
		}else if(StringUtils.isNotEmpty(timeStatus) && timeStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_WAITING)){
			meetingDetailList = meetingMapper.getNextMeetingInfoList(meetingDeviceParam);
		}

		if (CollectionUtil.isNotEmpty(meetingDetailList)){
			MeetingDetailDto meetingDetail = meetingDetailList.get(0);
			result = transObject(meetingDetail,MeetingDeviceInfoDto.class);
			// 查找当前会议的参与者
			MeetingAttendee selectAttendee = new MeetingAttendee();
			selectAttendee.setMeetingId(meetingDetail.getId());
			List<MeetingAttendee> meetingAttendeeList = attendeeService.selectMeetingAttendeeList(selectAttendee);
			result.setAttendeeAmount(meetingAttendeeList.size());
		}
		return result;
	}

	/**
	 * @return com.mkst.umap.app.admin.dto.meeting.MeetingDetailDto
	 * @Author wangyong
	 * @Description 获取会议的详细信息
	 * @Date 10:06 2020-08-04
	 * @Param 会议id
	 */
	@Override
	public MeetingDetailDto getDetailById(Long meetingId, String loginName) {

		// 获取会议详细信息
		MeetingDetailDto meetingDetail = meetingMapper.getDetailById(meetingId);
		// 容错处理
		if (meetingDetail == null) {
			return new MeetingDetailDto();
		}

		// 查找当前会议的参与者
		MeetingAttendee selectAttendee = new MeetingAttendee();
		selectAttendee.setMeetingId(meetingId);
		List<MeetingAttendee> meetingAttendeeList = attendeeService.selectMeetingAttendeeList(selectAttendee);

		// 将参与者写入结果
		ArrayList<String> attendeeArr = new ArrayList<>();
		meetingAttendeeList.stream().forEach(attendee -> attendeeArr.add(userService.selectUserById(attendee.getUserId()).getUserName()));
		meetingDetail.setAttendeeArr(attendeeArr);

		AuditProgress selectProgress = new AuditProgress();
		selectProgress.setBusinessId(meetingId);
		selectProgress.setBusinessType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
		List<AuditProgress> progressList = progressService.selectAuditProgressList(selectProgress);
		String canAudit = "0";
		if (CollectionUtil.isNotEmpty(progressList)){
			for (AuditProgress p : progressList){
				if (p.getProgress() == 0){
					continue;
				}
				if (BeanUtil.isEmpty(p.getRecordId())){
					if (StringUtils.equals(p.getCreateBy(),loginName)){
						canAudit = "1";
						break;
					}
				}
			}
		}
		Integer m = meetingDetail.getAuditProgress() == -1 ? MeetingAuditProgressEnum.values().length - 2 : meetingDetail.getAuditProgress() + 1;
		meetingDetail.setCanAudit(canAudit);
		meetingDetail.setAuditProgress(m);
		meetingDetail.setNextRole(MeetingAuditProgressEnum.getNextRoleCode(meetingDetail.getAuditProgress()));
		return meetingDetail;
	}

	public <T> T transObject(Object ob, Class<T> clazz) {
		String oldOb = JSON.toJSONString(ob);
		return JSON.parseObject(oldOb, clazz);
	}

	@Override
	public List<NameCountResult> selectDayCount(MeetingParam param) {
		param.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
		param.setAuditStatusArr(new String[]{KeyConstant.EVENT_AUDIT_STATUS_WAIT,KeyConstant.EVENT_AUDIT_STATUS_PASS});
		List<NameCountResult> result = meetingMapper.selectDayCount(param);
		result.stream().forEach(o -> o.setStatus(false));
		return result;
	}

	@Override
	public List<MeetingWebInfoVo> selectMeetingAuditList(Meeting selectMeeting) {
		return meetingMapper.selectMeetingAuditList(selectMeeting);
	}

	@Override
	public Map<String, List<TimeApplyResult>> selectTimeApplyList(MeetingParam param) {

		// 一个Key升序排序的treeMap
		Map<String, List<TimeApplyResult>> result = new TreeMap<>(Comparator.reverseOrder());
		ArrayList<TimeApplyResult> container = new ArrayList<>();
		HashMap<String, String> colorContainer = new HashMap<>();

		// 查出各种时间状态的申请
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
		container.addAll(meetingMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
		container.addAll(meetingMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
		container.addAll(meetingMapper.selectTimeApplyList(param));

		// 写入数据
		container.stream().forEach(timeApplyResult -> {
			if (!result.containsKey(timeApplyResult.getTimeCon())){
				List<TimeApplyResult> arrForTimeCon = new ArrayList<>();
				result.put(timeApplyResult.getTimeCon(),arrForTimeCon);
			}

			// 如果房间无初始数据，就初始一下
			if (!colorContainer.containsKey(timeApplyResult.getRoomName())){
				String color = new String();
				// 挑一个未被使用的颜色
				for (RoomColorEnum roomColorEnum : RoomColorEnum.values()) {
					if (colorContainer.containsValue(roomColorEnum.getValue())){
						continue;
					}
					color = roomColorEnum.getValue();
					break;
				}
				colorContainer.put(timeApplyResult.getRoomName(),color);
			}

			timeApplyResult.setRoomBackColor(colorContainer.get(timeApplyResult.getRoomName()));
			result.get(timeApplyResult.getTimeCon()).add(timeApplyResult);
		});

		return result;
	}

	@Override
	public List<MeetingAuditProgressInfoDto> selectAuditList(Long meetingId) {

		List<MeetingAuditResult> resultList = meetingMapper.selectAuditProgressList(meetingId);
		List<MeetingAuditProgressInfoDto> resultDtoList = transList(resultList, MeetingAuditProgressInfoDto.class);

		resultDtoList.stream().forEach(dto -> {
			dto.setRoleName(MeetingAuditProgressEnum.getRoleName(dto.getPProgress()));
			SysUser user = userService.selectUserByLoginName(dto.getPCreateBy());
			if (BeanUtil.isNotEmpty(user)){
				dto.setUserName(user.getUserName());
			}
			if (BeanUtil.isEmpty(dto.getRStatus())){
				dto.setRStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
			}
		});

		return resultDtoList;
	}

	public <T> List<T> transList(List<?> list, Class<T> clazz) {
		String oldOb = JSON.toJSONString(list);
		return JSON.parseArray(oldOb, clazz);
	}
}
