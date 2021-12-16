package com.mkst.umap.app.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mkst.umap.app.admin.mapper.PetitionMatterMapper;
import com.mkst.umap.app.admin.domain.PetitionMatter;
import com.mkst.umap.app.admin.service.IPetitionMatterService;
import com.mkst.mini.systemplus.common.support.Convert;

/**
 * 信访民行申诉事项 服务层实现
 * 
 * @author wangyong
 * @date 2020-08-25
 */
@Service
public class PetitionMatterServiceImpl implements IPetitionMatterService 
{
	@Autowired
	private PetitionMatterMapper petitionMatterMapper;

	/**
     * 查询信访民行申诉事项信息
     * 
     * @param id 信访民行申诉事项ID
     * @return 信访民行申诉事项信息
     */
    @Override
	public PetitionMatter selectPetitionMatterById(Long id)
	{
	    return petitionMatterMapper.selectPetitionMatterById(id);
	}
	
	/**
     * 查询信访民行申诉事项列表
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 信访民行申诉事项集合
     */
	@Override
	public List<PetitionMatter> selectPetitionMatterList(PetitionMatter petitionMatter)
	{
	    return petitionMatterMapper.selectPetitionMatterList(petitionMatter);
	}
	
    /**
     * 新增信访民行申诉事项
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 结果
     */
	@Override
	public int insertPetitionMatter(PetitionMatter petitionMatter)
	{
	    return petitionMatterMapper.insertPetitionMatter(petitionMatter);
	}
	
	/**
     * 修改信访民行申诉事项
     * 
     * @param petitionMatter 信访民行申诉事项信息
     * @return 结果
     */
	@Override
	public int updatePetitionMatter(PetitionMatter petitionMatter)
	{
	    return petitionMatterMapper.updatePetitionMatter(petitionMatter);
	}

	/**
     * 删除信访民行申诉事项对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePetitionMatterByIds(String ids)
	{
		return petitionMatterMapper.deletePetitionMatterByIds(Convert.toStrArray(ids));
	}

	@Override
	public PetitionMatter getPetitionMatterBing(Long petitionId, String matterType) {

		HashMap<String, Object> daoParam = new HashMap<>();
		daoParam.put("petitionId",petitionId);
		daoParam.put("matterType",matterType);

		PetitionMatter matter = petitionMatterMapper.getPetitionMatterBingList(daoParam);

		return matter;
	}
}
