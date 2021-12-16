package com.mkst.umap.app.admin.service;

import com.mkst.umap.app.admin.domain.ApproveApply;
import java.util.List;
import java.util.Set;

/**
 * 审核申请 服务层
 * 
 * @author wangyong
 * @date 2020-07-21
 */
public interface IApproveApplyService 
{
	/**
     * 查询审核申请信息
     * 
     * @param approveId 审核申请ID
     * @return 审核申请信息
     */
	public ApproveApply selectApproveApplyById(Long approveId);

	Set<Long> selectApplyIds(ApproveApply approveApply);

	/**
     * 查询审核申请列表
     * 
     * @param approveApply 审核申请信息
     * @return 审核申请集合
     */
	public List<ApproveApply> selectApproveApplyList(ApproveApply approveApply);

	/**
	 * 通过申请ID查询list
	 * @param applyId
	 * @return
	 */
	List<ApproveApply> selectApproveApplyByApplyId(Long applyId);
	
	/**
     * 新增审核申请
     * 
     * @param approveApply 审核申请信息
     * @return 结果
     */
	public int insertApproveApply(ApproveApply approveApply);
	
	/**
     * 修改审核申请
     * 
     * @param approveApply 审核申请信息
     * @return 结果
     */
	public int updateApproveApply(ApproveApply approveApply);
		
	/**
     * 删除审核申请信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteApproveApplyByIds(String ids);
	
}
