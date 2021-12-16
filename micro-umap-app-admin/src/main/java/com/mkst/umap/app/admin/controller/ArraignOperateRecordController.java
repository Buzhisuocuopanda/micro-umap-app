package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.ArraignOperateRecord;
import com.mkst.umap.app.admin.service.IArraignOperateRecordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提审操作日志 信息操作处理
 * 
 * @author wangyong
 * @date 2021-04-06
 */
@Controller
@RequestMapping("/admin/arraignOperateRecord")
public class ArraignOperateRecordController extends BaseController
{
    private String prefix = "app/arraign/arraignOperateRecord";
	
	@Autowired
	private IArraignOperateRecordService arraignOperateRecordService;
	
	@RequiresPermissions("admin:arraignOperateRecord:view")
	@GetMapping()
	public String arraignOperateRecord()
	{
	    return prefix + "/arraignOperateRecord";
	}
	
	/**
	 * 查询提审操作日志列表
	 */
	@RequiresPermissions("admin:arraignOperateRecord:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ArraignOperateRecord arraignOperateRecord)
	{
		startPage();
        List<ArraignOperateRecord> list = arraignOperateRecordService.selectArraignOperateRecordList(arraignOperateRecord);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出提审操作日志列表
	 */
	@RequiresPermissions("admin:arraignOperateRecord:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ArraignOperateRecord arraignOperateRecord)
    {
    	List<ArraignOperateRecord> list = arraignOperateRecordService.selectArraignOperateRecordList(arraignOperateRecord);
        ExcelUtil<ArraignOperateRecord> util = new ExcelUtil<ArraignOperateRecord>(ArraignOperateRecord.class);
        return util.exportExcel(list, "arraignOperateRecord");
    }
	
	/**
	 * 新增提审操作日志
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存提审操作日志
	 */
	@RequiresPermissions("admin:arraignOperateRecord:add")
	@Log(title = "提审操作日志", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ArraignOperateRecord arraignOperateRecord)
	{		
		return toAjax(arraignOperateRecordService.insertArraignOperateRecord(arraignOperateRecord));
	}

	/**
	 * 修改提审操作日志
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ArraignOperateRecord arraignOperateRecord = arraignOperateRecordService.selectArraignOperateRecordById(id);
		mmap.put("arraignOperateRecord", arraignOperateRecord);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存提审操作日志
	 */
	@RequiresPermissions("admin:arraignOperateRecord:edit")
	@Log(title = "提审操作日志", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ArraignOperateRecord arraignOperateRecord)
	{		
		return toAjax(arraignOperateRecordService.updateArraignOperateRecord(arraignOperateRecord));
	}
	
	/**
	 * 删除提审操作日志
	 */
	@RequiresPermissions("admin:arraignOperateRecord:remove")
	@Log(title = "提审操作日志", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(arraignOperateRecordService.deleteArraignOperateRecordByIds(ids));
	}
	
}
