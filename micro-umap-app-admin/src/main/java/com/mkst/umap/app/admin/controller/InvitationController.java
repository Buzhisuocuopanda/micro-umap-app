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
import com.mkst.umap.app.admin.domain.Invitation;
import com.mkst.umap.app.admin.service.IInvitationService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 邀请码 信息操作处理
 * 
 * @author wangyong
 * @date 2020-08-13
 */
@Controller
@RequestMapping("/admin/invitation")
public class InvitationController extends BaseController
{
    private String prefix = "admin/invitation";
	
	@Autowired
	private IInvitationService invitationService;
	
	@RequiresPermissions("admin:invitation:view")
	@GetMapping()
	public String invitation()
	{
	    return prefix + "/invitation";
	}
	
	/**
	 * 查询邀请码列表
	 */
	@RequiresPermissions("admin:invitation:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Invitation invitation)
	{
		startPage();
        List<Invitation> list = invitationService.selectInvitationList(invitation);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出邀请码列表
	 */
	@RequiresPermissions("admin:invitation:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Invitation invitation)
    {
    	List<Invitation> list = invitationService.selectInvitationList(invitation);
        ExcelUtil<Invitation> util = new ExcelUtil<Invitation>(Invitation.class);
        return util.exportExcel(list, "invitation");
    }
	
	/**
	 * 新增邀请码
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存邀请码
	 */
	@RequiresPermissions("admin:invitation:add")
	@Log(title = "邀请码", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Invitation invitation)
	{		
		return toAjax(invitationService.insertInvitation(invitation));
	}

	/**
	 * 修改邀请码
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Invitation invitation = invitationService.selectInvitationById(id);
		mmap.put("invitation", invitation);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存邀请码
	 */
	@RequiresPermissions("admin:invitation:edit")
	@Log(title = "邀请码", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Invitation invitation)
	{		
		return toAjax(invitationService.updateInvitation(invitation));
	}
	
	/**
	 * 删除邀请码
	 */
	@RequiresPermissions("admin:invitation:remove")
	@Log(title = "邀请码", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(invitationService.deleteInvitationByIds(ids));
	}
	
}
