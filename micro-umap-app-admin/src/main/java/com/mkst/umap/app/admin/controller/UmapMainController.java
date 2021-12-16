package com.mkst.umap.app.admin.controller;

import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.mkst.mini.systemplus.system.domain.Portlet;
import com.mkst.mini.systemplus.system.domain.SysNotice;
import com.mkst.mini.systemplus.system.service.IPortletService;
import com.mkst.mini.systemplus.system.service.ISysNoticeService;
import com.mkst.umap.app.admin.domain.VisitApply;
import com.mkst.umap.app.admin.service.IVisitApplyService;
import com.mkst.umap.base.core.util.QrcodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.config.Global;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysIndexService;
import com.mkst.umap.app.admin.dto.arraign.CountDto;
import com.mkst.umap.app.admin.service.IArraignAppointmentService;

import cn.hutool.core.util.NumberUtil;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 备勤间申请 信息操作处理
 * 
 * @author lijinghui
 * @date 2020-06-17
 */
@Controller
//@RequestMapping("/main")
public class UmapMainController extends BaseController
{
    private String prefix = "main";

	@Autowired
	private ISysIndexService sysIndexService;
	@Autowired
	private IArraignAppointmentService arraignAppointmentService;

	@Autowired
	private IPortletService portletService;
	@Autowired
	private ISysNoticeService sysNoticeService;

	@Autowired
	private IVisitApplyService visitApplyService;

	@GetMapping({"/main"})
	public String main(ModelMap mmap) {
		int totalDeptCounts = sysIndexService.selectDeptCounts();
		SysUser user = new SysUser();
		int totalUserCounts = sysIndexService.selectUserCounts(user);
		user.setUserType("99");
		int registerUserCounts = sysIndexService.selectUserCounts(user);
		int totalLoginCounts = sysIndexService.selectLoginCounts();

		List<CountDto> list = arraignAppointmentService.selectDeptFive30Day();
		if(list.size() > 5) {
			list= list.subList(0,5);
		}
		mmap.put("version", Global.getVersion());
		mmap.put("totalDeptCounts", NumberUtil.decimalFormat(",###", (long)totalDeptCounts));
		mmap.put("totalUserCounts", NumberUtil.decimalFormat(",###", (long)totalUserCounts));
		mmap.put("registerUserCounts", NumberUtil.decimalFormat(",###", (long)registerUserCounts));
		mmap.put("totalLoginCounts", NumberUtil.decimalFormat(",###", (long)totalLoginCounts));
		mmap.put("list",list);

		//通知公告
		List<SysNotice> notices = sysNoticeService.selectNoticeList();
		mmap.put("notices",notices);

		//桌面组件
		List<Portlet> portlets = portletService.selectAllPortlets();
		mmap.put("portlets",portlets);

		return prefix;
	}


	@GetMapping({"/open/visit/{id}"})
	public String toVisit(@PathVariable("id") Long id,ModelMap mmap) {
		VisitApply visitApply = visitApplyService.selectVisitApplyById(id.intValue());
		String qrcodeUrl = UUID.randomUUID()+".png" ;
		JSONObject json = new JSONObject();
		json.put("id",id);
		json.put("event","visit");
		json.put("type",visitApply.getType());
		QrcodeUtil.createQrcode(json.toJSONString(), Global.getProfile() +qrcodeUrl);
		mmap.put("visitApply",visitApply);
		mmap.put("qrcodeUrl",qrcodeUrl);
		return "portal/visit/visit";
	}

}
