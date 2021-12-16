package com.mkst.umap.app.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.system.domain.SysUser;
import com.mkst.mini.systemplus.system.service.ISysUserService;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.domain.Report;
import com.mkst.umap.app.admin.dto.ReplyDto;
import com.mkst.umap.app.admin.service.IReplyService;
import com.mkst.umap.app.admin.service.IReportService;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 随手拍/公益举报 信息操作处理
 * 
 * @author wangyong
 * @date 2020-08-27
 */
@Controller
@RequestMapping("/admin/report")
public class ReportController extends BaseController
{
    private String prefix = "app/report";
	
	@Autowired
	private IReportService reportService;

	@Autowired
	private ISysUserService userService;

	@Autowired
	private IReplyService replyService;

	@RequiresPermissions("admin:report:view")
	@GetMapping()
	public String report()
	{
	    return prefix + "/report";
	}

	/**
	 * 查询随手拍/公益举报列表
	 */
	@RequiresPermissions("admin:report:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Report report)
	{
		startPage();
        List<Report> list = reportService.selectReportList(report);
		return getDataTable(list);
	}

	/**
	 * @Author wangyong
	 * @Description 回复举报
	 * @Date 15:14 2020-09-01
	 * @Param
	 * @return
	 */

	@GetMapping(value = "/reply/{id}/{content}")
	@ResponseBody
	@Log(title = "举报回复",businessType = BusinessType.UPDATE)
	public AjaxResult reply(@PathVariable("id") Long id, @PathVariable("content") String content){
		return toAjax(reportService.reply(id, content, ShiroUtils.getLoginName()));
	}

	@RequiresPermissions("admin:meeting:edit")
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap map) {

		// 结果的主体信息
		Report report = reportService.selectReportById(id);
		List<String> fileBind = reportService.getFileBind(id);

		SysUser createByUser = new SysUser();
		if ("1".equals(report.getRealName())){
			createByUser = userService.selectUserById(report.getCreateBy());
		}

		List<ReplyDto> replyDtos = new ArrayList<>();
		if ("1".equals(report.getHasReplied())){
			Reply selectReply = new Reply();
			selectReply.setBusinessType(BusinessTypeEnum.UMAP_REPORT.getValue());
			selectReply.setObjectId(report.getId().toString());

			List<Reply> replyList = replyService.selectReplyList(selectReply);
			if (CollectionUtil.isNotEmpty(replyList)){
				// id content createTime
				replyList.stream().forEach(reply -> {
					// id content createTime
					ReplyDto replyDto = transObject(reply, ReplyDto.class);
					replyDto.setResponder(userService.selectUserByLoginName(reply.getCreateBy()).getUserName());
					replyDtos.add(replyDto);
				});
			}
		}

		map.put("report",report);
		map.put("fileBind",fileBind);
		map.put("createByUser",createByUser);
		map.put("replyDtos",replyDtos);

		return prefix + "/detail";
	}
	
	/**
	 * 删除随手拍/公益举报
	 */
	@RequiresPermissions("admin:report:remove")
	@Log(title = "随手拍/公益举报", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(reportService.deleteReportByIds(ids));
	}

	public <T> T transObject(Object ob, Class<T> clazz) {
		String oldOb = JSON.toJSONString(ob);
		return JSON.parseObject(oldOb, clazz);
	}
}
