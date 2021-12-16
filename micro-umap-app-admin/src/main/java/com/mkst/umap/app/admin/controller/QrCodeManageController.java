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
import com.mkst.umap.app.admin.domain.QrCodeManage;
import com.mkst.umap.app.admin.service.IQrCodeManageService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 二维码管理 信息操作处理
 * 
 * @author wangyong
 * @date 2020-07-13
 */
@Controller
@RequestMapping("/admin/qrCodeManage")
public class QrCodeManageController extends BaseController
{
    private String prefix = "app/qrCodeManage";
	
	@Autowired
	private IQrCodeManageService qrCodeManageService;
	
	@RequiresPermissions("admin:qrCodeManage:view")
	@GetMapping()
	public String qrCodeManage()
	{
	    return prefix + "/qrCodeManage";
	}
	
	/**
	 * 查询二维码管理列表
	 */
	@RequiresPermissions("admin:qrCodeManage:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(QrCodeManage qrCodeManage)
	{
		startPage();
        List<QrCodeManage> list = qrCodeManageService.selectQrCodeManageList(qrCodeManage);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出二维码管理列表
	 */
	@RequiresPermissions("admin:qrCodeManage:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(QrCodeManage qrCodeManage)
    {
    	List<QrCodeManage> list = qrCodeManageService.selectQrCodeManageList(qrCodeManage);
        ExcelUtil<QrCodeManage> util = new ExcelUtil<QrCodeManage>(QrCodeManage.class);
        return util.exportExcel(list, "qrCodeManage");
    }
	
	/**
	 * 新增二维码管理
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存二维码管理
	 */
	@RequiresPermissions("admin:qrCodeManage:add")
	@Log(title = "二维码管理", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(QrCodeManage qrCodeManage)
	{		
		return toAjax(qrCodeManageService.insertQrCodeManage(qrCodeManage));
	}

	/**
	 * 修改二维码管理
	 */
	@GetMapping("/edit/{qrCodeId}")
	public String edit(@PathVariable("qrCodeId") Long qrCodeId, ModelMap mmap)
	{
		QrCodeManage qrCodeManage = qrCodeManageService.selectQrCodeManageById(qrCodeId);
		mmap.put("qrCodeManage", qrCodeManage);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存二维码管理
	 */
	@RequiresPermissions("admin:qrCodeManage:edit")
	@Log(title = "二维码管理", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(QrCodeManage qrCodeManage)
	{		
		return toAjax(qrCodeManageService.updateQrCodeManage(qrCodeManage));
	}
	
	/**
	 * 删除二维码管理
	 */
	@RequiresPermissions("admin:qrCodeManage:remove")
	@Log(title = "二维码管理", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(qrCodeManageService.deleteQrCodeManageByIds(ids));
	}
	
}
