package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CaseTypeMapper;
import com.mkst.umap.app.admin.domain.CaseType;
import com.mkst.umap.app.admin.service.ICaseTypeService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 案件类型 服务层实现
 * 
 * @author wangyong
 * @date 2021-06-22
 */
@Service
public class CaseTypeServiceImpl implements ICaseTypeService 
{
	@Autowired
	private CaseTypeMapper caseTypeMapper;

	/**
     * 查询案件类型信息
     * 
     * @param id 案件类型ID
     * @return 案件类型信息
     */
    @Override
	public CaseType selectCaseTypeById(Integer id)
	{
	    return caseTypeMapper.selectCaseTypeById(id);
	}
	
	/**
     * 查询案件类型列表
     * 
     * @param caseType 案件类型信息
     * @return 案件类型集合
     */
	@Override
	public List<CaseType> selectCaseTypeList(CaseType caseType)
	{
	    return caseTypeMapper.selectCaseTypeList(caseType);
	}
	
    /**
     * 新增案件类型
     * 
     * @param caseType 案件类型信息
     * @return 结果
     */
	@Override
	public int insertCaseType(CaseType caseType)
	{
	    return caseTypeMapper.insertCaseType(caseType);
	}
	
	/**
     * 修改案件类型
     * 
     * @param caseType 案件类型信息
     * @return 结果
     */
	@Override
	public int updateCaseType(CaseType caseType)
	{
	    return caseTypeMapper.updateCaseType(caseType);
	}

	/**
     * 删除案件类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCaseTypeByIds(String ids)
	{
		return caseTypeMapper.deleteCaseTypeByIds(Convert.toStrArray(ids));
	}
	
}
