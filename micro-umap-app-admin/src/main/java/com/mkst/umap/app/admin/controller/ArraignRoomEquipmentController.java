package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.ArraignRoomEquipment;
import com.mkst.umap.app.admin.service.IArraignRoomEquipmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提审室设配 信息操作处理
 *
 * @author lijinghui
 * @date 2020-06-11
 */
@Controller
@RequestMapping("/admin/arraignRoomEquipment")
public class ArraignRoomEquipmentController extends BaseController {
    private String prefix = "app/arraign/arraignRoomEquipment";

    @Autowired
    private IArraignRoomEquipmentService arraignRoomEquipmentService;

    @RequiresPermissions("admin:arraignRoomEquipment:view")
    @GetMapping()
    public String arraignRoomEquipment() {
        return prefix + "/arraignRoomEquipment";
    }

    /**
     * 查询提审室设配列表
     */
    @RequiresPermissions("admin:arraignRoomEquipment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ArraignRoomEquipment arraignRoomEquipment) {
        startPage();
        List<ArraignRoomEquipment> list = arraignRoomEquipmentService.selectArraignRoomEquipmentList(arraignRoomEquipment);
        return getDataTable(list);
    }


    /**
     * 导出提审室设配列表
     */
    @RequiresPermissions("admin:arraignRoomEquipment:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ArraignRoomEquipment arraignRoomEquipment) {
        List<ArraignRoomEquipment> list = arraignRoomEquipmentService.selectArraignRoomEquipmentList(arraignRoomEquipment);
        ExcelUtil<ArraignRoomEquipment> util = new ExcelUtil<ArraignRoomEquipment>(ArraignRoomEquipment.class);
        return util.exportExcel(list, "arraignRoomEquipment");
    }

    /**
     * 新增提审室设配
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存提审室设配
     */
    @RequiresPermissions("admin:arraignRoomEquipment:add")
    @Log(title = "提审室设配", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ArraignRoomEquipment arraignRoomEquipment) {
        return toAjax(arraignRoomEquipmentService.insertArraignRoomEquipment(arraignRoomEquipment));
    }

    /**
     * 修改提审室设配
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        ArraignRoomEquipment arraignRoomEquipment = arraignRoomEquipmentService.selectArraignRoomEquipmentById(id);
        mmap.put("arraignRoomEquipment", arraignRoomEquipment);
        return prefix + "/edit";
    }

    /**
     * 修改保存提审室设配
     */
    @RequiresPermissions("admin:arraignRoomEquipment:edit")
    @Log(title = "提审室设配", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ArraignRoomEquipment arraignRoomEquipment) {
        return toAjax(arraignRoomEquipmentService.updateArraignRoomEquipment(arraignRoomEquipment));
    }

    /**
     * 删除提审室设配
     */
    @RequiresPermissions("admin:arraignRoomEquipment:remove")
    @Log(title = "提审室设配", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(arraignRoomEquipmentService.deleteArraignRoomEquipmentByIds(ids));
    }

}
