package com.mkst.umap.app.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.ArraignRoomLog;
import com.mkst.umap.app.admin.domain.Equipment;
import com.mkst.umap.app.admin.domain.RoomEquipment;
import com.mkst.umap.app.admin.dto.arraign.CountDto;
import com.mkst.umap.app.admin.service.IArraignRoomLogService;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.admin.service.IRoomEquipmentService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 房间 信息操作处理
 *
 * @author lijinghui
 * @date 2020-06-12
 */
@Controller
@RequestMapping("/admin/arraignRoom")
public class ArraignRoomController extends BaseController {
	private String prefix = "app/arraign/arraignRoom";

	@Autowired
	private IArraignRoomService arraignRoomService;
	@Autowired
	private IRoomEquipmentService roomEquipmentService;
	@Autowired
	private IArraignRoomLogService arraignRoomLogService;

	@RequiresPermissions("admin:arraignRoom:view")
	@GetMapping()
	public String arraignRoom() {
		return prefix + "/arraignRoom";
	}

	/**
	 * 查询房间列表
	 */
	@RequiresPermissions("admin:arraignRoom:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ArraignRoom arraignRoom) {
		startPage();
		List<ArraignRoom> list = arraignRoomService.selectArraignRoomList(arraignRoom);
		return getDataTable(list);
	}

	/**
	 * 查询会议室大屏列表
	 */
	@RequiresPermissions("admin:arraignRoom:list")
	@PostMapping("/screenList")
	@ResponseBody
	public List<Equipment> screenList() {

		Equipment selectEquipment = new Equipment();
		selectEquipment.setDevType("9");
		List<Equipment> list = roomEquipmentService.selectRemoteEquipmentList(selectEquipment);
		return list;
	}

	/**
	 * 查询房间列表
	 */
	@PostMapping("/selectRoomType0And30Day")
	@ResponseBody
	public TableDataInfo selectRoomType0And30Day() {
		startPage();
		List<CountDto> list = arraignRoomService.selectRoomType0And30Day();
		return getDataTable(list);
	}


	/**
	 * 导出房间列表
	 */
	@RequiresPermissions("admin:arraignRoom:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(ArraignRoom arraignRoom) {
		List<ArraignRoom> list = arraignRoomService.selectArraignRoomList(arraignRoom);
		ExcelUtil<ArraignRoom> util = new ExcelUtil<ArraignRoom>(ArraignRoom.class);
		return util.exportExcel(list, "arraignRoom");
	}

	/**
	 * 新增房间
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存房间
	 */
	@RequiresPermissions("admin:arraignRoom:add")
	@Log(title = "房间", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ArraignRoom arraignRoom) {
		return toAjax(arraignRoomService.insertArraignRoom(arraignRoom));
	}

	/**
	 * 修改房间
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap) {
		ArraignRoom arraignRoom = arraignRoomService.selectArraignRoomById(id);
		RoomEquipment selectEquip = new RoomEquipment();
		selectEquip.setRoomId(arraignRoom.getId());
		List<RoomEquipment> roomEquipments = roomEquipmentService.selectRoomEquipmentList(selectEquip);
		if (CollUtil.isNotEmpty(roomEquipments)){
			arraignRoom.setScreenId(roomEquipments.get(0).getEquipmentId());
		}
		mmap.put("arraignRoom", arraignRoom);
		return prefix + "/edit";
	}

	/**
	 * 修改保存房间
	 */
	@RequiresPermissions("admin:arraignRoom:edit")
	@Log(title = "房间", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ArraignRoom arraignRoom) {
		return toAjax(arraignRoomService.updateArraignRoom(arraignRoom));
	}

	/**
	 * 删除房间
	 */
	@RequiresPermissions("admin:arraignRoom:remove")
	@Log(title = "房间", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(arraignRoomService.deleteArraignRoomByIds(ids));
	}

	/**
	 * 自定义方法绑定请求参数的Date类型
	 *
	 * @param binder
	 * @throws Exception
	 */

	@InitBinder
	@Override
	public void initBinder(WebDataBinder binder) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		//true表示允许为空，false反之
		CustomDateEditor editor = new CustomDateEditor(df, true);
		binder.registerCustomEditor(Date.class, editor);
	}

	@RequiresPermissions("admin:meeting:edit")
	@GetMapping("/device/{id}")
	public String device(@PathVariable("id") String id, ModelMap map) {

		RoomEquipment selectRoomEquipment = new RoomEquipment();
		selectRoomEquipment.setRoomId(id);
		List<Equipment> bindEquipmentList = roomEquipmentService.selectRoomBindEquipmentList(selectRoomEquipment);

		List<Equipment> notBindEquipment = roomEquipmentService.selectRoomNotBindEquipmentList();

		map.put("bindEquipmentList", bindEquipmentList);
		map.put("notBindEquipment", notBindEquipment);
		return prefix + "/device";
	}

	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(String id,MultipartFile file) throws Exception {
		List<ArraignRoomLog> arraignRoomLogs = null;
		ExcelUtil<ArraignRoomLog> util = new ExcelUtil<ArraignRoomLog>(ArraignRoomLog.class);
		arraignRoomLogs = util.importExcel(file.getInputStream());

		String message =  arraignRoomLogService.importArraignRoomLog(id,arraignRoomLogs);
		return success(message);
	}

}
