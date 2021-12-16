package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.SpecialCaseAppointment;
import com.mkst.umap.app.admin.domain.vo.SpecialCaseAppointmentVo;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.ISpecialCaseAppointmentService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.AuditRecordTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 专案预约 信息操作处理
 *
 * @author wangyong
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/admin/specialCaseAppointment")
public class SpecialCaseAppointmentController extends BaseController {
	private String prefix = "app/specialcase/appointment";
	private String auditPrefix = "app/specialcase/audit";

	@Autowired
	private ISpecialCaseAppointmentService specialCaseAppointmentService;
	@Autowired
	private IAuditRecordService auditRecordService;

	@RequiresPermissions("admin:specialCaseAppointment:view")
	@GetMapping()
	public String specialCaseAppointment() {
		return prefix + "/specialCaseAppointment";
	}

	/**
	 * 查询专案预约列表
	 */
	@RequiresPermissions("admin:specialCaseAppointment:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SpecialCaseAppointment specialCaseAppointment) {
		startPage();
		List<SpecialCaseAppointmentVo> list = specialCaseAppointmentService.selectSpecialCaseAppointmentWebList(specialCaseAppointment);
		return getDataTable(list);
	}


	/**
	 * 导出专案预约列表
	 */
	@RequiresPermissions("admin:specialCaseAppointment:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SpecialCaseAppointment specialCaseAppointment) {
		List<SpecialCaseAppointment> list = specialCaseAppointmentService.selectSpecialCaseAppointmentList(specialCaseAppointment);
		ExcelUtil<SpecialCaseAppointment> util = new ExcelUtil<SpecialCaseAppointment>(SpecialCaseAppointment.class);
		return util.exportExcel(list, "specialCaseAppointment");
	}

	/**
	 * 新增专案预约
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存专案预约
	 */
	@RequiresPermissions("admin:specialCaseAppointment:add")
	@Log(title = "专案预约", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SpecialCaseAppointment specialCaseAppointment) {
		return toAjax(specialCaseAppointmentService.insertSpecialCaseAppointment(specialCaseAppointment));
	}

	/**
	 * 修改专案预约
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		SpecialCaseAppointment specialCaseAppointment = specialCaseAppointmentService.selectSpecialCaseAppointmentById(id);
		mmap.put("specialCaseAppointment", specialCaseAppointment);
		return prefix + "/edit";
	}

	/**
	 * 修改保存专案预约
	 */
	@RequiresPermissions("admin:specialCaseAppointment:edit")
	@Log(title = "专案预约", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SpecialCaseAppointment specialCaseAppointment) {
		return toAjax(specialCaseAppointmentService.updateSpecialCaseAppointment(specialCaseAppointment));
	}

	/**
	 * 删除专案预约
	 */
	@RequiresPermissions("admin:specialCaseAppointment:remove")
	@Log(title = "专案预约", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(specialCaseAppointmentService.deleteSpecialCaseAppointmentByIds(ids));
	}

	@RequiresPermissions("admin:arraignAppointment:view")
	@GetMapping("/audit")
	public String audit() {
		return auditPrefix + "/audit";
	}

	/**
	 * 查询提审预约审核列表
	 */
	@RequiresPermissions("admin:arraignAppointment:list")
	@PostMapping("/auditList")
	@ResponseBody
	public TableDataInfo auditList(SpecialCaseAppointment specialCaseAppointment) {
		startPage();
		specialCaseAppointment.setStatus(KeyConstant.EVENT_AUDIT_STATUS_WAIT);
		List<SpecialCaseAppointmentVo> list = specialCaseAppointmentService.selectCaseWebAuditList(specialCaseAppointment);
		return getDataTable(list);
	}

	/**
	 * 提交审核
	 */
	@PostMapping(value = "/doAudit/{id}/{status}/{reason}")
	@RequiresPermissions("admin:arraignRoom:audit")
	@Log(title = "专案预约审核", businessType = BusinessType.UPDATE)
	@ResponseBody
	public AjaxResult doAudit(@PathVariable Long id, @PathVariable String status, @PathVariable String reason) {
		return toAjax(specialCaseAppointmentService.audit(id, status, reason));
	}


	/**
	 * @return
	 * @Author wangyong
	 * @Description shoe detail
	 * @Date 18:06 2020-06-30
	 * @Param
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(id);
		auditRecord.setApplyType(AuditRecordTypeEnum.SpecialCaseAudit.getValue());
		List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordList(auditRecord);
		mmap.put("auditRecords", auditRecords);
		return auditPrefix + "/auditRecord";
	}

}
