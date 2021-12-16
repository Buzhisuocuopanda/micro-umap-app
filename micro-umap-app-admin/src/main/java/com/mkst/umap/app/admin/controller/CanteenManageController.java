package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.CanteenManage;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.ICanteenManageService;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * 食堂申请 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Controller
@RequestMapping("/admin/canteenManage")
public class CanteenManageController extends BaseController
{
    private String prefix = "app/canteenManage";
	
	@Autowired
	private ICanteenManageService canteenManageService;
	@Autowired
	private IAuditRecordService auditRecordService;
	
	@RequiresPermissions("admin:canteenManage:view")
	@GetMapping()
	public String canteenManage()
	{
	    return prefix + "/canteenManage";
	}
	
	/**
	 * 查询食堂申请列表
	 */
	@RequiresPermissions("admin:canteenManage:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CanteenManage canteenManage)
	{
		startPage();
        List<CanteenManage> list = canteenManageService.selectCanteenManageList(canteenManage);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出食堂申请列表
	 */
	@RequiresPermissions("admin:canteenManage:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CanteenManage canteenManage)
    {
    	List<CanteenManage> list = canteenManageService.selectCanteenManageList(canteenManage);
        ExcelUtil<CanteenManage> util = new ExcelUtil<CanteenManage>(CanteenManage.class);
        return util.exportExcel(list, "canteenManage");
    }
	
	/**
	 * 新增食堂申请
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存食堂申请
	 */
	@RequiresPermissions("admin:canteenManage:add")
	@Log(title = "食堂申请", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CanteenManage canteenManage)
	{		
		return toAjax(canteenManageService.insertCanteenManage(canteenManage));
	}

	/**
	 * 修改食堂申请
	 */
	@GetMapping("/edit/{canteenApplyId}")
	public String edit(@PathVariable("canteenApplyId") Long canteenApplyId, ModelMap mmap)
	{
		CanteenManage canteenManage = canteenManageService.selectCanteenManageById(canteenApplyId);
		mmap.put("canteenManage", canteenManage);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存食堂申请
	 */
	@RequiresPermissions("admin:canteenManage:edit")
	@Log(title = "食堂申请", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CanteenManage canteenManage)
	{		
		return toAjax(canteenManageService.updateCanteenManage(canteenManage));
	}
	
	/**
	 * 删除食堂申请
	 */
	@RequiresPermissions("admin:canteenManage:remove")
	@Log(title = "食堂申请", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(canteenManageService.deleteCanteenManageByIds(ids));
	}


	/**
	 * 提交审核
	 */
	@PostMapping("/audit/{id}/{status}")
	@RequiresPermissions("admin:carApply:audit")
	@Log(title = "车辆申请管理审核", businessType = BusinessType.UPDATE)
	@ResponseBody
	public AjaxResult doAudit(@PathVariable Long id, @PathVariable String status) {
		String msg = canteenManageService.audit(id, status);
		if(msg.contains("失败")) {
            return error(msg);
        } else {
            return success(msg);
        }
	}


	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(id);
		auditRecord.setApplyType(AuditRecordTypeEnum.CanteenLogisticsAudit.getValue());
		List<AuditRecord> lList = auditRecordService.selectAuditRecordList(auditRecord);
		auditRecord.setApplyType(AuditRecordTypeEnum.CanteenKitchenAudit.getValue());
		List<AuditRecord> kList = auditRecordService.selectAuditRecordList(auditRecord);
		List<AuditRecord> auditRecords = new LinkedList<>();
		auditRecords.addAll(lList);
		auditRecords.addAll(kList);
		mmap.put("auditRecords", auditRecords);
		return prefix + "/auditRecord";
	}
}
