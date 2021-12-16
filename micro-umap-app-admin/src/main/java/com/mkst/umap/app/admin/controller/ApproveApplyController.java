package com.mkst.umap.app.admin.controller;

import java.util.List;

import com.mkst.mini.systemplus.common.base.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.admin.domain.ApproveApply;
import com.mkst.umap.app.admin.service.IApproveApplyService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 审核申请 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-21
 */
@Controller
@RequestMapping("/admin/approveApply")
public class ApproveApplyController extends BaseController
{
    private String prefix = "admin/approveApply";
	
	@Autowired
	private IApproveApplyService approveApplyService;
	
	@RequiresPermissions("admin:approveApply:view")
	@GetMapping()
	public String approveApply()
	{
	    return prefix + "/approveApply";
	}
	
	/**
	 * 查询审核申请列表
	 */
	@RequiresPermissions("admin:approveApply:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ApproveApply approveApply)
	{
		startPage();
        List<ApproveApply> list = approveApplyService.selectApproveApplyList(approveApply);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出审核申请列表
	 */
	@RequiresPermissions("admin:approveApply:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ApproveApply approveApply)
    {
    	List<ApproveApply> list = approveApplyService.selectApproveApplyList(approveApply);
        ExcelUtil<ApproveApply> util = new ExcelUtil<ApproveApply>(ApproveApply.class);
        return util.exportExcel(list, "approveApply");
    }
	
	/**
	 * 新增审核申请
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存审核申请
	 */
	@RequiresPermissions("admin:approveApply:add")
	@Log(title = "审核申请", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ApproveApply approveApply)
	{		
		return toAjax(approveApplyService.insertApproveApply(approveApply));
	}

	/**
	 * 修改审核申请
	 */
	@GetMapping("/edit/{approveId}")
	public String edit(@PathVariable("approveId") Long approveId, ModelMap mmap)
	{
		ApproveApply approveApply = approveApplyService.selectApproveApplyById(approveId);
		mmap.put("approveApply", approveApply);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存审核申请
	 */
	@RequiresPermissions("admin:approveApply:edit")
	@Log(title = "审核申请", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ApproveApply approveApply)
	{		
		return toAjax(approveApplyService.updateApproveApply(approveApply));
	}
	
	/**
	 * 删除审核申请
	 */
	@RequiresPermissions("admin:approveApply:remove")
	@Log(title = "审核申请", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(approveApplyService.deleteApproveApplyByIds(ids));
	}
	
}
