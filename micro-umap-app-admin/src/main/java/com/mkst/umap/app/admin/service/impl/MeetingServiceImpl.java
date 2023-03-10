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
 * ?????? ???????????????
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
     * ??????????????????
     *
     * @param id ??????ID
     * @return ????????????
     */
    @Override
    public Meeting selectMeetingById(Long id) {
        return meetingMapper.selectMeetingById(id);
	}

	/**
	 * ??????????????????
	 *
	 * @param meeting ????????????
	 * @return ????????????
	 */
	@Override
	public List<Meeting> selectMeetingList(Meeting meeting) {
		return meetingMapper.selectMeetingList(meeting);
	}

	/**
	 * @return java.util.List<com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo>
	 * @Author wangyong
	 * @Description ??????Web??????List
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
	 * @Description ????????????
	 * @Date 15:56 2020-08-06
	 * @Param [id, auditStatus, reason]
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int auditMeeting(Long id, String auditStatus, String reason, String updateBy, MeetingParam meetingParam) {

		Meeting meeting = this.selectMeetingById(id);
		// ??????????????????
		if (meeting.getStatus().equals(KeyConstant.EVENT_IS_CANCEL_TRUE) || !meeting.getAuditStatus().equals(KeyConstant.EVENT_AUDIT_STATUS_WAIT)) {
			return 0;
		}

		// ??????????????????
		AuditRecord auditRecord = new AuditRecord(id, AuditRecordTypeEnum.MeetingRoomAudit.getValue(), auditStatus, "");
		auditRecord.setCreateBy(updateBy);
		auditRecord.setUpdateBy(updateBy);
		// ????????????????????????????????????reason????????????NoReasonFlag??????
		if (!reason.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			auditRecord.setReason(reason);
		}
		auditRecordMapper.insertAuditRecord(auditRecord);

		// ??????
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
			// ?????????????????????????????????
			// ??????????????????????????????
			AuditProgress updateProgress = new AuditProgress();
			updateProgress.setId(progress.getId() - 1);
			updateProgress.setTarget(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			updateProgress.setRecordId(auditRecord.getId());
			// updateProgress.setCreateBy(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			progressService.updateAuditProgress(updateProgress);
			if (KeyConstant.EVENT_AUDIT_STATUS_PASS.equals(meetingParam.getAuditStatus())){
				// ????????????????????????
				new Thread(() -> {
					// ??????????????????
					sendAppMsg(auditProgress.getTarget(), id);
				}).start();
			}else {
				nowProgressNum = MeetingAuditProgressEnum.FINISHED.getProgressCode();
				meeting.setAuditStatus(meetingParam.getAuditStatus());
				// ????????????????????????
				new Thread(() -> {
					// ??????????????????
					sendAuditSmsNotice(id, auditStatus);
				}).start();
			}
		}else {
			auditProgress.setProgress(nowProgressNum);
			List<AuditProgress> progresses = progressService.selectAuditProgressList(auditProgress);
			if (CollectionUtil.isEmpty(progresses)){
				throw new NullPointerException("???????????????????????????????????????"+ nowProgressNum);
			}
			AuditProgress progress = progresses.get(0);
			progress.setRecordId(auditRecord.getId());
			// progress.setCreateBy(userService.selectUserById(Long.valueOf(meetingParam.getTarget())).getLoginName());
			progressService.updateAuditProgress(progress);
			nowProgressNum = -1;
			// ?????????????????????
			meeting.setAuditStatus(auditStatus);
			// ????????????????????????
			new Thread(() -> {
				// ??????????????????
				sendAuditSmsNotice(id, auditStatus);
			}).start();
		}
		meeting.setAuditProgress(nowProgressNum);
		return this.updateMeeting(meeting);
	}

	private void sendAuditSmsNotice(Long meetingId, String auditStatus) {

		// ????????????????????????
		Meeting selectMeeting = new Meeting();
		selectMeeting.setId(meetingId);
		MeetingWebInfoVo meetingInfoVo = this.selectMeetingWebList(selectMeeting).get(0);

		// ????????????????????????
		Meeting meeting = this.selectMeetingById(meetingId);
		SysUser useByInfo = userService.selectUserById(meeting.getUseBy());
		SysUser createByInfo = userService.selectUserByLoginName(meeting.getCreateBy());

		// ????????????????????????sms
		StringBuffer smsMeetingInfo = new StringBuffer(SmsNoticeMsgConstant.MEETING_INFO);
		replaceStringBuffer(smsMeetingInfo, "${dept}", meetingInfoVo.getDept());
		replaceStringBuffer(smsMeetingInfo, "${subject}", meetingInfoVo.getSubject());
		/*replaceStringBuffer(smsMeetingInfo, "${meetingId}", meetingInfoVo.getId().toString());*/
		replaceStringBuffer(smsMeetingInfo, "${dd}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD, meetingInfoVo.getStartTime()));
		replaceStringBuffer(smsMeetingInfo, "${time}", UmapDateUtils.combine2Str(meetingInfoVo.getStartTime(), meetingInfoVo.getEndTime()));
		replaceStringBuffer(smsMeetingInfo, "${roomAddr}", meetingInfoVo.getRoomAddr());

		String useByName = meetingInfoVo.getUseBy();
		String createByName = meetingInfoVo.getCreateBy();

		// ?????????????????????
		StringBuffer smsToUseBy;
		StringBuffer smsToCreateBy;

		// ????????????
		if ("1".equals(auditStatus)) {
			smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_USE_BY);
			smsToCreateBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_CREATE_BY);
		} else if ("2".equals(auditStatus)) {
			smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_FAIL_SMS_TO_USE_BY);
			smsToCreateBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_FAIL_SMS_TO_CREATE_BY);
		} else {
			return;
		}

		// ??????????????????
		replaceStringBuffer(smsToUseBy, "${useByName}", useByName);
		replaceStringBuffer(smsToUseBy, "${createByName}", createByName);

		replaceStringBuffer(smsToCreateBy, "${useByName}", useByName);
		replaceStringBuffer(smsToCreateBy, "${createByName}", createByName);

		// ???????????????????????????
		try {
			YxtSmsConfig.getYxtSmsService().sendMsg(useByInfo.getPhonenumber(), smsToUseBy.append(smsMeetingInfo).toString());
			// ???????????????????????????????????????
			if (!useByInfo.getLoginName().equals(createByInfo.getLoginName())){
				YxtSmsConfig.getYxtSmsService().sendMsg(createByInfo.getPhonenumber(), smsToCreateBy.append(smsMeetingInfo).toString());
			}
		} catch (YxtSmsErrorException e) {
			e.printStackTrace();
		}

		// ?????????????????????????????????
		if ("1".equals(auditStatus)) {
			StringBuffer smsToAttendee = new StringBuffer(SmsNoticeMsgConstant.MEETING_AUDIT_PASS_SMS_TO_ATTENDEE);
			replaceStringBuffer(smsToAttendee, "${deptName}", createByInfo.getDept().getDeptName());
			replaceStringBuffer(smsToAttendee, "${useByName}", useByName);

			List<SysUser> attendeeInfoList = attendeeService.selectAttendeeListByMeetingId(meetingId);
			attendeeInfoList.stream().forEach(attendee -> {
				// ?????? ?????????????????????
				if (useByInfo.getLoginName().equals(attendee.getLoginName())){
					return;
				}
				// ???????????????????????????
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
		msgContent.setTitle("????????????");
		msgContent.setContent("????????????????????????????????????????????????");

		Map<String, String> params = new HashMap<>();
		params.put("bizKey", meetingId.toString());
		params.put("bizType", BusinessTypeEnum.UMAP_MEETING.getValue());
		msgContent.setParams(params);
		MsgPushUtils.push(msgContent, meetingId.toString(), BusinessTypeEnum.UMAP_MEETING.getValue(), target);
		MsgPushUtils.getMsgPushTask().execute();
	}
	/**
	 * @return ??????????????????
	 * @Author wangyong
	 * @Description ???????????????????????????????????????????????????????????????????????????????????????????????????
	 * @Date 9:54 2020-08-04
	 * @Param [meetingId]
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int cancelMeeting(Long meetingId, String remark) {
		Meeting updateMeeting = new Meeting();
		updateMeeting.setId(meetingId);
		// ????????????????????????
		new Thread(() -> {
			// ??????????????????
			sendCancelSmsNotice(meetingId,remark);
		}).start();
		// ?????????????????????
		updateMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_TRUE);
		return meetingMapper.updateMeeting(updateMeeting);
	}

	private void sendCancelSmsNotice(Long meetingId, String remark) {
		// ????????????????????????
		Meeting selectMeeting = new Meeting();
		selectMeeting.setId(meetingId);
		MeetingWebInfoVo meetingInfoVo = this.selectMeetingWebList(selectMeeting).get(0);

		// ????????????????????????
		Meeting meeting = this.selectMeetingById(meetingId);
		SysUser useByInfo = userService.selectUserById(meeting.getUseBy());

		// ????????????????????????sms
		StringBuffer smsMeetingInfo = new StringBuffer(SmsNoticeMsgConstant.MEETING_INFO);
		replaceStringBuffer(smsMeetingInfo, "${dept}", meetingInfoVo.getDept());
		replaceStringBuffer(smsMeetingInfo, "${subject}", meetingInfoVo.getSubject());
		//replaceStringBuffer(smsMeetingInfo, "${meetingId}", meetingInfoVo.getId().toString());
		replaceStringBuffer(smsMeetingInfo, "${dd}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD, meetingInfoVo.getStartTime()));
		replaceStringBuffer(smsMeetingInfo, "${time}", UmapDateUtils.combine2Str(meetingInfoVo.getStartTime(), meetingInfoVo.getEndTime()));
		replaceStringBuffer(smsMeetingInfo, "${roomAddr}", meetingInfoVo.getRoomAddr());

		String useByName = meetingInfoVo.getUseBy();

		// ?????????????????????
		StringBuffer smsToUseBy = new StringBuffer(SmsNoticeMsgConstant.MEETING_CANCEL_SMS_TO_USE_BY);
		StringBuffer smsToAttendee = new StringBuffer(SmsNoticeMsgConstant.MEETING_CANCEL_SMS_TO_ATTENDEE);

		replaceStringBuffer(smsToUseBy, "${useByName}", useByName);
		try {
			YxtSmsConfig.getYxtSmsService().sendMsg(useByInfo.getPhonenumber(), smsToUseBy.append(smsMeetingInfo).toString());
		} catch (YxtSmsErrorException e) {
			e.printStackTrace();
		}


		//???????????????????????????????????????????????????
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
	 * ????????????
	 *
	 * @param meeting ????????????
	 * @return ??????
	 */
	@Override
	public int insertMeeting(Meeting meeting) {
		return meetingMapper.insertMeeting(meeting);
	}

	/**
	 * ????????????
	 *
	 * @param meeting ????????????
	 * @return ??????
	 */
	@Override
	public int updateMeeting(Meeting meeting) {
		return meetingMapper.updateMeeting(meeting);
	}

	/**
	 * ??????????????????
	 *
	 * @param ids ?????????????????????ID
	 * @return ??????
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
				hereNotice = hereNotice + "?????????"+remark;
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

		// ?????????????????????????????????
		String meetingChangeNotice = StringUtils.replace(noticeSms, "${subject}", thisMeeting.getSubject());
		// ?????????????????????????????????
		meetingChangeNotice = StringUtils.replace(meetingChangeNotice, "${startTime}", UmapDateUtils.parseDateToStr(UmapDateUtils.YYYY_MM_DD_HH_MM, nowStartTime));

		// ???????????????
		ArraignRoom room = roomMapper.selectArraignRoomById(nowRoomId);
		if (BeanUtil.isNotEmpty(room)){
			meetingChangeNotice = StringUtils.replace(meetingChangeNotice,"${roomAddr}",room.getAddress());
		}

		final String notice = new String(meetingChangeNotice);
		List<SysUser> attendeeInfoList = attendeeService.selectAttendeeListByMeetingId(meeting.getId());
		attendeeInfoList.stream().forEach(attendee -> {
			String hereNotice =	StringUtils.replace(new String(notice),"${attendeeName}", attendee.getUserName());
			if (StringUtils.isNotEmpty(meeting.getRemark())){
				hereNotice = hereNotice + "?????????"+meeting.getRemark();
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
	 * @Description ????????????
	 * @Date 10:25 2020-08-04
	 * @Param [roomScheduleParam]
	 */
	@Override
	public List<MeetingRoomScheduleDto> getRoomSchedule(RoomScheduleParam roomScheduleParam) {

		ArrayList<MeetingRoomScheduleDto> resultList = new ArrayList<>();

		// ????????????
		ArraignRoom selectMeetingRoom = new ArraignRoom();
		// ?????????+??????
		selectMeetingRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());
		selectMeetingRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		List<ArraignRoom> arraignRooms = roomMapper.selectArraignRoomList(selectMeetingRoom);
		// ????????????
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
	 * @Description ???????????????????????????????????????
	 * @Date 10:12 2020-08-04
	 * @Param [meetingParam]
	 */
	@Override
	public List<MeetingRoomInfoDto> getRoomInfoList(MeetingParam meetingParam) {

		// ??????????????????
		List<MeetingRoomInfoDto> resultList = new ArrayList<>();

		// ?????????????????????
		ArraignRoom selectRoom = new ArraignRoom();
		// ????????????????????????????????????????????????
		selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		selectRoom.setType(RoomTypeEnum.MEETING_ROOM.getValue());

		List<ArraignRoom> meetingRooms = roomMapper.selectArraignRoomList(selectRoom);

		// ??????
		if (meetingRooms == null || meetingRooms.size() == 0) {
			return new ArrayList<>();
		}

		for (ArraignRoom meetingRoom : meetingRooms) {
			// ????????????
			MeetingRoomInfoDto meetingRoomInfoDto = transObject(meetingRoom, MeetingRoomInfoDto.class);

			Meeting selectMeeting = new Meeting();
			// ???????????????????????????
			selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
			selectMeeting.setAuditStatus(String.valueOf(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue()));
			selectMeeting.setCheckDate(meetingParam.getCheckDate());
			selectMeeting.setRoomId(meetingRoom.getId());

			List<MeetingInfoResult> meetingInfoResults = meetingMapper.selectMeetingInfoList(selectMeeting);

			// ??????
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
	 * @Description ????????????????????????
	 * @Date 10:09 2020-08-04
	 * @Param [meetingParam]
	 */
	@Override
	public List<MeetingInfoResult> selectMyMeetingList(MeetingParam meetingParam) {

		// ?????????
		List<MeetingInfoResult> results = new ArrayList<>();


		// useBy
		Meeting selectMeeting = transObject(meetingParam, Meeting.class);
		String checkNowStatus = meetingParam.getNowStatus();
		if (checkNowStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_WAITING) || checkNowStatus.equals(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING)) {
			selectMeeting.setAuditStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString());
			selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
		}

		List<MeetingInfoResult> meetingInfoResults = meetingMapper.selectMeetingInfoList(selectMeeting);

		// ??????????????????
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
			// ??????????????????????????????
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
	 * @Description ???????????????????????????
	 * @Date 10:06 2020-08-04
	 * @Param ??????id
	 */
	@Override
	public MeetingDetailDto getDetailById(Long meetingId, String loginName) {

		// ????????????????????????
		MeetingDetailDto meetingDetail = meetingMapper.getDetailById(meetingId);
		// ????????????
		if (meetingDetail == null) {
			return new MeetingDetailDto();
		}

		// ??????????????????????????????
		MeetingAttendee selectAttendee = new MeetingAttendee();
		selectAttendee.setMeetingId(meetingId);
		List<MeetingAttendee> meetingAttendeeList = attendeeService.selectMeetingAttendeeList(selectAttendee);

		// ????????????????????????
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

		// ??????Key???????????????treeMap
		Map<String, List<TimeApplyResult>> result = new TreeMap<>(Comparator.reverseOrder());
		ArrayList<TimeApplyResult> container = new ArrayList<>();
		HashMap<String, String> colorContainer = new HashMap<>();

		// ?????????????????????????????????
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_FINISHED);
		container.addAll(meetingMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_ONGOING);
		container.addAll(meetingMapper.selectTimeApplyList(param));
		param.setNowStatus(KeyConstant.EVENT_PROGRESS_STATUS_WAITING);
		container.addAll(meetingMapper.selectTimeApplyList(param));

		// ????????????
		container.stream().forEach(timeApplyResult -> {
			if (!result.containsKey(timeApplyResult.getTimeCon())){
				List<TimeApplyResult> arrForTimeCon = new ArrayList<>();
				result.put(timeApplyResult.getTimeCon(),arrForTimeCon);
			}

			// ?????????????????????????????????????????????
			if (!colorContainer.containsKey(timeApplyResult.getRoomName())){
				String color = new String();
				// ??????????????????????????????
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
