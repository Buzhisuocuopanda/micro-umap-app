package com.mkst.umap.app.admin.service.impl;

import com.mkst.mini.systemplus.common.support.Convert;
import com.mkst.umap.app.admin.domain.MailBase;
import com.mkst.umap.app.admin.mapper.MailBaseMapper;
import com.mkst.umap.app.admin.service.IMailBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮件基础 服务层实现
 * 
 * @author wangyong
 * @date 2020-09-24
 */
@Service
public class MailBaseServiceImpl implements IMailBaseService 
{
	@Autowired
	private MailBaseMapper mailBaseMapper;

	/**
     * 查询邮件基础信息
     * 
     * @param id 邮件基础ID
     * @return 邮件基础信息
     */
    @Override
	public MailBase selectMailBaseById(Long id)
	{
	    return mailBaseMapper.selectMailBaseById(id);
	}
	
	/**
     * 查询邮件基础列表
     * 
     * @param mailBase 邮件基础信息
     * @return 邮件基础集合
     */
	@Override
	public List<MailBase> selectMailBaseList(MailBase mailBase)
	{
	    return mailBaseMapper.selectMailBaseList(mailBase);
	}
	
    /**
     * 新增邮件基础
     * 
     * @param mailBase 邮件基础信息
     * @return 结果
     */
	@Override
	public int insertMailBase(MailBase mailBase)
	{
	    return mailBaseMapper.insertMailBase(mailBase);
	}
	
	/**
     * 修改邮件基础
     * 
     * @param mailBase 邮件基础信息
     * @return 结果
     */
	@Override
	public int updateMailBase(MailBase mailBase)
	{
	    return mailBaseMapper.updateMailBase(mailBase);
	}

	/**
     * 删除邮件基础对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMailBaseByIds(String ids)
	{
		return mailBaseMapper.deleteMailBaseByIds(Convert.toStrArray(ids));
	}
	
}
