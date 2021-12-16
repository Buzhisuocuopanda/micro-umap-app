package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.AuditProgress;

import java.util.List;

/**
 * 审核进度 数据层
 * 
 * @author wangyong
 * @date 2020-11-09
 */
public interface AuditProgressMapper 
{
	/**
     * 查询审核进度信息
     * 
     * @param id 审核进度ID
     * @return 审核进度信息
     */
	public AuditProgress selectAuditProgressById(Long id);
	
	/**
     * 查询审核进度列表
     * 
     * @param auditProgress 审核进度信息
     * @return 审核进度集合
     */
	public List<AuditProgress> selectAuditProgressList(AuditProgress auditProgress);
	
	/**
     * 新增审核进度
     * 
     * @param auditProgress 审核进度信息
     * @return 结果
     */
	public int insertAuditProgress(AuditProgress auditProgress);
	
	/**
     * 修改审核进度
     * 
     * @param auditProgress 审核进度信息
     * @return 结果
     */
	public int updateAuditProgress(AuditProgress auditProgress);
	
	/**
     * 删除审核进度
     * 
     * @param id 审核进度ID
     * @return 结果
     */
	public int deleteAuditProgressById(Long id);
	
	/**
     * 批量删除审核进度
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteAuditProgressByIds(String[] ids);
	
}