package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 申请审核记录 信息操作处理
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Controller
@RequestMapping("/admin/auditRecord")
public class AuditRecordController extends BaseController {
    private String prefix = "app/arraign/auditRecord";
    private String applyPrefix = "app/apply/applyAuditRecord";
    private String specialCasePrefix = "app/specialcase/auditRecord";

    @Autowired
    private IAuditRecordService auditRecordService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("admin:auditRecord:view")
    @GetMapping()
    public String auditRecord() {
        return prefix + "/auditRecord";
    }

    @RequiresPermissions("admin:auditRecord:view")
    @GetMapping("/applyAuditRecord")
    public String applyAuditRecord() {
        return applyPrefix + "/applyAuditRecord";
    }

    /**
     * @return java.lang.String
     * @Author wangyong
     * @Description 专案提审的审批记录
     * @Date 20:32 2020-07-07
     * @Param []
     */
    @RequiresPermissions("admin:auditRecord:view")
    @GetMapping("/specialCaseAuditRecord")
    public String specialCaseAuditRecord() {
        return specialCasePrefix + "/auditRecord";
    }

    /**
     * 查询申请审核记录列表
     */
    @RequiresPermissions("admin:auditRecord:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditRecord auditRecord) {
        startPage();
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        return getDataTable(list);
    }

    /**
     * 查询备勤间审核记录列表
     */
    @RequiresPermissions("admin:auditRecord:list")
    @PostMapping("/backUpList")
    @ResponseBody
    public TableDataInfo backUpList(AuditRecord auditRecord) {
        auditRecord.setApplyType(AuditRecordTypeEnum.BackUpAudit.getValue());
        startPage();
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        return getDataTable(list);
    }

    /**
     * 查询专案审核记录列表
     */
    @RequiresPermissions("admin:auditRecord:list")
    @PostMapping("/specialCaseAuditList")
    @ResponseBody
    public TableDataInfo specialCaseAuditList(AuditRecord auditRecord) {
        auditRecord.setApplyType(AuditRecordTypeEnum.SpecialCaseAudit.getValue());
        startPage();
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        for (AuditRecord record : list) {
            record.setCreateBy(userService.selectUserByLoginName(record.getCreateBy()).getUserName());
        }
        return getDataTable(list);
    }


    /**
     * 导出申请审核记录列表
     */
    @RequiresPermissions("admin:auditRecord:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuditRecord auditRecord) {
        List<AuditRecord> list = auditRecordService.selectAuditRecordList(auditRecord);
        ExcelUtil<AuditRecord> util = new ExcelUtil<AuditRecord>(AuditRecord.class);
        return util.exportExcel(list, "auditRecord");
    }

    /**
     * 新增申请审核记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存申请审核记录
     */
    @RequiresPermissions("admin:auditRecord:add")
    @Log(title = "申请审核记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuditRecord auditRecord) {
        return toAjax(auditRecordService.insertAuditRecord(auditRecord));
    }

    /**
     * 修改申请审核记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        AuditRecord auditRecord = auditRecordService.selectAuditRecordById(id);
        mmap.put("auditRecord", auditRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存申请审核记录
     */
    @RequiresPermissions("admin:auditRecord:edit")
    @Log(title = "申请审核记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditRecord auditRecord) {
        return toAjax(auditRecordService.updateAuditRecord(auditRecord));
    }

    /**
     * 删除申请审核记录
     */
    @RequiresPermissions("admin:auditRecord:remove")
    @Log(title = "申请审核记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditRecordService.deleteAuditRecordByIds(ids));
    }
}
