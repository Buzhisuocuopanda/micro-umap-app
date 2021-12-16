package com.mkst.umap.app.admin.controller;

import java.util.List;
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
import com.mkst.umap.app.admin.domain.RepastAppointment;
import com.mkst.umap.app.admin.service.IRepastAppointmentService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 就餐预约 信息操作处理
 * 
 * @author wangyong
 * @date 2020-11-30
 */
@Controller
@RequestMapping("/admin/repastAppointment")
public class RepastAppointmentController extends BaseController
{
    private String prefix = "app/repastAppointment";
	
	@Autowired
	private IRepastAppointmentService repastAppointmentService;
	
	@RequiresPermissions("admin:repastAppointment:view")
	@GetMapping()
	public String repastAppointment()
	{
	    return prefix + "/repastAppointment";
	}
	
	/**
	 * 查询就餐预约列表
	 */
	@RequiresPermissions("admin:repastAppointment:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(RepastAppointment repastAppointment)
	{
		startPage();
        List<RepastAppointment> list = repastAppointmentService.selectRepastAppointmentList(repastAppointment);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出就餐预约列表
	 */
	@RequiresPermissions("admin:repastAppointment:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RepastAppointment repastAppointment)
    {
    	List<RepastAppointment> list = repastAppointmentService.selectRepastAppointmentList(repastAppointment);
        ExcelUtil<RepastAppointment> util = new ExcelUtil<RepastAppointment>(RepastAppointment.class);
        return util.exportExcel(list, "repastAppointment");
    }
	
	/**
	 * 新增就餐预约
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存就餐预约
	 */
	@RequiresPermissions("admin:repastAppointment:add")
	@Log(title = "就餐预约", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(RepastAppointment repastAppointment)
	{		
		return toAjax(repastAppointmentService.insertRepastAppointment(repastAppointment));
	}

	/**
	 * 修改就餐预约
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		RepastAppointment repastAppointment = repastAppointmentService.selectRepastAppointmentById(id);
		mmap.put("repastAppointment", repastAppointment);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存就餐预约
	 */
	@RequiresPermissions("admin:repastAppointment:edit")
	@Log(title = "就餐预约", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(RepastAppointment repastAppointment)
	{		
		return toAjax(repastAppointmentService.updateRepastAppointment(repastAppointment));
	}
	
	/**
	 * 删除就餐预约
	 */
	@RequiresPermissions("admin:repastAppointment:remove")
	@Log(title = "就餐预约", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(repastAppointmentService.deleteRepastAppointmentByIds(ids));
	}

}
