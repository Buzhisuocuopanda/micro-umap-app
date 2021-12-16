package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.domain.BackUpGuest;
import com.mkst.umap.app.admin.service.IBackUpGuestService;
import com.mkst.umap.app.admin.service.IBackUpGuestService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 备勤间使用人 信息操作处理
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Controller
@RequestMapping("/admin/backUpGuest")
public class BackUpGuestController extends BaseController
{
    private String prefix = "app/apply/backUpGuest";
	
	@Autowired
	private IBackUpGuestService backUpGuestService;
	
	@RequiresPermissions("admin:backUpGuest:view")
	@GetMapping()
	public String backUpGuest()
	{
	    return prefix + "/backUpGuest";
	}
	
	/**
	 * 查询备勤间使用人列表
	 */
	@RequiresPermissions("admin:backUpGuest:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(BackUpGuest backUpGuest)
	{
		startPage();
        List<BackUpGuest> list = backUpGuestService.selectBackUpGuestList(backUpGuest);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出备勤间使用人列表
	 */
	@RequiresPermissions("admin:backUpGuest:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BackUpGuest backUpGuest)
    {
    	List<BackUpGuest> list = backUpGuestService.selectBackUpGuestList(backUpGuest);
        ExcelUtil<BackUpGuest> util = new ExcelUtil<BackUpGuest>(BackUpGuest.class);
        return util.exportExcel(list, "backUpGuest");
    }
	
	/**
	 * 新增备勤间使用人
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存备勤间使用人
	 */
	@RequiresPermissions("admin:backUpGuest:add")
	@Log(title = "备勤间使用人", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(BackUpGuest backUpGuest)
	{		
		return toAjax(backUpGuestService.insertBackUpGuest(backUpGuest));
	}

	/**
	 * 修改备勤间使用人
	 */
	@GetMapping("/edit/{guestId}")
	public String edit(@PathVariable("guestId") Integer guestId, ModelMap mmap)
	{
		BackUpGuest backUpGuest = backUpGuestService.selectBackUpGuestById(guestId);
		mmap.put("backUpGuest", backUpGuest);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存备勤间使用人
	 */
	@RequiresPermissions("admin:backUpGuest:edit")
	@Log(title = "备勤间使用人", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(BackUpGuest backUpGuest)
	{		
		return toAjax(backUpGuestService.updateBackUpGuest(backUpGuest));
	}
	
	/**
	 * 删除备勤间使用人
	 */
	@RequiresPermissions("admin:backUpGuest:remove")
	@Log(title = "备勤间使用人", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(backUpGuestService.deleteBackUpGuestByIds(ids));
	}
	
}
