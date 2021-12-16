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
import com.mkst.umap.app.admin.domain.CaseBase;
import com.mkst.umap.app.admin.service.ICaseBaseService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 案件 信息操作处理
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Controller
@RequestMapping("/admin/caseBase")
public class CaseBaseController extends BaseController
{
    private String prefix = "admin/caseBase";
	
	@Autowired
	private ICaseBaseService caseBaseService;
	
	@RequiresPermissions("admin:caseBase:view")
	@GetMapping()
	public String caseBase()
	{
	    return prefix + "/caseBase";
	}
	
	/**
	 * 查询案件列表
	 */
	@RequiresPermissions("admin:caseBase:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CaseBase caseBase)
	{
		startPage();
        List<CaseBase> list = caseBaseService.selectCaseBaseList(caseBase);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出案件列表
	 */
	@RequiresPermissions("admin:caseBase:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CaseBase caseBase)
    {
    	List<CaseBase> list = caseBaseService.selectCaseBaseList(caseBase);
        ExcelUtil<CaseBase> util = new ExcelUtil<CaseBase>(CaseBase.class);
        return util.exportExcel(list, "caseBase");
    }
	
	/**
	 * 新增案件
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存案件
	 */
	@RequiresPermissions("admin:caseBase:add")
	@Log(title = "案件", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CaseBase caseBase)
	{		
		return toAjax(caseBaseService.insertCaseBase(caseBase));
	}

	/**
	 * 修改案件
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		CaseBase caseBase = caseBaseService.selectCaseBaseById(id);
		mmap.put("caseBase", caseBase);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存案件
	 */
	@RequiresPermissions("admin:caseBase:edit")
	@Log(title = "案件", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CaseBase caseBase)
	{		
		return toAjax(caseBaseService.updateCaseBase(caseBase));
	}
	
	/**
	 * 删除案件
	 */
	@RequiresPermissions("admin:caseBase:remove")
	@Log(title = "案件", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(caseBaseService.deleteCaseBaseByIds(ids));
	}
	
}
