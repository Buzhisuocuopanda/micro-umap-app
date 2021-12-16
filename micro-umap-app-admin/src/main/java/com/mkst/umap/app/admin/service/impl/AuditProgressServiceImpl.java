package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.AuditProgress;
import com.mkst.umap.app.admin.mapper.AuditProgressMapper;
import com.mkst.umap.app.admin.service.IAuditProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审核进度 服务层实现
 * 
 * @author wangyong
 * @date 2020-11-09
 */
@Service
public class AuditProgressServiceImpl implements IAuditProgressService 
{
	@Autowired
	private AuditProgressMapper auditProgressMapper;

	/**
     * 查询审核进度信息
     * 
     * @param id 审核进度ID
     * @return 审核进度信息
     */
    @Override
	public AuditProgress selectAuditProgressById(Long id)
	{
	    return auditProgressMapper.selectAuditProgressById(id);
	}
	
	/**
     * 查询审核进度列表
     * 
     * @param auditProgress 审核进度信息
     * @return 审核进度集合
     */
	@Override
	public List<AuditProgress> selectAuditProgressList(AuditProgress auditProgress)
	{
	    return auditProgressMapper.selectAuditProgressList(auditProgress);
	}
	
    /**
     * 新增审核进度
     * 
     * @param auditProgress 审核进度信息
     * @return 结果
     */
	@Override
	public int insertAuditProgress(AuditProgress auditProgress)
	{
	    return auditProgressMapper.insertAuditProgress(auditProgress);
	}
	
	/**
     * 修改审核进度
     * 
     * @param auditProgress 审核进度信息
     * @return 结果
     */
	@Override
	public int updateAuditProgress(AuditProgress auditProgress)
	{
	    return auditProgressMapper.updateAuditProgress(auditProgress);
	}

	/**
     * 删除审核进度对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteAuditProgressByIds(String ids)
	{
		return auditProgressMapper.deleteAuditProgressByIds(Convert.toStrArray(ids));
	}
	
}
