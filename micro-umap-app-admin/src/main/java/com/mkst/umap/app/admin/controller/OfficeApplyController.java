package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.OfficeApply;
import com.mkst.umap.app.admin.service.IOfficeApplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 办公申请 信息操作处理
 *
 * @author wangyong
 * @date 2020-08-07
 */
@Controller
@RequestMapping("/admin/officeApply")
public class OfficeApplyController extends BaseController {
    private String prefix = "admin/officeApply";

    @Autowired
    private IOfficeApplyService officeApplyService;

    @RequiresPermissions("admin:officeApply:view")
    @GetMapping()
    public String officeApply() {
        return prefix + "/officeApply";
    }

    /**
     * 查询办公申请列表
     */
    @RequiresPermissions("admin:officeApply:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OfficeApply officeApply) {
        startPage();
        List<OfficeApply> list = officeApplyService.selectOfficeApplyList(officeApply);
        return getDataTable(list);
    }

    /**
     * 删除办公申请
     */
    @RequiresPermissions("admin:officeApply:remove")
    @Log(title = "办公申请", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(officeApplyService.deleteOfficeApplyByIds(ids));
    }

}
