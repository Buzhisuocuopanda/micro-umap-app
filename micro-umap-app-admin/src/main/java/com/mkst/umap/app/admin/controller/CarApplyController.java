package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.workflow.domain.EventAuditRecord;
import com.mkst.umap.app.admin.domain.AuditRecord;
import com.mkst.umap.app.admin.domain.CarApply;
import com.mkst.umap.app.admin.dto.carApply.CarApplyInfoDto;
import com.mkst.umap.app.admin.service.IAuditRecordService;
import com.mkst.umap.app.admin.service.ICarApplyService;
import com.mkst.umap.app.common.enums.ApproveTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆申请管理 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Controller
@RequestMapping("/admin/carApply")
public class CarApplyController extends BaseController {
    private String prefix = "app/carApply";
	
	@Autowired
	private ICarApplyService carApplyService;
	@Autowired
	private IAuditRecordService auditRecordService;
	
	@RequiresPermissions("admin:carApply:view")
	@GetMapping()
	public String carApply()
	{
	    return prefix + "/carApply";
	}
	
	/**
	 * 查询车辆申请管理列表
	 */
	@RequiresPermissions("admin:carApply:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CarApply carApply)
	{
		startPage();
        //List<CarApply> list = carApplyService.selectCarApplyList(carApply);
		List<CarApplyInfoDto> list = carApplyService.selectCarApplyDtoList(carApply);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出车辆申请管理列表
	 */
	@RequiresPermissions("admin:carApply:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CarApply carApply)
    {
    	List<CarApply> list = carApplyService.selectCarApplyList(carApply);
        ExcelUtil<CarApply> util = new ExcelUtil(CarApply.class);
        return util.exportExcel(list, "carApply");
    }
	
	/**
	 * 新增车辆申请管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存车辆申请管理
	 */
	@RequiresPermissions("admin:carApply:add")
	@Log(title = "车辆申请管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CarApply carApply)
	{		
		return toAjax(carApplyService.insertCarApply(carApply));
	}

	/**
	 * 修改车辆申请管理
	 */
	@GetMapping("/edit/{carApplyId}")
	public String edit(@PathVariable("carApplyId") Long carApplyId, ModelMap mmap)
	{
		CarApply carApply = carApplyService.selectCarApplyById(carApplyId);
		mmap.put("carApply", carApply);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存车辆申请管理
	 */
	@RequiresPermissions("admin:carApply:edit")
	@Log(title = "车辆申请管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CarApply carApply)
	{		
		return toAjax(carApplyService.updateCarApply(carApply));
	}
	
	/**
	 * 删除车辆申请管理
	 */
	@RequiresPermissions("admin:carApply:remove")
	@Log(title = "车辆申请管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(carApplyService.deleteCarApplyByIds(ids));
	}


	/**
	 * 提交审核
	 */
	@PostMapping("/audit/{id}/{status}/{reason}")
	@RequiresPermissions("admin:carApply:audit")
	@Log(title = "车辆申请管理审核", businessType = BusinessType.UPDATE)
	@ResponseBody
	public AjaxResult doAudit(@PathVariable Long id, @PathVariable String status, @PathVariable String reason) {
		return toAjax(carApplyService.audit(id, status, reason));
	}


	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setApplyId(id);
		auditRecord.setApplyType(ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue());
		//List<AuditRecord> auditRecords = auditRecordService.selectAuditRecordList(auditRecord);
		List<EventAuditRecord> eventAuditRecords = carApplyService.selectAuditList(ApproveTypeEnum.APPROVE_CAR_EXTRA.getValue(),id);
		mmap.put("auditRecords", eventAuditRecords);
		return prefix + "/auditRecord";
	}
}
