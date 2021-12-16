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
import com.mkst.umap.app.admin.domain.LhWs;
import com.mkst.umap.app.admin.service.ILhWsService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 龙华文书同步 信息操作处理
 * 
 * @author wangyong
 * @date 2020-09-15
 */
@Controller
@RequestMapping("/admin/lhWs")
public class LhWsController extends BaseController {
    private String prefix = "app/lhWs";
	
	@Autowired
	private ILhWsService lhWsService;
	
	@RequiresPermissions("admin:lhWs:view")
	@GetMapping()
	public String lhWs() {
	    return prefix + "/lhWs";
	}
	
	/**
	 * 查询龙华文书同步列表
	 */
	@RequiresPermissions("admin:lhWs:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(LhWs lhWs)
	{
		startPage();
        List<LhWs> list = lhWsService.selectLhWsList(lhWs);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出龙华文书同步列表
	 */
	@RequiresPermissions("admin:lhWs:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LhWs lhWs) {
    	List<LhWs> list = lhWsService.selectLhWsList(lhWs);
        ExcelUtil<LhWs> util = new ExcelUtil<LhWs>(LhWs.class);
        return util.exportExcel(list, "lhWs");
    }
	
	/**
	 * 新增龙华文书同步
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存龙华文书同步
	 */
	@RequiresPermissions("admin:lhWs:add")
	@Log(title = "龙华文书同步", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(LhWs lhWs)
	{		
		return toAjax(lhWsService.insertLhWs(lhWs));
	}

	/**
	 * 修改龙华文书同步
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		LhWs lhWs = lhWsService.selectLhWsById(id);
		mmap.put("lhWs", lhWs);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存龙华文书同步
	 */
	@RequiresPermissions("admin:lhWs:edit")
	@Log(title = "龙华文书同步", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(LhWs lhWs)
	{		
		return toAjax(lhWsService.updateLhWs(lhWs));
	}
	
	/**
	 * 删除龙华文书同步
	 */
	@RequiresPermissions("admin:lhWs:remove")
	@Log(title = "龙华文书同步", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(lhWsService.deleteLhWsByIds(ids));
	}
	
}
