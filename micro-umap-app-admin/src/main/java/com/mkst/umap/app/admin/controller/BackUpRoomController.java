package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.service.IBackUpRoomService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 备勤间 信息操作处理
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Controller
@RequestMapping("/admin/backUpRoom")
public class BackUpRoomController extends BaseController
{
    private String prefix = "app/apply/backUpRoom";
	
	@Autowired
	private IBackUpRoomService backUpRoomService;
	
	@RequiresPermissions("admin:backUpRoom:view")
	@GetMapping()
	public String backUpRoom()
	{
	    return prefix + "/backUpRoom";
	}
	
	/**
	 * 查询备勤间列表
	 */
	@RequiresPermissions("admin:backUpRoom:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(BackUpRoom backUpRoom)
	{
		startPage();
        List<BackUpRoom> list = backUpRoomService.selectBackUpRoomList(backUpRoom);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出备勤间列表
	 */
	@RequiresPermissions("admin:backUpRoom:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BackUpRoom backUpRoom)
    {
    	List<BackUpRoom> list = backUpRoomService.selectBackUpRoomList(backUpRoom);
        ExcelUtil<BackUpRoom> util = new ExcelUtil<BackUpRoom>(BackUpRoom.class);
        return util.exportExcel(list, "backUpRoom");
    }
	
	/**
	 * 新增备勤间
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存备勤间
	 */
	@RequiresPermissions("admin:backUpRoom:add")
	@Log(title = "备勤间", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(BackUpRoom backUpRoom)
	{		
		return toAjax(backUpRoomService.insertBackUpRoom(backUpRoom));
	}

	/**
	 * 修改备勤间
	 */
	@GetMapping("/edit/{roomId}")
	public String edit(@PathVariable("roomId") Integer roomId, ModelMap mmap)
	{
		BackUpRoom backUpRoom = backUpRoomService.selectBackUpRoomById(roomId);
		mmap.put("backUpRoom", backUpRoom);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存备勤间
	 */
	@RequiresPermissions("admin:backUpRoom:edit")
	@Log(title = "备勤间", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(BackUpRoom backUpRoom)
	{		
		return toAjax(backUpRoomService.updateBackUpRoom(backUpRoom));
	}
	
	/**
	 * 删除备勤间(逻辑删除,修改删除标记位)
	 */
	@RequiresPermissions("admin:backUpRoom:remove")
	@Log(title = "备勤间", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(backUpRoomService.deleteBackUpRoomByIdsUpdateDelFlag(ids));
	}

	/**
	 * 校验房间号
	 */
	@PostMapping("/checkRoomNumUnique")
	@ResponseBody
	public String checkRoomNumUnique(BackUpRoom backUpRoom)
	{
		return backUpRoomService.checkRoomNumUnique(backUpRoom.getRoomNum());
	}
	
}
