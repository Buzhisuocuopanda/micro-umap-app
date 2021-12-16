package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.umap.app.admin.api.bean.param.meeting.MeetingParam;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.Meeting;
import com.mkst.umap.app.admin.domain.vo.meeting.MeetingWebInfoVo;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.IMeetingAttendeeService;
import com.mkst.umap.app.admin.service.IMeetingService;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会议 信息操作处理
 *
 * @author wangyong
 * @date 2020-07-31
 */
@Controller
@RequestMapping("/admin/meeting")
public class MeetingController extends BaseController {
    private String prefix = "app/meeting";

    @Autowired
    private IMeetingService meetingService;
    @Autowired
    private IMeetingAttendeeService attendeeService;
    @Autowired
    private IAuditRecordService auditRecordService;

    @RequiresPermissions("admin:meeting:view")
    @GetMapping()
    public String meeting() {
        return prefix + "/meeting";
    }

    /**
     * 查询会议列表
     */
    @RequiresPermissions("admin:meeting:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Meeting meeting) {
        startPage();
        List<MeetingWebInfoVo> list = meetingService.selectMeetingWebList(meeting);
        return getDataTable(list);
    }

    @RequiresPermissions("admin:meeting:edit")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap map) {

        // 结果的主体信息
        Meeting selectMeeting = new Meeting();
        selectMeeting.setId(id);
        MeetingWebInfoVo meetingWebInfoVo = meetingService.selectMeetingWebList(selectMeeting).get(0);

        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setApplyId(id);
        auditRecord.setApplyType(AuditRecordTypeEnum.MeetingRoomAudit.getValue());
        List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordList(auditRecord);

        String isOverdue = meetingWebInfoVo.getStartTime().getTime() < System.currentTimeMillis() ? "1" : "0";

        map.put("auditRecords", auditRecords);
        map.put("meeting", meetingWebInfoVo);
        map.put("overdue", isOverdue);
        return prefix + "/detail";

    }

    @PostMapping("/listAttendee/{meetingId}")
    @ResponseBody
    public TableDataInfo listAttendee(@PathVariable("meetingId") Long meetingId) {
        this.startPage();
        List<SysUser> attendeeList = attendeeService.selectAttendeeListByMeetingId(meetingId);
        return getDataTable(attendeeList);
    }

    @PostMapping("/audit/{id}/{auditStatus}/{reason}")
    @RequiresPermissions("admin:meeting:edit")
    @ResponseBody
    @Log(title = "会议审核", businessType = BusinessType.UPDATE)
    public AjaxResult audit(@PathVariable("id") Long id, @PathVariable("auditStatus") String auditStatus, @PathVariable("reason") String reason) {
        MeetingParam param = new MeetingParam();
        param.setTarget(getUserId()+"");
        return toAjax(meetingService.auditMeeting(id, auditStatus, reason, ShiroUtils.getLoginName(), param));
    }

    /**
     * 删除会议
     */
    @RequiresPermissions("admin:meeting:remove")
    @Log(title = "删除会议", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(meetingService.deleteMeetingByIds(ids));
    }

}
