package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 申请审核记录 信息操作处理
 *
 * @author wangyong
 * @date 2020-06-11
 */
@Controller
@RequestMapping("/admin/auditArraignRecord")
public class ArraignAuditRecordController extends BaseController {

    private String prefix = "app/arraign/auditRecord";

    @Autowired
    private IAuditRecordService auditRecordService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("admin:auditArraignRecord:view")
    @GetMapping()
    public String auditRecord() {
        return prefix + "/auditRecord";
    }

    /**
     * 查询申请审核记录列表
     */
    @RequiresPermissions("admin:auditArraignRecord:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditRecord auditRecord) {
        startPage();
        auditRecord.setApplyType(AuditRecordTypeEnum.ArraignAudit.getValue());
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        for (AuditRecord record : list) {
            record.setCreateBy(userService.selectUserByLoginName(record.getCreateBy()).getUserName());
        }
        return getDataTable(list);
    }

    /**
     * 导出申请审核记录列表
     */
    @RequiresPermissions("admin:auditArraignRecord:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuditRecord auditRecord) {
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        ExcelUtil<AuditRecord> util = new ExcelUtil<AuditRecord>(AuditRecord.class);
        return util.exportExcel(list, "auditRecord");
    }
}
