package com.mkst.umap.app.admin.service.impl;

import java.util.List;

import com.mkst.umap.app.admin.api.bean.param.lhBook.LhWsParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.LhWsMapper;
import com.mkst.umap.app.admin.domain.LhWs;
import com.mkst.umap.app.admin.service.ILhWsService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 龙华文书同步 服务层实现
 * 
 * @author wangyong
 * @date 2020-09-15
 */
@Service
public class LhWsServiceImpl implements ILhWsService 
{
	@Autowired
	private LhWsMapper lhWsMapper;

	/**
     * 查询龙华文书同步信息
     * 
     * @param id 龙华文书同步ID
     * @return 龙华文书同步信息
     */
    @Override
	public LhWs selectLhWsById(Long id)
	{
	    return lhWsMapper.selectLhWsById(id);
	}
	
	/**
     * 查询龙华文书同步列表
     * 
     * @param lhWs 龙华文书同步信息
     * @return 龙华文书同步集合
     */
	@Override
	public List<LhWs> selectLhWsList(LhWs lhWs)
	{
	    return lhWsMapper.selectLhWsList(lhWs);
	}

	@Override
	public List<LhWs> selectLhWsListByAreaAndType(LhWsParam param){
		return lhWsMapper.selectLhWsListByAreaAndType(param);
	}
	
    /**
     * 新增龙华文书同步
     * 
     * @param lhWs 龙华文书同步信息
     * @return 结果
     */
	@Override
	public int insertLhWs(LhWs lhWs)
	{
	    return lhWsMapper.insertLhWs(lhWs);
	}
	
	/**
     * 修改龙华文书同步
     * 
     * @param lhWs 龙华文书同步信息
     * @return 结果
     */
	@Override
	public int updateLhWs(LhWs lhWs)
	{
	    return lhWsMapper.updateLhWs(lhWs);
	}

	/**
     * 删除龙华文书同步对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteLhWsByIds(String ids)
	{
		return lhWsMapper.deleteLhWsByIds(Convert.toStrArray(ids));
	}
	
}
