package com.mkst.umap.app.admin.controller;

import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.mini.systemplus.common.shiro.utils.ShiroUtils;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.umap.app.admin.domain.Petition;
import com.mkst.umap.app.admin.domain.Reply;
import com.mkst.umap.app.admin.service.IPetitionService;
import com.mkst.umap.app.admin.service.IReplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 信访主体 信息操作处理
 * 
 * @author wangyong
 * @date 2021-01-26
 */
@Controller
@RequestMapping("/admin/petition")
public class PetitionController extends BaseController
{
    private String prefix = "app/petition";
	
	@Autowired
	private IPetitionService petitionService;
	@Autowired
	private IReplyService replyService;
	
	@RequiresPermissions("admin:petition:view")
	@GetMapping()
	public String petition()
	{
	    return prefix + "/petition";
	}
	
	/**
	 * 查询信访主体列表
	 */
	@RequiresPermissions("admin:petition:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Petition petition)
	{
		startPage();
        List<Petition> list = petitionService.selectPetitionList(petition);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出信访主体列表
	 */
	@RequiresPermissions("admin:petition:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Petition petition)
    {
    	List<Petition> list = petitionService.selectPetitionList(petition);
        ExcelUtil<Petition> util = new ExcelUtil<Petition>(Petition.class);
        return util.exportExcel(list, "petition");
    }
	
	/**
	 * 新增信访主体
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存信访主体
	 */
	@RequiresPermissions("admin:petition:add")
	@Log(title = "信访主体", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Petition petition)
	{		
		return toAjax(petitionService.insertPetition(petition));
	}

	/**
	 * 修改信访主体
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		Petition petition = petitionService.selectPetitionById(id);
		mmap.put("petition", petition);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存信访主体
	 */
	@RequiresPermissions("admin:petition:edit")
	@Log(title = "信访主体", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Petition petition)
	{		
		return toAjax(petitionService.updatePetition(petition));
	}
	
	/**
	 * 删除信访主体
	 */
	@RequiresPermissions("admin:petition:remove")
	@Log(title = "信访主体", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(petitionService.deletePetitionByIds(ids));
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap)
	{
		Petition petition = petitionService.selectPetitionById(id);
		List<String> fileBind = petitionService.getFileBind(id);
		Reply reply = new Reply();
		reply.setObjectId(id.toString());
		List<Reply> replyList = replyService.selectReplyList(reply);
		if(replyList!=null&&replyList.size()!=0){
			mmap.put("reply",replyList.get(0));
		}
		mmap.put("petition", petition);
		mmap.put("fileBind",fileBind);
		mmap.put("fileSize",fileBind.size());
		return prefix + "/detail";
	}

	@PostMapping("/reply")
	@ResponseBody
	public AjaxResult reply(String id, String content)
	{
		Reply reply = new Reply();
		reply.setObjectId(id);
		reply.setBusinessType("umap_petition_party_building");
		reply.setContent(content);
		reply.setCreateBy(ShiroUtils.getLoginName());
		reply.setCreateTime(new Date());
		replyService.insertReply(reply);
		Petition petition = new Petition();
		petition.setId(Long.valueOf(id));
		petition.setHasReplied("1");
		petitionService.updatePetition(petition);
		return success("回复成功");
	}

}
