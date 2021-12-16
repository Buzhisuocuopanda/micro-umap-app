package com.mkst.umap.app.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.mkst.mini.systemplus.common.base.BaseController;
import com.mkst.mini.systemplus.system.domain.SysFileUpload;
import com.mkst.mini.systemplus.system.domain.SysPost;
import com.mkst.mini.systemplus.system.domain.SysUserPost;
import com.mkst.mini.systemplus.system.mapper.SysUserPostMapper;
import com.mkst.mini.systemplus.util.FileUploadExtendUtils;
import com.mkst.umap.app.common.constant.KeyConstant;
import com.mkst.umap.app.common.constant.MsgConstant;
import com.mkst.umap.app.common.enums.AuditStatusEnum;
import com.mkst.umap.app.common.enums.BusinessTypeEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mkst.mini.systemplus.common.annotation.Log;
import com.mkst.mini.systemplus.common.enums.BusinessType;
import com.mkst.umap.app.admin.domain.Identity;
import com.mkst.umap.app.admin.service.IIdentityService;
import com.mkst.mini.systemplus.framework.web.page.TableDataInfo;
import com.mkst.mini.systemplus.common.base.AjaxResult;
import com.mkst.mini.systemplus.common.utils.ExcelUtil;

/**
 * 申请身份 信息操作处理
 * 
 * @author wangyong
 * @date 2020-08-19
 */
@Controller
@RequestMapping("/admin/identity")
public class IdentityController extends BaseController
{
    private String prefix = "app/identity";
	
	@Autowired
	private IIdentityService identityService;

	@Autowired
	private SysUserPostMapper userPostMapper;
	
	@RequiresPermissions("admin:identity:view")
	@GetMapping()
	public String identity()
	{
	    return prefix + "/identity";
	}
	
	/**
	 * 查询申请身份列表
	 */
	@RequiresPermissions("admin:identity:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Identity identity)
	{
		startPage();
		identity.setNoPostCode(KeyConstant.LAWYER_CODE);
        List<Identity> list = identityService.selectIdentityList(identity);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出申请身份列表
	 */
	@RequiresPermissions("admin:identity:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Identity identity)
    {
    	List<Identity> list = identityService.selectIdentityList(identity);
        ExcelUtil<Identity> util = new ExcelUtil<Identity>(Identity.class);
        return util.exportExcel(list, "identity");
    }
	
	/**
	 * 新增申请身份
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存申请身份
	 */
	@RequiresPermissions("admin:identity:add")
	@Log(title = "申请身份", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Identity identity)
	{		
		return toAjax(identityService.insertIdentity(identity));
	}

	/**
	 * 修改申请身份
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		ArrayList<String> resultList = new ArrayList<>();
		Identity identity = identityService.selectIdentityById(id);
		List<SysFileUpload> fileUpload = FileUploadExtendUtils.findFileUpload(id.toString(), BusinessTypeEnum.UMAP_IDENTITY.getValue());
		if (!CollectionUtil.isEmpty(fileUpload)){
			fileUpload.stream().forEach(upload ->{
				upload.getFileEntityList().stream().forEach(file -> {
					resultList.add(file.getFilePath());
				});
			});
		}
		mmap.put("identity", identity);
		mmap.put("fileBind",resultList);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存申请身份
	 */
	@RequiresPermissions("admin:identity:edit")
	@Log(title = "申请身份", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Identity identity)
	{		
		return toAjax(identityService.updateIdentity(identity));
	}
	
	/**
	 * 删除申请身份
	 */
	@RequiresPermissions("admin:identity:remove")
	@Log(title = "申请身份", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(identityService.deleteIdentityByIds(ids));
	}


	/**
	 * 提交审核
	 */
	@PostMapping("/doAudit/{id}/{status}/{aduitCause}")
	@RequiresPermissions("admin:identity:edit")
	@Log(title = "审核", businessType = BusinessType.UPDATE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public AjaxResult doAudit(@PathVariable Integer id, @PathVariable String status, @PathVariable String aduitCause) {
		Identity identity = identityService.selectIdentityById(id);
		identity.setStatus(status);
		identity.setAduitTime(new Date());
		if (!aduitCause.equals(MsgConstant.USER_AUDIT_NO_REASON_FLAG)) {
			identity.setAduitCause(aduitCause);
		}
		SysUserPost sysUserPost = identityService.selectUserPost(identity.getUserId().longValue(), identity.getUserType().longValue());
		if(sysUserPost == null && AuditStatusEnum.EVENT_AUDIT_STATUS_PASS.getValue().toString().equals(status)){
			List<SysUserPost> list = new ArrayList<SysUserPost>();
			SysUserPost up = new SysUserPost();
			up.setUserId(identity.getUserId().longValue());
			up.setPostId(identity.getUserType().longValue());
			list.add(up);
			userPostMapper.batchUserPost(list);
		}
		return toAjax(identityService.updateIdentity(identity));
	}
}
