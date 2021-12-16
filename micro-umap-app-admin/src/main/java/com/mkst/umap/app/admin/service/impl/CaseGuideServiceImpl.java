package com.mkst.umap.app.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.CaseGuideMapper;
import com.mkst.umap.app.admin.domain.CaseGuide;
import com.mkst.umap.app.admin.service.ICaseGuideService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 指导案例 服务层实现
 * 
 * @author wangyong
 * @date 2021-06-25
 */
@Service
public class CaseGuideServiceImpl implements ICaseGuideService 
{
	@Autowired
	private CaseGuideMapper caseGuideMapper;

	/**
     * 查询指导案例信息
     * 
     * @param id 指导案例ID
     * @return 指导案例信息
     */
    @Override
	public CaseGuide selectCaseGuideById(Integer id)
	{
	    return caseGuideMapper.selectCaseGuideById(id);
	}
	
	/**
     * 查询指导案例列表
     * 
     * @param caseGuide 指导案例信息
     * @return 指导案例集合
     */
	@Override
	public List<CaseGuide> selectCaseGuideList(CaseGuide caseGuide)
	{
	    return caseGuideMapper.selectCaseGuideList(caseGuide);
	}
	
    /**
     * 新增指导案例
     * 
     * @param caseGuide 指导案例信息
     * @return 结果
     */
	@Override
	public int insertCaseGuide(CaseGuide caseGuide)
	{
	    return caseGuideMapper.insertCaseGuide(caseGuide);
	}
	
	/**
     * 修改指导案例
     * 
     * @param caseGuide 指导案例信息
     * @return 结果
     */
	@Override
	public int updateCaseGuide(CaseGuide caseGuide)
	{
	    return caseGuideMapper.updateCaseGuide(caseGuide);
	}

	/**
     * 删除指导案例对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCaseGuideByIds(String ids)
	{
		return caseGuideMapper.deleteCaseGuideByIds(Convert.toStrArray(ids));
	}
	
}
