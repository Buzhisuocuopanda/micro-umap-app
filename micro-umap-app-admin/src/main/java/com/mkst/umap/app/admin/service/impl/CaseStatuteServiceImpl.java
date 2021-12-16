package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CaseStatuteMapper;
import com.mkst.umap.app.admin.domain.CaseStatute;
import com.mkst.umap.app.admin.service.ICaseStatuteService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 案件相关法规 服务层实现
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Service
public class CaseStatuteServiceImpl implements ICaseStatuteService 
{
	@Autowired
	private CaseStatuteMapper caseStatuteMapper;

	/**
     * 查询案件相关法规信息
     * 
     * @param id 案件相关法规ID
     * @return 案件相关法规信息
     */
    @Override
	public CaseStatute selectCaseStatuteById(Integer id)
	{
	    return caseStatuteMapper.selectCaseStatuteById(id);
	}
	
	/**
     * 查询案件相关法规列表
     * 
     * @param caseStatute 案件相关法规信息
     * @return 案件相关法规集合
     */
	@Override
	public List<CaseStatute> selectCaseStatuteList(CaseStatute caseStatute)
	{
	    return caseStatuteMapper.selectCaseStatuteList(caseStatute);
	}
	
    /**
     * 新增案件相关法规
     * 
     * @param caseStatute 案件相关法规信息
     * @return 结果
     */
	@Override
	public int insertCaseStatute(CaseStatute caseStatute)
	{
	    return caseStatuteMapper.insertCaseStatute(caseStatute);
	}
	
	/**
     * 修改案件相关法规
     * 
     * @param caseStatute 案件相关法规信息
     * @return 结果
     */
	@Override
	public int updateCaseStatute(CaseStatute caseStatute)
	{
	    return caseStatuteMapper.updateCaseStatute(caseStatute);
	}

	/**
     * 删除案件相关法规对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCaseStatuteByIds(String ids)
	{
		return caseStatuteMapper.deleteCaseStatuteByIds(Convert.toStrArray(ids));
	}
	
}
