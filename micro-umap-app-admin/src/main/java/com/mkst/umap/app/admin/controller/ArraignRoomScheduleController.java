package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.ArraignRoomSchedule;
import com.mkst.umap.app.admin.service.IArraignRoomScheduleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源排程 信息操作处理
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Controller
@RequestMapping("/admin/arraignRoomSchedule")
public class ArraignRoomScheduleController extends BaseController {
	private String prefix = "app/arraign/arraignRoomSchedule";

	@Autowired
	private IArraignRoomScheduleService arraignRoomScheduleService;

	@RequiresPermissions("admin:arraignRoomSchedule:view")
	@GetMapping()
	public String arraignRoomSchedule() {
		return prefix + "/arraignRoomSchedule";
	}

	/**
	 * 查询资源排程列表
	 */
	@RequiresPermissions("admin:arraignRoomSchedule:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ArraignRoomSchedule arraignRoomSchedule) {
		startPage();
		List<ArraignRoomSchedule> list = arraignRoomScheduleService.selectArraignRoomScheduleList(arraignRoomSchedule);
		return getDataTable(list);
	}


	/**
	 * 导出资源排程列表
	 */
	@RequiresPermissions("admin:arraignRoomSchedule:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ArraignRoomSchedule arraignRoomSchedule) {
		List<ArraignRoomSchedule> list = arraignRoomScheduleService.selectArraignRoomScheduleList(arraignRoomSchedule);
		ExcelUtil<ArraignRoomSchedule> util = new ExcelUtil<ArraignRoomSchedule>(ArraignRoomSchedule.class);
		return util.exportExcel(list, "arraignRoomSchedule");
	}

	/**
	 * 新增资源排程
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存资源排程
	 */
	@RequiresPermissions("admin:arraignRoomSchedule:add")
	@Log(title = "资源排程", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ArraignRoomSchedule arraignRoomSchedule) {
		return toAjax(arraignRoomScheduleService.insertArraignRoomSchedule(arraignRoomSchedule));
	}

	/**
	 * 修改资源排程
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		ArraignRoomSchedule arraignRoomSchedule = arraignRoomScheduleService.selectArraignRoomScheduleById(id);
		mmap.put("arraignRoomSchedule", arraignRoomSchedule);
		return prefix + "/edit";
	}

	/**
	 * 修改保存资源排程
	 */
	@RequiresPermissions("admin:arraignRoomSchedule:edit")
	@Log(title = "资源排程", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ArraignRoomSchedule arraignRoomSchedule) {
		return toAjax(arraignRoomScheduleService.updateArraignRoomSchedule(arraignRoomSchedule));
	}

	/**
	 * 删除资源排程
	 */
	@RequiresPermissions("admin:arraignRoomSchedule:remove")
	@Log(title = "资源排程", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(arraignRoomScheduleService.deleteArraignRoomScheduleByIds(ids));
	}

}
