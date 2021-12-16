package com.mkst.umap.app.admin.controller;

import java.util.List;

import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.system.domain.SysUser;
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
import com.mkst.umap.app.admin.domain.CarInfo;
import com.mkst.umap.app.admin.service.ICarInfoService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 车辆 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-20
 */
@Controller
@RequestMapping("/admin/carInfo")
public class CarInfoController extends BaseController
{
    private String prefix = "app/carInfo";
	
	@Autowired
	private ICarInfoService carInfoService;
	
	@RequiresPermissions("admin:carInfo:view")
	@GetMapping()
	public String carInfo()
	{
	    return prefix + "/carInfo";
	}
	
	/**
	 * 查询车辆列表
	 */
	@RequiresPermissions("admin:carInfo:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CarInfo carInfo)
	{
		startPage();
        List<CarInfo> list = carInfoService.selectCarInfoList(carInfo);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出车辆列表
	 */
	@RequiresPermissions("admin:carInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CarInfo carInfo)
    {
    	List<CarInfo> list = carInfoService.selectCarInfoList(carInfo);
        ExcelUtil<CarInfo> util = new ExcelUtil(CarInfo.class);
        return util.exportExcel(list, "carInfo");
    }
	
	/**
	 * 新增车辆
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存车辆
	 */
	@RequiresPermissions("admin:carInfo:add")
	@Log(title = "车辆", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CarInfo carInfo)
	{
		carInfo.setCreateBy(ShiroUtils.getUserId()+"");
		carInfo.setUpdateBy(ShiroUtils.getUserId()+"");
		return toAjax(carInfoService.insertCarInfo(carInfo));
	}

	/**
	 * 修改车辆
	 */
	@GetMapping("/edit/{carId}")
	public String edit(@PathVariable("carId") Long carId, ModelMap mmap)
	{
		CarInfo carInfo = carInfoService.selectCarInfoById(carId);
		mmap.put("carInfo", carInfo);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存车辆
	 */
	@RequiresPermissions("admin:carInfo:edit")
	@Log(title = "车辆", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CarInfo carInfo)
	{
		carInfo.setUpdateBy(ShiroUtils.getUserId()+"");
		return toAjax(carInfoService.updateCarInfo(carInfo));
	}
	
	/**
	 * 删除车辆
	 */
	@RequiresPermissions("admin:carInfo:remove")
	@Log(title = "车辆", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(carInfoService.deleteCarInfoByIds(ids));
	}
	
}
