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
import com.mkst.umap.app.admin.domain.CaseType;
import com.mkst.umap.app.admin.service.ICaseTypeService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 案件类型 信息操作处理
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Controller
@RequestMapping("/admin/caseType")
public class CaseTypeController extends BaseController
{
    private String prefix = "admin/caseType";
	
	@Autowired
	private ICaseTypeService caseTypeService;
	
	@RequiresPermissions("admin:caseType:view")
	@GetMapping()
	public String caseType()
	{
	    return prefix + "/caseType";
	}
	
	/**
	 * 查询案件类型列表
	 */
	@RequiresPermissions("admin:caseType:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CaseType caseType)
	{
		startPage();
        List<CaseType> list = caseTypeService.selectCaseTypeList(caseType);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出案件类型列表
	 */
	@RequiresPermissions("admin:caseType:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CaseType caseType)
    {
    	List<CaseType> list = caseTypeService.selectCaseTypeList(caseType);
        ExcelUtil<CaseType> util = new ExcelUtil<CaseType>(CaseType.class);
        return util.exportExcel(list, "caseType");
    }
	
	/**
	 * 新增案件类型
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存案件类型
	 */
	@RequiresPermissions("admin:caseType:add")
	@Log(title = "案件类型", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CaseType caseType)
	{		
		return toAjax(caseTypeService.insertCaseType(caseType));
	}

	/**
	 * 修改案件类型
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		CaseType caseType = caseTypeService.selectCaseTypeById(id);
		mmap.put("caseType", caseType);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存案件类型
	 */
	@RequiresPermissions("admin:caseType:edit")
	@Log(title = "案件类型", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CaseType caseType)
	{		
		return toAjax(caseTypeService.updateCaseType(caseType));
	}
	
	/**
	 * 删除案件类型
	 */
	@RequiresPermissions("admin:caseType:remove")
	@Log(title = "案件类型", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(caseTypeService.deleteCaseTypeByIds(ids));
	}
	
}
