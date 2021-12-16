package com.mkst.umap.app.admin.controller;

import java.util.List;
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
import com.mkst.umap.app.admin.domain.CaseStatute;
import com.mkst.umap.app.admin.service.ICaseStatuteService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 案件相关法规 信息操作处理
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Controller
@RequestMapping("/admin/caseStatute")
public class CaseStatuteController extends BaseController
{
    private String prefix = "admin/caseStatute";
	
	@Autowired
	private ICaseStatuteService caseStatuteService;
	
	@RequiresPermissions("admin:caseStatute:view")
	@GetMapping()
	public String caseStatute()
	{
	    return prefix + "/caseStatute";
	}
	
	/**
	 * 查询案件相关法规列表
	 */
	@RequiresPermissions("admin:caseStatute:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CaseStatute caseStatute)
	{
		startPage();
        List<CaseStatute> list = caseStatuteService.selectCaseStatuteList(caseStatute);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出案件相关法规列表
	 */
	@RequiresPermissions("admin:caseStatute:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CaseStatute caseStatute)
    {
    	List<CaseStatute> list = caseStatuteService.selectCaseStatuteList(caseStatute);
        ExcelUtil<CaseStatute> util = new ExcelUtil<CaseStatute>(CaseStatute.class);
        return util.exportExcel(list, "caseStatute");
    }
	
	/**
	 * 新增案件相关法规
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存案件相关法规
	 */
	@RequiresPermissions("admin:caseStatute:add")
	@Log(title = "案件相关法规", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CaseStatute caseStatute)
	{		
		return toAjax(caseStatuteService.insertCaseStatute(caseStatute));
	}

	/**
	 * 修改案件相关法规
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		CaseStatute caseStatute = caseStatuteService.selectCaseStatuteById(id);
		mmap.put("caseStatute", caseStatute);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存案件相关法规
	 */
	@RequiresPermissions("admin:caseStatute:edit")
	@Log(title = "案件相关法规", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CaseStatute caseStatute)
	{		
		return toAjax(caseStatuteService.updateCaseStatute(caseStatute));
	}
	
	/**
	 * 删除案件相关法规
	 */
	@RequiresPermissions("admin:caseStatute:remove")
	@Log(title = "案件相关法规", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(caseStatuteService.deleteCaseStatuteByIds(ids));
	}
	
}
