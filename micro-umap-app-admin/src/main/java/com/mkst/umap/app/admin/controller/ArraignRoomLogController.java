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
import com.mkst.umap.app.admin.domain.ArraignRoomLog;
import com.mkst.umap.app.admin.service.IArraignRoomLogService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 提审室操作日志 信息操作处理
 * 
 * @author wangyong
 * @date 2020-09-21
 */
@Controller
@RequestMapping("/admin/arraignRoomLog")
public class ArraignRoomLogController extends BaseController
{
    private String prefix = "app/arraign/arraignRoomLog";
	
	@Autowired
	private IArraignRoomLogService arraignRoomLogService;
	
	@RequiresPermissions("admin:arraignRoomLog:view")
	@GetMapping()
	public String arraignRoomLog()
	{
	    return prefix + "/arraignRoomLog";
	}
	
	/**
	 * 查询提审室操作日志列表
	 */
	@RequiresPermissions("admin:arraignRoomLog:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ArraignRoomLog arraignRoomLog)
	{
		startPage();
        List<ArraignRoomLog> list = arraignRoomLogService.selectArraignRoomLogList(arraignRoomLog);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出提审室操作日志列表
	 */
	@RequiresPermissions("admin:arraignRoomLog:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ArraignRoomLog arraignRoomLog)
    {
    	List<ArraignRoomLog> list = arraignRoomLogService.selectArraignRoomLogList(arraignRoomLog);
        ExcelUtil<ArraignRoomLog> util = new ExcelUtil<ArraignRoomLog>(ArraignRoomLog.class);
        return util.exportExcel(list, "arraignRoomLog");
    }
	
	/**
	 * 新增提审室操作日志
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存提审室操作日志
	 */
	@RequiresPermissions("admin:arraignRoomLog:add")
	@Log(title = "提审室操作日志", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ArraignRoomLog arraignRoomLog)
	{		
		return toAjax(arraignRoomLogService.insertArraignRoomLog(arraignRoomLog));
	}

	/**
	 * 修改提审室操作日志
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		ArraignRoomLog arraignRoomLog = arraignRoomLogService.selectArraignRoomLogById(id);
		mmap.put("arraignRoomLog", arraignRoomLog);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存提审室操作日志
	 */
	@RequiresPermissions("admin:arraignRoomLog:edit")
	@Log(title = "提审室操作日志", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ArraignRoomLog arraignRoomLog)
	{		
		return toAjax(arraignRoomLogService.updateArraignRoomLog(arraignRoomLog));
	}
	
	/**
	 * 删除提审室操作日志
	 */
	@RequiresPermissions("admin:arraignRoomLog:remove")
	@Log(title = "提审室操作日志", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(arraignRoomLogService.deleteArraignRoomLogByIds(ids));
	}
	
}
