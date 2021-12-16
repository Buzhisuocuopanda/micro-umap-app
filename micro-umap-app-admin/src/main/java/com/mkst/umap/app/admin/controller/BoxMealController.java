package com.mkst.umap.app.admin.controller;

import java.util.Comparator;
import java.util.List;

import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.umap.app.admin.domain.ArraignRoom;
import com.mkst.umap.app.admin.domain.BackUpRoom;
import com.mkst.umap.app.admin.service.IArraignRoomService;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.enums.RoomTypeEnum;
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
import com.mkst.umap.app.admin.domain.BoxMeal;
import com.mkst.umap.app.admin.service.IBoxMealService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 食堂包厢餐次 信息操作处理
 * 
 * @author wangyong
 * @date 2020-08-31
 */
@Controller
@RequestMapping("/admin/boxMeal")
public class BoxMealController extends BaseController
{
    private String prefix = "app/canteenManage/boxMeal";
	
	@Autowired
	private IBoxMealService boxMealService;
	@Autowired
	private IArraignRoomService arraignRoomService;
	
	@RequiresPermissions("admin:boxMeal:view")
	@GetMapping()
	public String boxMeal()
	{
	    return prefix + "/boxMeal";
	}
	
	/**
	 * 查询食堂包厢餐次列表
	 */
	@RequiresPermissions("admin:boxMeal:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(BoxMeal boxMeal)
	{
		startPage();
        List<BoxMeal> list = boxMealService.selectBoxMealList(boxMeal);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出食堂包厢餐次列表
	 */
	@RequiresPermissions("admin:boxMeal:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BoxMeal boxMeal)
    {
    	List<BoxMeal> list = boxMealService.selectBoxMealList(boxMeal);
        ExcelUtil<BoxMeal> util = new ExcelUtil(BoxMeal.class);
        return util.exportExcel(list, "boxMeal");
    }
	
	/**
	 * 新增食堂包厢餐次
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
		ArraignRoom selectRoom = new ArraignRoom();
		//查询条件：房间可用
		selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		selectRoom.setType(RoomTypeEnum.CANTEEN_ROOM.getValue());

		List<ArraignRoom> roomList = arraignRoomService.selectArraignRoomList(selectRoom);
		mmap.put("roomList",roomList);
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存食堂包厢餐次
	 */
	@RequiresPermissions("admin:boxMeal:add")
	@Log(title = "食堂包厢餐次", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(BoxMeal boxMeal)
	{
		boxMeal.setCreateBy(ShiroUtils.getUserId()+"");
		return toAjax(boxMealService.insertBoxMeal(boxMeal));
	}

	/**
	 * 修改食堂包厢餐次
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		BoxMeal boxMeal = boxMealService.selectBoxMealById(id);
		ArraignRoom selectRoom = new ArraignRoom();
		//查询条件：房间可用
		selectRoom.setStatus(KeyConstant.RESOURCES_STATUS_AVAILABLE);
		selectRoom.setType(RoomTypeEnum.CANTEEN_ROOM.getValue());

		List<ArraignRoom> roomList = arraignRoomService.selectArraignRoomList(selectRoom);
		mmap.put("roomList",roomList);
		mmap.put("boxMeal", boxMeal);
	    return prefix + "/edit";
	}

	/**
	 * 修改保存食堂包厢餐次
	 */
	@RequiresPermissions("admin:boxMeal:edit")
	@Log(title = "食堂包厢餐次", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(BoxMeal boxMeal) {
		return toAjax(boxMealService.updateBoxMeal(boxMeal));
	}


	/**
	 * 删除食堂包厢餐次
	 */
	@RequiresPermissions("admin:boxMeal:remove")
	@Log(title = "食堂包厢餐次", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(boxMealService.deleteBoxMealByIds(ids));
	}
	
}
