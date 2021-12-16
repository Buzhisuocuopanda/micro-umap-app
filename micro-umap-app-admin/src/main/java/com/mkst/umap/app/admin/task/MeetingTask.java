package com.mkst.umap.app.admin.task;

import com.mkst.mini.systemplus.common.util.RedisUtils;
import com.mkst.mini.systemplus.sms.yixunt.config.YxtSmsConfig;
import com.mkst.mini.systemplus.sms.yixunt.exception.YxtSmsErrorException;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.Meeting;
import com.mkst.umap.app.admin.service.IMeetingService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.SmsNoticeMsgConstant;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.base.core.util.UmapDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MeetingTask
 * @Description 会议相关的定时任务
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-11 11:12
 */
@Component("MeetingTask")
public class MeetingTask {

    @Autowired
    private IMeetingService meetingService;
    @Autowired
    private ISysUserService userService;

    public void checkMeetingStatus() {

        ArrayList<Meeting> noticeMeetings = new ArrayList<>();

        // 查询未取消且未审核的会议
        Meeting selectMeeting = new Meeting();
        selectMeeting.setAuditStatus(AuditStatusEnum.EVENT_AUDIT_STATUS_WAIT.getValue().toString());
        selectMeeting.setStatus(KeyConstant.EVENT_IS_CANCEL_FALSE);
        List<Meeting> meetings = meetingService.selectMeetingList(selectMeeting);

        meetings.stream().forEach(meeting -> {
            boolean inDuration = UmapDateUtils.happenInDuration(meeting.getStartTime(), 60 * 60 * 1000L);
            String redisKey = "meeting:notice:overdue:" + meeting.getId();

            if (inDuration && !RedisUtils.exists(redisKey)) {
                RedisUtils.setInt(redisKey, 1, 60 * 60L);
                noticeMeetings.add(meeting);
            }
        });

        noticeMeetings.stream().forEach(meeting -> {

            SysUser useBy = userService.selectUserById(meeting.getUseBy());
            String msg = new String(SmsNoticeMsgConstant.MEETING_OVERDUE_SMS_TO_MANAGER)
                    .replace("${useByName}", useBy.getUserName())
                    .replace("${subject}", meeting.getSubject());

            try {
                YxtSmsConfig.getYxtSmsService().sendMsg(useBy.getPhonenumber(), msg);
            } catch (YxtSmsErrorException e) {
                e.printStackTrace();
            }
        });

    }
}
