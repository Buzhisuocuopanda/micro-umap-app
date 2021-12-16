package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/portal"})
public class PortalController extends BaseController {

    public String prefix = "portal/theme";

    @GetMapping("download")
    public String download()
    {
        return prefix + "/umap/download";
    }
}
