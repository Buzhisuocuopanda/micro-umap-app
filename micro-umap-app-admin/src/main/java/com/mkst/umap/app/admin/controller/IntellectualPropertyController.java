package com.mkst.umap.app.admin.controller;

import java.util.List;

import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.umap.app.admin.domain.IntellectualProperty;
import com.mkst.umap.app.admin.dto.intellectualproperty.IntelProInfoWebDto;
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
import com.mkst.umap.app.admin.service.IIntellectualPropertyService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 知识产权 信息操作处理
 * @author wangyong
 * @date 2020-08-12
 */
@Controller
@RequestMapping("/admin/intellectualProperty")
public class IntellectualPropertyController extends BaseController
{
    private String prefix = "app/intellectualproperty";
	
	@Autowired
	private IIntellectualPropertyService intellectualPropertyService;
	
	@RequiresPermissions("admin:intellectualProperty:view")
	@GetMapping()
	public String intellectualProperty()
	{
	    return prefix + "/intellectualProperty";
	}
	
	/**
	 * 查询知识产权列表
	 */
	@RequiresPermissions("admin:intellectualProperty:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(IntellectualProperty intellectualProperty)
	{
		startPage();
        List<IntelProInfoWebDto> list = intellectualPropertyService.selectIntellectualPropertyWebList(intellectualProperty);
		return getDataTable(list);
	}


	
}
