package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.ReceptionAppointment;
import com.mkst.umap.app.admin.service.IReceptionAppointmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 接待预约 信息操作处理
 *
 * @author wangyong
 * @date 2020-07-08
 */
@Controller
@RequestMapping("/admin/receptionAppointment")
public class ReceptionAppointmentController extends BaseController {
    private String prefix = "app/reception/appointment";

    @Autowired
    private IReceptionAppointmentService receptionAppointmentService;

    @RequiresPermissions("admin:receptionAppointment:view")
    @GetMapping()
    public String receptionAppointment() {
        return prefix + "/receptionAppointment";
    }

    /**
     * 查询接待预约列表
     */
    @RequiresPermissions("admin:receptionAppointment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ReceptionAppointment receptionAppointment) {
        startPage();
        List<ReceptionAppointment> list = receptionAppointmentService.selectReceptionAppointmentList(receptionAppointment);
        return getDataTable(list);
    }




    /**
     * 修改接待预约
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        ReceptionAppointment receptionAppointment = receptionAppointmentService.selectReceptionAppointmentById(id);
        mmap.put("receptionAppointment", receptionAppointment);
        return prefix + "/edit";
    }

    /**
     * 修改保存接待预约
     */
    @RequiresPermissions("admin:receptionAppointment:edit")
    @Log(title = "接待预约", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ReceptionAppointment receptionAppointment) {
        return toAjax(receptionAppointmentService.updateReceptionAppointment(receptionAppointment));
    }

    /**
     * 删除接待预约
     */
    @RequiresPermissions("admin:receptionAppointment:remove")
    @Log(title = "接待预约", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(receptionAppointmentService.deleteReceptionAppointmentByIds(ids));
    }

}
