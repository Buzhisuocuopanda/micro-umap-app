package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.ApproveApplyMapper;
import com.mkst.umap.app.admin.domain.ApproveApply;
import com.mkst.umap.app.admin.service.IApproveApplyService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 审核申请 服务层实现
 * 
 * @author wangyong
 * @date 2020-07-21
 */
@Service
public class ApproveApplyServiceImpl implements IApproveApplyService 
{
	@Autowired
	private ApproveApplyMapper approveApplyMapper;

	/**
     * 查询审核申请信息
     * 
     * @param approveId 审核申请ID
     * @return 审核申请信息
     */
    @Override
	public ApproveApply selectApproveApplyById(Long approveId)
	{
	    return approveApplyMapper.selectApproveApplyById(approveId);
	}

	@Override
	public Set<Long> selectApplyIds(ApproveApply approveApply){
    	return approveApplyMapper.selectApplyIds(approveApply);
	}

	/**
     * 查询审核申请列表
     * 
     * @param approveApply 审核申请信息
     * @return 审核申请集合
     */
	@Override
	public List<ApproveApply> selectApproveApplyList(ApproveApply approveApply)
	{
	    return approveApplyMapper.selectApproveApplyList(approveApply);
	}

	@Override
	public List<ApproveApply> selectApproveApplyByApplyId(Long applyId){
		return approveApplyMapper.selectApproveApplyByApplyId(applyId);
	}
	
    /**
     * 新增审核申请
     * 
     * @param approveApply 审核申请信息
     * @return 结果
     */
	@Override
	public int insertApproveApply(ApproveApply approveApply)
	{
	    return approveApplyMapper.insertApproveApply(approveApply);
	}
	
	/**
     * 修改审核申请
     * 
     * @param approveApply 审核申请信息
     * @return 结果
     */
	@Override
	public int updateApproveApply(ApproveApply approveApply)
	{
	    return approveApplyMapper.updateApproveApply(approveApply);
	}

	/**
     * 删除审核申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteApproveApplyByIds(String ids)
	{
		return approveApplyMapper.deleteApproveApplyByIds(Convert.toStrArray(ids));
	}
	
}
