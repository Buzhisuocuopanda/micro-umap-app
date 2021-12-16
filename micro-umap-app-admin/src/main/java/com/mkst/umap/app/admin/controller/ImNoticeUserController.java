package com.mkst.umap.app.admin.controller;

import java.util.List;

import com.mkst.umap.app.common.enums.BusinessTypeEnum;
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
import com.mkst.umap.app.admin.domain.ImNoticeUser;
import com.mkst.umap.app.admin.service.IImNoticeUserService;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * IM消息通知用户映射 信息操作处理
 * 
 * @author wangyong
 * @date 2021-04-12
 */
@Controller
@RequestMapping("/admin/imNoticeUser")
public class ImNoticeUserController extends BaseController
{
    private String prefix = "app/imNoticeUser";
	
	@Autowired
	private IImNoticeUserService imNoticeUserService;
	
	@RequiresPermissions("admin:imNoticeUser:view")
	@GetMapping()
	public String imNoticeUser()
	{
	    return prefix + "/imNoticeUser";
	}
	
	/**
	 * 查询IM消息通知用户映射列表
	 */
	@RequiresPermissions("admin:imNoticeUser:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ImNoticeUser imNoticeUser)
	{
		startPage();
        List<ImNoticeUser> list = imNoticeUserService.selectImNoticeUserList(imNoticeUser);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出IM消息通知用户映射列表
	 */
	@RequiresPermissions("admin:imNoticeUser:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ImNoticeUser imNoticeUser)
    {
    	List<ImNoticeUser> list = imNoticeUserService.selectImNoticeUserList(imNoticeUser);
        ExcelUtil<ImNoticeUser> util = new ExcelUtil<ImNoticeUser>(ImNoticeUser.class);
        return util.exportExcel(list, "imNoticeUser");
    }
	
	/**
	 * 新增IM消息通知用户映射
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存IM消息通知用户映射
	 */
	@RequiresPermissions("admin:imNoticeUser:add")
	@Log(title = "IM消息通知用户映射", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ImNoticeUser imNoticeUser)
	{		
		return toAjax(imNoticeUserService.insertImNoticeUser(imNoticeUser));
	}

	/**
	 * 修改IM消息通知用户映射
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ImNoticeUser imNoticeUser = imNoticeUserService.selectImNoticeUserById(id);
		mmap.put("imNoticeUser", imNoticeUser);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存IM消息通知用户映射
	 */
	@RequiresPermissions("admin:imNoticeUser:edit")
	@Log(title = "IM消息通知用户映射", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ImNoticeUser imNoticeUser)
	{		
		return toAjax(imNoticeUserService.updateImNoticeUser(imNoticeUser));
	}
	
	/**
	 * 删除IM消息通知用户映射
	 */
	@RequiresPermissions("admin:imNoticeUser:remove")
	@Log(title = "IM消息通知用户映射", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(imNoticeUserService.deleteImNoticeUserByIds(ids));
	}

}
