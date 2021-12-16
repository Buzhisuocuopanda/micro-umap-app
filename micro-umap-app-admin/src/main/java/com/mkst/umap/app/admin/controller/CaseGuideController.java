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
import com.mkst.umap.app.admin.domain.CaseGuide;
import com.mkst.umap.app.admin.service.ICaseGuideService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 指导案例 信息操作处理
 * 
 * @author wangyong
 * @date 2021-06-25
 */
@Controller
@RequestMapping("/admin/caseGuide")
public class CaseGuideController extends BaseController
{
    private String prefix = "admin/caseGuide";
	
	@Autowired
	private ICaseGuideService caseGuideService;
	
	@RequiresPermissions("admin:caseGuide:view")
	@GetMapping()
	public String caseGuide()
	{
	    return prefix + "/caseGuide";
	}
	
	/**
	 * 查询指导案例列表
	 */
	@RequiresPermissions("admin:caseGuide:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CaseGuide caseGuide)
	{
		startPage();
        List<CaseGuide> list = caseGuideService.selectCaseGuideList(caseGuide);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出指导案例列表
	 */
	@RequiresPermissions("admin:caseGuide:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CaseGuide caseGuide)
    {
    	List<CaseGuide> list = caseGuideService.selectCaseGuideList(caseGuide);
        ExcelUtil<CaseGuide> util = new ExcelUtil<CaseGuide>(CaseGuide.class);
        return util.exportExcel(list, "caseGuide");
    }
	
	/**
	 * 新增指导案例
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存指导案例
	 */
	@RequiresPermissions("admin:caseGuide:add")
	@Log(title = "指导案例", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CaseGuide caseGuide)
	{		
		return toAjax(caseGuideService.insertCaseGuide(caseGuide));
	}

	/**
	 * 修改指导案例
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		CaseGuide caseGuide = caseGuideService.selectCaseGuideById(id);
		mmap.put("caseGuide", caseGuide);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存指导案例
	 */
	@RequiresPermissions("admin:caseGuide:edit")
	@Log(title = "指导案例", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CaseGuide caseGuide)
	{		
		return toAjax(caseGuideService.updateCaseGuide(caseGuide));
	}
	
	/**
	 * 删除指导案例
	 */
	@RequiresPermissions("admin:caseGuide:remove")
	@Log(title = "指导案例", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(caseGuideService.deleteCaseGuideByIds(ids));
	}
	
}
