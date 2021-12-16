package com.mkst.umap.app.admin.mapper;

import com.mkst.umap.app.admin.domain.MailBase;

import java.util.List;

/**
 * 邮件基础 数据层
 * 
 * @author wangyong
 * @date 2020-09-24
 */
public interface MailBaseMapper 
{
	/**
     * 查询邮件基础信息
     * 
     * @param id 邮件基础ID
     * @return 邮件基础信息
     */
	public MailBase selectMailBaseById(Long id);
	
	/**
     * 查询邮件基础列表
     * 
     * @param mailBase 邮件基础信息
     * @return 邮件基础集合
     */
	public List<MailBase> selectMailBaseList(MailBase mailBase);
	
	/**
     * 新增邮件基础
     * 
     * @param mailBase 邮件基础信息
     * @return 结果
     */
	public int insertMailBase(MailBase mailBase);
	
	/**
     * 修改邮件基础
     * 
     * @param mailBase 邮件基础信息
     * @return 结果
     */
	public int updateMailBase(MailBase mailBase);
	
	/**
     * 删除邮件基础
     * 
     * @param id 邮件基础ID
     * @return 结果
     */
	public int deleteMailBaseById(Long id);
	
	/**
     * 批量删除邮件基础
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMailBaseByIds(String[] ids);
	
}