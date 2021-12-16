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
import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.service.IVisitApplyService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 拜访申请 信息操作处理
 * 
 * @author wangyong
 * @date 2020-08-13
 */
@Controller
@RequestMapping("/admin/visitApply")
public class VisitApplyController extends BaseController
{
    private String prefix = "admin/visitApply";
	
	@Autowired
	private IVisitApplyService visitApplyService;
	
	@RequiresPermissions("admin:visitApply:view")
	@GetMapping()
	public String visitApply()
	{
	    return prefix + "/visitApply";
	}
	
	/**
	 * 查询拜访申请列表
	 */
	@RequiresPermissions("admin:visitApply:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(VisitApply visitApply)
	{
		startPage();
        List<VisitApply> list = visitApplyService.selectVisitApplyList(visitApply);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出拜访申请列表
	 */
	@RequiresPermissions("admin:visitApply:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(VisitApply visitApply)
    {
    	List<VisitApply> list = visitApplyService.selectVisitApplyList(visitApply);
        ExcelUtil<VisitApply> util = new ExcelUtil<VisitApply>(VisitApply.class);
        return util.exportExcel(list, "visitApply");
    }
	
	/**
	 * 新增拜访申请
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存拜访申请
	 */
	@RequiresPermissions("admin:visitApply:add")
	@Log(title = "拜访申请", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(VisitApply visitApply)
	{		
		return toAjax(visitApplyService.insertVisitApply(visitApply));
	}

	/**
	 * 修改拜访申请
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		VisitApply visitApply = visitApplyService.selectVisitApplyById(id);
		mmap.put("visitApply", visitApply);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存拜访申请
	 */
	@RequiresPermissions("admin:visitApply:edit")
	@Log(title = "拜访申请", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(VisitApply visitApply)
	{		
		return toAjax(visitApplyService.updateVisitApply(visitApply));
	}
	
	/**
	 * 删除拜访申请
	 */
	@RequiresPermissions("admin:visitApply:remove")
	@Log(title = "拜访申请", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(visitApplyService.deleteVisitApplyByIds(ids));
	}
	
}
