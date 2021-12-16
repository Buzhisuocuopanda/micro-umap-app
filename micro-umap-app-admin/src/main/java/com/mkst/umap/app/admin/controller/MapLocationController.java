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
import com.mkst.umap.app.admin.domain.MapLocation;
import com.mkst.umap.app.admin.service.IMapLocationService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 地图位置 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Controller
@RequestMapping("/admin/mapLocation")
public class MapLocationController extends BaseController
{
    private String prefix = "admin/mapLocation";
	
	@Autowired
	private IMapLocationService mapLocationService;
	
	@RequiresPermissions("admin:mapLocation:view")
	@GetMapping()
	public String mapLocation()
	{
	    return prefix + "/mapLocation";
	}
	
	/**
	 * 查询地图位置列表
	 */
	@RequiresPermissions("admin:mapLocation:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MapLocation mapLocation)
	{
		startPage();
        List<MapLocation> list = mapLocationService.selectMapLocationList(mapLocation);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出地图位置列表
	 */
	@RequiresPermissions("admin:mapLocation:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MapLocation mapLocation)
    {
    	List<MapLocation> list = mapLocationService.selectMapLocationList(mapLocation);
        ExcelUtil<MapLocation> util = new ExcelUtil<MapLocation>(MapLocation.class);
        return util.exportExcel(list, "mapLocation");
    }
	
	/**
	 * 新增地图位置
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存地图位置
	 */
	@RequiresPermissions("admin:mapLocation:add")
	@Log(title = "地图位置", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MapLocation mapLocation)
	{		
		return toAjax(mapLocationService.insertMapLocation(mapLocation));
	}

	/**
	 * 修改地图位置
	 */
	@GetMapping("/edit/{locationId}")
	public String edit(@PathVariable("locationId") Long locationId, ModelMap mmap)
	{
		MapLocation mapLocation = mapLocationService.selectMapLocationById(locationId);
		mmap.put("mapLocation", mapLocation);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存地图位置
	 */
	@RequiresPermissions("admin:mapLocation:edit")
	@Log(title = "地图位置", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MapLocation mapLocation)
	{		
		return toAjax(mapLocationService.updateMapLocation(mapLocation));
	}
	
	/**
	 * 删除地图位置
	 */
	@RequiresPermissions("admin:mapLocation:remove")
	@Log(title = "地图位置", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mapLocationService.deleteMapLocationByIds(ids));
	}
	
}
