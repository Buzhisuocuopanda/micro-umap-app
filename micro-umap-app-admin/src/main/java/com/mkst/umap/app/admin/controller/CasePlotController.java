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
import com.mkst.umap.app.admin.domain.CasePlot;
import com.mkst.umap.app.admin.service.ICasePlotService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 案件计量 信息操作处理
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Controller
@RequestMapping("/admin/casePlot")
public class CasePlotController extends BaseController
{
    private String prefix = "admin/casePlot";
	
	@Autowired
	private ICasePlotService casePlotService;
	
	@RequiresPermissions("admin:casePlot:view")
	@GetMapping()
	public String casePlot()
	{
	    return prefix + "/casePlot";
	}
	
	/**
	 * 查询案件计量列表
	 */
	@RequiresPermissions("admin:casePlot:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CasePlot casePlot)
	{
		startPage();
        List<CasePlot> list = casePlotService.selectCasePlotList(casePlot);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出案件计量列表
	 */
	@RequiresPermissions("admin:casePlot:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CasePlot casePlot)
    {
    	List<CasePlot> list = casePlotService.selectCasePlotList(casePlot);
        ExcelUtil<CasePlot> util = new ExcelUtil<CasePlot>(CasePlot.class);
        return util.exportExcel(list, "casePlot");
    }
	
	/**
	 * 新增案件计量
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存案件计量
	 */
	@RequiresPermissions("admin:casePlot:add")
	@Log(title = "案件计量", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CasePlot casePlot)
	{		
		return toAjax(casePlotService.insertCasePlot(casePlot));
	}

	/**
	 * 修改案件计量
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		CasePlot casePlot = casePlotService.selectCasePlotById(id);
		mmap.put("casePlot", casePlot);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存案件计量
	 */
	@RequiresPermissions("admin:casePlot:edit")
	@Log(title = "案件计量", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CasePlot casePlot)
	{		
		return toAjax(casePlotService.updateCasePlot(casePlot));
	}
	
	/**
	 * 删除案件计量
	 */
	@RequiresPermissions("admin:casePlot:remove")
	@Log(title = "案件计量", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(casePlotService.deleteCasePlotByIds(ids));
	}
	
}
