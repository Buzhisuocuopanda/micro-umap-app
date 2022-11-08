package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.UserSpend;
import com.mkst.umap.app.admin.domain.UserSpendLog;
import com.mkst.umap.app.admin.service.IUserSpendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的消费 信息操作处理
 * 
 * @author wangyong
 * @date 2020-11-03
 */
@Controller
@RequestMapping("/admin/userSpend")
public class UserSpendController extends BaseController
{
    private String prefix = "app/userSpend";
	
	@Autowired
	private IUserSpendService userSpendService;
	
	@RequiresPermissions("admin:userSpend:view")
	@GetMapping()
	public String userSpend()
	{
	    return prefix + "/userSpend";
	}
	
	/**
	 * 查询我的消费列表
	 */
	@RequiresPermissions("admin:userSpend:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(UserSpend userSpend)
	{
		startPage();
        List<UserSpend> list = userSpendService.selectUserSpendList(userSpend);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出我的消费列表
	 */
	@RequiresPermissions("admin:userSpend:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserSpend userSpend)
    {
    	List<UserSpend> list = userSpendService.selectUserSpendList(userSpend);
        ExcelUtil<UserSpend> util = new ExcelUtil<UserSpend>(UserSpend.class);
        return util.exportExcel(list, "userSpend");
    }
	
	/**
	 * 新增我的消费
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存我的消费
	 */
	@RequiresPermissions("admin:userSpend:add")
	@Log(title = "我的消费", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(UserSpend userSpend)
	{		
		return toAjax(userSpendService.insertUserSpend(userSpend));
	}

	/**
	 * 修改我的消费
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		UserSpend userSpend = userSpendService.selectUserSpendById(id);
		mmap.put("userSpend", userSpend);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存我的消费
	 */
	@RequiresPermissions("admin:userSpend:edit")
	@Log(title = "我的消费", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(UserSpend userSpend)
	{		
		return toAjax(userSpendService.updateUserSpend(userSpend));
	}
	
	/**
	 * 删除我的消费
	 */
	@RequiresPermissions("admin:userSpend:remove")
	@Log(title = "我的消费", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(userSpendService.deleteUserSpendByIds(ids));
	}

	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(String id, MultipartFile file) throws Exception {
		List<UserSpendLog> logList = new ArrayList<>();
		ExcelUtil<UserSpendLog> logUtil = new ExcelUtil<>(UserSpendLog.class);
		logList = logUtil.importExcel(file.getInputStream());
		return userSpendService.importFromLog(logList);
	}
	
}
